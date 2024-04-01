package com.spring.task.gymcrm.dto;

import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.utils.UpdateDto;
import com.spring.task.gymcrm.utils.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Data
@UpdateDto(updatesClass = Trainer.class)
@Validated
public class TrainerDto {

    @NotNull(groups = ValidationGroups.OnUpdate.class,
            message = "Trainer ID required for update operation!")
    private Long id;

    @NotNull(groups = ValidationGroups.OnCreate.class,
            message = "Specialization required for create operation!")
    private Long specializationId;

    private UserDto userDto;
    private Set<Trainee> trainees;
    private Set<Training> trainings;
}
