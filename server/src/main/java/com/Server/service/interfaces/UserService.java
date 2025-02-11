package com.Server.service.interfaces;

import com.Server.repository.dto.authDTOs.LoginResponseDTO;
import com.Server.repository.dto.userDTOs.UserRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {

    void submitRegistrationRequest(UserRequestDTO userRequest);

    LoginResponseDTO authenticateUser(String email, String password);

    void logout(String accessToken, String refreshToken);

    PasswordEncoder getPasswordEncoder();
}
