package com.spring.task.gymcrm.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
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

    private User(UserBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.username = firstName + "." + lastName;
        this.password = builder.password;
        this.isActive = builder.isActive;
    }

    public static class UserBuilder {
        private String firstName;
        private String lastName;
        private String password;
        private Boolean isActive;

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
