package com.kiran.model.entity;

import javax.persistence.*;

/**
 * @author Kiran
 * @since 11/8/17
 */
@Entity
@Table(name="retro_detail")
public class RetroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="user_name")
    private String userName;

    @Column(name="retro_message")
    private String retroMessage;

    @Column(name="time_stamp")
    private String timeStamp;

    @Column(name="is_active")
    private boolean isActive;

    public RetroEntity() {
    }

    public RetroEntity(String userName, String retroMessage, String timeStamp, boolean isActive) {
        this.userName = userName;
        this.retroMessage = retroMessage;
        this.timeStamp = timeStamp;
        this.isActive = isActive;
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
