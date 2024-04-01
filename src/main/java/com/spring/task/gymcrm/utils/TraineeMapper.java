package com.spring.task.gymcrm.utils;

import com.spring.task.gymcrm.dto.TraineeDto;
import com.spring.task.gymcrm.entity.Trainee;
import com.spring.task.gymcrm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraineeMapper {
    private final UserMapper userMapper;
    public Trainee toTrainee(TraineeDto traineeDto) {
        User user = userMapper.toUser(traineeDto.getUserDto());
        return Trainee.builder().id(traineeDto.getId())
                .dateOfBirth(traineeDto.getDateOfBirth())
                .address(traineeDto.getAddress())
                .user(user)
                .trainers(traineeDto.getTrainers())
                .trainings(traineeDto.getTrainings())
                .build();
    }
}
