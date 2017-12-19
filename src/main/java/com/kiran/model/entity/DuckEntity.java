package com.kiran.model.entity;

import javax.persistence.*;

/**
 * Created by Kiran on 12/18/17.
 */

@Entity
@Table(name="retro_detail")
public class DuckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "total_duck")
    private int total_duck;

    public DuckEntity() {
    }

    public DuckEntity(String userName, int total_duck) {
        this.userName = userName;
        this.total_duck = total_duck;
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

    public int getTotal_duck() {
        return total_duck;
    }

    public void setTotal_duck(int total_duck) {
        this.total_duck = total_duck;
    }
}
