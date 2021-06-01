package com.jutti.bazaar.shop.shoe.shopping.api.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Otpdata {
    @SerializedName("otptoken")
    @Expose
    private String otptoken;
    @SerializedName("otp")
    @Expose
    private String otp;

    public String getOtptoken() {
        return otptoken;
    }

    public void setOtptoken(String otptoken) {
        this.otptoken = otptoken;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
