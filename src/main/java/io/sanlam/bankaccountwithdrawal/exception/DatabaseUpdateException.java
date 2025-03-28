package io.sanlam.bankaccountwithdrawal.exception;

public class DatabaseUpdateException extends RuntimeException {
    public DatabaseUpdateException(String message) {
        super(message);
    }
    public DatabaseUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}