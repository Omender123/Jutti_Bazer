package com.jutti.bazaar.shop.shoe.shopping.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.jutti.bazaar.shop.shoe.shopping.AppUpdateChecker;
import com.jutti.bazaar.shop.shoe.shopping.Favourites.Wishlist;
import com.jutti.bazaar.shop.shoe.shopping.Login_Register.LoginActivity;
import com.jutti.bazaar.shop.shoe.shopping.Fragment.Homefrag;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
import com.jutti.bazaar.shop.shoe.shopping.Profile.Profile;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Search.Search;
import com.jutti.bazaar.shop.shoe.shopping.SharedPreferernce.MyPreferences;
import com.jutti.bazaar.shop.shoe.shopping.SharedPreferernce.PrefConf;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.Welcome.DialogueCustom;
import com.razorpay.Checkout;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

//selling
public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "resp";
    LinearLayout contact;
    SharedPreferences sharedPref;
    Boolean yes = false;
    TextView count;
    String callno = "", whatsappno = "", email_address = "";

    String Token = "", user = "", Name = "", Phone = "";
    String stat;
    NavigationView navigationView;
    RelativeLayout notification, wishlist, my_cart;
    ImageView search;
    BottomNavigationView navView;
    TextView tv_version;
    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container1, new Homefrag());

                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                    return true;

                case R.id.navigation_fav:
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.container1, new Wishlist());
                    fragmentTransaction1.commit();
                    fragmentTransaction1.addToBackStack(null);
                    //   startActivity(new Intent(Home.this, LoginUser.class));
                    return true;
                case R.id.navigation_dashboard:
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.container1, new Contact());
                    fragmentTransaction2.commit();
                    fragmentTransaction2.addToBackStack(null);
                    return true;
                case R.id.navigation_myaccount:
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.container1, new Profile());
                    fragmentTransaction3.commit();
                    fragmentTransaction3.addToBackStack(null);
                    // startActivity(new Intent(Home.this, Notification.class));
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setSupportActionBar(toolbar);


        Token = sharedPref.getString("user_login_token", "");
        user = sharedPref.getString("user_id", "");
        Name = sharedPref.getString("user_name", "");
        Phone = sharedPref.getString("user_mobile", "");
        settings();
        setTitle("CBS Kitchenware");


        stat = sharedPref.getString("user_status", "");


        notification = findViewById(R.id.notification);
        wishlist = findViewById(R.id.wishlist);
        my_cart = findViewById(R.id.my_cart);
        search = findViewById(R.id.search);


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, NotificationActivity.class));

            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.container1, new Wishlist());
                fragmentTransaction.commit();
            }
        });
        my_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, My_cart.class));
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Search.class));
            }
        });

      /*  try {
            AppUpdateChecker appUpdateChecker = new AppUpdateChecker(this);  //pass the activity in constructure
            appUpdateChecker.checkForUpdate(false);
        } catch (Exception e) {
        }*/

//        contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                final BottomSheetDialog dialog = new BottomSheetDialog(Home.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(true);
//                dialog.setContentView(R.layout.dialog_support);
//
//
//                TextView dcall = (TextView) dialog.findViewById(R.id.dcall);
//                TextView dchat = (TextView) dialog.findViewById(R.id.dchat);
//                ImageView back = (ImageView) dialog.findViewById(R.id.back);
//
//                back.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dcall.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(Intent.ACTION_DIAL);
//                        intent.setData(Uri.parse("tel:" + callno));
//                        startActivity(intent);
//                    }
//                });
//                dchat.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Uri uri = Uri.parse("https://wa.me/" + whatsappno + "/?text=" + "Hello, I need help regarding CBS Kitchenware"); // missing 'http://' will cause crashed
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
//                    }
//                });
//
//
//                dialog.show();
//            }
//        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
//        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView navUsername = (TextView) header.findViewById(R.id.tv_name);
        TextView navUserphone = (TextView) header.findViewById(R.id.tv_number);
        ImageView navimg = (ImageView) header.findViewById(R.id.imageView);

        Glide.with(Home.this)
                .load(sharedPref.getString("user_image", ""))
                .into(navimg);

        navUsername.setText(Name);
        navUserphone.setText(Phone);

        navView = findViewById(R.id.bottom_nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container1, new Homefrag());
        fragmentTransaction.commit();

