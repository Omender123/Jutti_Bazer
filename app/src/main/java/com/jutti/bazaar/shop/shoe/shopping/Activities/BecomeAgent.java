package com.jutti.bazaar.shop.shoe.shopping.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.api.SavedPrefManager;
import com.jutti.bazaar.shop.shoe.shopping.databinding.ActivityBecomeAgentBinding;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class BecomeAgent extends AppCompatActivity {

    ActivityBecomeAgentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkagentrequest(SavedPrefManager.getStringPreferences(BecomeAgent.this,"user_id"));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_become_agent);
    }

    public void saveForm(View view) {

        if (binding.etName.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid name", Toast.LENGTH_SHORT).show();
        } else if (binding.etEmail.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
        } else if (binding.etAddress.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid address", Toast.LENGTH_SHORT).show();
        } else if (binding.etAadhar.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid Aadhar number", Toast.LENGTH_SHORT).show();
        } else if (binding.etCity.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid city", Toast.LENGTH_SHORT).show();
        } else if (binding.etPin.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid pin code", Toast.LENGTH_SHORT).show();
        } else if (binding.etBankName.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid bank name", Toast.LENGTH_SHORT).show();
        } else if (binding.etBankHolder.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid account holder name", Toast.LENGTH_SHORT).show();
        } else if (binding.etAccountNum.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid account number", Toast.LENGTH_SHORT).show();
        } else if (binding.etIfsc.getText().toString().equals("")) {
            Toast.makeText(this, "Enter valid IFSC Code", Toast.LENGTH_SHORT).show();
        } else {

            agentrequest(""+binding.etName.getText().toString()
                    , ""+ SavedPrefManager.getStringPreferences(BecomeAgent.this,"user_id")
                    , ""+binding.etAddress.getText().toString()
                    , ""+binding.etEmail.getText().toString()
                    , ""+binding.etAadhar.getText().toString()
                    , ""+binding.etCity.getText().toString()
                    , ""+binding.etPin.getText().toString()
                    , ""+binding.etBankName.getText().toString()
                    , ""+binding.etAccountNum.getText().toString()
                    , ""+binding.etBankHolder.getText().toString(),
                    ""+binding.etIfsc.getText().toString()
            );


        }


    }


    public void agentrequest(final String name, String user_id, String address, String email, String aadhar, String city, String pin, String bankName, String accountNumber, String accountName, String ifsc) {
        final ProgressDialog progressDialog = new ProgressDialog(BecomeAgent.this);
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("TAG", "agentrequest: "+"" + name+ "" + email+ "" + aadhar+
                "" + address+ "" + city+ "" + pin+ "" + bankName+
                "" + accountNumber+"" + accountName+ "" + ifsc);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.agentrequest(user_id, "" + name, "" + email, "" + aadhar,
                "" + address, "" + city, "" + pin, "" + bankName,
                "" + accountNumber, "" + accountName, "" + ifsc);


        call.enqueue(new Callback<ResponseBody>() {

            @SuppressLint("WrongConstant")
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
                            checkagentrequest(SavedPrefManager.getStringPreferences(BecomeAgent.this,"user_id"));

                            progressDialog.dismiss();


                        } else {
                            Toast.makeText(BecomeAgent.this, "" + message, Toast.LENGTH_SHORT).show();


                        }

                    } catch (Exception e) {
                        Log.i("result_exception", "" + e.getMessage());
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                } else if (response.code() == 404) {
                    progressDialog.dismiss();

                    // Toast.makeText(Register_user.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    // Toast.makeText(Register_user.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("result_t", "" + t.getMessage());

                progressDialog.dismiss();

            }


        });
    }
    public void checkagentrequest(final String user_id) {
        final ProgressDialog progressDialog = new ProgressDialog(BecomeAgent.this);
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.checkagentrequest(user_id);


        call.enqueue(new Callback<ResponseBody>() {

            @SuppressLint("WrongConstant")
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

                            binding.llSubmitted.setVisibility(View.VISIBLE);
                            binding.form.setVisibility(View.GONE);
                            progressDialog.dismiss();


                        } else {
                            progressDialog.dismiss();

                            binding.llSubmitted.setVisibility(View.GONE);
                            binding.form.setVisibility(View.VISIBLE);
                          //  Toast.makeText(BecomeAgent.this, "" + message, Toast.LENGTH_SHORT).show();


                        }

                    } catch (Exception e) {
                        Log.i("result_exception", "" + e.getMessage());
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                } else if (response.code() == 404) {
                    progressDialog.dismiss();

                    // Toast.makeText(Register_user.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    // Toast.makeText(Register_user.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("result_t", "" + t.getMessage());

                progressDialog.dismiss();

            }


        });
    }

    public void goBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public interface Service {
        @Headers("Authkey: APPCBSBDMPL")
        @FormUrlEncoded
        @POST("agentrequest")
        Call<ResponseBody> agentrequest(
                @Field("user_id") String user_id,
                @Field("name") String name,
                @Field("email") String email,
                @Field("aadhar") String aadhar,
                @Field("address") String address,
                @Field("city") String city,
                @Field("pincode") String pincode,
                @Field("bankname") String bankname,
                @Field("accountnumber") String accountnumber,
                @Field("accountname") String accountname,
                @Field("ifsc") String ifsc);

        @Headers("Authkey: APPCBSBDMPL")
        @FormUrlEncoded
        @POST("checkagentrequest")
        Call<ResponseBody> checkagentrequest(@Field("user_id") String user_id);


    }

}
