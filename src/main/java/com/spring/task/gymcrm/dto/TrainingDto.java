package com.spring.task.gymcrm.dto;

import com.spring.task.gymcrm.utils.UpdateDto;
import com.spring.task.gymcrm.utils.ValidationGroups;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@UpdateDto(updatesClass = TrainingDto.class)
@Validated
public class TrainingDto {

    @NotNull(groups = ValidationGroups.UpdateOperation.class,
            message = "Training ID required for update operation!")
    private Long id;

    @NotNull(groups = ValidationGroups.CreateOperation.class,
            message = "Trainee must be set!")
    private Long traineeId;

    @NotNull(groups = ValidationGroups.CreateOperation.class,
            message = "Trainer must be set!")
    private Long trainerId;

    @NotNull(groups = ValidationGroups.CreateOperation.class,
            message = "Training name must be set!")
    @Size(min = 2, max = 50, message = "Training name must be between 2 and 50 characters!")
    private String trainingName;

    @NotNull(groups = ValidationGroups.CreateOperation.class,
            message = "Training type ID must be set!")
    private Long trainingTypeId;

    @NotNull(groups = ValidationGroups.CreateOperation.class,
            message = "Training date must be set!")
    private Date trainingDate;

    @NotNull(groups = ValidationGroups.CreateOperation.class,
            message = "Training duration must be set!")
    @Min(value = 1, message = "Training duration must be at least 1 minute!")
    private Integer trainingDuration;
}
