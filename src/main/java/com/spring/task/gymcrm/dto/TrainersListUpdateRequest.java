package com.spring.task.gymcrm.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainersListUpdateRequest {
    private String traineeUsername;
    private List<String> trainersUsernames;
}
