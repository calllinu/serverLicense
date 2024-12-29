package com.Server.service;

import com.Server.repository.dto.LoginResponseDTO;
import com.Server.repository.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDTO addUser(String username, String email, String password, String fullName, Long subsidiaryId);
    LoginResponseDTO authenticateUser(String username, String password);
    void logout(String accessToken, String refreshToken);
}
