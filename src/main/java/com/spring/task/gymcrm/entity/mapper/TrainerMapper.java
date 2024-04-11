package com.spring.task.gymcrm.entity.mapper;

import com.spring.task.gymcrm.dto.Credentials;
import com.spring.task.gymcrm.dto.TrainerDto;
import com.spring.task.gymcrm.entity.Trainer;
import com.spring.task.gymcrm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public Credentials toRegistrationAnswer(TrainerDto trainerDto) {
        return userMapper.toCredentials(trainerDto.getUserDto());
    }

    public TrainerDto toDto(Trainer trainer) {
       return TrainerDto.builder().id(trainer.getId())
               .userDto(userMapper.toDto(trainer.getUser()))
               .trainees(trainer.getTrainees())
               .trainings(trainer.getTrainings())
               .build();
    }

    public List<TrainerDto> toDtoList(List<Trainer> trainers) {
        List<TrainerDto> trainerDtoList = new ArrayList<>();
        for (Trainer trainer : trainers) {
            trainerDtoList.add(toDto(trainer));
        }
        return trainerDtoList;
    }
}
