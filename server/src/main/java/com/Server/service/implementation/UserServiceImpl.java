
package com.Server.service.implementation;

import com.Server.exception.UserNotFoundException;
import com.Server.repository.EmployeeRepository;
import com.Server.repository.RegistrationRequestRepository;
import com.Server.repository.SubsidiaryRepository;
import com.Server.repository.UserRepository;
import com.Server.repository.dto.authDTOs.LoginResponseDTO;
import com.Server.repository.dto.userDTOs.UserRequestDTO;
import com.Server.repository.entity.*;
import com.Server.repository.entity.enums.RequestStatus;
import com.Server.repository.entity.enums.Role;
import com.Server.security.JwtUtil;
import com.Server.service.interfaces.EmailService;
import com.Server.service.interfaces.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Log4j2
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final SubsidiaryRepository subsidiaryRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Set<String> blacklistedTokens = new HashSet<>();

    public UserServiceImpl(UserRepository userRepository,
                           RegistrationRequestRepository registrationRequestRepository,
                           SubsidiaryRepository subsidiaryRepository,
                           EmployeeRepository employeeRepository,
                           EmailService emailService,
                           ModelMapper modelMapper,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.registrationRequestRepository = registrationRequestRepository;
        this.subsidiaryRepository = subsidiaryRepository;
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    private void initialize() {
        blacklistedTokens.clear();
        if (log.isInfoEnabled()) {
            log.info("Blacklisted tokens cleared on server startup.");
        }
    }

    @Override
    public void submitRegistrationRequest(UserRequestDTO userRequest) {
        Subsidiary subsidiary = subsidiaryRepository.findById(userRequest.getSubsidiaryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid subsidiary ID"));

        Organization organization = subsidiary.getOrganization();

        User admin = organization.getSubsidiaries().stream()
                .flatMap(sub -> sub.getEmployees().stream())
                .map(Employee::getUser)
                .filter(user -> user.getRole() == Role.ORG_ADMIN)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No ORG_ADMIN found!"));

        RegistrationRequest request = new RegistrationRequest();
        request.setUsername(userRequest.getUsername());
        request.setEmail(userRequest.getEmail());
        request.setFullName(userRequest.getFullName());

        String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());
        request.setPassword(encryptedPassword);
        request.setSubsidiary(subsidiary);
        request.setAdminId(admin.getUserId());
        request.setStatus(RequestStatus.PENDING);
        registrationRequestRepository.save(request);

        Map<String, Object> adminTemplateData = new HashMap<>();
        adminTemplateData.put("fullName", request.getFullName());
        adminTemplateData.put("email", request.getEmail());
        adminTemplateData.put("subsidiary", subsidiary.getSubsidiaryCode());

        emailService.sendTemplateEmail(admin.getEmail(),
                "[SafetyNet AI] New Registration Request",
                "admin-notification.ftl",
                adminTemplateData);
    }

    @Override
    public LoginResponseDTO authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            String accessToken = jwtUtil.generateAccessToken(user.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

            LoginResponseDTO loginResponseDTO = modelMapper.map(user, LoginResponseDTO.class);
            loginResponseDTO.setAccessToken(accessToken);
            loginResponseDTO.setRefreshToken(refreshToken);
            loginResponseDTO.setUserId(user.getUserId());

            return loginResponseDTO;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        if (accessToken == null || refreshToken == null) {
            throw new IllegalArgumentException("Access token and refresh token must be provided");
        }

        blacklistedTokens.add(accessToken);
        blacklistedTokens.add(refreshToken);

        if (log.isInfoEnabled()) {
            log.info("User logged out successfully. Tokens blacklisted: AccessToken '{}', RefreshToken '{}'", accessToken, refreshToken);
        }
    }

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }
}