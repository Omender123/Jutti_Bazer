package com.jutti.bazaar.shop.shoe.shopping.api.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KycDetailsDataResponseData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("gst_number")
    @Expose
    private String gstNumber;
    @SerializedName("gst_image")
    @Expose
    private String gstImage;
    @SerializedName("adhar_pan_number")
    @Expose
    private String adharPanNumber;
    @SerializedName("adhar_pan_image")
    @Expose
    private String adharPanImage;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getGstImage() {
        return gstImage;
    }

    public void setGstImage(String gstImage) {
        this.gstImage = gstImage;
    }

    public String getAdharPanNumber() {
        return adharPanNumber;
    }

    public void setAdharPanNumber(String adharPanNumber) {
        this.adharPanNumber = adharPanNumber;
    }

    public String getAdharPanImage() {
        return adharPanImage;
    }

    public void setAdharPanImage(String adharPanImage) {
        this.adharPanImage = adharPanImage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
