package com.spring.task.gymcrm.service;


import com.spring.task.gymcrm.dto.*;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.entity.mapper.TraineeMapper;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.repository.TraineeRepository;
import com.spring.task.gymcrm.utils.PasswordUtils;
import com.spring.task.gymcrm.utils.ReflectiveFieldUpdater;
import com.spring.task.gymcrm.utils.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final TrainerService trainerService;


    @Override
    public Trainee create(@Valid TraineeDto traineeDto) {
        traineeDto.getUserDto().setPassword(PasswordUtils.generatePassword());
        Trainee trainee = traineeMapper.toTrainee(traineeDto);

        return traineeRepository.save(trainee);
    }

    @Override
    public Optional<Trainee> getById(long id) {
        return traineeRepository.findById(id);
    }

    @Override
    public Optional<Trainee> getByUsername(@NotNull String username) {
        return traineeRepository.findByUsername(username);
    }

    @Override
    @Validated({ValidationGroups.UpdateOperation.class})
    public Trainee update(@Valid TraineeDto traineeDto) {
        Trainee existingTrainee = traineeRepository.findById(traineeDto.getId()).orElseThrow(() ->
                new EntityNotFoundException("Filed to update Trainee with ID " + traineeDto.getId() + " not found!"));
        updateFields(existingTrainee, traineeDto);
        return traineeRepository.save(existingTrainee);
    }

    private void updateFields(Trainee existingTrainee, TraineeDto traineeDto) {
        ReflectiveFieldUpdater<User, UserDto> userFieldUpdater =
                new ReflectiveFieldUpdater<>(existingTrainee.getUser(), traineeDto.getUserDto());
        userFieldUpdater.updateFields();
        ReflectiveFieldUpdater<Trainee, TraineeDto> traineeFiledUpdater =
                new ReflectiveFieldUpdater<>(existingTrainee, traineeDto);
        traineeFiledUpdater.updateFields();
    }

    @Override
    public void deleteById(long id) {
        traineeRepository.deleteById(id);
    }

    @Override
    public void deleteByUsername(String username) {
        Trainee trainee = getByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("Can not delete Trainee username " + username + " not found!"));
        traineeRepository.delete(trainee);
    }

    @Override
    public void activate(long id) {
        Trainee trainee = getById(id).orElseThrow(() ->
                new EntityNotFoundException("Can not activate Trainee ID " + id + " not found!"));
        trainee.getUser().setIsActive(true);
        traineeRepository.save(trainee);
    }

    @Override
    public void deActivate(long id) {
        Trainee trainee = getById(id).orElseThrow(() ->
                new EntityNotFoundException("Can not deactivate Trainee ID " + id + " not found!"));
        trainee.getUser().setIsActive(false);
        traineeRepository.save(trainee);
    }

    @Override
    public void changePassword(@Valid PasswordChangeRequest passwordChangeRequest) {
        String username = passwordChangeRequest.getUsername();
        Trainee trainee = getByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("Can not change password for Trainee username " + username + " not found!"));
        if (trainee.getUser().getPassword().equals(passwordChangeRequest.getNewPassword())) {
            throw new UpdateRequestValidationException("New password should not be the same as old!");
        }
        trainee.getUser().setPassword(passwordChangeRequest.getNewPassword());
        traineeRepository.save(trainee);
    }

    @Override
    public List<Trainer> updateTrainersList(TrainersListUpdateRequest trainersListUpdateRequest) {
        Trainee trainee = getByUsername(trainersListUpdateRequest.getTraineeUsername()).orElseThrow(() ->
                new EntityNotFoundException("Can not update trainers list for Trainee username " + trainersListUpdateRequest.getTraineeUsername() + " not found!"));
        for (String trainerUsername : trainersListUpdateRequest.getTrainersUsernames()) {
            trainerService.getByUsername(trainerUsername).ifPresent(trainee::addTrainer);
        }
        Trainee savedTrainee = traineeRepository.save(trainee);
        return new ArrayList<>(savedTrainee.getTrainers());
    }
}
