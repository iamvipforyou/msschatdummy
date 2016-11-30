package com.mss.msschat.DataBase.Dto;

/**
 * Created by mss on 29/11/16.
 */

public class ContactsDto extends Dto {


    public static final long SerializableLONG = 1L;


    @Column(name = "contactId", type = "TEXT")
    public String contactId;

    @Column(name = "contactUserId", type = "TEXT")
    public String contactUserId;

    @Column(name = "phoneNumber", type = "TEXT")
    public String phoneNumber;

    @Column(name = "userPicture", type = "TEXT")
    public String userPicture;

    @Column(name = "userName", type = "TEXT")
    public String userName;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactUserId() {
        return contactUserId;
    }

    public void setContactUserId(String contactUserId) {
        this.contactUserId = contactUserId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
