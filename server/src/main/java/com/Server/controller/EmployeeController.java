package com.Server.controller;

import com.Server.repository.entity.Employee;
import com.Server.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/user")
    public ResponseEntity<Employee> getEmployeeByUserId(@RequestParam Long userId) {
        try {
            Employee employee = employeeService.getEmployeeByUserId(userId);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user")
    public ResponseEntity<Employee> updateEmployee(@RequestParam Long userId, @RequestBody Employee updatedEmployee) {
        try {
            Employee updated = employeeService.updateEmployee(userId, updatedEmployee);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
