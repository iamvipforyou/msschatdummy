package com.mss.msschat.Models.AddingMemberResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Success {

    @SerializedName("phoneNumber")
    @Expose
    private Long phoneNumber;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private SuccessResponseData data;

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
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The data
     */
    public SuccessResponseData getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(SuccessResponseData data) {
        this.data = data;
    }

}
