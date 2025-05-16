package com.aston.crud_api.util.user_exceptions;

public class UsernameAlreadyExistException extends Exception {
    public UsernameAlreadyExistException() {
    }

    public UsernameAlreadyExistException(String message) {
        super(message);
    }
}
