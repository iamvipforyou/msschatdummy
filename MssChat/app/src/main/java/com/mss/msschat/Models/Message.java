package com.mss.msschat.Models;

public class Message {

    public final int id;
    public final String message;
    public final String messageUser;

    public Message(int id, String message, String messageUser) {
        this.id = id;
        this.message = message;
        this.messageUser = messageUser;
    }
}
