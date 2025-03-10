package com.speer.notes.entity;

import lombok.Getter;

@Getter
public enum ERole {
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    private final String displayValue;

    ERole(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