//update
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkUpdate();
        checkUpdate1();

    }

    private void checkUpdate1() {
        String developer = MyPreferences.getInstance(Home.this).getString(PrefConf.under_development, "");
        String priority_version_title = MyPreferences.getInstance(Home.this).getString(PrefConf.priority_version_title, "");
        String priority_version_description = MyPreferences.getInstance(Home.this).getString(PrefConf.priority_version_description, "");
        String priorversion = MyPreferences.getInstance(Home.this).getString(PrefConf.priority_version, "");

        try {
            android.content.pm.PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            String device_version = verCode + "" + version;
            device_version = device_version.replace(".", "");
            priorversion = priorversion.replace(".", "");
            //  Toast.makeText(this, priorversion+" "+device_version, Toast.LENGTH_SHORT).show();

            if (Integer.parseInt(priorversion) <= Integer.parseInt(device_version)) {
                if (developer.equals("false")) {
                    // Add_1();
                } else {
                    DialogueCustom.dialogue_custom(Home.this, "Important Alert",
                            "The App is under Maintenance",
                            "We are currently performing server maintenance. We'll be back shortly. Sorry for inconvenience, Please Try Again later",
                            "GOT IT", "", false, R.drawable.ic_maintenance,
                            "exit", "", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));
                }
            } else {

                DialogueCustom.dialogue_custom(Home.this, "Update to Continue",
                        "" + priority_version_title,
                        "" + priority_version_description,
                        "Update", "Exit", true, R.drawable.ic_notification,
                        "Update", "Exit", Color.parseColor("#1EBEA5"), Color.parseColor("#FFA1A1A1"));

            }

        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    Home.this.startUpdateFlow(appUpdateInfo);
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    Home.this.startUpdateFlow(appUpdateInfo);
                }
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, Home.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (navView.getSelectedItemId() == R.id.navigation_home) {
                new AlertDialog.Builder(Home.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Closing Activity")
                        .setMessage("Are you sure you want to close App?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent a = new Intent(Intent.ACTION_MAIN);
                                a.addCategory(Intent.CATEGORY_HOME);
                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Home.this.startActivity(a);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    Home.this.finishAffinity();
                                }
                                Home.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            } else {
                startActivity(new Intent(this, Home.class));
                finish();
            }

        }

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Brand) {
            Product_list.brandID = "";
            Product_list.categoryID = "";
            Product_list.searchID = "";
            Product_list.search_gender = "";

            Brand_list.tit = "Brand List";
            Intent intent = new Intent(Home.this, Brand_list.class);
            startActivity(intent);
            Homefrag.role = "1";
        } else if (id == R.id.Products) {

            Product_list.brandID = "";
            Product_list.categoryID = "";
            Product_list.searchID = "";
            Product_list.search_gender = "";


            Brand_list.tit = "Select Brand";
            Brand_list.see = "yes";
            Intent intent = new Intent(Home.this, Brand_list.class);
            startActivity(intent);
            Homefrag.role = "afterproduct";


        } else if (id == R.id.Category) {

            Product_list.brandID = "";
            Product_list.categoryID = "";
            Product_list.searchID = "";
            Product_list.search_gender = "";

            Brand_list.tit = "Category List";
            Intent intent = new Intent(Home.this, Brand_list.class);
            startActivity(intent);
            Homefrag.role = "cat";


        } else if (id == R.id.nav_home) {
            startActivity(new Intent(Home.this, Home.class));
        } else if (id == R.id.feedback) {
            showfeedback();
        } else if (id == R.id.MyCart) {
            startActivity(new Intent(Home.this, My_cart.class));

        } else if (id == R.id.nav_orders) {
            startActivity(new Intent(Home.this, OrdersList.class));

        } else if (id == R.id.share) {
            share();

        } else if (id == R.id.AboutUs) {
            startActivity(new Intent(Home.this, About_Us.class));

        } else if (id == R.id.Agent) {
            startActivity(new Intent(Home.this, BecomeAgent.class));

        } else if (id == R.id.Logout) {


            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to Logout ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Checkout.clearUserData(Home.this);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.clear();
                        editor.commit();

                        startActivity(new Intent(Home.this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();


        } else if (id == R.id.seller) {


            Intent intent = getPackageManager().getLaunchIntentForPackage("com.cj.shoes.online.shop.jutti.bazaar.sales");
            if (intent != null) {
                // We found the activity now start the activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // Bring user to the market or let them choose an app?
                intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    intent.setData(Uri.parse("market://details?id=" + "com.cj.shoes.online.shop.jutti.bazaar.sales"));
                } catch (android.content.ActivityNotFoundException anfe) {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "com.cj.shoes.online.shop.jutti.bazaar.sales"));
                }

                startActivity(intent);
            }


            //    startActivity(new Intent(Home.this, Webb.class));
            //startActivity(new Intent(Home.this, Web_view.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void share() {


        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + "com.jutti.bazaar.shop.shoe.shopping";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }

    }


    public void settings() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.settings();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // isLoading = false;
                if (response.isSuccessful()) {
                    try {

                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        Log.i("result", "" + obj);

                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Log.i("arr_response", "warning  " + message);

                        } else {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            JSONObject objdata = obj.getJSONObject("data");


                            if (!objdata.isNull("call_us")) {

                                callno = objdata.getString("call_us");
                                editor.putString("call_us", objdata.getString("call_us"));


                            } else {
                                callno = "";
                            }
                            if (!objdata.isNull("whatsapp_number")) {

                                whatsappno = objdata.getString("whatsapp_number");
                                editor.putString("whatsapp_number", objdata.getString("whatsapp_number"));


                            } else {
                                whatsappno = "";
                            }
                            if (!objdata.isNull("email_address")) {

                                email_address = objdata.getString("email_address");

                            } else {
                                email_address = "";
                            }
                            editor.commit();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                    }
                } else if (response.code() == 401) {
                    Log.i("arr_response", "response.code  " + response.code());
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());

                   /* DialogueCustom.dialogue_custom(SplashActivity.this,"Something went Wrong!",
                            "Aw Snap! Error code:"+response.code(),
                            "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
                            "GOT IT","",false, R.drawable.error,
                            "exit","", Color.parseColor("#1EBEA5"),Color.parseColor("#FFA1A1A1"));*/
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("t", "Something went wrong at server!" + t);

            }
        });
    }


