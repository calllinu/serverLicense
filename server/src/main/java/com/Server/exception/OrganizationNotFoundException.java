package com.Server.exception;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrganizationNotFoundException extends RuntimeException {

    /**
     * Custom exception class to indicate that the email already exists in the system
     *
     * @param message The error message explaining the reason for the exception
     */
    public OrganizationNotFoundException(String message) {
        super(message);
    }
}
