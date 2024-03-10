package com.spring.task.gymcrm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Trainee {
    private Long id;
    private Long userId;
    private String address;
    private LocalDate dateOfBirth;

    public Trainee(Long userId, String address, LocalDate dateOfBirth) {
        this.userId = userId;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
