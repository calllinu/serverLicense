package com.Server.repository.dto.authDTOs;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String email;
    private String otp;
    private String newPassword;
}
