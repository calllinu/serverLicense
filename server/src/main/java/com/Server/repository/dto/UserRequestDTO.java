package com.Server.repository.dto;

import com.Server.repository.entity.Role;
import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String email;
    private String fullName;
    private String password;
    private Long subsidiaryId;
}