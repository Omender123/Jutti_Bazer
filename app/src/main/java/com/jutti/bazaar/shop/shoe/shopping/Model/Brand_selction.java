package com.jutti.bazaar.shop.shoe.shopping.Model;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.jutti.bazaar.shop.shoe.shopping.Activities.Home;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_select_brand;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
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

public class Brand_selction extends AppCompatActivity {
    String imageurlprod = "",category="";
    RecyclerView recycler_brand_selction;

    LinearLayout yesresult;
    ImageView noresult,back_from_list;
    TextView category_nam,seealll;
    public static String cat_id = "" ,cat_name = "";
    Adapter_select_brand adapter_select_brand;
    GridLayoutManager gridLayoutManager;

    public static ArrayList<String> brand_id = new ArrayList<>();
    public static ArrayList<String> cate_id = new ArrayList<>();
    public static ArrayList<String> brand_image = new ArrayList<>();
    public static ArrayList<String> brand_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_selction);

        init();
        Clicks();


    }

    private void init() {

        recycler_brand_selction = findViewById(R.id.recycler_brand_selction);
        noresult = findViewById(R.id.nores);
        yesresult = findViewById(R.id.yesresult);
        back_from_list = findViewById(R.id.back_from_list);
        seealll = findViewById(R.id.seealll);
        category_nam = findViewById(R.id.category_nam);

        brandlistbycategory("" + cat_id);
        adapter_select_brand = new Adapter_select_brand(this);

        category_nam.setText(""+cat_name);

    }


    private void Clicks() {
        back_from_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat_id="";
                cat_name="";
                startActivity(new Intent(Brand_selction.this, Home.class));

            }
        });
        seealll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product_list.categoryID=""+cat_id;
                startActivity(new Intent(Brand_selction.this,Product_list.class));

            }
        });
    }

    public void brandlistbycategory(final String category_id) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.brandlistbycategory(category_id);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();

                        Log.i("result", "" + result);
                        JSONObject obj = new JSONObject(result);
                        Log.d("resp", "" + obj);
                        String success = obj.optString("success");
                        String message = obj.optString("message");
                        brand_id.clear();
                        cate_id.clear();
                        brand_image.clear();
                        brand_name.clear();

                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (obj.has("image_base_path")) {
                                imageurlprod = obj.getString("image_base_path");
                                Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                            }
                            if (obj.has("category_id")) {
                                category=obj.getString("category_id");
                               // Toast.makeText(Brand_selction.this, ""+category, Toast.LENGTH_SHORT).show();
                            }


                            if (success.equals("true")) {
                                message = obj.optString("message");

                             //  Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();


                                if (obj.has("data")) {

                                    JSONArray arr_order = obj.getJSONArray("data");

                                    if (arr_order.length() > 0) {
                                        Log.d("responseee", "" + arr_order);

                                        noresult.setVisibility(View.GONE);

                                        for (int i = 0; i < arr_order.length(); i++) {
                                            JSONObject jsonObject = arr_order.optJSONObject(i);
                                            //////////////    Adding to Datamodel   ////////////////


                                            if (!jsonObject.isNull("brand_id")) {
                                                brand_id.add(jsonObject.getString("brand_id"));

                                            } else {
                                                brand_id.add("");
                                            }
                                            if (!jsonObject.isNull("brand_name")) {
                                                brand_name.add(jsonObject.getString("brand_name"));

                                            } else {
                                                brand_name.add("");
                                            }
                                            if (!jsonObject.isNull("brand_image")) {
                                                brand_image.add(imageurlprod + jsonObject.getString("brand_image"));

                                            } else {
                                                brand_image.add("");
                                            }

                                            //show_yesdata();


                                            setup_cart_items();

                                        }
                                    } else {
                                        setup_cart_items();
                                        noresult.setVisibility(View.VISIBLE);
                                        // Toast.makeText(Brand_selction.this, "No Result", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(Brand_selction.this, "No Result", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(Brand_selction.this, "No Result", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            String err_message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "error" + err_message, Toast.LENGTH_SHORT).show();
                            Log.d("error", "" + err_message);
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                        Log.d("e", "" + e);
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {

                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setup_cart_items() {

        gridLayoutManager = new GridLayoutManager(this, 2);
        recycler_brand_selction.setAdapter(adapter_select_brand);
        recycler_brand_selction.setLayoutManager(gridLayoutManager);

        adapter_select_brand.notifyDataSetChanged();


    }

    @Override
    public void onBackPressed() {

        cat_id="";
        cat_name="";
       finish();
        super.onBackPressed();
    }

    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("brandlistbycategory ")
        @FormUrlEncoded
        Call<ResponseBody> brandlistbycategory(@Field("category_id") String category_id
        );

    }


}
