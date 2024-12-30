package com.Server.controller;

import com.Server.repository.dto.*;
import com.Server.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserRequestDTO userRequest) {
        try {
            UserResponseDTO userResponse = userService.addUser(
                    userRequest.getUsername(),
                    userRequest.getEmail(),
                    userRequest.getFullName(),
                    userRequest.getPassword(),
                    userRequest.getSubsidiaryId()
            );

            return new ResponseEntity<>(userResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error adding user: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO loginResponse = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/log-out")
    public ResponseEntity<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO request) {
        try {
            String accessToken = request.getAccessToken();
            String refreshToken = request.getRefreshToken();

            userService.logout(accessToken, refreshToken);

            LogoutResponseDTO response = new LogoutResponseDTO("Logout successful", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LogoutResponseDTO response = new LogoutResponseDTO("Logout failed: " + e.getMessage(), false);
            return ResponseEntity.status(500).body(response);
        }
    }
}
