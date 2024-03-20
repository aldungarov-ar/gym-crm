package com.spring.task.gymcrm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Trainee extends User {
    public Trainee(User user) {
        super(user);
    }

    private String address;
    private LocalDate dateOfBirth;
}
