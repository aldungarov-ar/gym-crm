package com.spring.task.gymcrm.service;


import com.spring.task.gymcrm.dto.TrainingDto;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.repository.CriteriaName;

import java.util.List;
import java.util.Map;

public interface TrainingService {
    Training create(TrainingDto trainingDto);

    List<Training> findByTraineeUsername(String username);

    List<Training> findByTraineeUsernameWithCriteria(String username, Map<CriteriaName, Object> criteria);

    List<Training> findByTrainerUsername(String username);

    List<Training> findByTrainerUsernameWithCriteria(String username, Map<CriteriaName, Object> criteria);
}
