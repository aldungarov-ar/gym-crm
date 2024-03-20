package com.spring.task.gymcrm.dto;

import lombok.Data;

@Data
public class TrainerUpdateRequest {
    private Long userId;
    private Long specializationId;
    private UserUpdateRequest userUpdateRequest;
}
