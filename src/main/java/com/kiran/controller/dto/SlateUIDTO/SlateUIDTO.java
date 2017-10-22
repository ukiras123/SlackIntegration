package com.kiran.controller.dto.SlateUIDTO;

/**
 * @author Kiran
 * @since 10/20/17
 */
public class SlateUIDTO {

    private String apiName;
    private String branch;
    private String email;
    private String automationLevel; //Regression, smoke etc

    public SlateUIDTO() {
    }

    public SlateUIDTO(String apiName, String branch, String email, String automationLevel) {
        this.apiName = apiName;
        this.branch = branch;
        this.email = email;
        this.automationLevel = automationLevel;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAutomationLevel() {
        return automationLevel;
    }

    public void setAutomationLevel(String automationLevel) {
        this.automationLevel = automationLevel;
    }

    @Override
    public String toString() {
        return "SlateUIDTO{" +
                "apiName='" + apiName + '\'' +
                ", branch='" + branch + '\'' +
                ", email='" + email + '\'' +
                ", automationLevel='" + automationLevel + '\'' +
                '}';
    }
}
