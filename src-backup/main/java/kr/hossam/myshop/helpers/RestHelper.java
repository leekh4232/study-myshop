package kr.hossam.myshop.helpers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestHelper {
    @Autowired
    private HttpServletResponse response;

    /**
     * JSON 형식의 응답을 출력하기 위한 메서드
     * @param status - HTTP 상태 코드
     * @param message - 결과 메시지
     * @param data - JSON으로 변환할 데이터 컬렉션
     * @param error - 에러 메시지
     * @return Map
     */
    public Map<String, Object> sendJson(int status, String message, Object data, Exception error) {
        /** 1) JSON 형식 출력을 위한 HTTP 헤더 설정 */
        // JSON 형식임을 명시함
        response.setContentType("application/json; charset=UTF-8");

        // HTTP 상태 코드 설정 (200, 404, 500 등)
        response.setStatus(status);

        // CORS 보안 문제 방지 --> CrossDomain에 의한 접근 허용
        // (보안에 좋지 않기 때문에 이 옵션을 허용할 경우 인증키 등의 보안장치가 필요함)
        // 여기서는 실습을 위해 허용함
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Origin", "*");

        /** 2) JSON으로 변환할 Map객체의 구성 */
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        // HTTP 상태 코드와 결과 문자열 추가
        result.put("status", status);
        result.put("message", message);

        // data가 전달되었다면 result에 병합한다.
        if (data != null) {
            result.put("item", data);
        }

        // error가 전달되었다면 result에 포함한다.
        if (error != null) {
            result.put("error", error.getClass().getName());
            result.put("message", error.getMessage());

            // printStackTrace()의 출력 내용을 문자열로 반환받는다.
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            PrintStream ps = new PrintStream(bos);
//            error.printStackTrace(ps);
//
//            String trace = bos.toString();
//            result.put("trace", trace);
        }

        // 데이터 생성 시각을 포함한다 (브라우저 캐시 방지)
        result.put("timestamp", LocalDateTime.now().toString());

        return result;
    }

    /**
     * JSON 형식의 응답을 출력하기 위한 메서드
     * @param data - JSON으로 변환할 데이터 컬렉션
     * @return Map
     */
    public Map<String, Object> sendJson(Object data) {
        return this.sendJson(200, "OK", data, null);
    }

    /**
     * JSON 형식의 응답을 출력하기 위한 메서드
     * 특별한 결과값 없이 요청에 대한 성공 여부만을 알리기 위해 사용한다.
     * @return Map
     */
    public Map<String, Object> sendJson() {
        return this.sendJson(200, "OK", null, null);
    }

    /**
     * 에러 상황을 JSON 형식으로 출력하기 위한 메서드<br /><br />
     * <strong>주의</strong>: 이 메서드는 HTTP 상태 코드와 메시지를 파라미터로 받아
     * JSON 형식으로 에러 메시지를 리턴한다.
     * @param status - HTTP 상태 코드
     * @param message - 결과 메시지
     * @return Map&lt;String, Object&gt;
     */
    public Map<String, Object> sendError(int status, String message) {
        Exception error = new Exception(message);
        return this.sendJson(status, null, null, error);
    }

    /**
     * JSON 형식으로 에러 메시지를 리턴한다.
     * HTTP 상태코드는 400으로 설정하고, 결과 메시지는 파라미터로 전달되는 값을 설정한다.
     * 파라미터 유효성 검사 실패 등의 경우에 사용한다.
     *
     * @param message - 에러 메시지
     *
     * @return Map
     */
    public Map<String, Object> badRequest(String message) {
        return this.sendError(400, message);
    }

    /**
     * JSON 형식으로 에러 메시지를 리턴한다.
     * HTTP 상태코드는 400으로 설정하고, 결과 메시지는 파라미터로 전달되는 error 객체를 사용한다.
     * 파라미터 유효성 검사 실패 등의 경우에 사용한다.
     *
     * @param error - 에러 객체
     *
     * @return Map
     */
    public Map<String, Object> badRequest(Exception error) {
        return this.sendJson(400, null, null, error);
    }

    /**
     * JSON 형식으로 에러 메시지를 리턴한다.
     * HTTP 상태코드는 500으로 설정하고, 결과 메시지는 파라미터로 전달되는 값을 설정한다.
     * 400 에러 이외의 모든 경우에 사용한다. 주로 DB연동 등의 처리에서 발생한 에러를 처리한다.
     *
     * @param message - 에러 메시지
     *
     * @return Map
     */
    public Map<String, Object> serverError(String message) {
        return this.sendError(500, message);
    }

    /**
     * JSON 형식으로 에러 메시지를 리턴한다.
     * HTTP 상태코드는 500으로 설정하고, 결과 메시지는 파라미터로 전달되는 error 객체를 사용한다.
     * 400 에러 이외의 모든 경우에 사용한다. 주로 DB연동 등의 처리에서 발생한 에러를 처리한다.
     *
     * @param error - 에러 객체
     *
     * @return Map
     */
    public Map<String, Object> serverError(Exception error) {
        return this.sendJson(500, null, null, error);
    }
}