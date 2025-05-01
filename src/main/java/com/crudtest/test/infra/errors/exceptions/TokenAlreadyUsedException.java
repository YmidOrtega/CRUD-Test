package com.crudtest.test.infra.errors.exceptions;

public class TokenAlreadyUsedException extends Throwable {
    public TokenAlreadyUsedException(String message) {
        super(message);
    }
}
