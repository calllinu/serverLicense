package com.Server.repository.dto.subsidiaryDTOs;

import lombok.Data;

@Data
public class SubsidiaryUpdateRequestDTO {
    private String country;
    private String city;
    private String address;
}
