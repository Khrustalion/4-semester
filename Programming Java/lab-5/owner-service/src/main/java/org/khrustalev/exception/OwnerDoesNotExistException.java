package org.khrustalev.exception;

public class OwnerDoesNotExistException extends RuntimeException {
    public OwnerDoesNotExistException(String message) {
        super(message);
    }
}
