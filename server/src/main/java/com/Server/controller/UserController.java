package com.Server.controller;

import com.Server.exception.OrganizationNotFoundException;
import com.Server.exception.SubsidiaryNotFoundException;
import com.Server.repository.OrganizationRepository;
import com.Server.repository.SubsidiaryRepository;
import com.Server.repository.UserRepository;
import com.Server.repository.dto.*;
import com.Server.repository.entity.Employee;
import com.Server.repository.entity.Organization;
import com.Server.repository.entity.Subsidiary;
import com.Server.security.TokenBlacklistService;
import com.Server.service.UserService;
import com.Server.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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
                    userRequest.getOrganizationId(),
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
            if (loginResponse != null) {
                String accessToken = jwtUtil.generateAccessToken(loginResponse.getUsername());
                String refreshToken = jwtUtil.generateRefreshToken(loginResponse.getUsername());

                loginResponse.setAccessToken(accessToken);
                loginResponse.setRefreshToken(refreshToken);

                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
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
