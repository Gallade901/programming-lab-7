package org.example.exceptions;

public class NotUniqueItemException extends Exception{
    public NotUniqueItemException(String message) {
        super(message);
    }
    public NotUniqueItemException() {

    }
}
