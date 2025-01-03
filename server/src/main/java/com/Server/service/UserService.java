package com.Server.service;

import com.Server.repository.dto.LoginResponseDTO;
import com.Server.repository.dto.UserRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {

    void submitRegistrationRequest(UserRequestDTO userRequest);

    void declineRegistrationRequest(Long requestId);

    LoginResponseDTO authenticateUser(String email, String password);

    void logout(String accessToken, String refreshToken);

    PasswordEncoder getPasswordEncoder();
}
