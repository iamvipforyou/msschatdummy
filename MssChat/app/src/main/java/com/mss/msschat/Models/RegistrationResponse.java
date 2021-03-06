package com.mss.msschat.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RegistrationResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("data")
    @Expose
    private RegistrationResponseData data;

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The responseMessage
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * @param responseMessage The responseMessage
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    /**
     * @return The data
     */
    public RegistrationResponseData getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(RegistrationResponseData data) {
        this.data = data;
    }

}
