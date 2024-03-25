package com.spring.task.gymcrm.exception;

public class UpdateRequestValidationException extends RuntimeException {
    public UpdateRequestValidationException(String message) {
        super(message);
    }
}
