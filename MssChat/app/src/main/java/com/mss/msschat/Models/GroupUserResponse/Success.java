package com.mss.msschat.Models.GroupUserResponse;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Success {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("phoneNumber")
    @Expose
    private Long phoneNumber;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("isblocked")
    @Expose
    private Boolean isblocked;
    @SerializedName("user")
    @Expose
    private List<User> user = new ArrayList<User>();
    @SerializedName("groupId")
    @Expose
    private List<GroupId> groupId = new ArrayList<GroupId>();

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
    public Long getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber The phoneNumber
     */
    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    /**
     * @return The isblocked
     */
    public Boolean getIsblocked() {
        return isblocked;
    }

    /**
     * @param isblocked The isblocked
     */
    public void setIsblocked(Boolean isblocked) {
        this.isblocked = isblocked;
    }

    /**
     * @return The user
     */
    public List<User> getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(List<User> user) {
        this.user = user;
    }

    /**
     * @return The groupId
     */
    public List<GroupId> getGroupId() {
        return groupId;
    }

    /**
     * @param groupId The groupId
     */
    public void setGroupId(List<GroupId> groupId) {
        this.groupId = groupId;
    }

}
