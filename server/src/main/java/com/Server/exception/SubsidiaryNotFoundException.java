package com.Server.exception;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SubsidiaryNotFoundException extends RuntimeException {
    public SubsidiaryNotFoundException(String message) {
        super(message);
    }
}
