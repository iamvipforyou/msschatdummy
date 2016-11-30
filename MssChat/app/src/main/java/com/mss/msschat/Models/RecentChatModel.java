package com.mss.msschat.Models;

/**
 * Created by mss on 24/11/16.
 */

public class RecentChatModel {

    String userId;
    String userName;
    String imageUrl;
    String content;
    String time;


    public RecentChatModel() {

    }

    public RecentChatModel(String userId, String userName, String imageUrl, String content, String time) {

        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.content = content;
        this.time = time;


    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
