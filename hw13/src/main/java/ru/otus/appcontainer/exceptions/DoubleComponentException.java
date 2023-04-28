package ru.otus.appcontainer.exceptions;

public class DoubleComponentException extends RuntimeException {

    public DoubleComponentException() {
        super();
    }

    public DoubleComponentException(String message) {
        super(message);
    }

    public DoubleComponentException(String message, Throwable cause) {
        super(message, cause);
    }
}
