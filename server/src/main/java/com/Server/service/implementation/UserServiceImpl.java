package com.Server.service.implementation;

import com.Server.exception.EmailAlreadyExistException;
import com.Server.exception.UserAlreadyExistException;
import com.Server.repository.UserRepository;
import com.Server.repository.dto.LoginResponseDTO;
import com.Server.repository.dto.UserResponseDTO;
import com.Server.repository.entity.Role;
import com.Server.repository.entity.User;
import com.Server.service.UserService;
import com.Server.security.JwtUtil;
import lombok.extern.java.Log;
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

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponseDTO addUser(String username, String email, String fullName, String password) {
        if (userRepository.findByUsernameOrEmail(username, email).isPresent()) {
            if (log.isErrorEnabled()) {
                log.error("Attempt to add user failed: User '{}' already exists", username);
            }
            throw new UserAlreadyExistException("User already exists!");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            if (log.isErrorEnabled()) {
                log.error("Attempt to add user failed: Email '{}' already exists", email);
            }
            throw new EmailAlreadyExistException("Email already exist!");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        String encodedPassword = this.passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setRole(Role.ANALYST);
        userRepository.save(user);

        if (log.isInfoEnabled()) {
            log.info("New user added successfully: Username '{}', Email '{}'", username, email);
        }

        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public LoginResponseDTO authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            String accessToken = jwtUtil.generateAccessToken(user.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

            LoginResponseDTO loginResponseDTO = modelMapper.map(user, LoginResponseDTO.class);
            loginResponseDTO.setAccessToken(accessToken);
            loginResponseDTO.setRefreshToken(refreshToken);

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
}
