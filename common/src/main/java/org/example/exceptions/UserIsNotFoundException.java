package org.example.exceptions;

public class UserIsNotFoundException extends Exception {
    public UserIsNotFoundException(String message) {
        super(message);
    }

    public UserIsNotFoundException() {
    }
}
