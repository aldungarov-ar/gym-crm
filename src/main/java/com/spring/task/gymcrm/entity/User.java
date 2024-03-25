package com.spring.task.gymcrm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isActive;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.username = firstName + "." + this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.username = this.firstName + "." + lastName;
    }
}
