package com.spring.task.gymcrm.utils;

import com.spring.task.gymcrm.dto.TrainerDto;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.TrainingType;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerMapper {
    private final UserMapper userMapper;
    private final TrainingTypeService trainingTypeService;

    public Trainer toTrainer(TrainerDto trainerDto) {
        User user = userMapper.toUser(trainerDto.getUserDto());
        TrainingType trainingType = trainingTypeService.get(trainerDto.getSpecializationId()).orElseThrow(() ->
                new EntityNotFoundException("Failed to create Trainer. Specialization ID: " + trainerDto.getSpecializationId() + " not found!"));
        return Trainer.builder().id(trainerDto.getId())
                .user(user)
                .specialization(trainingType)
                .trainees(trainerDto.getTrainees())
                .trainings(trainerDto.getTrainings())
                .build();
    }
}
