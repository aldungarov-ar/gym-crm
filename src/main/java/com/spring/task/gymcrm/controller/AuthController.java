package com.spring.task.gymcrm.controller;

import com.spring.task.gymcrm.dto.LoginRequest;
import com.spring.task.gymcrm.dto.PasswordChangeRequest;
import com.spring.task.gymcrm.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    @GetMapping("/login")
    public ResponseEntity login(@Valid LoginRequest loginRequest) {
        userService.login(loginRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity changePassword(@Valid PasswordChangeRequest passwordChangeRequest) {
        userService.changePassword(passwordChangeRequest);
        return ResponseEntity.ok().build();
    }
}
