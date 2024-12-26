package com.Server.repository.dto;

import lombok.Data;

@Data
public class LogoutResponseDTO {
    private String message;
    private boolean success;

    public LogoutResponseDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}