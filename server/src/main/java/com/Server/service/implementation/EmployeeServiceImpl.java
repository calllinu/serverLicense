package com.Server.service.implementation;

import com.Server.repository.EmployeeRepository;
import com.Server.repository.entity.Employee;
import com.Server.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

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
        try {
            if (updatedEmployee.getDateOfBirth() != null) {
                LocalDate dateOfBirth = LocalDate.parse(updatedEmployee.getDateOfBirth().toString());
                existingEmployee.setDateOfBirth(dateOfBirth);
            }
            if (updatedEmployee.getDateOfHiring() != null) {
                LocalDate dateOfHiring = LocalDate.parse(updatedEmployee.getDateOfHiring().toString());
                existingEmployee.setDateOfHiring(dateOfHiring);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
        updateIfNotNull(existingEmployee::setQualification, updatedEmployee.getQualification());

        employeeRepository.save(existingEmployee);
    }

    private <T> void updateIfNotNull(java.util.function.Consumer<T> setter, T value) {
        Optional.ofNullable(value).ifPresent(setter);
    }

    public boolean hasNullFields(Long userId) {
        Optional<Employee> employeeOptional = employeeRepository.findByUserUserId(userId);
        if (employeeOptional.isEmpty())
            return true;

        Employee employee = employeeOptional.get();
        List<Function<Employee, Object>> fieldGetters = List.of(
                Employee::getEmployeeCNP,
                Employee::getDateOfBirth,
                Employee::getFullName,
                Employee::getQualification,
                Employee::getYearsOfExperience,
                Employee::getDateOfHiring,
                Employee::getSubsidiary,
                Employee::getUser
        );

        return fieldGetters.stream().anyMatch(getter -> getter.apply(employee) == null);
    }
}
