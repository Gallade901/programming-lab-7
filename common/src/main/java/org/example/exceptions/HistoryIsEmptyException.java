package org.example.exceptions;

public class HistoryIsEmptyException extends Exception {
    public HistoryIsEmptyException() {
    }

    public HistoryIsEmptyException(String message) {
        super(message);
    }
}
