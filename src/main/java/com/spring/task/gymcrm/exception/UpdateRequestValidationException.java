package com.spring.task.gymcrm.exception;

public class UpdateRequestValidationException extends RuntimeException {
    public UpdateRequestValidationException(String message) {
        super(message);
    }

    public UpdateRequestValidationException(String message, RuntimeException e) {
        super(message, e);
    }
}
