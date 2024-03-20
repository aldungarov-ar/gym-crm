package com.spring.task.gymcrm.entity;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Training {
    private Long id;
    private Long traineeId;
    private Long trainerId;
    private String name;
    private Long trainingTypeId;
    private LocalDate date;
    private Duration duration;
}
