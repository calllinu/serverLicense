package com.Server.repository.dto;

import com.Server.repository.entity.Industry;
import com.Server.repository.entity.Subsidiary;
import lombok.Data;
import java.util.List;

@Data
public class OrganizationResponseDTO {
    private Long organizationId;
    private String organizationCode;
    private String name;
    private Integer yearOfEstablishment;
    private Industry industry;
    private List<Subsidiary> subsidiaries;
}
