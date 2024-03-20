package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dao.TrainerDAO;
import com.spring.task.gymcrm.dto.TrainerUpdateRequest;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.utils.PasswordUtils;
import com.spring.task.gymcrm.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerService {
    private final TrainerDAO trainerDAO;
    private final TrainingTypeService trainingTypeService;

    public Trainer create(TrainerUpdateRequest request) {
        log.debug("Creating new Trainer: {}", request);
        validateTrainerCreateRequest(request);
        Trainer trainer = createTrainerFromRequest(request);
        Trainer createdTrainee = trainerDAO.save(trainer);
        log.info("Trainer created successfully with ID: {}", createdTrainee.getId());
        return createdTrainee;
    }

    private void validateTrainerCreateRequest(TrainerUpdateRequest request) {
        UserUtils.validateUserCreateRequest(request.getUserUpdateRequest());

        boolean errorOccurred = false;
        String message = "Failed to create new Trainer: \n";

        String trainingTypeValidationErrorMessage = validateTrainingType(request.getSpecializationId());
        if (!trainingTypeValidationErrorMessage.isEmpty()) {
            message += trainingTypeValidationErrorMessage;
            errorOccurred = true;
        }

        if (errorOccurred) {
            log.error(message);
            throw new UpdateRequestValidationException(message);
        }
    }

    private Trainer createTrainerFromRequest(TrainerUpdateRequest request) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(request.getUserUpdateRequest().getFirstName());
        trainer.setLastName(request.getUserUpdateRequest().getLastName());
        trainer.setActive(request.getUserUpdateRequest().isActiveUser());
        trainer.setPassword(PasswordUtils.generatePassword());

        trainer.setSpecializationId(request.getSpecializationId());
        return trainer;
    }

    public Trainer update(TrainerUpdateRequest request) {
        log.debug("Updating Trainer with ID: {}. Update request: {}.", request.getUserId(), request);
        Trainer trainer = get(request.getUserId());
        if (request.getSpecializationId() != null && request.getSpecializationId() >= 0) {
            trainer.setSpecializationId(request.getSpecializationId());
        }
        if (request.getUserUpdateRequest() != null) {
            UserUtils.updateUserFields(trainer, request.getUserUpdateRequest());
        }
        return trainerDAO.save(trainer);
    }

    public Trainer get(Long userId) {
        Trainer trainer = trainerDAO.findById(userId);
        if (trainer == null) {
            log.error("Trainee with ID {} not found.", userId);
            throw new EntityNotFoundException("Trainee with ID " + userId + " not found.");
        }
        return trainer;
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
