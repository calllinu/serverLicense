package com.Server.repository.dto.subsidiaryDTOs;

import com.Server.repository.entity.Subsidiary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubsidiaryForOrganizationDTO {
    private Long organizationId;
    private List<Subsidiary> subsidiaries;
}
