package com.Server.repository.entity.enums;

public enum Salary {
    LOW,
    MEDIUM,
    HIGH;

    public String transformToString() {
        return this.name().toLowerCase();
    }
}
