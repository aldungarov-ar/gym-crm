package com.spring.task.gymcrm.v2.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TraineeUpdateRequest {
    private Long traineeId;
    private LocalDate dateOfBirth;
    private String address;
    private UserUpdateRequest userUpdateRequest;
}
