package ru.otus.appcontainer.exceptions;

public class NotFoundComponentException extends RuntimeException {
    public NotFoundComponentException() {
        super();
    }

    public NotFoundComponentException(String message) {
        super(message);
    }

    public NotFoundComponentException(String message, Throwable cause) {
        super(message, cause);
    }
}
