package kr.hossam.myshop.exceptions;

import org.springframework.http.HttpStatus;

public class StringFormatException extends MyException {
    public StringFormatException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}