package com.example.chat;

import java.util.Date;

public class Message {
    private String message;
    private boolean fromCurrentUser;
    private Date dateTime;

    public Message(String message, boolean fromCurrentUser) {
        this.message = message;
        this.fromCurrentUser = fromCurrentUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFromCurrentUser() {
        return fromCurrentUser;
    }
}
