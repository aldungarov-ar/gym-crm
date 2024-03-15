package com.spring.task.gymcrm.v2.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private Boolean isActive;
}
