package com.jutti.bazaar.shop.shoe.shopping.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jutti.bazaar.shop.shoe.shopping.Model.Model_address;
import com.jutti.bazaar.shop.shoe.shopping.Activities.Home;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.databinding.ActivitySearchBinding;

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

public class Search extends AppCompatActivity {

    ActivitySearchBinding binding;
    public static ArrayList<String> id = new ArrayList();
    public static ArrayList<String> title = new ArrayList();
    public static ArrayList<String> cat = new ArrayList();
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Search.this, Home.class));
            }
        });

        init();
    }

    private void init() {

        binding.etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() > 1)
                    searchnames(binding.etSearch.getText().toString());
            }
        });
    }


    public void searchnames(final String keyword) {

        binding.pb.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.searchnames(keyword);
        //     Toast.makeText(this, ""+product_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        title.clear();
                        cat.clear();
                        id.clear();

                        String result = response.body().string();

                        Log.i("resp_address", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");



                            if (success.equals("true")) {

                                binding.pb.setVisibility(View.GONE);

                                if (obj.has("data")) {

                                    JSONArray arr_product = obj.optJSONArray("data");
                                    if (arr_product.length() > 0) {
                                        Log.d("resp", "" + arr_product);


                                        for (int i = 0; i < arr_product.length(); i++) {
                                            JSONObject jsonObject_prod = arr_product.optJSONObject(i);
                                            //////////////    Adding to Datamodel   ////////////////

                                            Model_address model_cart = new Model_address();
                                            if (!jsonObject_prod.isNull("ID")) {
                                                id.add(jsonObject_prod.optString("ID"));

                                            } else {
                                                id.add("");
                                            }
                                            if (!jsonObject_prod.isNull("TITLE")) {
                                                title.add(jsonObject_prod.optString("TITLE"));
                                            } else {
                                                title.add("");
                                            }
                                            if (!jsonObject_prod.isNull("TYPE")) {
                                                cat.add(jsonObject_prod.optString("TYPE"));
                                            } else {
                                                cat.add("");
                                            }


                                        }

                                        setup_search();


                                    } else {

                                        Toast.makeText(Search.this, "No search result found", Toast.LENGTH_SHORT).show();
                                    }



                            } else {
                                binding.pb.setVisibility(View.GONE);
                                    Toast.makeText(Search.this, message, Toast.LENGTH_SHORT).show();

                            }


                        }
                    } catch (Exception e) {
                        binding.pb.setVisibility(View.GONE);

                        Toast.makeText(Search.this, "" + e, Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {

                    binding.pb.setVisibility(View.GONE);

                    // Toast.makeText(mContext, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    //  Toast.makeText(mContext, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                    binding.pb.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Toast.makeText(mContext, "Something went wrong at server!", Toast.LENGTH_SHORT).show();

                binding.pb.setVisibility(View.GONE);

            }
        });
    }

    public void setup_search() {

        Adapter_search adapter_notification = new Adapter_search(this);
        binding.recycler.setLayoutManager(new GridLayoutManager(this, 1));
        binding.recycler.setAdapter(adapter_notification);
        adapter_notification.notifyDataSetChanged();

    }


    public interface Service {

        @Headers("Authkey:APPCBSBDMPL")
        @POST("searchnames")
        @FormUrlEncoded
        Call<ResponseBody> searchnames(@Field("keyword") String keyword);

    }


}