package com.project.pescueshop.model.exception;

public class RetryException extends FriendlyException {
    public RetryException(String message) {
        super(message);
    }

    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
