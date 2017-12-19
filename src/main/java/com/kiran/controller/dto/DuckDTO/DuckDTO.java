package com.kiran.controller.dto.DuckDTO;

/**
 * @author Kiran
 * @since 12/18/17
 */
public class DuckDTO {

    private String userName;
    private int totalDuck;

    public DuckDTO() {
    }

    public DuckDTO(String userName, int totalDuck) {
        this.userName = userName;
        this.totalDuck = totalDuck;
    }

    public int getTotalDuck() {
        return totalDuck;
    }

    public void setTotalDuck(int totalDuck) {
        this.totalDuck = totalDuck;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addDuck()
    {
        totalDuck++;
    }
    public void removeDuck()
    {
        totalDuck--;
    }


}
