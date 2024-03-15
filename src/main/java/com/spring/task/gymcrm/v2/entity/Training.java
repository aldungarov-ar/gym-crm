package com.spring.task.gymcrm.v2.entity;

import com.spring.task.gymcrm.dto.TrainingUpdateRq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Training {
    private Long id;
    private Trainee trainee;
    private Trainer trainer;
    private String name;
    private TrainingType trainingType;
    private LocalDate date;
    private Duration duration;
}
