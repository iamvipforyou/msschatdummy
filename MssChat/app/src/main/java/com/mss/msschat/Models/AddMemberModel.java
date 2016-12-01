package com.mss.msschat.Models;

import java.util.ArrayList;
import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMemberModel {

    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("groupId")
    @Expose
    private String groupId;
    @SerializedName("users")
    @Expose
    private List<Long> users = new ArrayList<Long>();

    /**
     * @return The adminId
     */
    public String getAdminId() {
        return adminId;
    }

    /**
     * @param adminId The adminId
     */
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    /**
     * @return The groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId The groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return The users
     */
    public List<Long> getUsers() {
        return users;
    }

    /**
     * @param users The users
     */
    public void setUsers(List<Long> users) {
        this.users = users;
    }

}
