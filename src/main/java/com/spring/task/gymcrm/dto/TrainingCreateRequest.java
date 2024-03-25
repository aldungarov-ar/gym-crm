package com.spring.task.gymcrm.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TrainingCreateRequest {
    private Long traineeId;
    private Long trainerId;
    private String name;
    private Long trainingTypeId;
    private Date date;
    private int duration;
}
