package com.jutti.bazaar.shop.shoe.shopping.api.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KycDetailsResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private KycDetailsDataResponseData data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public KycDetailsDataResponseData getData() {
        return data;
    }

    public void setData(KycDetailsDataResponseData data) {
        this.data = data;
    }
}
