package com.spring.task.gymcrm.dto;

import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.utils.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@UpdateDto(updatesClass = User.class)
@Validated
public class UserDto {
    @NotNull(groups = ValidationGroups.UpdateOperation.class,
            message = "User ID required for update operation!")
    private Long id;

    @NotNull(groups = ValidationGroups.CreateOperation.class,
            message = "First name is required!")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters!")
    private String firstName;

    @NotNull(groups = ValidationGroups.CreateOperation.class,
            message = "Last name is required!")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters!")
    private String lastName;

    private String password;
    private Boolean isActive;
}
