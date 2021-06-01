package com.jutti.bazaar.shop.shoe.shopping.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_order_list;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Order;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class OrdersList extends AppCompatActivity {

    LinearLayout llorder1;
    ImageView back;
    Adapter_order_list adapter_order_list;
    ProgressDialog progressDialog;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recycler_order_list;
    SharedPreferences sharedPreferences;
    String Token = "", user = "", image = "";
    TextView no_order;
    public static ArrayList<Model_Order> order = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        init();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Token = sharedPreferences.getString("user_login_token", "");
        user = sharedPreferences.getString("user_id", "");

        orderlist("" + Token, "" + user);

    }


    void init() {
        back = findViewById(R.id.iv_back);
        recycler_order_list = findViewById(R.id.recycler_order_list);
        no_order = findViewById(R.id.no_order);

        adapter_order_list = new Adapter_order_list(OrdersList.this);
        linearLayoutManager = new LinearLayoutManager(OrdersList.this);
        clicks();
    }


    void clicks() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrdersList.this, Home.class));
            }
        });

    }


    public void removeOrder(String orderID) {
        hideorder(orderID);
    }

    public void orderlist(final String user_login_token, String user_id) {


        progressDialog = new ProgressDialog(OrdersList.this);
        progressDialog.setMessage("Please Wait..");
        // progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.orderlist(user_login_token, user_id);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();

                        Log.i("result", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");
                        order.clear();


                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (success.equals("true")) {
                                message = obj.optString("message");

                                //  Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                if (obj.has("data")) {

                                    JSONArray arr_order = obj.getJSONArray("data");

                                    if (arr_order.length() > 0) {
                                        Log.d("responseee", "" + arr_order);


                                        for (int i = 0; i < arr_order.length(); i++) {
                                            JSONObject jsonObject_prod = arr_order.optJSONObject(i);
                                            //////////////    Adding to Datamodel   ////////////////

                                            Model_Order model_order = new Model_Order();
                                            if (!jsonObject_prod.isNull("order_id")) {
                                                model_order.setORDER_ID(jsonObject_prod.optString("order_id"));

                                            } else {
                                                model_order.setORDER_ID("");
                                            }
                                            if (!jsonObject_prod.isNull("order_display_id")) {
                                                model_order.setDisplay_id(jsonObject_prod.optString("order_display_id"));

                                            } else {
                                                model_order.setDisplay_id("");
                                            }
                                            if (!jsonObject_prod.isNull("order_date")) {
                                                model_order.setORDER_DATE(jsonObject_prod.optString("order_date"));
                                            } else {
                                                model_order.setORDER_DATE("");
                                            }
                                            if (!jsonObject_prod.isNull("order_status")) {
                                                model_order.setSTATUS(jsonObject_prod.optString("order_status"));

                                            } else {
                                                model_order.setSTATUS("");
                                            }
                                            if (!jsonObject_prod.isNull("order_total_price")) {
                                                model_order.setORDER_AMOUNT("₹ " + jsonObject_prod.optString("order_total_price"));
                                            } else {
                                                model_order.setORDER_AMOUNT("");
                                            }

                                            if (!jsonObject_prod.isNull("seller_name")) {
                                                model_order.setSELLER_NAME(jsonObject_prod.optString("seller_name"));
                                            } else {
                                                model_order.setSELLER_NAME("");
                                            }

                                            if (!jsonObject_prod.isNull("user_id")) {
                                                model_order.setUSER_ID(jsonObject_prod.optString("user_id"));
                                            } else {
                                                model_order.setUSER_ID("");
                                            }

                                            order.add(model_order);

                                            setup_cart_items();

                                        }
                                        adapter_order_list.notifyDataSetChanged();
                                        setup_cart_items();
                                    } else {
                                        recycler_order_list.setVisibility(View.GONE);
                                        no_order.setVisibility(View.VISIBLE);
                                    }

                                }

                            }


                        } else {
                            progressDialog.dismiss();
                            String err_message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "error" + err_message, Toast.LENGTH_SHORT).show();
                            Log.d("error", "" + err_message);
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.d("e", "" + e);
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void hideorder(final String order_id) {
        progressDialog = new ProgressDialog(OrdersList.this);
        progressDialog.setMessage("Please Wait..");
        // progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.hideorder(order_id, user);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();

                        Log.i("result", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");




                            if (success.equals("true")) {

                                progressDialog.dismiss();

                                orderlist("" + Token, "" + user);
                                Toast.makeText(OrdersList.this, ""+message, Toast.LENGTH_SHORT).show();

                        } else {
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.d("error", "" + message);
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.d("e", "" + e);
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setup_cart_items() {


        recycler_order_list.setLayoutManager(linearLayoutManager);
        recycler_order_list.setAdapter(adapter_order_list);
    }


    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("orderlist")
        @FormUrlEncoded
        Call<ResponseBody> orderlist(@Field("user_login_token") String user_login_token,
                                     @Field("user_id") String user_id);



    @Headers("Authkey:APPCBSBDMPL")
    @POST("hideorder")
    @FormUrlEncoded
    Call<ResponseBody> hideorder(@Field("order_id") String order_id,
                                 @Field("user_id") String user_id);

}


    @Override
    public void onBackPressed() {
        startActivity(new Intent(OrdersList.this, Home.class));

        super.onBackPressed();
    }
}
