package com.Server.controller;

import com.Server.repository.EmployeeRepository;
import com.Server.repository.RegistrationRequestRepository;
import com.Server.repository.SubsidiaryRepository;
import com.Server.repository.UserRepository;
import com.Server.repository.dto.*;
import com.Server.repository.entity.*;
import com.Server.service.EmailService;
import com.Server.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
public class UserController {
    private final UserService userService;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final SubsidiaryRepository subsidiaryRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public UserController(UserService userService,
                          EmailService emailService,
                          RegistrationRequestRepository registrationRequestRepository,
                          SubsidiaryRepository subsidiaryRepository,
                          UserRepository userRepository,
                          EmployeeRepository employeeRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.registrationRequestRepository = registrationRequestRepository;
        this.subsidiaryRepository = subsidiaryRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> submitRegistrationRequest(@RequestBody UserRequestDTO userRequest) {
        try {
            Subsidiary subsidiary = subsidiaryRepository.findById(userRequest.getSubsidiaryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid subsidiary ID"));

            Organization organization = subsidiary.getOrganization();
            String adminEmail = organization.getAdminEmail();

            RegistrationRequest request = new RegistrationRequest();
            request.setUsername(userRequest.getUsername());
            request.setEmail(userRequest.getEmail());
            request.setFullName(userRequest.getFullName());

            String encryptedPassword = userService.getPasswordEncoder().encode(userRequest.getPassword());
            request.setPassword(encryptedPassword);
            request.setSubsidiary(subsidiary);
            request.setStatus(RequestStatus.PENDING);
            registrationRequestRepository.save(request);

            Map<String, Object> adminTemplateData = new HashMap<>();
            adminTemplateData.put("fullName", request.getFullName());
            adminTemplateData.put("email", request.getEmail());
            adminTemplateData.put("subsidiary", subsidiary.getSubsidiaryCode());

            emailService.sendTemplateEmail(adminEmail,
                    "[SafetyNet AI] New Registration Request",
                    "admin-notification.ftl",
                    adminTemplateData);

            return new ResponseEntity<>("Registration request submitted successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error submitting registration request: {}", e.getMessage());
            return new ResponseEntity<>("Failed to submit registration request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping("/register/decline/{requestId}")
    public ResponseEntity<String> declineRegistrationRequest(@PathVariable Long requestId) {
        try {
            RegistrationRequest request = registrationRequestRepository.findById(requestId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid request ID"));

            if (request.getStatus() != RequestStatus.PENDING) {
                return new ResponseEntity<>("Request is not in a pending state.", HttpStatus.BAD_REQUEST);
            }

            request.setStatus(RequestStatus.REJECTED);
            registrationRequestRepository.save(request);

            Map<String, Object> userTemplateData = new HashMap<>();
            userTemplateData.put("fullName", request.getFullName());
            userTemplateData.put("message", "Your registration request has been declined. Please contact support for further information.");

            emailService.sendTemplateEmail(request.getEmail(),
                    "[SafetyNet AI] Registration Declined",
                    "user-notification.ftl",
                    userTemplateData);

            return new ResponseEntity<>("Registration request declined successfully.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error declining registration request: {}", e.getMessage());
            return new ResponseEntity<>("Failed to decline registration request.", HttpStatus.INTERNAL_SERVER_ERROR);
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
