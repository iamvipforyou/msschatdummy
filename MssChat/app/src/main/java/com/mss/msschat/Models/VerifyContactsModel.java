package com.mss.msschat.Models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VerifyContactsModel {

    @SerializedName("contacts")
    @Expose
    private List<Long> contacts = new ArrayList<Long>();

    /**
     * @return The contacts
     */
    public List<Long> getContacts() {
        return contacts;
    }

    /**
     * @param contacts The contacts
     */
    public void setContacts(List<Long> contacts) {
        this.contacts = contacts;
    }

}
