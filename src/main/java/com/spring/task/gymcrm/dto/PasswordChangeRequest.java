package com.spring.task.gymcrm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class PasswordChangeRequest {
    @NotNull(message = "Username is required!")
    private String username;
    @Size(min = 10, message = "New password should be at least 10 characters long!")
    private String newPassword;
}
