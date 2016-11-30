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

}