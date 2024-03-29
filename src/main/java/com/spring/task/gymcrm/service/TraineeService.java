package com.spring.task.gymcrm.service;


import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.exception.RequestValidationException;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.repository.TraineeRepository;
import com.spring.task.gymcrm.utils.PasswordUtils;
import com.spring.task.gymcrm.utils.UserUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraineeService {
    private final TraineeRepository traineeRepository;

    public Trainee create(Trainee request) {
        log.debug("Creating new Trainee: {}", request);
        validateCreateTraineeRequest(request);
        Trainee trainee = getTraineeFromRequest(request);
        Trainee createdTrainee = traineeRepository.save(trainee);
        log.info("Trainee created successfully with ID: {}", createdTrainee.getId());
        return createdTrainee;
    }

    private void validateCreateTraineeRequest(Trainee request) {
        UserUtils.validateCreateUserRequest(request.getUser());
        boolean errorOccurred = false;
        String message = "Failed to create new Trainee: \n";
        if (request.getDateOfBirth() != null && request.getDateOfBirth().after(new Date())) {
            message += "User must be born to become trainee!";
            errorOccurred = true;
        }
        if (errorOccurred) {
            log.error(message);
            throw new UpdateRequestValidationException(message);
        }
    }

    private Trainee getTraineeFromRequest(Trainee request) {
        User requestUser = request.getUser();
        UserUtils.validateCreateUserRequest(requestUser);

        User user = User.builder()
                .firstName(requestUser.getFirstName())
                .lastName(requestUser.getLastName())
                .isActive(requestUser.getIsActive())
                .password(PasswordUtils.generatePassword())
                .build();

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        return trainee;
    }

    public Trainee update(Trainee request) {
        if (request == null) {
            throw new UpdateRequestValidationException("Update Trainee failed: request must not be null!");
        }

        log.debug("Updating Trainee with ID: {}", request.getId());

        Trainee trainee = get(request.getId()).orElseThrow(() -> new EntityNotFoundException("Failed to update Trainee. Trainee ID: " + request.getId() + " not found!"));
        if (request.getDateOfBirth() != null && !request.getDateOfBirth().equals(trainee.getDateOfBirth()) &&
                request.getDateOfBirth().before(new Date())) {
            trainee.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null && !request.getAddress().equals(trainee.getAddress())) {
            trainee.setAddress(request.getAddress());
        }
        if (request.getUser() != null) {
            UserUtils.updateUserFields(trainee.getUser(), request.getUser());
        }
        return traineeRepository.save(trainee);
    }

    public Optional<Trainee> get(Long id) {
        if (id == null) {
            throw new RequestValidationException("Trainee ID must be set!");
        }
        return traineeRepository.findById(id);
    }

    public Optional<Trainee> get(String username) {
        if (StringUtils.isBlank(username)) {
            throw new RequestValidationException("Trainee USERNAME must be set!");
        }
        return traineeRepository.findByUsername(username);
    }

    public void delete(Trainee trainee) {
        traineeRepository.delete(trainee);
    }

    public void activate(Long id) {
        if (id == null) {
            throw new UpdateRequestValidationException("Trainee ID must be set!");
        }
        Trainee trainee = traineeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Failed to activate Trainee. Trainee ID: " + id + " not found!"));
        trainee.getUser().setIsActive(true);
        traineeRepository.save(trainee);
    }

    public void deActivate(Long id) {
        if (id == null) {
            throw new UpdateRequestValidationException("Trainee ID must be set!");
        }
        Trainee trainee = traineeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Failed to activate Trainee. Trainee ID: " + id + " not found!"));
        trainee.getUser().setIsActive(false);
        traineeRepository.save(trainee);
    }
}
