package kr.hossam.myshop.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 이미 존재하는 데이터에 대한 예외 처리 클래스.
 * 예를 들어, 회원가입 시 중복된 아이디나 이메일이 입력되었을 때 발생한다.
 */
public class AlreadyExistsException extends MyException {
    public AlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}