package edu.ted.webshop.exception;

import java.sql.SQLException;

public class DataException extends RuntimeException{
    public DataException(String message, SQLException throwables) {
        super(message);
    }

    public DataException(Exception e) {
        super(e);
    }
}
