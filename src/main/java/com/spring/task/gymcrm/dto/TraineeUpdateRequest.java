package com.spring.task.gymcrm.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TraineeUpdateRequest {
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;
    private UserUpdateRequest userUpdateRequest;
}
