package com.Server.repository.dto;

import lombok.Data;
import java.util.List;

@Data
public class SubsidiaryResponseDTO {
    private Long subsidiaryId;
    private String subsidiaryCode;
    private String country;
    private String city;
    private String address;
    private Long organizationId;
}
