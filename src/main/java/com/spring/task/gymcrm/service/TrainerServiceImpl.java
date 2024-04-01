package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.dto.TrainerDto;
import com.spring.task.gymcrm.dto.UserDto;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.TrainingType;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.repository.TrainerRepository;
import com.spring.task.gymcrm.utils.PasswordUtils;
import com.spring.task.gymcrm.utils.ReflectiveFieldUpdater;
import com.spring.task.gymcrm.entity.mapper.TrainerMapper;
import com.spring.task.gymcrm.utils.ValidationGroups;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingTypeService trainingTypeService;
    private final TrainerMapper trainerMapper;

    @Override
    public Trainer create(@Valid TrainerDto trainerDto) {
        trainerDto.getUserDto().setPassword(PasswordUtils.generatePassword());
        Trainer trainer = trainerMapper.toTrainer(trainerDto);
        Long specializationId = trainerDto.getSpecializationId();
        TrainingType trainingType = trainingTypeService.get(specializationId).orElseThrow(() ->
                new EntityNotFoundException("Failed to create Trainer. Specialization ID: " + specializationId + " not found!"));
        trainer.setSpecialization(trainingType);
        return trainerRepository.save(trainer);
    }

    @Override
    public Optional<Trainer> getById(long id) {
        return trainerRepository.findById(id);
    }

    @Override
    public Optional<Trainer> getByUsername(@NotNull String username) {
        return trainerRepository.findByUsername(username);
    }

    @Override
    @Validated({ValidationGroups.UpdateOperation.class})
    public Trainer update(@Valid TrainerDto trainerDto) {
        Trainer existingTrainer = trainerRepository.findById(trainerDto.getId()).orElseThrow(() ->
                new EntityNotFoundException("Filed to update Trainee with ID " + trainerDto.getId() + " not found!"));
        updateFields(existingTrainer, trainerDto);
        return trainerRepository.save(existingTrainer);
    }

    private void updateFields(Trainer existingTrainer, TrainerDto trainerDto) {
        ReflectiveFieldUpdater<User, UserDto> userFieldUpdater =
                new ReflectiveFieldUpdater<>(existingTrainer.getUser(), trainerDto.getUserDto());
        userFieldUpdater.updateFields();
        ReflectiveFieldUpdater<Trainer, TrainerDto> traineeFiledUpdater =
                new ReflectiveFieldUpdater<>(existingTrainer, trainerDto);
        traineeFiledUpdater.updateFields();

        Long specializationId = trainerDto.getSpecializationId();
        TrainingType trainingType = trainingTypeService.get(specializationId).orElseThrow(() ->
                new EntityNotFoundException("Failed to create Trainer. Specialization ID: " + specializationId + " not found!"));
        existingTrainer.setSpecialization(trainingType);
    }

    @Override
    public void delete(long id) {
        trainerRepository.deleteById(id);
    }

    @Override
    public void activate(long id) {
        Trainer trainer = getById(id).orElseThrow(() ->
                new EntityNotFoundException("Can not activate Trainee ID " + id + " not found!"));
        trainer.getUser().setIsActive(true);
        trainerRepository.save(trainer);
    }

    @Override
    public void deActivate(long id) {
        Trainer trainer = getById(id).orElseThrow(() ->
                new EntityNotFoundException("Can not activate Trainee ID " + id + " not found!"));
        trainer.getUser().setIsActive(false);
        trainerRepository.save(trainer);
    }

    @Override
    public void changePassword(@Valid PasswordChangeRequest passwordChangeRequest) {
        String username = passwordChangeRequest.getUsername();
        Trainer trainer = getByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("Can not change password for Trainer username " + username + " not found!"));
        if (trainer.getUser().getPassword().equals(passwordChangeRequest.getNewPassword())) {
            throw new UpdateRequestValidationException("New password should not be the same as old!");
        }
        trainer.getUser().setPassword(passwordChangeRequest.getNewPassword());
        trainerRepository.save(trainer);
    }

    @Override
    @Transactional
    public List<Trainer> getTrainersNotAssignToTrainee(@NotNull String traineeUsername) {
        return trainerRepository.findTrainersNotAssignedByTraineeUsernameIgnoreCase(traineeUsername);
    }
}
