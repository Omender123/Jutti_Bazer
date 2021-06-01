package com.jutti.bazaar.shop.shoe.shopping.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;

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

public class Add_Address extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    EditText et_pincode, et_locality, et_address, et_city, et_state, et_landmark, et_mobile, et_name,et_address1;
    LinearLayout base_view;
    Button bt_save;
    TextView title;

    public static String From="", city = "", state = "", pin = "", add = "", add1 = "", name = "", phone = "", landmark = "", locality = "",address_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__address);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Add_Address.this);

        init();

        if (!From.equals("")){
            et_name.setText("");
            address_id="";
            et_pincode.setText("");
            et_locality.setText("");
            et_address.setText("");
            et_address1.setText("");
            et_city.setText("");
            et_state.setText("");
            et_landmark.setText("");
            et_mobile.setText("");
        }else {
            et_name.setText(name);
            et_pincode.setText(pin);
            et_locality.setText(locality);
            et_address.setText(add);
            et_address1.setText(add1);
            et_city.setText(city);
            et_state.setText(state);
            et_landmark.setText(landmark);
            et_mobile.setText(phone);
        }


        if (!name.equals("")) {
            bt_save.setText("Update");
            title.setText("Edit Address");
        }
    }

    private void init() {

        et_pincode = findViewById(R.id.et_pincode);
        bt_save = findViewById(R.id.bt_save);
        title = findViewById(R.id.title);
        et_locality = findViewById(R.id.et_locality);
        et_address = findViewById(R.id.et_address);
        et_address1 = findViewById(R.id.et_address1);
        et_city = findViewById(R.id.et_city);
        et_state = findViewById(R.id.et_state);
        et_landmark = findViewById(R.id.et_landmark);
        et_mobile = findViewById(R.id.et_mobile);
        et_name = findViewById(R.id.et_name);
        base_view = findViewById(R.id.base_view);
    }

    public void goBack(View view) {
        onBackPressed();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void save_address(View view) {


        if (et_pincode.getText().toString().equals("")) {

            et_pincode.setError("Enter valid Pincode");
        } else if (et_address.getText().toString().equals("")) {

            et_address.setError("Enter valid Address lane 2");
        } else if (et_city.getText().toString().equals("")) {

            et_city.setError("Enter valid city");
        } else if (et_state.getText().toString().equals("")) {

            et_state.setError("Enter valid state");
        } else if (et_locality.getText().toString().equals("")) {

            et_locality.setError("Enter valid Address lane 1");
        } else if (et_mobile.getText().toString().equals("")) {

            et_mobile.setError("Enter valid mobile");
        } else if (et_name.getText().toString().equals("")) {

            et_name.setError("Enter valid name");
        } else {

            if (!From.equals("")) {

                newaddress(""+ et_name.getText().toString(),
                        "" + sharedPreferences.getString("user_id", ""),
                        "" + et_address.getText().toString(),
                        ""+et_address1.getText().toString(),
                        "" + et_landmark.getText().toString(),
                        "" + et_city.getText().toString(),
                        "" + et_state.getText().toString(),
                        "" + et_locality.getText().toString(),
                        "" + et_mobile.getText().toString(),
                        "" + et_pincode.getText().toString());
            } else {
                editaddress(""+address_id,
                        "" + et_name.getText().toString(),
                        "" + sharedPreferences.getString("user_id", ""),
                        "" + et_address.getText().toString(),
                        "" + et_address1.getText().toString(),
                        "" + et_landmark.getText().toString(),
                        "" + et_city.getText().toString(),
                        "" + et_state.getText().toString(),
                        "" + et_locality.getText().toString(),
                        "" + et_mobile.getText().toString(),
                        "" + et_pincode.getText().toString());

            }
        }
    }

    public void cancel(View view) {
       onBackPressed();
    }



    public void newaddress(final String name, String user_id, String address, String address1, String landmark, String city, String state, String locality, String mobile, String pincode) {
        final ProgressDialog progressDialog = new ProgressDialog(Add_Address.this);
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.i("result", "" +name+user_id+address+""+ landmark+""+ city+""+ state+""+locality+""+mobile+ ""+pincode);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.newaddress(""+name,""+ user_id, ""+address,""+address1,""+ landmark,""+ city,""+ state, ""+locality, ""+mobile, ""+pincode);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();

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

                            progressDialog.dismiss();


                            if (message.equals("New address saved.")) {
                                Snackbar.make(base_view, "" + message, BaseTransientBottomBar.LENGTH_LONG).show();
                                startActivity(new Intent(Add_Address.this, AddressActivity.class));
                                finish();

                            }

                        } else {
                           Toast.makeText(Add_Address.this, "" + message, Toast.LENGTH_SHORT).show();


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

    public void editaddress(final String address_id,String name, String user_id, String address,  String address1, String landmark, String city, String state, String locality, String mobile, String pincode) {
        Log.d("TAG", "editaddress: "+address);
        Log.d("TAG", "editaddress1: "+address1);

        final ProgressDialog progressDialog = new ProgressDialog(Add_Address.this);
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.editaddress(address_id,name, user_id, address, address1, landmark, city, state, locality, mobile, pincode);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {

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


                            if (message.equals("Address updated.")) {
                                Snackbar.make(base_view, "" + message, Snackbar.LENGTH_LONG).show();
                                onBackPressed();

                            }

                        } else {
                            //  Toast.makeText(LoginUser.this, "" + message, Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();


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



    public interface Service {
        @Headers("Authkey: APPCBSBDMPL")
        @FormUrlEncoded
        @POST("newaddress")
        Call<ResponseBody> newaddress(@Field("name") String name,
                                      @Field("user_id") String user_id,
                                      @Field("address") String address,
                                      @Field("address1") String address1,
                                      @Field("landmark") String landmark,
                                      @Field("city") String city,
                                      @Field("state") String state,
                                      @Field("locality") String locality,
                                      @Field("mobile") String mobile,
                                      @Field("pincode") String pincode);

        @Headers("Authkey: APPCBSBDMPL")
        @FormUrlEncoded
        @POST("editaddress")
        Call<ResponseBody> editaddress(@Field("address_id") String address_id,@Field("name") String name,
                                       @Field("user_id") String user_id,
                                       @Field("address") String address, @Field("address1") String address1,
                                       @Field("landmark") String landmark,
                                       @Field("city") String city,
                                       @Field("state") String state,
                                       @Field("locality") String locality,
                                       @Field("mobile") String mobile,
                                       @Field("pincode") String pincode);


    }

}
