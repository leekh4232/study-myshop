package kr.hossam.myshop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import kr.hossam.myshop.exceptions.MyException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    /**
     * 모든 예외를 처리하는 핸들러
     * @param e 발생한 예외
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> myExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);

        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

        // MyException을 상속받은 예외 클래스의 경우, 상태코드를 가져온다.
        // --> MyException을 상속 받았다는 것은 개발자가 직접 발생시킨 예외라는 의미
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            status = myException.getStatus().value();
        }

        // 에러 내용을 JSON으로 변환하기 위한 Map 객체 생성
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("status", status);
        result.put("message", e.getMessage() != null ? e.getMessage() : HttpStatus.INTERNAL_SERVER_ERROR);
        result.put("error", e.getClass().getSimpleName());
        result.put("timestamp", LocalDateTime.now().toString());

        // 브라우저에 JSON 형식으로 응답을 반환
        return ResponseEntity.status(status).body(result);
    }

}