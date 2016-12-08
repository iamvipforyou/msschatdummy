package com.mss.msschat.DataBase.Dto;

/**
 * Created by mss on 1/12/16.
 */

public class RecentChatDto extends Dto {

    @Column(name = "rUserName", type = "TEXT")
    String userName;

    @Column(name = "rMessage", type = "TEXT")
    String message;

    @Column(name = "rSenderId", type = "TEXT")
    String senderId;

    @Column(name = "rFrom", type = "TEXT")
    String from;

    @Column(name = "rDateTime", type = "TEXT")
    String dateTime;

    @Column(name = "rTypeMessage", type = "TEXT")
    String typeMessage;


    @Column(name = "rImage", type = "TEXT")
    String profileImage;

    @Column(name = "rCount", type = "TEXT")
    String messageCount;

    boolean isSELECTED;

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


    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public boolean isSELECTED() {
        return isSELECTED;
    }

    public void setSELECTED(boolean SELECTED) {
        isSELECTED = SELECTED;
    }


    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }
}
