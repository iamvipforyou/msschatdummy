
package com.mss.msschat.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteGroupResponseData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("groupPic")
    @Expose
    private String groupPic;
    @SerializedName("name")
    @Expose
    private String name;
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
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The _id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The groupPic
     */
    public String getGroupPic() {
        return groupPic;
    }

    /**
     * 
     * @param groupPic
     *     The groupPic
     */
    public void setGroupPic(String groupPic) {
        this.groupPic = groupPic;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The v
     */
    public Integer getV() {
        return v;
    }

    /**
     * 
     * @param v
     *     The __v
     */
    public void setV(Integer v) {
        this.v = v;
    }

    /**
     * 
     * @return
     *     The adminId
     */
    public List<String> getAdminId() {
        return adminId;
    }

    /**
     * 
     * @param adminId
     *     The adminId
     */
    public void setAdminId(List<String> adminId) {
        this.adminId = adminId;
    }

    /**
     * 
     * @return
     *     The isDeleted
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 
     * @param isDeleted
     *     The isDeleted
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
