package com.spring.task.gymcrm.exception;

import java.sql.SQLException;

public class DBUpdateException extends RuntimeException {
    public DBUpdateException(String message, SQLException e) {
        super(message, e);
    }
}
