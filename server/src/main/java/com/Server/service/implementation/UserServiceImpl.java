package com.Server.service.implementation;

import com.Server.exception.OrganizationNotFoundException;
import com.Server.exception.SubsidiaryNotFoundException;
import com.Server.exception.UserAlreadyExistException;
import com.Server.exception.UserNotFoundException;
import com.Server.repository.EmployeeRepository;
import com.Server.repository.SubsidiaryRepository;
import com.Server.repository.UserRepository;
import com.Server.repository.dto.LoginResponseDTO;
import com.Server.repository.dto.UserResponseDTO;
import com.Server.repository.entity.*;
import com.Server.service.UserService;
import com.Server.security.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Set<String> blacklistedTokens = new HashSet<>();
    private final SubsidiaryRepository subsidiaryRepository;
    private final EmployeeRepository employeeRepository;

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           JwtUtil jwtUtil,
                           SubsidiaryRepository subsidiaryRepository,
                           EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
        this.subsidiaryRepository = subsidiaryRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostConstruct
    private void initialize() {
        blacklistedTokens.clear();
        if (log.isInfoEnabled()) {
            log.info("Blacklisted tokens cleared on server startup.");
        }
    }

    @Override
    public UserResponseDTO addUser(String username, String email, String fullName, String password, Long subsidiaryId) {

        if (userRepository.findByUsernameOrEmail(username, email).isPresent()) {
            if (log.isErrorEnabled()) {
                log.error("Attempt to add user failed: User '{}' already exists", username);
            }
            throw new UserAlreadyExistException("User already exists!");
        }

        Subsidiary subsidiary = subsidiaryRepository.findById(subsidiaryId)
                .orElseThrow(() -> new SubsidiaryNotFoundException("Subsidiary not found"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        String encodedPassword = this.passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setRole(Role.EMPLOYEE);

        userRepository.save(user);

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setSubsidiary(subsidiary);
        employee.setFullName(fullName);

        employeeRepository.save(employee);

        user.setEmployee(employee);
        userRepository.save(user);

        if (log.isInfoEnabled()) {
            log.info("New user added successfully: Username '{}', Email '{}'", username, email);
        }

        return modelMapper.map(user, UserResponseDTO.class);
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
