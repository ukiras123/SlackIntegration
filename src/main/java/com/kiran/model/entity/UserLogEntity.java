package com.kiran.model.entity;

import javax.persistence.*;


/**
 * @author Kiran
 * @since 9/5/17
 */

@Entity
@Table(name="user_log")
public class UserLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="user_name")
    private String userName;

    @Column(name="time_stamp")
    private String timeStamp;

    @Column(name="info")
    private String info;

    public UserLogEntity() {

    }
    public UserLogEntity(String userName, String timeStamp, String info) {
        this.userName = userName;
        this.timeStamp = timeStamp;
        this.info = info;
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
}
