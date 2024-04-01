package org.example.exceptions;

/**
 * Is throwed when marine is not found.
 */
public class MarineNotFoundException extends Exception {
    public MarineNotFoundException(String message) {
        super(message);
    }
    public MarineNotFoundException () {

    }
}
