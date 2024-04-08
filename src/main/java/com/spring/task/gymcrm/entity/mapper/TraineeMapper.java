package com.spring.task.gymcrm.entity.mapper;

import com.spring.task.gymcrm.dto.RegistrationAnswer;
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

    public TraineeDto toDto(Trainee trainee) {
        return TraineeDto.builder().id(trainee.getId())
                .dateOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .userDto(userMapper.toDto(trainee.getUser()))
                .trainers(trainee.getTrainers())
                .trainings(trainee.getTrainings())
                .build();
    }

    public RegistrationAnswer toRegistrationAnswer(Trainee trainee) {
        return userMapper.toRegistrationAnswer(trainee.getUser());
    }
}
