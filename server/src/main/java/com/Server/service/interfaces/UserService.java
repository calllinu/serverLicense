package com.Server.service.interfaces;

import com.Server.repository.dto.authDTOs.LoginResponseDTO;
import com.Server.repository.dto.userDTOs.UserRequestDTO;
import com.Server.repository.entity.Employee;

public interface UserService {
    void submitRegistrationRequest(UserRequestDTO userRequest);

    LoginResponseDTO authenticateUser(String email, String password);

    void logout(String accessToken, String refreshToken);

    void sendOtp(String email);

    boolean verifyOtp(String email, String otp);

    void changePassword(String email, String otp, String password);

    void updateUserDetails(Long userId, Employee updatedEmployee);
}
