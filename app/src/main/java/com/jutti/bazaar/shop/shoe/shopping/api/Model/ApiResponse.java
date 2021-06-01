package com.jutti.bazaar.shop.shoe.shopping.api.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("otpdata")
    @Expose
    private Otpdata otpdata;

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

    public Otpdata getOtpdata() {
        return otpdata;
    }

    public void setOtpdata(Otpdata otpdata) {
        this.otpdata = otpdata;
    }
}
