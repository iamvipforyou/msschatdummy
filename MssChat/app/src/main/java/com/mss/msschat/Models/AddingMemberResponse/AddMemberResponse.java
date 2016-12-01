
package com.mss.msschat.Models.AddingMemberResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMemberResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("data")
    @Expose
    private MainMembersData data;

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
    public MainMembersData getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(MainMembersData data) {
        this.data = data;
    }

}
