package com.aston.crud_api.util.user_exceptions;

public class IncorrectInputException extends Exception {
    public IncorrectInputException() {
    }

    public IncorrectInputException(String message) {
        super(message);
    }
}
