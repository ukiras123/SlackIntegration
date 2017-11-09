package com.kiran.controller.dto.RetroDTO;

/**
 * @author Kiran
 * @since 11/8/17
 */
public class RetroDTO {

    private String userName;
    private String retroMessage;
    private String timeStamp;
    private boolean isActive;

    public RetroDTO() {
    }

    public RetroDTO(String userName, String retroMessage, String timeStamp, boolean isActive) {
        this.userName = userName;
        this.retroMessage = retroMessage;
        this.timeStamp = timeStamp;
        this.isActive = isActive;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
