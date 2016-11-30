package com.mss.msschat.Models;

/**
 * Created by mss on 28/11/16.
 */

public class ChatMessageModel {

    String message;
    String type;


    public ChatMessageModel(){

    }

    public ChatMessageModel(String message,String type){

        this.message = message;
        this.type = type;

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
