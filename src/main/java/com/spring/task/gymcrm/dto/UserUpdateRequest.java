package com.spring.task.gymcrm.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private boolean userIsActive;
}
