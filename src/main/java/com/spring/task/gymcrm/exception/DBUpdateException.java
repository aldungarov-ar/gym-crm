package com.spring.task.gymcrm.exception;

public class DBUpdateException extends RuntimeException {
    public DBUpdateException(Exception e) {
        super(e);
    }

    public DBUpdateException(String message, Exception e) {
        super(message, e);
    }
}
