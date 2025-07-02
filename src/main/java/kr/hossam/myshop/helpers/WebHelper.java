package kr.hossam.myshop.helpers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class WebHelper {

    private HttpServletRequest request;
    private HttpServletResponse response;

    private String cookieDomain; // 기본 도메인 설정 (필요시 설정 가능)
    private String cookiePath; // 기본 경로 설정

    @Autowired
    public WebHelper(HttpServletRequest request, HttpServletResponse response,
                     @Value("${spring.cookie.domain}") String cookieDomain,
                     @Value("${spring.cookie.path}") String cookiePath) {
        this.request = request;
        this.response = response;
    }

    /**
     * 클라이언트의 IP 주소를 가져오는 메서드
     * 여러 가지 방법으로 시도하고 가장 적합한 IP 주소를 반환합니다.
     *
     * @return IP 주소
     */
    public String getClientIp() {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 쿠키값을 저장한다.
     *
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간 (0이면 지정 안함, 음수일 경우 즉시 삭제)
     */
    public void writeCookie(String name, String value, int maxAge) {
        if (value != null && !value.equals("")) {
            try {
                // -> import java.net.URLEncoder;
                value = URLEncoder.encode(value, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                // -> import java.io.UnsupportedEncodingException;
                e.printStackTrace();
            }
        }

        // --> import jakarta.servlet.http.Cookie;
        Cookie cookie = new Cookie(name, value);

        // 쿠키의 도메인 설정 --> application.properties에서 설정된 값을 사용
        cookie.setDomain(this.cookieDomain);
        // 쿠키의 유효 경로 설정 --> application.properties에서 설정된 값을 사용
        cookie.setPath(this.cookiePath);

        // 쿠키가 Javascript에서 접근하지 못하도록 설정
        cookie.setHttpOnly(true);

        // 쿠키 유효시간 설정
        // 설정하지 않으면 브라우저를 닫기 전까지 유지됨
        // 0 이하로 설정하면 즉시 삭제됨
        // 즉, -1로 설정하면 즉시 삭제, 0으로 설정하면 브라우저를 닫기 전까지 유지됨
        if (maxAge != 0) {
            cookie.setMaxAge(maxAge);
        }

        response.addCookie(cookie);
    }

    /**
     * 쿠키값을 삭제한다.
     *
     * @param name - 쿠키 이름
     */
    public void deleteCookie(String name) {
        this.writeCookie(name, null, -1);
    }

    /**
     * HTTP 상태 코드를 설정하고 메시지를 출력한 후, 지정된 페이지로 이동한다.
     * 이동할 페이지가 없을 경우 이전 페이지로 이동한다.
     *
     * @param statusCode - HTTP 상태 코드 (예: 404)
     * @param url        - 이동할 URL
     * @param message    - 출력할 메시지
     */
    public void redirect(int statusCode, String url, String message) {
        // HTTP 403 Forbidden 클라이언트 오류 상태 응답 코드는 서버에 요청이 전달되었지만,
        // 권한 때문에 거절되었다는 것을 의미
        response.setStatus(statusCode);
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            log.error("응답 출력 스트림 생성 실패", e);
            return;
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang='ko'>");
        out.println("<head>");
        out.println("<script>");

        if (message != null && !message.isEmpty()) {
            out.println("alert('" + message + "');");
        }

        if (url != null && !url.isEmpty()) {
            //out.println("<meta http-equiv='refresh' content='0; url=" + url + "' />");
            out.println("window.location.replace('" + url + "');");
        } else {
            out.println("history.back();");
        }

        out.println("</script>");
        out.println("</head>");
        out.println("<body>");
        out.println("</body>");
        out.println("</html>");

        out.flush();
    }

    /**
     * HTTP 상태 코드를 200으로 설정하고 메시지를 출력한 후, 지정된 페이지로 이동한다.
     *
     * @param url     - 이동할 URL
     * @param message - 출력할 메시지
     */
    public void redirect(String url, String message) {
        this.redirect(200, url, message);
    }

    /**
     * HTTP 상태 코드를 200으로 설정하고 메시지 출력 없이 지정된 페이지로 이동한다.
     *
     * @param url - 이동할 URL
     */
    public void redirect(String url) {
        this.redirect(200, url, null);
    }

    /**
     * HTTP 상태 코드를 설정하고 메시지를 출력 없이 지정된 페이지로 이동한다.
     *
     * @param statusCode - HTTP 상태 코드 (예: 404)
     * @param url        - 이동할 URL
     */
    public void redirect(int statusCode, String url) {
        this.redirect(statusCode, url, null);
    }

    /**
     * 파라미터가 잘못된 경우에 호출할 이전 페이지 이동 기능
     *
     * @param e - 에러정보를 담고 있는 객체.
     *          Exception 으로 선언했으므로 어떤 하위 객체가 전달되더라도 형변환되어 받는다.
     */
    public void badRequest(Exception e) {
        log.error("[403] BadRequest Error ::: {}", e.getMessage(), e);
        this.redirect(403, null, e.getMessage());
    }

    /**
     * 파라미터가 잘못된 경우에 호출할 이전 페이지 이동 기능
     *
     * @param message - 개발자가 직접 전달하는 에러 메시지
     */
    public void badRequest(String message) {
        log.error("[403] BadRequest Error ::: {}", message);
        this.redirect(403, null, message);
    }

    /**
     * JAVA 혹은 SQL 에서 잘못된 경우에 호출할 이전 페이지 이동 기능
     *
     * @param e - 에러정보를 담고 있는 객체.
     *          Exception 으로 선언했으므로 어떤 하위 객체가 전달되더라도 형변환되어 받는다.
     */
    public void serverError(Exception e) {
        String message = e.getMessage().trim().replace("'", "\\'").split(System.lineSeparator())[0];
        log.error("[500] Server Error ::: {}", message, e);
        this.redirect(500, null, message);
    }

    /**
     * JAVA 혹은 SQL 에서 잘못된 경우에 호출할 이전 페이지 이동 기능
     *
     * @param message - 개발자가 직접 전달하는 에러 메시지
     */
    public void serverError(String message) {
        log.error("[500] Server Error ::: {}", message);
        this.redirect(500, null, message);
    }
}