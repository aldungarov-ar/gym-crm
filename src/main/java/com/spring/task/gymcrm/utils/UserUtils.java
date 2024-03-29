package com.spring.task.gymcrm.utils;

import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class UserUtils {

    private UserUtils() {
    }

    public static void validateCreateUserRequest(User requestUser) {
        boolean errorOccurred = false;
        String message = "Failed to create new User: \n";

        if (requestUser == null) {
            message += "User create request is missing!\n";
            log.error(message);
            throw new UpdateRequestValidationException(message);
        }
        if (StringUtils.isBlank(requestUser.getFirstName())) {
            message += "First name is missing!\n";
            errorOccurred = true;
        }
        if (StringUtils.isBlank(requestUser.getLastName())) {
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
        if (!Objects.equals(requestUser.getIsActive(), user.getIsActive())) {
            user.setIsActive(requestUser.getIsActive());
        }
    }
}
