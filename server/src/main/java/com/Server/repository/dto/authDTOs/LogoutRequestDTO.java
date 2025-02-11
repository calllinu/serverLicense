package com.Server.repository.dto.authDTOs;

import lombok.Data;

@Data
public class LogoutRequestDTO {
    private String accessToken;
    private String refreshToken;
}
