package com.Server.service.implementation;

import com.Server.repository.EmployeeRepository;
import com.Server.repository.entity.Employee;
import com.Server.service.interfaces.EmailService;
import com.Server.service.interfaces.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                                EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
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

                int yearsOfExperience = calculateYearsOfExperience(dateOfHiring);
                existingEmployee.setYearsOfExperience(yearsOfExperience);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }

        updateIfNotNull(existingEmployee::setQualification, updatedEmployee.getQualification());
        employeeRepository.save(existingEmployee);

        if (!hasNullFields(userId)) {
            Map<String, Object> profileComplete = new HashMap<>();
            profileComplete.put("userName", existingEmployee.getFullName());

            emailService.sendTemplateEmail(existingEmployee.getUser().getEmail(),
                    "[SafetyNet AI] Profile completed",
                    "profile-completed.ftl",
                    profileComplete);
        }
    }

    private int calculateYearsOfExperience(LocalDate dateOfHiring) {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getYear() - dateOfHiring.getYear();
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

    @Override
    public void deleteEmployee(Long userId) {
        employeeRepository.deleteByUserUserId(userId);
    }

    @Override
    public List<Employee> getEmployeesBySubsidiaryId(Long subsidiaryId) {
        return employeeRepository.findAllBySubsidiaryId(subsidiaryId);
    }
}
