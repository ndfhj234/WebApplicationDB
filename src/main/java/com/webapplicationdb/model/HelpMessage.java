package com.webapplicationdb.model;

import java.sql.Timestamp;

public class HelpMessage {
    private int messageId;
    private String userName;
    private String message;
    private Timestamp createdAt;

    public HelpMessage() {}

    public HelpMessage(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
