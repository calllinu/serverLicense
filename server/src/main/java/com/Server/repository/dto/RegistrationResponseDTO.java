package com.Server.repository.dto;

import com.Server.repository.entity.RequestStatus;
import com.Server.repository.entity.Subsidiary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponseDTO {

    private Long requestId;
    private String username;
    private String email;
    private String fullName;
    private Subsidiary subsidiary;
    private RequestStatus status;

}
