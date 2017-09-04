package com.kiran.service.exception;

/**
 * @author Kiran
 * @since 9/4/17
 */
public class InvalidMove extends RuntimeException{
    String error_message;

    public InvalidMove(String error_message) {
        this.error_message = error_message;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
