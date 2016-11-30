package com.mss.msschat.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CreateGroupModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("groupPic")
    @Expose
    private String groupPic;
    @SerializedName("adminId")
    @Expose
    private String adminId;

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

    /**
     * @return The groupPic
     */
    public String getGroupPic() {
        return groupPic;
    }

    /**
     * @param groupPic The groupPic
     */
    public void setGroupPic(String groupPic) {
        this.groupPic = groupPic;
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
