package com.Server.service;

import com.Server.repository.entity.Employee;

public interface EmployeeService {
    Employee getEmployeeByUserId(Long userId);
    Employee updateEmployee(Long userId, Employee updatedEmployee);
}
