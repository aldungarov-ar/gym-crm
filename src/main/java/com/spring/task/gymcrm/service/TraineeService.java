package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.dto.TraineeDto;
import com.spring.task.gymcrm.dto.TraineeTrainersUpdateRequest;
import com.spring.task.gymcrm.dto.TrainerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TraineeService {
    TraineeDto create(@Valid TraineeDto traineeDto);

    TraineeDto getByUsername(@NotNull String username);

    TraineeDto update(@Valid TraineeDto traineeDto);

    void deleteById(long id);

    void deleteByUsername(String username);

    void activate(long id);

    void deActivate(long id);

    void changePassword(@Valid PasswordChangeRequest passwordChangeRequest);

    List<TrainerDto> updateTrainers(TraineeTrainersUpdateRequest traineeTrainersUpdateRequest);
}
