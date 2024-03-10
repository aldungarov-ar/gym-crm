package com.spring.task.gymcrm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordUpdateRq {
    private Long userId;
    private String newPassword;
}
