package com.Server.repository.dto.userDTOs;

import com.Server.repository.entity.enums.Role;
import lombok.Data;

@Data
public class UserResponseDTO {
    private String username;
    private String email;
    private String fullName;
    private Role role;
}


