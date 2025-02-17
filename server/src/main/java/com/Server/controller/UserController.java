package com.Server.controller;

import com.Server.repository.dto.authDTOs.LoginRequestDTO;
import com.Server.repository.dto.authDTOs.LoginResponseDTO;
import com.Server.repository.dto.authDTOs.LogoutRequestDTO;
import com.Server.repository.dto.authDTOs.LogoutResponseDTO;
import com.Server.repository.dto.userDTOs.UserRequestDTO;
import com.Server.service.interfaces.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<String> submitRegistrationRequest(@RequestBody UserRequestDTO userRequest) {
        try {
            userService.submitRegistrationRequest(userRequest);
            return new ResponseEntity<>("Registration request submitted successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error submitting registration request: {}", e.getMessage());
            return new ResponseEntity<>("Failed to submit registration request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
            userService.logout(request.getAccessToken(), request.getRefreshToken());
            LogoutResponseDTO response = new LogoutResponseDTO("Logout successful", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LogoutResponseDTO response = new LogoutResponseDTO("Logout failed: " + e.getMessage(), false);
            return ResponseEntity.status(500).body(response);
        }
    }
}