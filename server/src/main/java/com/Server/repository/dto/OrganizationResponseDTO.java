package com.Server.repository.dto;

import com.Server.repository.entity.Industry;
import com.Server.repository.entity.Subsidiary;
import com.Server.repository.entity.User;
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
