package org.example.exceptions;

public class CommandUsageException extends Exception{
    public CommandUsageException() {
    }

    public CommandUsageException(String message) {
        super(message);
    }
}
