package com.kiran.model.response;

/**
 * @author Kiran
 * @since 11/8/17
 */
public class RetroResponse {

    private String userName;
    private String retroMessage;

    public RetroResponse(String userName, String retroMessage) {
        this.userName = userName;
        this.retroMessage = retroMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRetroMessage() {
        return retroMessage;
    }

    public void setRetroMessage(String retroMessage) {
        this.retroMessage = retroMessage;
    }
}
