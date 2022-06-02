package com.example.live_tik_tac_toe_project;


import java.util.Date;

public class userSpecificModel {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String messageId;
    private String sender,reciever;
    private String readStatus="0";

    public userSpecificModel(String messageText, String messageUser, String messageId, String sender, String reciever) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageId = messageId;
        this.sender = sender;
        this.reciever = reciever;
        this.messageTime = new Date().getTime();

    }

    public userSpecificModel() {
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }
}
