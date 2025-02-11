package com.Server.repository.dto.userDTOs;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String email;
    private String fullName;
    private String password;
    private Long subsidiaryId;
}