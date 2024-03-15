package com.spring.task.gymcrm.v2.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Trainee {
    private Long id;
    private String address;
    private LocalDate dateOfBirth;
    private User user;
}
