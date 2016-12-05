package com.mss.msschat.Models;


/**
 * Created by mss on 28/11/16.
 */

public class ChatMessageModel {


    String userName;


    String message;


    String senderId;


    String from;


    String dateTime;


    String typeMessage;


    public ChatMessageModel() {


    }

    public ChatMessageModel(String userName, String message, String senderId, String from, String dateTime, String typeMessage) {

        this.userName = userName;
        this.message = message;
        this.senderId = senderId;
        this.from = from;
        this.dateTime = dateTime;
        this.typeMessage = typeMessage;

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
