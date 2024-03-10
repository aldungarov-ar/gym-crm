package com.spring.task.gymcrm.entity;

import com.spring.task.gymcrm.dto.TrainingUpdateRq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Training {
    private Long id;
    private Long traineeId;
    private Long trainerId;
    private String name;
    private Long trainingType;
    private LocalDate date;
    private Duration duration;

    public Training(TrainingUpdateRq trainingUpdateRq) {
        this.traineeId = trainingUpdateRq.getTraineeId();
        this.trainerId = trainingUpdateRq.getTrainerId();
        this.name = trainingUpdateRq.getName();
        this.trainingType = trainingUpdateRq.getTrainingType();
        this.date = trainingUpdateRq.getDate();
        this.duration = trainingUpdateRq.getDuration();
    }
}
