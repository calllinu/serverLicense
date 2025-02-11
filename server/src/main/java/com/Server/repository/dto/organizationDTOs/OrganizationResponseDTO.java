package com.Server.repository.dto.organizationDTOs;

import com.Server.repository.dto.userDTOs.UserResponseDTO;
import com.Server.repository.entity.enums.Industry;
import com.Server.repository.entity.Subsidiary;
import lombok.Data;
import java.util.List;

@Data
public class OrganizationResponseDTO {
    private Long organizationId;
    private String organizationCode;
    private String name;
    private Long yearOfEstablishment;
    private Industry industry;
    private UserResponseDTO admin;
    private List<Subsidiary> subsidiaries;
}
