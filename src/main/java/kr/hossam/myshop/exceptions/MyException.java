package kr.hossam.myshop.exceptions;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 프로그램 내에서 개발자가 직접 발생시키는 모든 예외 상황에 대한 상위 클래스
 * HTTP 상태코드를 멤버변수로 내장한다.
 */
@Getter
@Setter
public class MyException extends Exception {

    /** HTTP 상태코드 객체 */
    private HttpStatus status;

    /**
     * 상위 클래스의 생성자를 재정의하는 생성자
     *
     * HTTP 상태코드를 기본값으로 INTERNAL_SERVER_ERROR(500)로 지정한다.
     *
     * @param message 예외 메시지
     */
    public MyException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 직접 추가한 생성자
     *
     * 상황에 따른 HTTP 상태코드를 지정할 수 있다.
     *
     * @param status
     * @param message
     */
    public MyException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}