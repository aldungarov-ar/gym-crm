package com.spring.task.gymcrm.dto;

import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.utils.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.Set;

@Data
@UpdateDto(updatesClass = Trainee.class)
@Validated
public class TraineeDto {
    @NotNull(groups = ValidationGroups.UpdateOperation.class,
            message = "Trainee ID required for update operation!")
    private Long id;

    @Past(message = "Date of birth must be in the past!")
    private Date dateOfBirth;

    private String address;
    private UserDto userDto;
    private Set<Trainer> trainers;
    private Set<Training> trainings;
}
