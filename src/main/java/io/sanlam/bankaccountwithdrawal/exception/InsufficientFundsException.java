package io.sanlam.bankaccountwithdrawal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//This automatically returns a 400 Bad Request when the exception is thrown in a controller.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }

    //This allows you to wrap another exception inside it, which is useful for debugging.
    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }

 }