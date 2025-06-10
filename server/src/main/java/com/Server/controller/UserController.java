package com.Server.controller;

import com.Server.repository.dto.authDTOs.*;
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
    public ResponseEntity<Void> submitRegistrationRequest(@RequestBody UserRequestDTO userRequest) {
        try {
            userService.submitRegistrationRequest(userRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error submitting registration request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    @PostMapping("/send-otp")
    public ResponseEntity<Void> sendOtp(@RequestBody OTPRequestDTO otpRequestDTO) {
        try {
            userService.sendOtp(otpRequestDTO.getEmail());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error sending OTP: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Void> verifyOtp(@RequestBody OTPVerificationDTO otpVerificationDTO) {
        try {
            if (userService.verifyOtp(otpVerificationDTO.getEmail(), otpVerificationDTO.getOtp())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.error("Error verifying OTP: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        try {
            System.out.println("passwordResetDTO.otp() = " + passwordResetDTO.getOtp());
            userService.changePassword(passwordResetDTO.getEmail(), passwordResetDTO.getOtp(), passwordResetDTO.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error changing password: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
