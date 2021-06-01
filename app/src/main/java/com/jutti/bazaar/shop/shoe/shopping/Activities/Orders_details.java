package com.jutti.bazaar.shop.shoe.shopping.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_detail;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_After_Order;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.api.SavedPrefManager;
import com.jutti.bazaar.shop.shoe.shopping.databinding.ActivityOrdersDetailsBinding;


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

public class Orders_details extends AppCompatActivity {
    RecyclerView recycler_order_detail;
    ProgressDialog progressDialog;
    String image, order_display_id, order_Total_price, order_status, Order_date;
    SharedPreferences sharedPreferences;
    ImageView r1, r2, r3, r4, back;
    LinearLayout llremark, traking;
    View v1, v2, v3;
    TextView tv_seller, mobile, name, address, remark, Status_track, toatalprice;
    Adapter_detail adapter_order_detail;
    public static String order = "", productID = "", sellerID = "";
    public static ArrayList<Model_After_Order> orderdetail = new ArrayList<>();

    public static ArrayList<String> prodID = new ArrayList();
    public static ArrayList<String> prodShopID = new ArrayList();
    public static ArrayList<String> prodtitle = new ArrayList();
    public static ArrayList<String> qty = new ArrayList();
    public static ArrayList<String> price = new ArrayList();
    public static ArrayList<String> product_size = new ArrayList();
    public static ArrayList<String> prodShopName = new ArrayList();
    public static ArrayList<String> subtot = new ArrayList();
    public static ArrayList<String> img = new ArrayList();
    public static ArrayList<String> type = new ArrayList();
    ActivityOrdersDetailsBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders_details);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        context = Orders_details.this;

        order = getIntent().getStringExtra("order_id");
        SavedPrefManager.saveStringPreferences(Orders_details.this, "OID", "" + order);


        recycler_order_detail = findViewById(R.id.recycler_order_detail);
        orderlist("" + sharedPreferences.getString("user_login_token", "")
                , "" + sharedPreferences.getString("user_id", ""), "" + order);
        Log.d("Resultorder", "" + sharedPreferences.getString("user_login_token", "")
                + sharedPreferences.getString("user_id", "") + order);

        init();
    }

    private void init() {

        toatalprice = findViewById(R.id.totalprice);
        Status_track = findViewById(R.id.Status_track);
        r1 = findViewById(R.id.r1);
        tv_seller = findViewById(R.id.tv_seller);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        back = findViewById(R.id.iv_back_afterorder);

        remark = findViewById(R.id.remark);
        traking = findViewById(R.id.traking);
        llremark = findViewById(R.id.llremark);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        mobile = (TextView) findViewById(R.id.mobile);

        clicks();
    }

    private void clicks() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orders_details.this, OrdersList.class));
            }
        });

        binding.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               dialog();
            }
        });

        binding.relOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reorderitems("" + sharedPreferences.getString("user_id", ""), "" + order);
            }
        });

        binding.tvTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Orders_details.this, OrderTracking.class));
            }
        });


    }


    public void orderlist(final String user_login_token, String user_id, String order_id) {


        progressDialog = new ProgressDialog(Orders_details.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.orderdetails(user_login_token, user_id, order_id);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    orderdetail.clear();
                    prodShopID.clear();
                    prodID.clear();
                    prodtitle.clear();
                    qty.clear();
                    price.clear();
                    subtot.clear();
                    img.clear();
                    type.clear();
                    product_size.clear();
                    prodShopName.clear();


                    try {

                        String result = response.body().string();

                        Log.i("result", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");
                        orderdetail.clear();


                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (success.equals("true")) {
                                message = obj.optString("message");

                                //  Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                if (obj.has("image_base_path")) {
                                    image = obj.getString("image_base_path");
                                    Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                                }

                                if (obj.has("data")) {
                                    JSONObject objdata = obj.getJSONObject("data");

                                    String order_address_name = "", address_line_2 = "", order_address_locality = "", order_address_landmark = "",
                                            order_address = "", order_address_city = "", order_address_state = "", order_address_pincode = "", order_address_mobile = "";

                                    if (!objdata.isNull("order_address_locality")) {
                                        order_address_locality = objdata.getString("order_address_locality");
                                    } else {
                                        order_address_locality = "";
                                    }
                                    if (!objdata.isNull("order_address_locality")) {
                                        tv_seller.setText(objdata.getString("seller_name"));
                                    } else {
                                        tv_seller.setText("");
                                    }
                                    if (!objdata.isNull("order_address_landmark")) {
                                        order_address_landmark = objdata.getString("order_address_landmark");
                                    } else {
                                        order_address_landmark = "";
                                    }
                                    if (!objdata.isNull("order_address")) {
                                        order_address = objdata.getString("order_address");
                                    } else {
                                        order_address = "";
                                    }
//                                    if (!objdata.isNull("address_line_2")) {
//                                        address_line_2 = objdata.getString("address_line_2");
//                                    } else {
//                                        address_line_2 = "";
//                                    }
                                    if (!objdata.isNull("order_address_city")) {
                                        order_address_city = objdata.getString("order_address_city");
                                    } else {
                                        order_address_city = "";
                                    }
                                    if (!objdata.isNull("order_address_state")) {
                                        order_address_state = objdata.getString("order_address_state");
                                    } else {
                                        order_address_state = "";
                                    }
                                    if (!objdata.isNull("order_address_pincode")) {
                                        order_address_pincode = objdata.getString("order_address_pincode");
                                    } else {
                                        order_address_pincode = "";
                                    }


                                    name.setText(objdata.getString("order_address_name"));
                                    address.setText("Pincode:" + order_address_pincode + " " + order_address_locality + " " + order_address_landmark + " " + order_address + " " + order_address_city + " " + order_address_state);
                                    mobile.setText(objdata.getString("order_address_mobile"));


                                    if (!objdata.isNull("order_display_id")) {
                                        order_display_id = objdata.getString("order_display_id");
                                    } else {
                                        order_display_id = "";
                                    }
                                    if (!objdata.isNull("order_total_price")) {
                                        toatalprice.setText("₹ " + objdata.getString("order_total_price"));
                                    } else {
                                        toatalprice.setText("");
                                    }
                                    if (!objdata.isNull("order_remark")) {
                                        remark.setText(objdata.getString("order_remark"));
                                    } else {
                                        remark.setText("");
                                        llremark.setVisibility(View.GONE);
                                    }
                                    if (!objdata.isNull("order_status")) {
                                        String stat = objdata.getString("order_status");
                                        Status_track.setText(objdata.getString("order_status"));
                                        Log.d("stats", "onResponse: " + stat);
                                        if (stat.contains("Processing")) {
                                            binding.cancelOrder.setVisibility(View.VISIBLE);

                                            r1.setImageResource(R.drawable.track);
                                        } else if (stat.contains("Confirmed")) {
                                            binding.cancelOrder.setVisibility(View.GONE);

                                            r1.setImageResource(R.drawable.track);
                                            v1.setBackgroundColor(Color.parseColor("#8BC34A"));
                                            r2.setImageResource(R.drawable.track);
                                        } else if (stat.contains("Shipped")) {
                                            binding.cancelOrder.setVisibility(View.GONE);

                                            r1.setImageResource(R.drawable.track);
                                            r2.setImageResource(R.drawable.track);
                                            r3.setImageResource(R.drawable.track);
                                            v1.setBackgroundColor(Color.parseColor("#8BC34A"));
                                            v2.setBackgroundColor(Color.parseColor("#8BC34A"));

                                        } else if (stat.contains("Delivered")) {
                                            binding.cancelOrder.setVisibility(View.GONE);

                                            binding.relOrder.setVisibility(View.VISIBLE);
                                            v1.setBackgroundColor(Color.parseColor("#8BC34A"));
                                            v2.setBackgroundColor(Color.parseColor("#8BC34A"));
                                            v3.setBackgroundColor(Color.parseColor("#8BC34A"));
                                            r1.setImageResource(R.drawable.track);
                                            r2.setImageResource(R.drawable.track);
                                            r3.setImageResource(R.drawable.track);
                                            r4.setImageResource(R.drawable.track);


                                        } else if (stat.contains("Cancelled")) {
                                            binding.cancelOrder.setVisibility(View.GONE);

                                            traking.setVisibility(View.GONE);
                                            llremark.setVisibility(View.VISIBLE);
                                        } else {
                                            traking.setVisibility(View.VISIBLE);
                                            llremark.setVisibility(View.GONE);
                                        }

                                    } else {
                                        Status_track.setText("");
                                    }
                                    if (!objdata.isNull("order_date")) {
                                        Order_date = objdata.getString("order_date");
                                    } else {
                                        Order_date = "";
                                    }

                                    if (objdata.has("order_meta")) {
                                        JSONArray arr_order = objdata.getJSONArray("order_meta");

                                        if (arr_order.length() > 0) {
                                            Log.d("responseee", "" + arr_order);

                                            for (int i = 0; i < arr_order.length(); i++) {
                                                JSONObject jsonObject_prod = arr_order.optJSONObject(i);
// Adding to Datamodel

                                                if (!jsonObject_prod.isNull("seller_user_id")) {
                                                    prodShopID.add(jsonObject_prod.getString("seller_user_id"));
                                                } else {
                                                    prodShopID.add("");
                                                }
                                                if (!jsonObject_prod.isNull("seller_name")) {
                                                    prodShopName.add(jsonObject_prod.getString("seller_name"));
                                                } else {
                                                    prodShopName.add("");
                                                }

                                                if (!jsonObject_prod.isNull("product_id")) {
                                                    prodID.add(jsonObject_prod.getString("product_id"));
                                                } else {
                                                    prodID.add("");
                                                }

                                                if (!jsonObject_prod.isNull("product_title")) {

                                                    prodtitle.add(jsonObject_prod.getString("product_title"));
                                                } else {
                                                    prodtitle.add("");

                                                }

                                                if (!jsonObject_prod.isNull("cart_quantity")) {

                                                    qty.add(jsonObject_prod.getString("cart_quantity"));
                                                } else {
                                                    qty.add("");

                                                }

                                                if (!jsonObject_prod.isNull("product_price")) {

                                                    price.add("₹ " + jsonObject_prod.getString("product_price"));
                                                } else {
                                                    price.add("");

                                                }
                                                if (!jsonObject_prod.isNull("product_size")) {

                                                    product_size.add(jsonObject_prod.getString("product_size"));
                                                } else {
                                                    product_size.add("");

                                                }
                                                if (!jsonObject_prod.isNull("price_sub_total")) {

                                                    subtot.add("₹ " + jsonObject_prod.getString("price_sub_total"));
                                                } else {
                                                    subtot.add("");

                                                }
                                                if (!jsonObject_prod.isNull("product_image")) {

                                                    img.add(image + jsonObject_prod.getString("product_image"));
                                                } else {
                                                    img.add("");

                                                }
                                                if (!jsonObject_prod.isNull("product_stock_type")) {

                                                    type.add(jsonObject_prod.getString("product_stock_type"));
                                                } else {
                                                    type.add("");

                                                }

                                            }
                                            setPriceDataList();
// adapter_order_detail.notifyDataSetChanged();
// setup_cart_items();
                                        }
                                    }
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Orders_details.this, "" + message, Toast.LENGTH_SHORT).show();
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

    public void cancelorder(String user_id, String order_id, String cancel_remark) {
        progressDialog = new ProgressDialog(Orders_details.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.cancelorder(""+user_id,""+ order_id, ""+cancel_remark);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        progressDialog.dismiss();

                        String result = response.body().string();

                        Log.i("result", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("success")) {
                            Toast.makeText(Orders_details.this, "" + message, Toast.LENGTH_SHORT).show();
                            success = obj.optString("success");
                            orderlist("" + sharedPreferences.getString("user_login_token", "")
                                    , "" + sharedPreferences.getString("user_id", ""), "" + order);


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

    public void reorderitems(String user_id, String order_id) {
        progressDialog = new ProgressDialog(Orders_details.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.reorderitems(user_id, order_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        progressDialog.dismiss();
                        String result = response.body().string();

                        Log.i("result", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("success")) {
                            Toast.makeText(Orders_details.this, "" + message, Toast.LENGTH_SHORT).show();

                            success = obj.optString("success");
                            orderlist("" + sharedPreferences.getString("user_login_token", "")
                                    , "" + sharedPreferences.getString("user_id", ""), "" + order);


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


    private void dialog() {
        final Dialog dialog_options = new Dialog(context);
        dialog_options.setContentView(R.layout.dialog_cancel);
        dialog_options.setCancelable(true);
        dialog_options.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_options.show();

        final EditText et_name = dialog_options.findViewById(R.id.et_name);

        TextView send = dialog_options.findViewById(R.id.send);
        TextView cancelPolicy = dialog_options.findViewById(R.id.cancelPolicy);
        final CheckBox cb = dialog_options.findViewById(R.id.cb);
        ImageView iv_cancel = dialog_options.findViewById(R.id.iv_cancel);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_name.getText().toString().equals("")){

                    Toast.makeText(Orders_details.this, "Please add reason to cancellation", Toast.LENGTH_SHORT).show();
                }else if(!cb.isChecked()){
                    Toast.makeText(Orders_details.this, "Please agree to our cancellation policies", Toast.LENGTH_SHORT).show();

                }else {
                    dialog_options.dismiss();

                    cancelorder("" + sharedPreferences.getString("user_id", ""),
                            "" + order, ""+et_name.getText().toString());

                }

                }


        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_options.dismiss();
            }
        });
        cancelPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: "+SavedPrefManager.getStringPreferences(context,"url_cancel"));
                openWebPage(SavedPrefManager.getStringPreferences(context,"url_cancel"));

            }
        });

    }



    public void openWebPage(String url) {

        Uri webpage = Uri.parse(url);

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://" + url);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @SuppressLint("WrongConstant")
    private void setPriceDataList() {
        Adapter_detail adapter_detail = new Adapter_detail(Orders_details.this);
        recycler_order_detail.setLayoutManager(new GridLayoutManager(Orders_details.this, 1));
        recycler_order_detail.setAdapter(adapter_detail);
        adapter_detail.notifyDataSetChanged();
    }


    public interface Service {

        @Headers("Authkey:APPCBSBDMPL")
        @POST("orderdetails")
        @FormUrlEncoded
        Call<ResponseBody> orderdetails(@Field("user_login_token") String user_login_token,
                                        @Field("user_id") String user_id,
                                        @Field("order_id") String order_id);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("reorderitems")
        @FormUrlEncoded
        Call<ResponseBody> reorderitems(@Field("user_id") String user_id,
                                        @Field("order_id") String order_id);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("cancelorder")
        @FormUrlEncoded
        Call<ResponseBody> cancelorder(@Field("user_id") String user_id,
                                       @Field("order_id") String order_id,
                                       @Field("cancel_remark") String cancel_remark);
    }

    @Override
    public void onBackPressed() {
        order = "";

        startActivity(new Intent(Orders_details.this, OrdersList.class));
        super.onBackPressed();
    }
}