
package com.mss.msschat.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RegistrationResponseData {

    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("deviceType")
    @Expose
    private String deviceType;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("phoneNumber")
    @Expose
    private long phoneNumber;
    @SerializedName("profilePic")
    @Expose
    private Object profilePic;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("_id")
    @Expose
    private String id;

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
     *     The deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 
     * @param deviceType
     *     The deviceType
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 
     * @return
     *     The deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 
     * @param deviceId
     *     The deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * 
     * @return
     *     The phoneNumber
     */
    public long getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 
     * @param phoneNumber
     *     The phoneNumber
     */
    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 
     * @return
     *     The profilePic
     */
    public Object getProfilePic() {
        return profilePic;
    }

    /**
     * 
     * @param profilePic
     *     The profilePic
     */
    public void setProfilePic(Object profilePic) {
        this.profilePic = profilePic;
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

}
