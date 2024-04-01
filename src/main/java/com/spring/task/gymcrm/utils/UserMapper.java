package com.spring.task.gymcrm.utils;

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
}
