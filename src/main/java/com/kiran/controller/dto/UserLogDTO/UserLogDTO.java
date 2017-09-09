package com.kiran.controller.dto.UserLogDTO;

/**
 * @author Kiran
 * @since 9/5/17
 */
public class UserLogDTO {
    private String userName;
    private String timeStamp;
    private String info;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public UserLogDTO() {
    }

    public UserLogDTO(String userName, String timeStamp, String info) {
        this.userName = userName;
        this.timeStamp = timeStamp;
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLogDTO userLog = (UserLogDTO) o;

        if (userName != null ? !userName.equals(userLog.userName) : userLog.userName != null) return false;
        return timeStamp != null ? timeStamp.equals(userLog.timeStamp) : userLog.timeStamp == null;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (timeStamp != null ? timeStamp.hashCode() : 0);
        return result;
    }
}
