package com.Server.repository.dto;

import com.Server.repository.entity.Role;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
