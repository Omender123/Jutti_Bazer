package com.jutti.bazaar.shop.shoe.shopping.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jutti.bazaar.shop.shoe.shopping.Login_Register.LoginActivity;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.Welcome.DialogueCustom;
import com.jutti.bazaar.shop.shoe.shopping.Welcome.WelcomeActivity;
import com.jutti.bazaar.shop.shoe.shopping.api.SavedPrefManager;


import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public class SplashActivity extends AppCompatActivity {
    public static SharedPreferences sharedPref;
    public static int delay_splash = 2500;
    public static int final_count = 0;
    public static String splash_stages = "2", ad_id_banner = "", ad_id_intersticial = "", priority_version_title, priority_version_description;
    public static JSONObject data_obj;
    Handler handler;

    SharedPreferences sharedPreferences;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();


    }

    void init() {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        handler = new Handler();


//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putString("user_login_token", "12KLenGemxoCSeR1HOpXC8rLS1pNyOTB");
//        editor.putString("user_id", "12");
//
//        editor.commit();

        // startActivity(new Intent(context, Home.class));

        Utils.updatedtoken(this, "" + sharedPref.getString("user_login_token", ""), "" + sharedPref.getString("user_id", ""), "" + sharedPref.getString("deviceToken", ""));
        Log.d("updatefirebase", ": " + sharedPref.getString("user_login_token", "") + "userid:;;;;;" + sharedPref.getString("user_id", "") + "device token;;;;;;" + sharedPref.getString("deviceToken", ""));
        //  Toast.makeText(this, ""+sharedPref.getString("deviceToken", ""), Toast.LENGTH_SHORT).show();
        if (Utils.isNetworkConnected(this)) {

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "getInstanceId failed", task.getException());
                                return;
                            }
// Get new Instance ID token
                            String token = task.getResult().getToken();
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("deviceToken", token);
                            editor.commit();
// Log and toast
// String msg = getString(R.string.msg_token_fmt, token);
                            Log.d("token", token);
