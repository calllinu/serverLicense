package com.Server.service.implementation;

import com.Server.repository.EmployeeRepository;
import com.Server.repository.entity.Employee;
import com.Server.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee getEmployeeByUserId(Long userId) {
        Optional<Employee> employee = employeeRepository.findByUserUserId(userId);
        return employee.orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public void updateEmployee(Long userId, Employee updatedEmployee) {
        Employee existingEmployee = getEmployeeByUserId(userId);

        updateIfNotNull(existingEmployee::setFullName, updatedEmployee.getFullName());
        updateIfNotNull(existingEmployee::setEmployeeCNP, updatedEmployee.getEmployeeCNP());
        updateIfNotNull(existingEmployee::setDateOfBirth, updatedEmployee.getDateOfBirth());
        updateIfNotNull(existingEmployee::setQualification, updatedEmployee.getQualification());

        employeeRepository.save(existingEmployee);
    }

    private <T> void updateIfNotNull(java.util.function.Consumer<T> setter, T value) {
        Optional.ofNullable(value).ifPresent(setter);
    }


}
