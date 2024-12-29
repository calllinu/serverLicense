package com.Server.repository.dto;

import com.Server.repository.entity.Industry;
import lombok.Data;

@Data
public class OrganizationRequestDTO {
    private String registerCode;
    private String name;
    private Integer yearOfEstablishment;
    private Industry industry;
}
