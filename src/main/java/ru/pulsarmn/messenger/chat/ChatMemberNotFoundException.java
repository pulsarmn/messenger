package ru.pulsarmn.messenger.chat;


public class ChatMemberNotFoundException extends RuntimeException {

    public ChatMemberNotFoundException() {
    }

    public ChatMemberNotFoundException(String message) {
        super(message);
    }
}
