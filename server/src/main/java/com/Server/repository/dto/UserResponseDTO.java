package com.Server.repository.dto;

import com.Server.repository.entity.Role;
import lombok.Data;

@Data
public class UserResponseDTO {
    private String username;
    private String email;
    private String fullName;
    private Role role;
}


