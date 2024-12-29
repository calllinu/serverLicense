package com.Server.exception;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {

    /**
     * Custom exception class to indicate that a user already exists in the system
     *
     * @param message The error message explaining the reason for the exception
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}