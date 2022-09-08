package com.kimcompany.jangbogbackendver2.Exception.Exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class TokenException extends Exception{
    private final HttpStatus httpStatus;
    private final String detail;

    public TokenException(HttpStatus httpStatus, String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }
}
