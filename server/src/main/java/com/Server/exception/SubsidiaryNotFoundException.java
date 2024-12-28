package com.Server.exception;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SubsidiaryNotFoundException extends RuntimeException {

    /**
     * Custom exception class to indicate that the email already exists in the system
     *
     * @param message The error message explaining the reason for the exception
     */
    public SubsidiaryNotFoundException(String message) {
        super(message);
    }
}
