package com.kiran.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Kiran
 * @since 9/29/17
 */

@Entity
@Table(name="user_sign_up")
public class UserSignUpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name="user_name",unique=true)
    private String userName;

    @NotNull
    @Column(name="first_name")
    private String firstName;

    @NotNull
    @Column(name="last_name")
    private String lastName;

    @NotNull
    @Column(name="email")
    private String email;

    @NotNull
    @Column(name="pass")
    private String password;

    @Column(name="jira_user")
    private String jiraUser;

    @Column(name="jira_pass")
    private String jiraPassword;

    public UserSignUpEntity() {
    }

    public UserSignUpEntity(String userName, String firstName, String lastName, String email, String password, String jiraUser, String jiraPassword) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.jiraUser = jiraUser;
        this.jiraPassword = jiraPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJiraUser() {
        return jiraUser;
    }

    public void setJiraUser(String jiraUser) {
        this.jiraUser = jiraUser;
    }

    public String getJiraPassword() {
        return jiraPassword;
    }

    public void setJiraPassword(String jiraPassword) {
        this.jiraPassword = jiraPassword;
    }
}
