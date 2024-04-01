package org.example.exceptions;

/**
 * Is throwed when something not in declared limits.
 */
public class NotInDeclaredLimitsException extends Exception {
    public NotInDeclaredLimitsException(String message) {
        super(message);
    }
    public NotInDeclaredLimitsException(){

    }
}
