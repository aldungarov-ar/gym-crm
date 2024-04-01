package com.spring.task.gymcrm.dto;

import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.utils.UpdateDto;
import com.spring.task.gymcrm.utils.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@UpdateDto(updatesClass = User.class)
@Validated
public class UserDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class,
            message = "User ID required for update operation!")
    private Long id;

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters!")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters!")
    private String lastName;

    private String password;
    private Boolean isActive;
}
