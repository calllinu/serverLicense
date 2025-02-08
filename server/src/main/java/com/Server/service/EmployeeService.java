package com.Server.service;

import com.Server.repository.entity.Employee;

public interface EmployeeService {
    Employee getEmployeeByUserId(Long userId);
    void updateEmployee(Long userId, Employee updatedEmployee);
    boolean hasNullFields(Long userId);
}
