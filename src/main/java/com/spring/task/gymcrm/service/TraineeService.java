package com.spring.task.gymcrm.service;


import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.exception.GetRequestValidationException;
import com.spring.task.gymcrm.exception.UnauthorizedException;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.repository.TraineeRepository;
import com.spring.task.gymcrm.utils.PasswordUtils;
import com.spring.task.gymcrm.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraineeService {
    private final TraineeRepository traineeRepository;

    public Trainee create(Trainee request) {
        log.debug("Creating new Trainee: {}", request);
        validateTraineeCreateRequest(request);
        Trainee trainee = createTraineeFromRequest(request);
        Trainee createdTrainee = traineeRepository.save(trainee);
        log.info("Trainee created successfully with ID: {}", createdTrainee.getId());
        return createdTrainee;
    }

    private void validateTraineeCreateRequest(Trainee request) {
        UserUtils.validateUserCreateRequest(request.getUser());
        boolean errorOccurred = false;
        String message = "Failed to create new Trainee: \n";
        if (request.getDateOfBirth().after(new Date())) {
            message += "User must be born to become trainee!";
            errorOccurred = true;
        }
        if (errorOccurred) {
            log.error(message);
            throw new UpdateRequestValidationException(message);
        }
    }

    private Trainee createTraineeFromRequest(Trainee request) {
        validateTraineeCreateRequest(request);

        User user = new User();
        user.setFirstName(request.getUser().getFirstName());
        user.setLastName(request.getUser().getLastName());
        user.setIsActive(request.getUser().getIsActive());
        user.setPassword(PasswordUtils.generatePassword());

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        return trainee;
    }

    public Trainee update(Trainee request) {
        log.debug("Updating Trainee with ID: {}", request.getId());

        if (!isAuthorized(request)) {
            throw new UnauthorizedException("Authorization failed when trying to update trainee id " + request.getId());
        }

        Trainee trainee = get(request.getId());
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

    public Trainee get(Long id) {
        if (id == null) {
            throw new GetRequestValidationException("Trainee ID must be set!");
        }
        return traineeRepository.findById(id).orElse(null);
    }

    public Trainee get(String username) {
        if (username == null || username.isEmpty()) {
            throw new GetRequestValidationException("Trainee USERNAME must be set!");
        }
        return traineeRepository.findByUser_Username(username).orElse(null);
    }

    public void delete(Trainee trainee) {
        if (!isAuthorized(trainee)) {
            throw new UnauthorizedException("Authorization failed when trying to delete trainee id " + trainee.getId());
        }
        traineeRepository.delete(trainee);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isAuthorized(Trainee request) {
        Long id = request.getId();
        Trainee trainee = traineeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Unauthorized! Trainee id: %s not found!", id)));
        return trainee.getUser().getUsername().equals(request.getUser().getUsername()) &&
                trainee.getUser().getPassword().equals(request.getUser().getPassword());
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
