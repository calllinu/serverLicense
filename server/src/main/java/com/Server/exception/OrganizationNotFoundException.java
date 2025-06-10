package com.Server.exception;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrganizationNotFoundException extends RuntimeException {
    public OrganizationNotFoundException(String message) {
        super(message);
    }
}
