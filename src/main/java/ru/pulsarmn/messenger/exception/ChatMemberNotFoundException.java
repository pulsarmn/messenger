package ru.pulsarmn.messenger.exception;


public class ChatMemberNotFoundException extends RuntimeException {

    public ChatMemberNotFoundException() {
    }

    public ChatMemberNotFoundException(String message) {
        super(message);
    }
}
