package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.TrainingType;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.exception.RequestValidationException;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.repository.TrainerRepository;
import com.spring.task.gymcrm.utils.PasswordUtils;
import com.spring.task.gymcrm.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingTypeService trainingTypeService;

    public Trainer create(Trainer request) {
        log.debug("Creating new Trainer: {}", request);
        validateTrainerCreateRequest(request);
        Trainer trainer = createTrainerFromRequest(request);
        Trainer createdTrainee = trainerRepository.save(trainer);
        log.info("Trainer created successfully with ID: {}", createdTrainee.getId());
        return createdTrainee;
    }

    private void validateTrainerCreateRequest(Trainer request) {
        UserUtils.validateCreateUserRequest(request.getUser());

        TrainingType trainingType = trainingTypeService.get(request.getSpecialization().getId());
        if (trainingType == null) {
            throw new EntityNotFoundException("Training type with ID " + request.getSpecialization().getId() + " not found!");
        }
    }

    private Trainer createTrainerFromRequest(Trainer request) {
        User user = new User();
        user.setFirstName(request.getUser().getFirstName());
        user.setLastName(request.getUser().getLastName());
        user.setIsActive(request.getUser().getIsActive());
        user.setPassword(PasswordUtils.generatePassword());

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        Long specializationId = request.getSpecialization().getId();
        TrainingType trainingType = trainingTypeService.get(specializationId);
        trainer.setSpecialization(trainingType);
        return trainer;
    }

    public Trainer update(Trainer request) {
        if (request == null) {
            throw new UpdateRequestValidationException("Update Trainer failed: request must not be null!");
        }

        log.debug("Updating Trainer with ID: {}", request.getId());

        Trainer trainer = get(request.getId()).orElseThrow(() -> new EntityNotFoundException("Failed to update Trainer. Trainer ID: " + request.getId() + " not found!"));

        if (request.getSpecialization() != null) {
            trainer.setSpecialization(request.getSpecialization());
        }
        if (request.getUser() != null) {
            UserUtils.updateUserFields(trainer.getUser(), request.getUser());
        }
        return trainerRepository.save(trainer);
    }

    public Optional<Trainer> get(Long id) {
        if (id == null) {
            throw new RequestValidationException("Trainer ID must be set!");
        }
        return trainerRepository.findById(id);
    }

    public Optional<Trainer> get(String username) {
        if (username == null || username.isEmpty()) {
            throw new RequestValidationException("Trainer USERNAME must be set!");
        }
        return trainerRepository.findByUsername(username);
    }

    public void activate(Long id) {
        if (id == null) {
            throw new UpdateRequestValidationException("Trainer ID must be set!");
        }
        Trainer trainer = trainerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Failed to activate Trainer. Trainer ID: " + id + " not found!"));
        trainer.getUser().setIsActive(true);
        trainerRepository.save(trainer);
    }

    public void deActivate(Long id) {
        if (id == null) {
            throw new UpdateRequestValidationException("Trainer ID must be set!");
        }
        Trainer trainer = trainerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Failed to activate Trainer. Trainer ID: " + id + " not found!"));
        trainer.getUser().setIsActive(false);
        trainerRepository.save(trainer);
    }

    public List<Trainer> getTrainersNotAssigned(String traineeUserName) {
        return trainerRepository.findTrainersNotAssignedByTraineeUsernameIgnoreCase(traineeUserName);
    }
}