//    @Override
//    protected void onResumeFragments() {
//        cartdetails("" + Token, "" + user);
//
//        super.onResumeFragments();
//    }


    public void feedback(String name, String mobile, String email, final String message) {

        final ProgressDialog progressDialog = new ProgressDialog(Home.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.feedback(name, mobile, email, message);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");

                        String message = obj.optString("message");

                        Log.i("Resp: ", obj + "");

                        if (success.equals("true")) {
                            Log.i("message: ", message + "");

                            dialog("Thank you for sharing your valuable feedback.");
                            //  Toast.makeText(Home.this, ""+message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Home.this, "" + message, Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
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

    void dialog(String message) {

        final BottomSheetDialog dialog = new BottomSheetDialog(Home.this);
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


    }

    private void showfeedback() {


        final Dialog dialog = new Dialog(Home.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_feedabck);


        final EditText et_desc = (EditText) dialog.findViewById(R.id.et_desc);
        final EditText name = (EditText) dialog.findViewById(R.id.name);
        final EditText email = (EditText) dialog.findViewById(R.id.email);
        final EditText phone = (EditText) dialog.findViewById(R.id.phone);
        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        Button send = (Button) dialog.findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_desc.getText().toString().equals("")) {

                    Toast.makeText(Home.this, "Please add you valuable feedback", Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().equals("")) {

                    Toast.makeText(Home.this, "Please add your  name", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().equals("")) {

                    Toast.makeText(Home.this, "please add email", Toast.LENGTH_SHORT).show();
                } else if (phone.getText().toString().equals("")) {

                    Toast.makeText(Home.this, "Please add Phone number", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();

                    feedback("" + name.getText().toString()
                            , "" + phone.getText().toString()
                            , "" + email.getText().toString()
                            , "" + et_desc.getText().toString()
                    );

                }


            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }


    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("feedback")
        @FormUrlEncoded
        Call<ResponseBody> feedback(@Field("name") String name,
                                    @Field("mobile") String mobile,
                                    @Field("email") String email,
                                    @Field("message") String message);

        @Headers("Authkey:APPCBSBDMPL")
        @GET("settings")
        Call<ResponseBody> settings();
    }

}