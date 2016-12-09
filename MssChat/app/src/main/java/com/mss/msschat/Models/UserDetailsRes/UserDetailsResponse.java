
package com.mss.msschat.Models.UserDetailsRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailsResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("data")
    @Expose
    private UserDetailsResponseData data;

    /**
     * 
     * @return
     *     The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The responseMessage
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * 
     * @param responseMessage
     *     The responseMessage
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    /**
     * 
     * @return
     *     The data
     */
    public UserDetailsResponseData getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(UserDetailsResponseData data) {
        this.data = data;
    }

}