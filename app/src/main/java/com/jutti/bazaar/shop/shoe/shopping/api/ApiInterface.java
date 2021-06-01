package com.jutti.bazaar.shop.shoe.shopping.api;

import android.content.Context;


import com.jutti.bazaar.shop.shoe.shopping.api.Model.ApiResponse;
import com.jutti.bazaar.shop.shoe.shopping.api.Model.KycDetailsResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiInterface {
    Context context = null;

    @Headers("Authkey: APPCBSBDMPL")
    @Multipart
    @POST("v1/forgotpassword")
    Call<ApiResponse> forgotPasswrd(@Part("mobile") RequestBody mobile, @Part("password") RequestBody password, @Part("otp") RequestBody otp, @Part("otptoken") RequestBody otptoken);


    @Headers("Authkey: APPCBSBDMPL")
    @Multipart
    @POST("v2/kycdetails")
    Call<KycDetailsResponse> kycDetails(@Part("user_id") RequestBody user_id);

}
