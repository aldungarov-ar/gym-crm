package com.spring.task.gymcrm.service;

import com.spring.task.gymcrm.dto.LoginRequest;
import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.entity.User;
import com.spring.task.gymcrm.exception.EntityNotFoundException;
import com.spring.task.gymcrm.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public void changePassword(@Valid PasswordChangeRequest request) {
        log.debug("Changing password for user with Username: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Invalid username or password!"));

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        log.info("Password changed successfully for user with Username: {}", request.getUsername());
    }

    public void login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("Invalid username or password!"));
        if (!user.getPassword().equals(password)) {
            throw new EntityNotFoundException("Invalid username or password!");
        }
    }
}
