package com.Server.repository.dto.authDTOs;
import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}

