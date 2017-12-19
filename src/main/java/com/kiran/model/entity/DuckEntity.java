package com.kiran.model.entity;

import javax.persistence.*;

/**
 * Created by Kiran on 12/18/17.
 */

@Entity
@Table(name="duck_log")
public class DuckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "total_duck")
    private int totalDuck;

    public DuckEntity() {
    }

    public DuckEntity(String userName, int totalDuck) {
        this.userName = userName;
        this.totalDuck = totalDuck;
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

    public int getTotalDuck() {
        return totalDuck;
    }

    public void setTotalDuck(int totalDuck) {
        this.totalDuck = totalDuck;
    }
}
