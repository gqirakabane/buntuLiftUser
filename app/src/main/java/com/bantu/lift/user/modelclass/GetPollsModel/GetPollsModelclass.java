
package com.bantu.lift.user.modelclass.GetPollsModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPollsModelclass {

    @SerializedName("errorCode")
    @Expose
    private String errorCode;
    @SerializedName("errorMsg")
    @Expose
    private String errorMsg;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
