package org.example.exceptions;

public class ConnectionErrorException extends Exception {
    public ConnectionErrorException() {
    }

    public ConnectionErrorException(String message) {
        super(message);
    }
}
