package com.jutti.bazaar.shop.shoe.shopping.Login_Register;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.jutti.bazaar.shop.shoe.shopping.Activities.Home;
import com.jutti.bazaar.shop.shoe.shopping.Activities.SplashActivity;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.api.ApiCallBack;
import com.jutti.bazaar.shop.shoe.shopping.api.ApiManager;
import com.jutti.bazaar.shop.shoe.shopping.api.ApiResponseListener;
import com.jutti.bazaar.shop.shoe.shopping.api.Model.ApiResponse;
import com.jutti.bazaar.shop.shoe.shopping.api.SavedPrefManager;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity {
    Dialog dialog_alert;
    Button btn_login, btn_verify;
    boolean reg = false;
    LinearLayout ll_register;
    EditText et_nbr, et_signin_phone, et_signin_password;
    TextView resend, txt_switchto_signup, txt_switchto_signin, tv_forgot_password;
    RelativeLayout ll_signup, ll_signin, base_view;
    Button btn_signin, btn_signup;
    String Selected_type = "Customer";
    SharedPreferences sharedPreferences;
    LinearLayout cv_otp;
    ImageView cancel;
    String otp_token = "", otp = "", mob = "", shop = "", pass = "";
     EditText edt_email,et_otop,et_password;
     Button btn_submit;
            Dialog dialog;

    EditText et_signup_email, et_signup_phone, et_signup_password, et_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_email);

        edt_email = (EditText) dialog.findViewById(R.id.etd_phone_no);
        et_otop = (EditText) dialog.findViewById(R.id.et_otop);
        et_password = (EditText) dialog.findViewById(R.id.et_password);
        btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
        ll_register = findViewById(R.id.ll_register);
        et_nbr = findViewById(R.id.et_nbr);

        btn_login = (Button) findViewById(R.id.btn_login);
        base_view = findViewById(R.id.base_view);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reg) {
                    startActivity(new Intent(getApplicationContext(), OtpActivity.class));
                } else {

                    if (et_nbr.getText().toString().equals("0000000000")) {
                        showloader_success("You are a WholeSeller", "You are Registered as WholeSeller, So you cannot login into customer app as Seller.");
                    } else {

                        Toast.makeText(LoginActivity.this, "Register with your details to proceed", Toast.LENGTH_SHORT).show();
                        reg = true;
                        ll_register.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        nostatusbar();
    }

    private void nostatusbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {

        inits();
        onClicks();

        super.onResume();
    }


    private void onClicks() {
        txt_switchto_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              signInScreen();
            }
        });
        txt_switchto_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              signUpScreen();
            }
        });


        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_signin_phone.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Enter valid Phone No.", Toast.LENGTH_SHORT).show();

                } else if (et_signin_password.getText().toString().equals("")) {

                    Toast.makeText(LoginActivity.this, "Enter valid Password", Toast.LENGTH_SHORT).show();

                } else {

                    loginwithpassword("" + et_signin_phone.getText().toString(), "" + et_signin_password.getText().toString());
                }

            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registersendotp("" + et_signup_phone.getText().toString());
                shop = et_signup_email.getText().toString();
                pass = et_signup_password.getText().toString();

            }
        });

    }

    public void dummy_intent(String selected_type) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", "1");
        editor.putString("user_type", selected_type);
        editor.commit();
        editor.apply();

        SplashActivity.get_appinfo(getApplicationContext());
        Intent beta_intent = new Intent(LoginActivity.this, Home.class);
        // beta_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(beta_intent);


    }

    void signInScreen(){
        ll_signin.setVisibility(View.VISIBLE);
        ll_signup.setVisibility(View.GONE);
        Selected_type = "Customer";
    }
    void signUpScreen(){
        ll_signin.setVisibility(View.GONE);
        ll_signup.setVisibility(View.VISIBLE);
        Selected_type = "Customer";
    }


    private void inits() {

        txt_switchto_signup = findViewById(R.id.txt_switchto_signup);
        txt_switchto_signin = findViewById(R.id.txt_switchto_signin);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);

        ll_signup = findViewById(R.id.ll_signup);
        ll_signin = findViewById(R.id.ll_signin);
        et_signin_phone = findViewById(R.id.et_signin_phone);
        et_signin_password = findViewById(R.id.et_signin_password);

        btn_signin = findViewById(R.id.btn_signin);
        btn_signup = findViewById(R.id.btn_signup);
        cv_otp = findViewById(R.id.cv_otp);
        cancel = findViewById(R.id.cancel);
        et_otp = findViewById(R.id.et_otp);
        resend = findViewById(R.id.resend);
        btn_verify = findViewById(R.id.btn_verify);


        et_signup_password = findViewById(R.id.et_signup_password);
        et_signup_phone = findViewById(R.id.et_signup_phone);
        et_signup_email = findViewById(R.id.et_signup_email);

        clicks();

    }

    private void clicks() {
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_signin_phone.getText().toString().equals("")) {
                    et_signin_phone.setError("Please enter your phone");
                } else {
                    forgotPassword(et_signin_phone.getText().toString());
                }
            }
        });


        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_otp.getText().toString().equals("")) {
                    et_otp.setError("Enter valid OTP");
                } else {

                    registercheckdotp("" + mob, "" + otp_token, "" + et_otp.getText().toString());
                }

            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registersendotp("" + et_signup_phone.getText().toString());

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cv_otp.setVisibility(View.GONE);
                ll_signup.setVisibility(View.VISIBLE);


            }
        });

    }

    public void next_intent(View view) {
        beta_intent();
    }

    private void beta_intent() {
        //    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }


    public void showloader_success(String title, String description) {
        dialog_alert = new Dialog(this);
        dialog_alert.setContentView(R.layout.dialog_seller);
        dialog_alert.show();
        dialog_alert.setCancelable(true);
        dialog_alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView tv_dial_subtitle = (TextView) dialog_alert.findViewById(R.id.tv_dial_subtitle);
        tv_dial_subtitle.setText(title);
        TextView tv_dial_desc = (TextView) dialog_alert.findViewById(R.id.tv_dial_desc);
        tv_dial_desc.setText(description);
        Button btn = (Button) dialog_alert.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_alert.dismiss();

                FindNOpenCBS();


            }
        });
    }

    public void FindNOpenCBS() {
        Intent i;
        PackageManager manager = getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage("com.kitchen.utensils.shopping.cbs");
            if (i == null)
                throw new PackageManager.NameNotFoundException();
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {

//if not found in device then will come here
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.kitchen.utensils.shopping.cbs"));
            startActivity(intent);
        }
    }


    public void loginwithpassword(final String mobile, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.loginwithpassword(mobile, password);
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
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            JSONObject data = obj.getJSONObject("date");

                            // Toast.makeText(LoginUser.this, "Your otp is :" + otpdata.getString("otp"), Toast.LENGTH_LONG).show();
                            if (!data.isNull("user_id")) {
                                editor.putString("user_id", data.getString("user_id"));
                            } else {
                                editor.putString("user_id", "");
                            }
                            if (!data.isNull("user_name")) {

                                editor.putString("user_name", data.getString("user_name"));
                            } else {
                                editor.putString("user_name", "");
                            }
                            if (!data.isNull("user_email")) {
                                editor.putString("user_email", data.getString("user_email"));
                            } else {
                                editor.putString("user_email", "");
                            }
                            if (!data.isNull("user_mobile")) {
                                editor.putString("user_mobile", data.getString("user_mobile"));
                            } else {
                                editor.putString("user_mobile", "");
                            }
                            if (!data.isNull("user_address")) {
                                editor.putString("user_address", data.getString("user_address"));
                            } else {
                                editor.putString("user_address", "");
                            }
                            if (!data.isNull("user_gst_number")) {
                                editor.putString("user_gst_number", data.getString("user_gst_number"));
                            } else {
                                editor.putString("user_gst_number", "");
                            }
                            if (!data.isNull("user_company_name")) {
                                editor.putString("user_company_name", data.getString("user_company_name"));
                            } else {
                                editor.putString("user_company_name", "");
                            }
                            if (!data.isNull("user_login_token")) {
                                editor.putString("user_login_token", data.getString("user_login_token"));
                            } else {
                                editor.putString("user_login_token", "");
                            }
                            if (!data.isNull("user_status")) {
                                editor.putString("user_status", data.getString("user_status"));
                            } else {
                                editor.putString("user_status", "");
                            }
                            if (!data.isNull("user_city")) {
                                editor.putString("user_city", data.getString("user_city"));
                            } else {
                                editor.putString("user_city", "");
                            }
                            if (!data.isNull("user_state")) {
                                editor.putString("user_state", data.getString("user_state"));
                            } else {
                                editor.putString("user_state", "");
                            }
                            if (!data.isNull("user_pincode")) {
                                editor.putString("user_pincode", data.getString("user_pincode"));
                            } else {
                                editor.putString("user_pincode", "");
                            }
                            if (!data.isNull("user_image")) {
                                editor.putString("user_image", data.getString("user_image"));
                            } else {
                                editor.putString("user_image", "");
                            }
                            if (!data.isNull("user_whatsapp_number")) {
                                editor.putString("user_whatsapp_number", data.getString("user_whatsapp_number"));
                            } else {
                                editor.putString("user_whatsapp_number", "");
                            }
                            if (!data.isNull("user_entry_date")) {
                                editor.putString("user_entry_date", data.getString("user_entry_date"));
                            } else {
                                editor.putString("user_entry_date", "");
                            }
                            if (!data.isNull("user_last_login_date")) {
                                editor.putString("user_last_login_date", data.getString("user_last_login_date"));
                            } else {
                                editor.putString("user_last_login_date", "");
                            }
                            if (!data.isNull("user_seller_token")) {
                                editor.putString("user_seller_token", data.getString("user_seller_token"));
                            } else {
                                editor.putString("user_seller_token", "");
                            }
                            editor.putString("tut_played", "true");

                            editor.putString("login", "true");

                            editor.commit();

                            SplashActivity.get_appinfo(getApplicationContext());
                            startActivity(new Intent(LoginActivity.this, Home.class));
                        } else {
                            if(message.equals("Password is incorrect.")){
                                Snackbar.make(base_view, "" + message, Snackbar.LENGTH_LONG).show();
                            }else {
                                signUpScreen();
                                Snackbar.make(base_view, "" + message, Snackbar.LENGTH_LONG).show();
                            }

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

    public void registersendotp(final String mobile) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Verifing User...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.registersendotp(mobile);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        Log.i("obj", obj + "");

                        if (success.equals("true")) {
                            progressDialog.dismiss();

                            JSONObject otpdata = obj.getJSONObject("otpdata");
                            Log.i("otpdata", otpdata + "");
                            //  Toast.makeText(Register_user.this, "" + message, Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            otp_token = "" + otpdata.getString("otptoken");


                          //  otp = "" + otpdata.getString("otp");
                            mob = "" + mobile;
                            ;
                            editor.putString("otptoken", otpdata.getString("otptoken"));
                           // otpdata.getString("otp");
                            //  editor.putString("otp", otpdata.getString("otp"));

                            Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                            editor.commit();
                            cv_otp.setVisibility(View.VISIBLE);
                            ll_signin.setVisibility(View.GONE);
                            ll_signup.setVisibility(View.GONE);
                        } else {

                            progressDialog.dismiss();
                            signInScreen();
                            final BottomSheetDialog dialog = new BottomSheetDialog(LoginActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.dialog_message);


                            TextView ok = (TextView) dialog.findViewById(R.id.ok);
                            TextView text = (TextView) dialog.findViewById(R.id.text);


                            text.setText("" + message);

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();


                            // Toast.makeText(Register_user.this, ""+message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        //Toast.makeText(Register_user.this, "" +message, Toast.LENGTH_LONG).show();

                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();

                    // Toast.makeText(Register_user.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {


                    progressDialog.dismiss();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    //  Toast.makeText(Register_user.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();


                // Toast.makeText(Register_user.this, "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void registercheckdotp(String mobile, String otptoken, String otp) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.registercheckdotp(mobile, otptoken, otp);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        Log.i("obj", obj + "");

                        if (success.equals("true")) {
                            progressDialog.dismiss();

                            registerwithpassword("" + shop,
                                    "" + mob,
                                    "" + pass);

                            cv_otp.setVisibility(View.VISIBLE);
                            ll_signin.setVisibility(View.GONE);
                            ll_signup.setVisibility(View.GONE);
                        } else {

                            progressDialog.dismiss();

                            final BottomSheetDialog dialog = new BottomSheetDialog(LoginActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.dialog_message);


                            TextView ok = (TextView) dialog.findViewById(R.id.ok);
                            TextView text = (TextView) dialog.findViewById(R.id.text);


                            text.setText("" + message);

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();


                            // Toast.makeText(Register_user.this, ""+message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        //Toast.makeText(Register_user.this, "" +message, Toast.LENGTH_LONG).show();

                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();

                    // Toast.makeText(Register_user.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {


                    progressDialog.dismiss();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    //  Toast.makeText(Register_user.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();


                // Toast.makeText(Register_user.this, "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void registerwithpassword(String shop_name, String mobile, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.registerwithpassword(shop_name, mobile, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        Log.i("obj", obj + "");

                        if (success.equals("true")) {

                            progressDialog.dismiss();
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            JSONObject data = obj.getJSONObject("data");

                            // Toast.makeText(LoginUser.this, "Your otp is :" + otpdata.getString("otp"), Toast.LENGTH_LONG).show();
                            if (!data.isNull("user_id")) {
                                editor.putString("user_id", data.getString("user_id"));
                            } else {
                                editor.putString("user_id", "");
                            }
                            if (!data.isNull("user_name")) {

                                editor.putString("user_name", data.getString("user_name"));
                            } else {
                                editor.putString("user_name", "");
                            }
                            if (!data.isNull("user_email")) {
                                editor.putString("user_email", data.getString("user_email"));
                            } else {
                                editor.putString("user_email", "");
                            }
                            if (!data.isNull("user_mobile")) {
                                editor.putString("user_mobile", data.getString("user_mobile"));
                            } else {
                                editor.putString("user_mobile", "");
                            }
                            if (!data.isNull("user_address")) {
                                editor.putString("user_address", data.getString("user_address"));
                            } else {
                                editor.putString("user_address", "");
                            }
                            if (!data.isNull("user_gst_number")) {
                                editor.putString("user_gst_number", data.getString("user_gst_number"));
                            } else {
                                editor.putString("user_gst_number", "");
                            }
                            if (!data.isNull("user_company_name")) {
                                editor.putString("user_company_name", data.getString("user_company_name"));
                            } else {
                                editor.putString("user_company_name", "");
                            }
                            if (!data.isNull("user_login_token")) {
                                editor.putString("user_login_token", data.getString("user_login_token"));
                            } else {
                                editor.putString("user_login_token", "");
                            }
                            if (!data.isNull("user_status")) {
                                editor.putString("user_status", data.getString("user_status"));
                            } else {
                                editor.putString("user_status", "");
                            }
                            if (!data.isNull("user_city")) {
                                editor.putString("user_city", data.getString("user_city"));
                            } else {
                                editor.putString("user_city", "");
                            }
                            if (!data.isNull("user_state")) {
                                editor.putString("user_state", data.getString("user_state"));
                            } else {
                                editor.putString("user_state", "");
                            }
                            if (!data.isNull("user_pincode")) {
                                editor.putString("user_pincode", data.getString("user_pincode"));
                            } else {
                                editor.putString("user_pincode", "");
                            }
                            if (!data.isNull("user_image")) {
                                editor.putString("user_image", data.getString("user_image"));
                            } else {
                                editor.putString("user_image", "");
                            }
                            if (!data.isNull("user_whatsapp_number")) {
                                editor.putString("user_whatsapp_number", data.getString("user_whatsapp_number"));
                            } else {
                                editor.putString("user_whatsapp_number", "");
                            }
                            if (!data.isNull("user_entry_date")) {
                                editor.putString("user_entry_date", data.getString("user_entry_date"));
                            } else {
                                editor.putString("user_entry_date", "");
                            }
                            if (!data.isNull("user_last_login_date")) {
                                editor.putString("user_last_login_date", data.getString("user_last_login_date"));
                            } else {
                                editor.putString("user_last_login_date", "");
                            }
                            if (!data.isNull("user_seller_token")) {
                                editor.putString("user_seller_token", data.getString("user_seller_token"));
                            } else {
                                editor.putString("user_seller_token", "");
                            }
                            editor.putString("tut_played", "true");

                            editor.putString("login", "true");

                            editor.commit();
                            SplashActivity.get_appinfo(getApplicationContext());
                            startActivity(new Intent(LoginActivity.this, Home.class));
                        } else {
                            Snackbar.make(base_view, "" + message, Snackbar.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }


                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        //Toast.makeText(Register_user.this, "" +message, Toast.LENGTH_LONG).show();

                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();

                    // Toast.makeText(Register_user.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {


                    progressDialog.dismiss();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    //  Toast.makeText(Register_user.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();


                // Toast.makeText(Register_user.this, "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(LoginActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    public interface Service {
        @Headers("Authkey: APPCBSBDMPL")
        @FormUrlEncoded
        @POST("loginwithpassword")
        Call<ResponseBody> loginwithpassword(@Field("mobile") String mobile,
                                             @Field("password") String password);


        @Headers("Authkey:APPCBSBDMPL")
        @POST("registersendotp")
        @FormUrlEncoded
        Call<ResponseBody> registersendotp(@Field("mobile") String mobile);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("registercheckdotp")
        @FormUrlEncoded
        Call<ResponseBody> registercheckdotp(@Field("mobile") String mobile,
                                             @Field("otptoken") String otptoken,
                                             @Field("otp") String otp);


        @Headers("Authkey: APPCBSBDMPL")
        @FormUrlEncoded
        @POST("registerwithpassword")
        Call<ResponseBody> registerwithpassword(@Field("shop_name") String shop_name,
                                                @Field("mobile") String mobile,
                                                @Field("password") String password);


    }

    public void forgotPassword(final String username) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiManager apiManager = ApiManager.getInstance(LoginActivity.this);
        ApiCallBack<ApiResponse> call = new ApiCallBack<ApiResponse>(new ApiResponseListener<ApiResponse>() {
            @Override
            public void onApiSuccess(ApiResponse response, String apiName) {

                if (response != null) {
                    progressDialog.dismiss();
                    if (response.getSuccess()) {
                        openEmailDialog();
                        Toast.makeText(LoginActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                        SavedPrefManager.saveStringPreferences(LoginActivity.this, AppConstant.OTPTOKEN, response.getOtpdata().getOtptoken());
                        SavedPrefManager.saveStringPreferences(LoginActivity.this, AppConstant.USERNAME, username);
                        edt_email.setText(username);

                    } else {
                        Toast.makeText(LoginActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onApiError(String responses, String apiName) {
                progressDialog.dismiss();
            }

            @Override
            public void onApiFailure(String failureMessage, String apiName) {
                progressDialog.dismiss();
            }
        }, AppConstant.FORGOT_PASS, LoginActivity.this);
        RequestBody mobile = RequestBody.create(MediaType.parse("text"), username);
        RequestBody password = RequestBody.create(MediaType.parse("text"), "");
        RequestBody otp_ = RequestBody.create(MediaType.parse("text"), "");
        RequestBody otptoken = RequestBody.create(MediaType.parse("text"), "");
        apiManager.forgotPassword(call, mobile, password, otp_, otptoken);
    }

    public void openEmailDialog() {
//        dialog.setContentView(R.layout.dialog_email);
        dialog.show();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_email);
//        dialog.show();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (edt_email.getText().toString().equals("")) {
                    et_signin_phone.setError("Please enter your phone");
                }  else if (et_otop.getText().toString().equals("")) {
                    et_otop.setError("Please enter  otp");
                } else if (et_password.getText().toString().equals("")) {
                    et_password.setError("Please enter  password");
                }





                else {
                    if (!Utils.isNetworkConnected(LoginActivity.this)) {
                        Utils.conDialog(LoginActivity.this);
                    } else {
                       verifyPassword();
                    }
                }
            }
        });
    }

    public void verifyPassword() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiManager apiManager = ApiManager.getInstance(LoginActivity.this);
        ApiCallBack<ApiResponse> call = new ApiCallBack<ApiResponse>(new ApiResponseListener<ApiResponse>() {
            @Override
            public void onApiSuccess(ApiResponse response, String apiName) {

                if (response != null) {
                    progressDialog.dismiss();
                    if (response.getSuccess()) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(LoginActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onApiError(String responses, String apiName) {
                progressDialog.dismiss();
            }

            @Override
            public void onApiFailure(String failureMessage, String apiName) {
                progressDialog.dismiss();
            }
        }, AppConstant.FORGOT_PASS, LoginActivity.this);
        RequestBody mobile = RequestBody.create(MediaType.parse("text"),edt_email.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text"), et_password.getText().toString());
        RequestBody otp_ = RequestBody.create(MediaType.parse("text"), et_otop.getText().toString());
        RequestBody otptoken = RequestBody.create(MediaType.parse("text"), SavedPrefManager.getStringPreferences(LoginActivity.this, AppConstant.OTPTOKEN));
        apiManager.forgotPassword(call, mobile, password, otp_, otptoken);
    }
}

