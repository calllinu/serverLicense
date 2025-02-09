package com.Server.controller;

import com.Server.repository.dto.EmployeeResponseDTO;
import com.Server.repository.entity.Employee;
import com.Server.service.EmployeeService;
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
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByUserId(@RequestParam Long userId) {
        try {
            Employee employee = employeeService.getEmployeeByUserId(userId);
            EmployeeResponseDTO employeeDTO = EmployeeResponseDTO.fromEntity(employee);
            return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
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

    @GetMapping("/null-fields/{userId}")
    public ResponseEntity<Boolean> hasNullFields(@PathVariable Long userId) {
        try {
            boolean hasNullFields = employeeService.hasNullFields(userId);
            return new ResponseEntity<>(hasNullFields, HttpStatus.OK);
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
