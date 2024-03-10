package com.spring.task.gymcrm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class TrainingUpdateRq {
    private Long traineeId;
    private Long trainerId;
    private String name;
    private Long trainingType;
    private LocalDate date;
    private Duration duration;
}
