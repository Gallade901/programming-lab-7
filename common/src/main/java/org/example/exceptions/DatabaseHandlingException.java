package org.example.exceptions;

public class DatabaseHandlingException extends Exception {
    public DatabaseHandlingException() {
    }

    public DatabaseHandlingException(String message) {
        super(message);
    }
}
