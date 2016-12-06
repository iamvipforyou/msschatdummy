
package com.mss.msschat.Models.RemoveGroupUserResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoveGrpResponseData {

    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("success")
    @Expose
    private List<Success> success = null;

    /**
     * 
     * @return
     *     The error
     */
    public Object getError() {
        return error;
    }

    /**
     * 
     * @param error
     *     The error
     */
    public void setError(Object error) {
        this.error = error;
    }

    /**
     * 
     * @return
     *     The success
     */
    public List<Success> getSuccess() {
        return success;
    }

    /**
     * 
     * @param success
     *     The success
     */
    public void setSuccess(List<Success> success) {
        this.success = success;
    }

}