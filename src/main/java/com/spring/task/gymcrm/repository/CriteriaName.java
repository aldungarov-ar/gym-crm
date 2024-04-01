package com.spring.task.gymcrm.repository;

import lombok.Getter;

@Getter
public enum CriteriaName {
    FROM_DATE("fromDate"),
    TO_DATE("toDate"),
    TRAINER_USERNAME("trainerUsername"),
    TRAINEE_USERNAME("traineeUsername"),
    TRAINING_TYPE_ID("trainingType");

    private final String value;

    CriteriaName(String value) {
        this.value = value;
    }
}
