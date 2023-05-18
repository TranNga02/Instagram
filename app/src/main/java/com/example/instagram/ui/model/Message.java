package com.example.instagram.ui.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private String msgId;
    private String senderId;
    private String message;
    private Date timestamp;

    public Message(String msgId, String senderId, String message, Date timestamp) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
