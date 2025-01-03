package com.Server.repository.dto;

import com.Server.repository.entity.Industry;
import lombok.Data;

@Data
public class OrganizationRequestDTO {
    private String organizationCode;
    private String name;
    private Long yearOfEstablishment;
    private Industry industry;
}
