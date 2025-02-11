package com.Server.repository.dto.authDTOs;

import com.Server.repository.entity.enums.Role;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
