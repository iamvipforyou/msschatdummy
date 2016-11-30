package com.mss.msschat.DataBase.Dto;


public class InboxDto extends Dto {

    public static final long SerializableLONG = 1L;

    @Column(name = "id", type = "TEXT")
    public String ID;

    @Column(name = "messageType", type = "TEXT")
    public String messageType;

    @Column(name = "requestId", type = "TEXT")
    public String requestId;

    @Column(name = "messageId", type = "TEXT")
    public String messageId;

    @Column(name = "senderMobile", type = "TEXT")
    public String senderMobile;

    @Column(name = "userName", type = "TEXT")
    public String userName;

    @Column(name = "channelName", type = "TEXT")
    public String channelName;

    @Column(name = "content", type = "TEXT")
    public String content;

    @Column(name = "contentType", type = "TEXT")
    public String contentType;

    @Column(name = "reactions", type = "TEXT")
    public String reactions;

    @Column(name = "dispatchTime", type = "TEXT")
    public String dispatchTime;

    @Column(name = "senderPhoto", type = "TEXT")
    public String senderPhoto;


    @Column(name = "isRead", type = "TEXT")
    public String isRead;

    @Column(name = "attachmentVideoUrl", type = "TEXT")
    public String attachmentVideoUrl;

    @Column(name = "attachmentImageUrl", type = "TEXT")
    public String attachmentImageUrl;


    public String getSenderPhoto() {
        return senderPhoto;
    }

    public void setSenderPhoto(String senderPhoto) {
        this.senderPhoto = senderPhoto;
    }

    boolean isSELECTED;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getReactions() {
        return reactions;
    }

    public void setReactions(String reactions) {
        this.reactions = reactions;
    }

    public String getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(String dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public boolean isSELECTED() {
        return isSELECTED;
    }

    public void setSELECTED(boolean SELECTED) {
        isSELECTED = SELECTED;
    }


    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }


    public String getAttachmentVideoUrl() {
        return attachmentVideoUrl;
    }

    public void setAttachmentVideoUrl(String attachmentVideoUrl) {
        this.attachmentVideoUrl = attachmentVideoUrl;
    }

    public String getAttachmentImageUrl() {
        return attachmentImageUrl;
    }

    public void setAttachmentImageUrl(String attachmentImageUrl) {
        this.attachmentImageUrl = attachmentImageUrl;
    }
}
