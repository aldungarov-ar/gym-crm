package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.exception.UnauthorizedException;
import com.spring.task.gymcrm.exception.UpdateRequestValidationException;
import com.spring.task.gymcrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public void changePassword(PasswordChangeRequest request) {
        log.debug("Changing password for user with Username: {}", request.getUsername());

        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new UpdateRequestValidationException("New password must be different from the old password");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found with Username: " + request.getUsername()));

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new UnauthorizedException("Old password is incorrect");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        log.info("Password changed successfully for user with Username: {}", request.getUsername());
    }
}
