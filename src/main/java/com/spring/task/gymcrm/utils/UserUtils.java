package com.spring.task.gymcrm.utils;

import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.dto.UserUpdateRequest;
import com.spring.task.gymcrm.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserUtils {

    private UserUtils() {
    }

    public static void validateUserCreateRequest(UserUpdateRequest request) {
        boolean errorOccurred = false;
        String message = "Failed to create new User: \n";
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            message += "First name is missing!\n";
            errorOccurred = true;
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            message += "Last name is missing!\n";
            errorOccurred = true;
        }
        if (errorOccurred) {
            log.error(message);
            throw new UpdateRequestValidationException(message);
        }
    }

    public static void updateUserFields(User user, UserUpdateRequest request) {
        String requestFirstName = request.getFirstName();
        if (requestFirstName != null && !requestFirstName.isEmpty() &&
                !requestFirstName.equals(user.getFirstName())) {
            user.setFirstName(requestFirstName);
        }
        String requestLastName = request.getLastName();
        if (requestLastName != null && !requestLastName.isEmpty() &&
                !requestLastName.equals(user.getLastName())) {
            user.setLastName(requestLastName);
        }
        if (request.isUserIsActive() != user.isActive()) {
            user.setActive(request.isUserIsActive());
        }
    }
}
