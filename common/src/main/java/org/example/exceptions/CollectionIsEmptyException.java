package org.example.exceptions;

/**
 * Is throwed when collection is empty.
 */
public class CollectionIsEmptyException extends Exception {
    public CollectionIsEmptyException(String message) {
        super(message);
    }
    public CollectionIsEmptyException() {

    }
}
