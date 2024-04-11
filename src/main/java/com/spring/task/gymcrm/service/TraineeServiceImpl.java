package com.spring.task.gymcrm.service;


import com.spring.task.gymcrm.dto.*;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.entity.mapper.TraineeMapper;
import com.spring.task.gymcrm.entity.mapper.TrainerMapper;
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
import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;

    @Override
    public TraineeDto getByUsername(String username) {
        Trainee trainee = findByUsername(username);
        return traineeMapper.toDto(trainee);
    }

    @Override
    public TraineeDto create(@Valid TraineeDto traineeDto) {
        traineeDto.getUserDto().setPassword(PasswordUtils.generatePassword());
        Trainee trainee = traineeMapper.toTrainee(traineeDto);
        Trainee savedTrainee = traineeRepository.save(trainee);

        return traineeMapper.toDto(savedTrainee);
    }

    @Override
    @Validated({ValidationGroups.UpdateOperation.class})
    public TraineeDto update(@Valid TraineeDto traineeDto) {
        Trainee existingTrainee = traineeRepository.findById(traineeDto.getId()).orElseThrow(() ->
                new EntityNotFoundException("Failed to update Trainee with ID " + traineeDto.getId() + " not found!"));
        updateFields(existingTrainee, traineeDto);
        Trainee savedTrainee = traineeRepository.save(existingTrainee);

        return traineeMapper.toDto(savedTrainee);
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
        Trainee trainee = findByUsername(username);
        traineeRepository.delete(trainee);
    }

    @Override
    public void activate(long id) {
        Trainee trainee = findById(id);
        trainee.getUser().setIsActive(true);
        traineeRepository.save(trainee);
    }

    @Override
    public void deActivate(long id) {
        Trainee trainee = findById(id);
        trainee.getUser().setIsActive(false);
        traineeRepository.save(trainee);
    }

    @Override
    public void changePassword(@Valid PasswordChangeRequest passwordChangeRequest) {
        String username = passwordChangeRequest.getUsername();
        Trainee trainee = findByUsername(username);
        if (trainee.getUser().getPassword().equals(passwordChangeRequest.getNewPassword())) {
            throw new UpdateRequestValidationException("New password should not be the same as old!");
        }
        trainee.getUser().setPassword(passwordChangeRequest.getNewPassword());
        traineeRepository.save(trainee);
    }

    @Override
    public List<TrainerDto> updateTrainers(TraineeTrainersUpdateRequest traineeTrainersUpdateRequest) {
        Trainee trainee = findByUsername(traineeTrainersUpdateRequest.getTraineeUsername());
        for (String trainerUsername : traineeTrainersUpdateRequest.getTrainersUsernames()) {
            trainerService.getByUsername(trainerUsername).ifPresent(trainee::addTrainer);
        }
        Trainee savedTrainee = traineeRepository.save(trainee);
        Set<Trainer> trainers = savedTrainee.getTrainers();
        return trainerMapper.toDtoList(new ArrayList<>(trainers));
    }

    private Trainee findById(long id) {
        return traineeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Trainee with ID " + id + " not found!"));
    }

    private Trainee findByUsername(@NotNull String username) {
        return traineeRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("Trainee with username " + username + " not found!"));
    }
}
