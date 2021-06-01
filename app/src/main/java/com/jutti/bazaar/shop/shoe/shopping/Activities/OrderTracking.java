package com.jutti.bazaar.shop.shoe.shopping.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_tracking;
import com.jutti.bazaar.shop.shoe.shopping.Model.ModelTracking;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.api.SavedPrefManager;
import com.jutti.bazaar.shop.shoe.shopping.databinding.ActivityOrderTrackingBinding;

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

public class OrderTracking extends AppCompatActivity {

    ActivityOrderTrackingBinding binding;
    Context context;
    public static ArrayList<ModelTracking> tracking = new ArrayList<ModelTracking>();
    SharedPreferences sharedPreferences;
    public static String orderID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_tracking);
        context = OrderTracking.this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
clicks();

        orderlist(""+ SavedPrefManager.getStringPreferences(OrderTracking.this,"OID"));

    }

    private void clicks() {
        binding.ivBackAfterorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void orderlist(final String orderID) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.trackorder(orderID);

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


                        if (obj.has("success")) {


                            if (success.equals("true")) {
                                message = obj.optString("message");

                                //  Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                if (obj.has("data")) {

                                    JSONObject jsonData = obj.getJSONObject("data");

                                    JSONArray jsonArray = jsonData.getJSONArray("tracking_details");

                                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                                    JSONArray jsonArr = jsonObject.getJSONArray("checkpoints");

                                    if (jsonArr.length() > 0) {
                                        Log.d("responseee", "" + jsonArr);


                                        for (int i = 0; i < jsonArr.length(); i++) {
                                            JSONObject jsonObject_prod = jsonArr.optJSONObject(i);
                                            //////////////    Adding to Datamodel   ////////////////

                                            ModelTracking model_order = new ModelTracking();
                                            if (!jsonObject_prod.isNull("city")) {
                                                model_order.setCity(jsonObject_prod.optString("city"));

                                            } else {
                                                model_order.setCity("");
                                            }
                                            if (!jsonObject_prod.isNull("checkpoint_time")) {

                                                model_order.setCheckpoint_time(jsonObject_prod.optString("checkpoint_time"));

                                            } else {
                                                model_order.setCheckpoint_time("");
                                            }
                                            if (!jsonObject_prod.isNull("message")) {
                                                model_order.setMessage(jsonObject_prod.optString("message"));
                                            } else {
                                                model_order.setMessage("");
                                            }


                                            tracking.add(model_order);

                                            setupTracking();

                                        }


                                    } else {

                                        Toast.makeText(getApplicationContext(), "No tracking available yet." , Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();

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

    private void setupTracking() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        Adapter_tracking adapterTracking = new Adapter_tracking(OrderTracking.this);
        binding.shimmerRecyclerView.setLayoutManager(linearLayoutManager);
        binding.shimmerRecyclerView.setAdapter(adapterTracking);
    }


    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("trackorder")
        @FormUrlEncoded
        Call<ResponseBody> trackorder(@Field("order_id") String order_id);

    }

}