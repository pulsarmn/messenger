package ru.pulsarmn.messenger.message.exception;


public class MessageOwnershipException extends RuntimeException {

    public MessageOwnershipException() {
    }

    public MessageOwnershipException(String message) {
        super(message);
    }
}
