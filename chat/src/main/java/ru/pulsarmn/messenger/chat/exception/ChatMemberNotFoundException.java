package ru.pulsarmn.messenger.chat.exception;


public class ChatMemberNotFoundException extends RuntimeException {

    public ChatMemberNotFoundException() {
    }

    public ChatMemberNotFoundException(String message) {
        super(message);
    }
}
