package com.spring.task.gymcrm.entity.mapper;

import com.spring.task.gymcrm.dto.RegistrationAnswer;
import com.spring.task.gymcrm.dto.UserDto;
import com.spring.task.gymcrm.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUser(UserDto userDto) {
        return User.builder().id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getFirstName() + "." + userDto.getLastName())
                .password(userDto.getPassword())
                .isActive(userDto.getIsActive())
                .build();
    }

    public UserDto toDto(User user) {
        return UserDto.builder().id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .isActive(user.getIsActive())
                .build();
    }

    public RegistrationAnswer toRegistrationAnswer(User user) {
        RegistrationAnswer registrationAnswer = new RegistrationAnswer();
        registrationAnswer.setUsername(user.getUsername());
        registrationAnswer.setPassword(user.getPassword());

        return registrationAnswer;
    }
}
