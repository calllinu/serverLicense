package com.Server.service;

import com.Server.repository.entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee getEmployeeByUserId(Long userId);
    void updateEmployee(Long userId, Employee updatedEmployee);
    boolean hasNullFields(Long userId);
    void deleteEmployee(Long userId);
    List<Employee> getEmployeesBySubsidiaryId(Long subsidiaryId);
}
