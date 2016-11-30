package com.mss.msschat.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mss on 29/11/16.
 */

public class ContactData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("phoneNumber")
    @Expose
    private long phoneNumber;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The _id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The phoneNumber
     */
    public long getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber The phoneNumber
     */
    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return The profilePic
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * @param profilePic The profilePic
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }


}
