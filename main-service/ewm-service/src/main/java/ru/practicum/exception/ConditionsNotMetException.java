package ru.practicum.exception;

public class ConditionsNotMetException extends RuntimeException {
    public ConditionsNotMetException(String message) {
        super(message);
    }

    public ConditionsNotMetException(String message, Throwable cause) {
        super(message, cause);
    }
}

