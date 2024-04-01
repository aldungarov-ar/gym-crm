package com.spring.task.gymcrm.entity.mapper;

import com.spring.task.gymcrm.dto.TrainerDto;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerMapper {
    private final UserMapper userMapper;

    public Trainer toTrainer(TrainerDto trainerDto) {
        User user = userMapper.toUser(trainerDto.getUserDto());
        return Trainer.builder().id(trainerDto.getId())
                .user(user)
                .trainees(trainerDto.getTrainees())
                .trainings(trainerDto.getTrainings())
                .build();
    }
}
