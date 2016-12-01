package com.mss.msschat.DataBase;


import java.util.LinkedHashMap;

public class TableHelper {


    public static LinkedHashMap<String, String> getAllContacts() {

        LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();

        fields.put("contactId", "INTEGER PRIMARY KEY AUTOINCREMENT");
        fields.put("contactUserId", "Text");
        fields.put("phoneNumber", "Text");
        fields.put("userPicture", "Text");
        fields.put("userName", "Text");

        return fields;


    }

    public static LinkedHashMap<String, String> getAllMessages() {

        LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();

        fields.put("messageId", "INTEGER PRIMARY KEY AUTOINCREMENT");
        fields.put("message", "Text");
        fields.put("userName", "Text");
        fields.put("sideFrom", "Text");
        fields.put("senderId", "Text");
        fields.put("dateTime", "Text");
        fields.put("typeMessage", "Text");

        return fields;


    }


    public static LinkedHashMap<String, String> getAllRecentMessages() {

        LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();

        fields.put("rMessage", "Text");
        fields.put("rUserName", "Text");
        fields.put("rFrom", "Text");
        fields.put("rSenderId", "Text PRIMARY KEY ");
        fields.put("rDateTime", "Text");
        fields.put("rTypeMessage", "Text");
        fields.put("rImage","Text");

        return fields;


    }


}