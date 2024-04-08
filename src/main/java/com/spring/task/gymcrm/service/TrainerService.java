package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.dto.TrainerDto;
import com.spring.task.gymcrm.entity.Trainer;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer create(@Valid TrainerDto trainerDto);

    Optional<Trainer> getById(long id);

    Optional<Trainer> getByUsername(@NotNull String username);

    Trainer update(@Valid TrainerDto trainerDto);

    void deleteById(long id);

    void deleteByUsername(String username);

    void activate(long id);

    void deActivate(long id);

    void changePassword(@Valid PasswordChangeRequest passwordChangeRequest);

    @Transactional
    List<Trainer> getTrainersNotAssignToTrainee(@NotNull String traineeUsername);
}