// Toast.makeText(context, token, Toast.LENGTH_SHORT).show();
                        }
                    });


            get_appinfo(context);
            init_handler();


        }
    }

    public static void get_appinfo(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call = service.getappinfo();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {


                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        String success = obj.getString("success");
                        Log.d("resp_app_info", "onResponse: " + result);
                        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editorlog = sharedPref.edit(); // Editor of shared


                        if (success.equals("true")) {
                            SavedPrefManager.saveStringPreferences(context, "razorpay_key", "" + obj.optString("razorpay_key"));
                            SavedPrefManager.saveStringPreferences(context, "url_cancel", "" + obj.optString("url_cancel"));
                            SavedPrefManager.saveStringPreferences(context, "url_return", "" + obj.optString("url_return"));
                            SavedPrefManager.saveStringPreferences(context, "url_terms", "" + obj.optString("url_terms"));
                            SavedPrefManager.saveStringPreferences(context, "url_privacypolicy", "" + obj.optString("url_privacypolicy"));
                            SavedPrefManager.saveStringPreferences(context, "online_payment_discount", "" + obj.optString("online_payment_discount"));


                            //  Toast.makeText(context, ""+ obj.optString("url_cancel"), Toast.LENGTH_SHORT).show();


                            data_obj = obj.getJSONObject("data");


                            if (!data_obj.isNull("app_package_name")) {
                                editorlog.putString("app_package_name", data_obj.optString("app_package_name"));

                            } else {
                                editorlog.putString("app_package_name", "");
                            }


                            if (!data_obj.isNull("app_url")) {
                                editorlog.putString("app_url", data_obj.optString("app_url"));

                            } else {
                                editorlog.putString("app_url", "");

                            }
                            if (!data_obj.isNull("splash_stages")) {
                                editorlog.putString("splash_stages", data_obj.optString("splash_stages"));

                            } else {
                                editorlog.putString("splash_stages", "");

                            }
                            if (!data_obj.isNull("paid_enabled")) {
                                editorlog.putString("paid_enabled", data_obj.optString("paid_enabled"));

                            } else {
                                editorlog.putString("paid_enabled", "");
                            }
                            if (!data_obj.isNull("current_version")) {
                                editorlog.putString("current_version", data_obj.optString("current_version"));

                            } else {
                                editorlog.putString("current_version", "");

                            }
                            if (!data_obj.isNull("priority_version")) {
                                editorlog.putString("priority_version", data_obj.optString("priority_version"));

                            } else {
                                editorlog.putString("priority_version", "");

                            }
                            if (!data_obj.isNull("priority_version_title")) {
                                editorlog.putString("priority_version_title", data_obj.optString("priority_version_title"));

                            } else {
                                editorlog.putString("priority_version_title", "");

                            }
                            if (!data_obj.isNull("priority_version_description")) {
                                editorlog.putString("priority_version_description", data_obj.optString("priority_version_description"));

                            } else {
                                editorlog.putString("priority_version_description", "");

                            }

                            if (!data_obj.isNull("under_development")) {
                                editorlog.putString("under_development", data_obj.optString("under_development"));

                            } else {
                                editorlog.putString("under_development", "");

                            }
                            if (!data_obj.isNull("privacy_policy")) {
                                editorlog.putString("privacy_policy", data_obj.optString("privacy_policy"));

                            } else {
                                editorlog.putString("privacy_policy", "");

                            }
                            if (!data_obj.isNull("ad_enabled")) {
                                editorlog.putString("ad_enabled", data_obj.optString("ad_enabled"));

                            } else {
                                editorlog.putString("ad_enabled", "");

                            }
                            if (!data_obj.isNull("ad_id_intersticial")) {
                                editorlog.putString("ad_id_intersticial", data_obj.optString("ad_id_intersticial"));

                            } else {
                                editorlog.putString("ad_id_intersticial", "");

                            }
                            if (!data_obj.isNull("ad_id_banner")) {
                                editorlog.putString("ad_id_banner", data_obj.optString("ad_id_banner"));

                            } else {
                                editorlog.putString("ad_id_banner", "");

                            }
                            if (!data_obj.isNull("register_enabled")) {
                                editorlog.putString("register_enabled", data_obj.optString("register_enabled"));

                            } else {
                                editorlog.putString("register_enabled", "");

                            }

                            if (!data_obj.isNull("cod_advance_payment")) {
                                editorlog.putString("cod_advance_payment", data_obj.optString("cod_advance_payment"));


                            } else {
                                editorlog.putString("cod_advance_payment", "");

                            }
                            if (!data_obj.isNull("online_payment_discount")) {
                                editorlog.putString("online_payment_discount", data_obj.optString("online_payment_discount"));

                            } else {
                                editorlog.putString("online_payment_discount", "");

                            }

                            editorlog.commit();

                            priority_version_title = sharedPref.getString("priority_version_title", "");
                            priority_version_description = sharedPref.getString("priority_version_description", "");
                            ad_id_intersticial = sharedPref.getString("ad_id_intersticial", "");
                            ad_id_banner = sharedPref.getString("ad_id_banner", "");
                            splash_stages = data_obj.optString("splash_stages");

//                                if (appinfo_obj.optString("ad_enabled").equals("true")) {
//                                    // init_ad();
//                                    //  Toast.makeText(context, ad_id_banner+"  "+ad_id_intersticial+ "  enabled ", Toast.LENGTH_SHORT).show();
//                                }

                            //compare_appversion(context,data_obj.optString("priority_version"));

                            //      Toast.makeText(context, ""+appinfo_obj.optString("current_version"), Toast.LENGTH_SHORT).show();

                        } else {
                            DialogueCustom.dialogue_custom(context, "Something went Wrong!",
                                    "Aw Snap!!",
                                    "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
                                    "GOT IT", "", false, R.drawable.nointernet,
                                    "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        DialogueCustom.dialogue_custom(context, "Something went Wrong!",
                                "Aw Snap!!!",
                                "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
                                "GOT IT", "", false, R.drawable.nointernet,
                                "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    //  Toast.makeText(HomeActivity.this, "401", Toast.LENGTH_SHORT).show();
                    // Handle unauthorized
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());

                    DialogueCustom.dialogue_custom(context, "Something went Wrong!",
                            "Aw Snap! Error code:" + response.code(),
                            "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
                            "GOT IT", "", false, R.drawable.nointernet,
                            "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));

                    //   Toast.makeText(HomeActivity.this, "Message"+ "code..."+response.code() + " message..." + response.message()+" body..."+response.body(), Toast.LENGTH_SHORT).show();
                    // Handle other responses
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(context, "error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                //  Toast.makeText(context, "Something went wrong at server! Please try Later", Toast.LENGTH_LONG).show();
                DialogueCustom.dialogue_custom(context, "Something went Wrong!",
                        "Aw Snap!!!!",
                        "Something wrong with network, Please Try Again later",
                        "GOT IT", "", false, R.drawable.nointernet,
                        "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));

            }
        });
    }

    public interface Service {
        @GET("appinfo")
        @Headers({"Authkey:APPCBSBDMPL"})
        Call<ResponseBody> getappinfo();
    }

    public static void compare_appversion(Context context,String priorversion) {
        try {
            android.content.pm.PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            String device_version = verCode + "" + version;
            device_version = device_version.replace(".", "");
            priorversion = priorversion.replace(".", "");
            //  Toast.makeText(this, priorversion+" "+device_version, Toast.LENGTH_SHORT).show();

            if (Integer.parseInt(priorversion) <= Integer.parseInt(device_version)) {
                if (data_obj.optString("under_development").toString().equals("false")) {
                   // Add_1();
                } else {
                    DialogueCustom.dialogue_custom(context, "Important Alert",
                            "The App is under Maintenance",
                            "We are currently performing server maintenance. We'll be back shortly. Sorry for inconvenience, Please Try Again later",
                            "GOT IT", "", false, R.drawable.ic_maintenance,
                            "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));
                }
            } else {

                DialogueCustom.dialogue_custom(context, "Update to Continue",
                        "" + priority_version_title,
                        "" + priority_version_description,
                        "Update", "Exit", true, R.drawable.ic_notification,
                        "Update", "Exit", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));

            }

        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void nostatusbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void init_handler() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Add_1();
                    }
                }, delay_splash);
    }

    public  void Add_1() {

            Next_Activity();

    }

    public void Next_Activity() {
        if (sharedPref.getString("tut_played", "").equals("true")) {


            if (sharedPref.getString("registered", "").equals("true")) {
                startActivity(new Intent(context, Home.class));
                finish();

            } else if (sharedPref.getString("login", "").equals("true")) {
                startActivity(new Intent(context, Home.class));
                finish();

            } else {
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }


        } else {
            startActivity(new Intent(context, LoginActivity.class));
        }


    }
}


