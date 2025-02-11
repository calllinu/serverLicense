package com.Server.repository.dto.organizationDTOs;

import com.Server.repository.entity.enums.Industry;
import lombok.Data;

@Data
public class OrganizationRequestDTO {
    private String organizationCode;
    private String name;
    private Long yearOfEstablishment;
    private Industry industry;
}
