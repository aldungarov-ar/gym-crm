package com.spring.task.gymcrm.service;


import com.spring.task.gymcrm.dto.TrainingCreateRequest;
import com.spring.task.gymcrm.entity.Training;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.exception.RequestValidationException;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.repository.CriteriaName;
import com.spring.task.gymcrm.repository.TrainingCriteriaRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingCriteriaRepository trainingRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    public Training create(TrainingCreateRequest request) {
        log.debug("Creating new Training: {}", request);
        validateTrainingCreateRequest(request);
        Training training = createTrainingFromRequest(request);
        Training createdTraining = trainingRepository.save(training);
        log.info("Training created successfully with ID: {}", createdTraining.getId());
        return createdTraining;
    }

    private Training createTrainingFromRequest(TrainingCreateRequest request) {
        Training training = new Training();
        training.setTrainee(traineeService.get(request.getTraineeId())
                .orElseThrow(() -> new EntityNotFoundException("Failed to create Training: Trainee not found!")));
        training.setTrainer(trainerService.get(request.getTrainerId())
                .orElseThrow(() -> new EntityNotFoundException("Failed to create Training: Trainer not found!")));
        training.setTrainingName(request.getName());
        training.setTrainingType(trainingTypeService.get(request.getTrainingTypeId()));
        training.setTrainingDate(request.getDate());
        training.setTrainingDuration(request.getDuration());
        return training;
    }

    private void validateTrainingCreateRequest(TrainingCreateRequest request) {
        List<Function<TrainingCreateRequest, String>> validations = Arrays.asList(
                this::validateTrainee,
                this::validateTrainer,
                this::validateTrainingType
        );

        boolean errorOccurred = false;
        StringBuilder message = new StringBuilder("Failed to create new Training: \n");

        for (Function<TrainingCreateRequest, String> validation : validations) {
            String errorMessage = validation.apply(request);
            if (!StringUtils.isBlank(errorMessage)) {
                message.append(errorMessage);
                errorOccurred = true;
            }
        }

        if (errorOccurred) {
            log.error(message.toString());
            throw new UpdateRequestValidationException(message.toString());
        }
    }


    /*private void validateTrainingCreateRequest(TrainingCreateRequest request) {
        boolean errorOccurred = false;
        String message = "Failed to create new Training: \n";

        String trainerValidationErrorMessage = validateTrainer(request.getTrainerId());
        if (!StringUtils.isBlank(trainerValidationErrorMessage)) {
            message += trainerValidationErrorMessage;
            errorOccurred = true;
        }

        String traineeValidationErrorMessage = validateTrainee(request.getTraineeId());
        if (!StringUtils.isBlank(traineeValidationErrorMessage)) {
            message += traineeValidationErrorMessage;
            errorOccurred = true;
        }

        if (StringUtils.isBlank(request.getName())) {
            message += "Training name must be set!\n";
            errorOccurred = true;
        }

        String trainingTypeValidationErrorMessage = validateTrainingType(request.getTrainingTypeId());
        if (StringUtils.isBlank(trainingTypeValidationErrorMessage)) {
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
    }*/

    private String validateTrainer(TrainingCreateRequest request) {
        Long trainerId = request.getTrainerId();
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

    private String validateTrainee(TrainingCreateRequest request) {
        Long traineeId = request.getTraineeId();
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

    private String validateTrainingType(TrainingCreateRequest request) {
        Long trainingTypeId = request.getTrainingTypeId();
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

    public List<Training> findByTraineeUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new RequestValidationException("Could not find training. TRAINEE username is not provided!");
        }
        return trainingRepository.findByTraineeUsername(username);
    }

    public List<Training> findByTraineeUsernameWithCriteria(String username, Map<CriteriaName, Object> criteria) {
        return trainingRepository.findByTraineeUsernameWithCriteria(username, criteria);
    }

    public List<Training> findByTrainerUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new RequestValidationException("Could not find training. TRAINER username is not provided!");
        }
        return trainingRepository.findByTrainerUsername(username);
    }

    public List<Training> findByTrainerUsernameWithCriteria(String username, Map<CriteriaName, Object> criteria) {
        return trainingRepository.findByTrainerUsernameWithCriteria(username, criteria);
    }
}
