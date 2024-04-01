package org.example.exceptions;

public class UserAlreadyExists extends Exception {
    public UserAlreadyExists(String message) {
        super(message);
    }

    public UserAlreadyExists() {
    }
}
