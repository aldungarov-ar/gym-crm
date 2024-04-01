package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.dto.TraineeDto;
import com.spring.task.gymcrm.entity.Trainee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface TraineeService {
    Trainee create(@Valid TraineeDto traineeDto);

    Optional<Trainee> get(long id);

    Optional<Trainee> get(@NotNull String username);

    Trainee update(@Valid TraineeDto traineeDto);

    void delete(long id);

    void activate(long id);

    void deActivate(long id);

    void changePassword(@Valid PasswordChangeRequest passwordChangeRequest);
}
