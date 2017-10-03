package com.kiran.model.response;

/**
 * @author Kiran
 * @since 9/29/17
 */
public class GenericErrorResponse {

    private String errorMessage;

    public GenericErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
