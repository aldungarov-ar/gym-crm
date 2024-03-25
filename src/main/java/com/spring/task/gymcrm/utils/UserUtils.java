package com.spring.task.gymcrm.utils;

import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserUtils {

    private UserUtils() {
    }

    public static void validateUserCreateRequest(User requestUser) {
        boolean errorOccurred = false;
        String message = "Failed to create new User: \n";
        if (requestUser.getFirstName() == null || requestUser.getFirstName().isEmpty()) {
            message += "First name is missing!\n";
            errorOccurred = true;
        }
        if (requestUser.getLastName() == null || requestUser.getLastName().isEmpty()) {
            message += "Last name is missing!\n";
            errorOccurred = true;
        }
        if (errorOccurred) {
            log.error(message);
            throw new UpdateRequestValidationException(message);
        }
    }

    public static void updateUserFields(User user, User requestUser) {
        String requestFirstName = requestUser.getFirstName();
        if (requestFirstName != null && !requestFirstName.isEmpty() &&
                !requestFirstName.equals(user.getFirstName())) {
            user.setFirstName(requestFirstName);
        }
        String requestLastName = requestUser.getLastName();
        if (requestLastName != null && !requestLastName.isEmpty() &&
                !requestLastName.equals(user.getLastName())) {
            user.setLastName(requestLastName);
        }
        if (requestUser.getIsActive() != user.getIsActive()) {
            user.setIsActive(requestUser.getIsActive());
        }
    }
}
