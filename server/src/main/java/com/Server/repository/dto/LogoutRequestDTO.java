package com.Server.repository.dto;

import lombok.Data;

@Data
public class LogoutRequestDTO {
    private String accessToken;
    private String refreshToken;
}
