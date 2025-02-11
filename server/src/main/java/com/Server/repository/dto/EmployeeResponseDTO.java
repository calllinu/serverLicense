package com.Server.repository.dto;

import com.Server.repository.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long employeeId;
    private String employeeCNP;
    private LocalDate dateOfBirth;
    private LocalDate dateOfHiring;
    private String fullName;
    private Qualification qualification;
    private Integer yearsOfExperience;
    private Subsidiary subsidiary;
    private Organization organization;
    private Feedback feedback;

    public static EmployeeResponseDTO fromEntity(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getEmployeeId(),
                employee.getEmployeeCNP(),
                employee.getDateOfBirth(),
                employee.getDateOfHiring(),
                employee.getFullName(),
                employee.getQualification(),
                employee.getYearsOfExperience(),
                employee.getSubsidiary(),
                employee.getSubsidiary().getOrganization(),
                employee.getFeedback()
        );
    }
}
