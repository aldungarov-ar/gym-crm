package com.spring.task.gymcrm.service;


import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.utils.PasswordUtils;
import com.spring.task.gymcrm.dao.TraineeDAO;
import com.spring.task.gymcrm.dto.TraineeUpdateRequest;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraineeService {
    private final TraineeDAO traineeDAO;

    public Trainee create(TraineeUpdateRequest request) {
        log.debug("Creating new Trainee: {}", request);
        validateTraineeCreateRequest(request);
        Trainee trainee = createTraineeFromRequest(request);
        Trainee createdTrainee = traineeDAO.save(trainee);
        log.info("Trainee created successfully with ID: {}", createdTrainee.getId());
        return createdTrainee;
    }

    private void validateTraineeCreateRequest(TraineeUpdateRequest request) {
        UserUtils.validateUserCreateRequest(request.getUserUpdateRequest());
        boolean errorOccurred = false;
        String message = "Failed to create new Trainee: \n";
        if (request.getDateOfBirth().isAfter(LocalDate.now())) {
            message += "User must be born to become trainee!";
            errorOccurred = true;
        }
        if (errorOccurred) {
            log.error(message);
            throw new UpdateRequestValidationException(message);
        }
    }

    private Trainee createTraineeFromRequest(TraineeUpdateRequest request) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(request.getUserUpdateRequest().getFirstName());
        trainee.setLastName(request.getUserUpdateRequest().getLastName());
        trainee.setActive(request.getUserUpdateRequest().isUserIsActive());
        trainee.setPassword(PasswordUtils.generatePassword());

        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        return trainee;
    }

    public Trainee update(TraineeUpdateRequest request) {
        log.debug("Updating Trainee with ID: {}. Update request: {}.", request.getUserId(), request);
        Trainee trainee = get(request.getUserId());
        if (request.getDateOfBirth() != null && !request.getDateOfBirth().equals(trainee.getDateOfBirth()) &&
                request.getDateOfBirth().isBefore(LocalDate.now())) {
            trainee.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null && !request.getAddress().equals(trainee.getAddress())) {
            trainee.setAddress(request.getAddress());
        }
        if (request.getUserUpdateRequest() != null) {
            UserUtils.updateUserFields(trainee, request.getUserUpdateRequest());
        }
        return traineeDAO.save(trainee);
    }

    public Trainee get(Long id) {
        if (id == null) {
            throw new UpdateRequestValidationException("Trainee ID must be set!");
        }
        return traineeDAO.findById(id);
    }

    public boolean delete(Long userId) {
        Trainee trainee = get(userId);
        return traineeDAO.delete(trainee.getId());
    }
}
