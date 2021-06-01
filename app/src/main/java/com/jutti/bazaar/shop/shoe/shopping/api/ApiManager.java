package com.jutti.bazaar.shop.shoe.shopping.api;

import android.content.Context;


import com.jutti.bazaar.shop.shoe.shopping.api.Model.ApiResponse;
import com.jutti.bazaar.shop.shoe.shopping.api.Model.KycDetailsResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class ApiManager extends ApiClient {


    private static ApiManager apiManager;
    private Context mContext;
    private String accessToken;

    public ApiManager(Context context) {
        super(context);
        mContext = context;

    }


    public static ApiManager getInstance(Context context) {
        if (apiManager == null) {
            apiManager = new ApiManager(context);
        }
        return apiManager;
    }



    public void forgotPassword(Callback<ApiResponse> callback, RequestBody mobile, RequestBody password, RequestBody otp, RequestBody otptoken) {
        ApiClient.current(mContext, false).forgotPasswrd(mobile,password,otp,otptoken).enqueue(callback);
    }

    public void getKycDetails(Callback<KycDetailsResponse> callback, RequestBody user_id) {
        ApiClient.current(mContext, false).kycDetails(user_id).enqueue(callback);
    }


}