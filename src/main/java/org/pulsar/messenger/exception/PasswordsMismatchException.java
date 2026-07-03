package org.pulsar.messenger.exception;


public class PasswordsMismatchException extends RuntimeException {

    public PasswordsMismatchException() {
    }

    public PasswordsMismatchException(String message) {
        super(message);
    }

    public PasswordsMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordsMismatchException(Throwable cause) {
        super(cause);
    }
}
