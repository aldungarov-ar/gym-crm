package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.dto.TraineeDto;
import com.spring.task.gymcrm.dto.TrainersListUpdateRequest;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.Trainer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee create(@Valid TraineeDto traineeDto);

    Optional<Trainee> getById(long id);

    Optional<Trainee> getByUsername(@NotNull String username);

    Trainee update(@Valid TraineeDto traineeDto);

    void deleteById(long id);

    void deleteByUsername(String username);

    void activate(long id);

    void deActivate(long id);

    void changePassword(@Valid PasswordChangeRequest passwordChangeRequest);

    List<Trainer> updateTrainersList(TrainersListUpdateRequest trainersListUpdateRequest);
}
