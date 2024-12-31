package com.Server.repository.dto;

import com.Server.repository.entity.Employee;
import com.Server.repository.entity.Organization;
import com.Server.repository.entity.Subsidiary;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.Server.repository.entity.Qualification;

import java.util.Date;

@Data
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long employeeId;
    private String employeeCNP;
    private Date dateOfBirth;
    private String fullName;
    private Qualification qualification;
    private Integer yearsOfExperience;
    private Subsidiary subsidiary;
    private Organization organization;

    public static EmployeeResponseDTO fromEntity(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getEmployeeId(),
                employee.getEmployeeCNP(),
                employee.getDateOfBirth(),
                employee.getFullName(),
                employee.getQualification(),
                employee.getYearsOfExperience(),
                employee.getSubsidiary(),
                employee.getSubsidiary().getOrganization()
        );
    }
}
