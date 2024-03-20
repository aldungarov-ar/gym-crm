package com.spring.task.gymcrm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

    public User(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.isActive = user.isActive();
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
