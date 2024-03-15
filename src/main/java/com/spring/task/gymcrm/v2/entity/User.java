package com.spring.task.gymcrm.v2.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;

    public User() {
        this.isActive = false;
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = firstName + "." + lastName;
        this.isActive = false;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.username = firstName + "." + this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.username = this.firstName + "." + lastName;
    }
}
