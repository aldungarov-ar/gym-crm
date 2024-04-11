package com.spring.task.gymcrm.dto;

import lombok.Data;

import java.util.List;

@Data
public class TraineeTrainersUpdateRequest {
    private String traineeUsername;
    private List<String> trainersUsernames;
}
