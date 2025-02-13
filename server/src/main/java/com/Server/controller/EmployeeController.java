package com.Server.controller;

import com.Server.repository.dto.employeeDTOs.EmployeeDetailsResponseDTO;
import com.Server.repository.dto.employeeDTOs.EmployeeResponseDTO;
import com.Server.repository.entity.Employee;
import com.Server.service.interfaces.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/user")
    public ResponseEntity<EmployeeDetailsResponseDTO> getEmployeeByUserId(@RequestParam Long userId) {
        try {
            Employee employee = employeeService.getEmployeeByUserId(userId);
            boolean hasNullFields = employeeService.hasNullFields(userId);
            EmployeeResponseDTO employeeDTO = EmployeeResponseDTO.fromEntity(employee);
            EmployeeDetailsResponseDTO employeeDetailsDTO = new EmployeeDetailsResponseDTO(employeeDTO, hasNullFields);
            return new ResponseEntity<>(employeeDetailsDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<Void> updateEmployee(
            @RequestBody Employee updatedEmployee,
            @PathVariable Long userId) {
        try {
            employeeService.updateEmployee(userId, updatedEmployee);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long userId) {
        try {
            employeeService.deleteEmployee(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/subsidiary-employees/{subsidiaryId}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesBySubsidiaryId(@PathVariable Long subsidiaryId) {
        try {
            List<Employee> employees = employeeService.getEmployeesBySubsidiaryId(subsidiaryId);
            List<EmployeeResponseDTO> employeesDTO = employees.stream()
                    .map(EmployeeResponseDTO::fromEntity)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(employeesDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
