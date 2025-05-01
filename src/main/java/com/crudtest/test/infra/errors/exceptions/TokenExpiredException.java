package com.crudtest.test.infra.errors.exceptions;

public class TokenExpiredException extends Throwable {
    public TokenExpiredException(String message) {
        super(message);
    }
}
