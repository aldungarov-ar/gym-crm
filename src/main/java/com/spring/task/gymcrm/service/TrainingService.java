package com.spring.task.gymcrm.service;


import com.spring.task.gymcrm.dto.TrainingCreateRequest;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.dao.TrainingDAO;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingDAO trainingDAO;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    public Training create(TrainingCreateRequest request) {
        log.debug("Creating new Training: {}", request);
        validateTrainingCreateRequest(request);
        Training training = createTrainingFromRequest(request);
        Training createdTraining = trainingDAO.save(training);
        log.info("Training created successfully with ID: {}", createdTraining.getId());
        return createdTraining;
    }

    private Training createTrainingFromRequest(TrainingCreateRequest request) {
        Training training = new Training();
        training.setTraineeId(traineeService.get(request.getTraineeId()).getId());
        training.setTrainerId(trainerService.get(request.getTrainerId()).getId());
        training.setName(request.getName());
        training.setTrainingTypeId(trainingTypeService.get(request.getTrainingTypeId()).getId());
        training.setDate(request.getDate());
        training.setDuration(request.getDuration());
        return training;
    }

    private void validateTrainingCreateRequest(TrainingCreateRequest request) {
        boolean errorOccurred = false;
        String message = "Failed to create new Training: \n";

        String trainerValidationErrorMessage = validateTrainer(request.getTrainerId());
        if (trainerValidationErrorMessage != null && !trainerValidationErrorMessage.isEmpty()) {
            message += trainerValidationErrorMessage;
            errorOccurred = true;
        }

        String traineeValidationErrorMessage = validateTrainee(request.getTraineeId());
        if (traineeValidationErrorMessage != null && !traineeValidationErrorMessage.isEmpty()) {
            message += traineeValidationErrorMessage;
            errorOccurred = true;
        }

        if (request.getName() == null || request.getName().isEmpty()) {
            message += "Training name must be set!\n";
            errorOccurred = true;
        }

        String trainingTypeValidationErrorMessage = validateTrainingType(request.getTrainingTypeId());
        if (trainingTypeValidationErrorMessage != null && !trainingTypeValidationErrorMessage.isEmpty()) {
            message += trainingTypeValidationErrorMessage;
            errorOccurred = true;
        }

        if (request.getDate() == null) {
            message += "Training date must be set!\n";
            errorOccurred = true;
        }
        if (errorOccurred) {
            log.error(message);
            throw new UpdateRequestValidationException(message);
        }
    }

    private String validateTrainer(Long trainerId) {
        String errorMessage = "";
        if (trainerId == null || trainerId < 0) {
            errorMessage += "Trainer ID must be set and > 0!\n";
        } else {
            try {
                trainerService.get(trainerId);
            } catch (EntityNotFoundException e) {
                errorMessage += "Trainer with ID " + trainerId + " not found!\n";
            }
        }

        return errorMessage;
    }

    private String validateTrainee(Long traineeId) {
        String errorMessage = "";
        if (traineeId == null || traineeId < 0) {
            errorMessage += "Trainee ID must be set and > 0!\n";
        } else {
            try {
                traineeService.get(traineeId);
            } catch (EntityNotFoundException e) {
                errorMessage += "Trainee with ID " + traineeId + " not found!\n";
            }
        }

        return errorMessage;
    }

    private String validateTrainingType(Long trainingTypeId) {
        String errorMessage = "";
        if (trainingTypeId == null || trainingTypeId < 0) {
            errorMessage += "Training type ID must be set and > 0!\n";
        } else {
            try {
                trainingTypeService.get(trainingTypeId);
            } catch (EntityNotFoundException e) {
                errorMessage += "Training type with ID " + trainingTypeId + " not found!\n";
            }
        }

        return errorMessage;
    }
}
