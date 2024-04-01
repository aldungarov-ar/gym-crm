package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.dto.TrainerDto;
import com.spring.task.gymcrm.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer create(TrainerDto trainerDto);

    Optional<Trainer> get(long id);

    Optional<Trainer> get(String username);

    Trainer update(TrainerDto trainerDto);

    void delete(long id);

    void activate(long id);

    void deActivate(long id);

    void changePassword(PasswordChangeRequest passwordChangeRequest);

    List<Trainer> getTrainersNotAssignToTrainee(String traineeUsername);
}
