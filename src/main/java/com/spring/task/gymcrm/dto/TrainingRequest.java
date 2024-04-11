package com.spring.task.gymcrm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@Validated
public class TrainingRequest {
    @NotNull
    private String traineeUsername;
    private Date dateFrom;
    private Date dateTo;
    private String trainerName;
    private Long trainingTypeId;
}
