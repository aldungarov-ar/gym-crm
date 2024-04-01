package com.spring.task.gymcrm.service;


import com.spring.task.gymcrm.dto.TrainingDto;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.repository.CriteriaName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public interface TrainingService {
    Training create(@Valid TrainingDto trainingDto);

    List<Training> findByTraineeUsername(@NotNull String username);

    List<Training> findByTraineeUsernameWithCriteria(@NotNull String username, @NotNull Map<CriteriaName, Object> criteria);

    List<Training> findByTrainerUsername(@NotNull String username);

    List<Training> findByTrainerUsernameWithCriteria(@NotNull String username, @NotNull Map<CriteriaName, Object> criteria);
}
