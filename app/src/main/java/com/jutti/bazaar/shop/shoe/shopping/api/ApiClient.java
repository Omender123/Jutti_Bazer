package com.jutti.bazaar.shop.shoe.shopping.api;

import android.content.Context;


import com.jutti.bazaar.shop.shoe.shopping.Login_Register.AppConstant;
import com.jutti.bazaar.shop.shoe.shopping.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static ApiClient apiClient = null;
    private static boolean isTrue;
    private ApiInterface apiInterface;
    private SavedPrefManager prefManager;
    private Context context;
    private Request.Builder requestBuilder;

    ApiClient(final Context context) {
        this.context = context;
        prefManager = SavedPrefManager.getInstance(context);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        httpClient.connectTimeout(120, TimeUnit.SECONDS);
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.writeTimeout(120, TimeUnit.SECONDS);


        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                if (isTrue) {

                    requestBuilder = original.newBuilder()
                            .header("Authorization", "JWT " + SavedPrefManager.getStringPreferences(context, AppConstant.KEY_JWT_TOKEN))
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());
                } else {
                    requestBuilder = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());
                }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }


    public static ApiInterface current(Context context, boolean istrue) {
        isTrue = istrue;
        if (istrue) {
            return getInstance(context, true).getApiInterface();
        } else {
            return getInstance(context, false).getApiInterface();
        }
    }


    public static ApiClient getInstance(Context context, boolean istrue) {
        if (istrue) {
            if (apiClient == null) {
                apiClient = new ApiClient(context);
            }
        } else {
            if (apiClient == null) {
                apiClient = new ApiClient(context);
            }
        }

        return apiClient;
    }

    private ApiInterface getApiInterface() {
        return apiInterface;
    }

}
