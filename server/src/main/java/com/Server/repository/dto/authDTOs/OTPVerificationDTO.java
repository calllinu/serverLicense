package com.Server.repository.dto.authDTOs;

import lombok.Data;

@Data
public class OTPVerificationDTO {
    private String email;
    private String otp;
}
