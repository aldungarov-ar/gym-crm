package com.spring.task.gymcrm.v2.dto;

import lombok.Data;

@Data
public class TrainerUpdateRequest {
    private Long trainerId;
    private Long trainingTypeId;
    private UserUpdateRequest userUpdateRequest;
}
