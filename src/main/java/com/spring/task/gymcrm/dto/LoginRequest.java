package com.spring.task.gymcrm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull(message = "Username required!")
    private String username;
    @NotNull(message = "Password required!")
    private String password;
}
