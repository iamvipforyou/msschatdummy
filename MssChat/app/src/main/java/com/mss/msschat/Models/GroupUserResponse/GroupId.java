package com.mss.msschat.Models.GroupUserResponse;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupId {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("groupPic")
    @Expose
    private String groupPic;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("isDelete")
    @Expose
    private Boolean isDelete;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("adminId")
    @Expose
    private List<String> adminId = new ArrayList<String>();
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;

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
     * @return The isDelete
     */
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * @param isDelete The isDelete
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * @return The v
     */
    public Integer getV() {
        return v;
    }

    /**
     * @param v The __v
     */
    public void setV(Integer v) {
        this.v = v;
    }

    /**
     * @return The adminId
     */
    public List<String> getAdminId() {
        return adminId;
    }

    /**
     * @param adminId The adminId
     */
    public void setAdminId(List<String> adminId) {
        this.adminId = adminId;
    }

    /**
     * @return The isDeleted
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted The isDeleted
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
