package kr.hossam.myshop.exceptions;

import org.springframework.http.HttpStatus;

public class ServiceNoResultException extends MyException {
    public ServiceNoResultException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}