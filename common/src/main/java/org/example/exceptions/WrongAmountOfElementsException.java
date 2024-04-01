package org.example.exceptions;

/**
 * Is throwed when wrong amount of elements.
 */
public class WrongAmountOfElementsException extends Exception{
    public WrongAmountOfElementsException(String message) {
        super(message);
    }

    public WrongAmountOfElementsException() {

    }
}
