
package com.bantu.lift.user.modelclass.LogoutModelclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutModelclass {

    @SerializedName("errorCode")
    @Expose
    private String errorCode;
    @SerializedName("errorMsg")
    @Expose
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
