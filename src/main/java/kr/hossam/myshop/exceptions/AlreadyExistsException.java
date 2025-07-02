package kr.hossam.myshop.exceptions;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends MyException {
    public AlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}