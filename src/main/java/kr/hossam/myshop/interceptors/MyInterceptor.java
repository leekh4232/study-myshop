package kr.hossam.myshop.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.hossam.myshop.helpers.SessionCheckHelper;
import kr.hossam.myshop.helpers.WebHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ua_parser.Client;
import ua_parser.Parser;
import java.nio.file.AccessDeniedException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyInterceptor implements HandlerInterceptor {
    /** 페이지의 실행 시작 시각을 저장할 변수 */
    long startTime = 0;

    /** 페이지의 실행 완료 시각을 저장할 변수  */
    long endTime = 0;

    /** WebHelper 객체 */
    private final WebHelper webHelper;

    /**
     * Controller 실행 전에 수행되는 메서드
     * 클라이언트(웹브라우저)의 요청을 컨트롤러에 전달 하기 전에 호출된다.
     * return 값으로 boolean 값을 전달하는데 false 인 경우
     * controller를 실행 시키지 않고 요청을 종료한다.
     * 보통 이곳에서 각종 체크작업과 로그를 기록하는 작업을 진행한다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //log.debug("MyInterceptor.preHandle 실행됨");

        log.info("---------- new client connect ------------");

        //webHelper = new WebHelper(request);

        /** 1) 페이지의 실행 시작 시각을 구한다. */
        startTime = System.currentTimeMillis();

        /** 2) 접속한 클라이언트(=웹 브라우저) 정보 확인하기 */
        // Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0
        String ua = request.getHeader("user-agent");
        Parser uaParser = new Parser();
        Client c = uaParser.parse(ua);

        String fmt = "[Client] %s, %s, %s %s, %s %s";

        String ipAddr = webHelper.getClientIp();
        String osVersion = c.os.major + (c.os.minor != null ? "." + c.os.minor : "");
        String uaVersion = c.userAgent.major + (c.userAgent.minor != null ? "." + c.userAgent.minor : "");
        String clientInfo = String.format(fmt, ipAddr, c.device.family, c.os.family, osVersion, c.userAgent.family, uaVersion);

        log.info(clientInfo);

        /** 3) 클라이언트의 요청 정보(URL) 확인하기 */
        // 현재 URL 획득
        String url = request.getRequestURL().toString();

        // GET방식인지, POST방식인지 조회한다.
        String methodName = request.getMethod();

        // URL에서 "?"이후에 전달되는 GET파라미터 문자열을 모두 가져온다.
        String queryString = request.getQueryString();

        // 가져온 값이 있다면 URL과 결합하여 완전한 URL을 구성한다.
        if (queryString != null) {
            url = url + "?" + queryString;
        }

        // 획득한 정보를 로그로 표시한다.
        log.info(String.format("[%s] %s", methodName, url));

        /** 4) 클라이언트가 전달한 모든 파라미터 확인하기 */
        Map<String, String[]> params = request.getParameterMap();

        for (String key : params.keySet()) {
            String[] value = params.get(key);
            log.info(String.format("(param) <-- %s = %s", key, String.join(",", value)));
        }

        /** 5) 클라이언트가 머물렀던 이전 페이지 확인하기 */
        String referer = request.getHeader("referer");

        // 이전에 머물렀던 페이지가 존재한다면?
        // --> 직전 종료시간과 이번 접속의 시작시간 과의 차이는 이전 페이지에 머문 시간을 의미한다.
        if (referer != null && endTime > 0) {
            log.info(String.format("- REFERER : time=%d, url=%s", startTime - endTime, referer));
        }

        /** 6) 로그인 여부에 따른 페이지 접근 제어 */
        // @SessionCheckHelper 어노테이션이 붙은 메서드에 대해서만 세션 체크를 수행한다.
        if (handler instanceof HandlerMethod handlerMethod) {
            // 세션 검사용 어노테이션을 가져온다.
            SessionCheckHelper annotation = handlerMethod.getMethodAnnotation(SessionCheckHelper.class);

            // 어노테이션이 존재한다면 세션 체크를 수행한다.
            if (annotation != null) {
                // 컨트롤러 유형을 가져온다.
                Class<?> beanType = handlerMethod.getBeanType();
                // Restful 방식의 컨트롤러인지 검사
                boolean isRestful = beanType.isAnnotationPresent(RestController.class);

                // 세션 검사 여부를 결정하는 enable 속성을 가져온다.
                // enable이 true인 경우 세션이 있어야 접근 가능하고,
                // false인 경우 세션이 없어야 접근 가능하다.
                boolean enable = annotation.enable();

                // 로그인 여부를 체크한다.
                HttpSession session = request.getSession();
                boolean isLoggedIn = session != null && session.getAttribute("memberInfo") != null;

                if (enable) {           // 로그인 중에만 접근 가능한 페이지 검사
                    if (!isLoggedIn) {  // 로그인을 하지 않은 상태라면?
                        if (isRestful) {
                            throw new AccessDeniedException("로그인이 필요합니다.");
                        } else {
                            response.setStatus(403);
                            response.sendRedirect(request.getContextPath() + "/account/login");
                        }

                        return false;
                    }
                } else {                // 로그인하지 않은 상태에서만 접근 가능한 페이지 검사
                    if (isLoggedIn) {   // 로그인을 한 상태라면?
                        if (isRestful) {
                            throw new AccessDeniedException("로그인 중에는 접근할 수 없습니다.");
                        } else {
                            response.setStatus(403);
                            response.sendRedirect(request.getContextPath() + "/account/login");
                        }
                        return false;
                    }
                }
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    /**
     * view 단으로 forward 되기 전에 수행.
     * 컨트롤러 로직이 실행된 이후 호출된다.
     * 컨트롤러 단에서 에러 발생 시 해당 메서드는 수행되지 않는다.
     * request로 넘어온 데이터 가공 시 많이 사용된다.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //log.debug("MyInterceptor.postHandle 실행됨");

        /** 1) 컨트롤러의 실행 종료 시각을 가져온다. */
        endTime = System.currentTimeMillis();

        /** 2) 컨트롤러가 실행하는데 걸린 시각을 구한다. */
        log.info(String.format("running time: %d(ms)", endTime-startTime));

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }


    /**
     * 컨트롤러 종료 후 view가 정상적으로 랜더링 된 후 제일 마지막에 실행이 되는 메서드.
     * (잘 사용 안함)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //log.debug("MyInterceptor.afterCompletion 실행됨");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}