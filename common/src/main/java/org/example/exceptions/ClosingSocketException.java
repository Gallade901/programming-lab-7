package org.example.exceptions;

public class ClosingSocketException extends Exception {
    public ClosingSocketException() {
    }

    public ClosingSocketException(String message) {
        super(message);
    }
}
