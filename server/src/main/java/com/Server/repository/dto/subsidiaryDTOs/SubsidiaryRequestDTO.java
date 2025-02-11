package com.Server.repository.dto.subsidiaryDTOs;

import lombok.Data;

@Data
public class SubsidiaryRequestDTO {
    private String subsidiaryCode;
    private String country;
    private String city;
    private String address;
    private Long organizationId;
}
