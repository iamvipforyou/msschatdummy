
package com.mss.msschat.Models.AddingMemberResponse;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainMembersData {

    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("success")
    @Expose
    private List<Success> success = new ArrayList<Success>();

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
