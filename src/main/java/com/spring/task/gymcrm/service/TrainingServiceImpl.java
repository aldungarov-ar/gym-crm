package com.spring.task.gymcrm.service;


import com.spring.task.gymcrm.dto.TrainingDto;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.repository.CriteriaName;
import com.spring.task.gymcrm.repository.TrainingCriteriaRepository;
import com.spring.task.gymcrm.utils.TrainingMapper;
import com.spring.task.gymcrm.utils.ValidationGroups;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    private final TrainingCriteriaRepository trainingRepository;
    private final TrainingMapper trainingMapper;

    @Override
    @Validated({ValidationGroups.CreateOperation.class})
    @Transactional
    public Training create(@Valid TrainingDto trainingDto) {
        Training training = trainingMapper.toTraining(trainingDto);
        training.getTrainee().addTrainer(training.getTrainer());
        return trainingRepository.save(training);
    }

    public List<Training> findByTraineeUsername(@NotNull String username) {
        return trainingRepository.findByTraineeUsername(username);
    }

    public List<Training> findByTraineeUsernameWithCriteria(@NotNull String username, @NotNull Map<CriteriaName, Object> criteria) {
        return trainingRepository.findByTraineeUsernameWithCriteria(username, criteria);
    }

    public List<Training> findByTrainerUsername(@NotNull String username) {
        return trainingRepository.findByTrainerUsername(username);
    }

    public List<Training> findByTrainerUsernameWithCriteria(@NotNull String username, @NotNull Map<CriteriaName, Object> criteria) {
        return trainingRepository.findByTrainerUsernameWithCriteria(username, criteria);
    }
}
