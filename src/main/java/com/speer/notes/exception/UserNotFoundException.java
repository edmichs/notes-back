package com.speer.notes.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User not found with identifier: " + username);
    }
}
