package com.mss.msschat.DataBase.Dto;

/**
 * Created by mss on 30/11/16.
 */

public class MessageDto extends Dto {


    public static final long SerializableLONG = 1L;

    @Column(name = "messageId",type = "TEXT")
    String messageId;


    @Column(name = "userName",type = "TEXT")
    String userName;

    @Column(name = "message",type = "TEXT")
      String message;

    @Column(name = "senderId",type = "TEXT")
    String senderId;

    @Column(name = "from",type = "TEXT")
    String from;

    @Column(name = "dateTime",type = "TEXT")
    String dateTime;

    @Column(name = "typeMessage",type = "TEXT")
    String typeMessage;


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }
}
