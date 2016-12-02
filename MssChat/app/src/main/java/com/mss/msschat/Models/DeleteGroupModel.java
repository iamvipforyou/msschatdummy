package com.mss.msschat.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteGroupModel {

    @SerializedName("groupId")
    @Expose
    private String groupId;
    @SerializedName("adminId")
    @Expose
    private String adminId;

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

}
