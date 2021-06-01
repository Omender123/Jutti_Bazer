package com.jutti.bazaar.shop.shoe.shopping.api;

import android.content.Context;


import com.jutti.bazaar.shop.shoe.shopping.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApiCallBack<T> implements Callback<T> {
    private ApiResponseListener<T> apiListener;
    private String apiName;
    private Context mContext;

    public ApiCallBack(ApiResponseListener<T> apiListener, String apiName, Context mContext) {
        this.apiListener = apiListener;
        this.apiName = apiName;
        this.mContext = mContext;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {


        if (response.code() == HttpURLConnection.HTTP_OK) {
            apiListener.onApiSuccess(response.body(), apiName);
            System.out.println("response 200");
        } else if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {

            String apiResponseError = "";
            String fdsf = "";
            try {
                if (response.errorBody() != null) {
                    apiResponseError = response.errorBody().string();
                    JSONObject jsonObject = new JSONObject(apiResponseError);
                    fdsf = jsonObject.getString("message");
                } else {
                    apiResponseError = response.message();
                    JSONObject jsonObject = new JSONObject(apiResponseError);
                    fdsf = jsonObject.getString("message");
                    //    ToastUtils.showToastShort(mContext, String.valueOf(fdsf));
                    System.out.println("onApiSuccess");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            apiListener.onApiError(fdsf, apiName);
        } else {
            String apiResponseError = "";
            apiListener.onApiError(response.message(), apiName);
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        System.out.println("ApiFailure");
        apiListener.onApiFailure(mContext.getString(R.string.server_error), apiName);
    }


}
