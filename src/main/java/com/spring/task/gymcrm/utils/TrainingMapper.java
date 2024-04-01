package com.spring.task.gymcrm.utils;

import com.spring.task.gymcrm.dto.TrainingDto;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.entity.TrainingType;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.service.TraineeService;
import com.spring.task.gymcrm.service.TrainerService;
import com.spring.task.gymcrm.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingMapper {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    public Training toTraining(TrainingDto trainingDto) {
        Trainee trainee = findTrainee(trainingDto);
        Trainer trainer = findTrainer(trainingDto);
        TrainingType trainingType = findTrainingType(trainingDto);

        return Training.builder().trainee(trainee)
                .trainer(trainer)
                .trainingName(trainingDto.getTrainingName())
                .trainingType(trainingType)
                .trainingDate(trainingDto.getTrainingDate())
                .trainingDuration(trainingDto.getTrainingDuration())
                .build();
    }

    private TrainingType findTrainingType(TrainingDto trainingDto) {
        Long trainingTypeId = trainingDto.getTrainingTypeId();
        return trainingTypeService.get(trainingTypeId).orElseThrow(() ->
                new EntityNotFoundException("Failed to create Training! Training type ID " + trainingTypeId + " not found!"));
    }

    private Trainer findTrainer(TrainingDto trainingDto) {
        Long trainerId = trainingDto.getTrainerId();
        return trainerService.get(trainerId).orElseThrow(() ->
                new EntityNotFoundException("Failed to create Training! Trainer ID " + trainerId + " not found!"));
    }

    private Trainee findTrainee(TrainingDto trainingDto) {
        Long traineeId = trainingDto.getTraineeId();
        return traineeService.get(traineeId).orElseThrow(() ->
                new EntityNotFoundException("Failed to create Training! Trainee ID " + traineeId + " not found!"));
    }
}
