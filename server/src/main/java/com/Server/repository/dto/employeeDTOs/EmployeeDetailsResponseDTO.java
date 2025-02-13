package com.Server.repository.dto.employeeDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeDetailsResponseDTO {
    private EmployeeResponseDTO employee;
    private boolean hasNullFields;
}
