package com.instant.message_app.entity;

public class ChatMessage {

    private String message;
    private Result user;
    private boolean isCurrent;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getUser() {
        return user;
    }

    public void setUser(Result user) {
        this.user = user;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }
}
