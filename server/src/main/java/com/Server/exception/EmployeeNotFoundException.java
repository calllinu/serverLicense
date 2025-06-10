package com.Server.exception;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
