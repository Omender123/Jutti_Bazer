package com.jutti.bazaar.shop.shoe.shopping.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_notification;
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

public class NotificationActivity extends AppCompatActivity {

    ImageView iv_back_notification;
    RecyclerView recycler_notification;
    SharedPreferences sharedPreferences;
    LinearLayout ll_progress,ll_nodata,ll_yesdata;
    TextView clear;


    public static ArrayList<String> ID = new ArrayList<>();
    public static ArrayList<String> NOTIFICATION_TEXT = new ArrayList<>();
    public static ArrayList<String> DATE_ADDED = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        init();
    }

    private void init() {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        iv_back_notification=findViewById(R.id.viewback);
        recycler_notification=findViewById(R.id.recycler_notification);
        ll_progress = (LinearLayout) findViewById(R.id.ll_progress);
        ll_yesdata = (LinearLayout) findViewById(R.id.ll_yesdata);
        ll_nodata = (LinearLayout) findViewById(R.id.ll_nodata);
        click();

        notificationslist(""+sharedPreferences.getString("user_login_token",""),
                ""+sharedPreferences.getString("user_id","")
        );

    }

    private void click() {


        iv_back_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });
    }





    public void notificationslist(String user_login_token,String user_id){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.getnotification(user_login_token,user_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    show_progress();
                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");

                        Log.i("Resp: ", obj + "");

                        if (obj.has("error")) {
                            String error_message = obj.optString("error_message");
                            Toast.makeText(getApplicationContext(), ""+error_message, Toast.LENGTH_SHORT).show();
                        }else{
                            if (obj.has("data")){
                                ID.clear();
                                NOTIFICATION_TEXT.clear();
                                DATE_ADDED.clear();

                                JSONArray dataarray=obj.getJSONArray("data");

                                if (dataarray.length()>0){
                                    for (int i=0;i<dataarray.length();i++){
                                        JSONObject dataobj=dataarray.getJSONObject(i);


                                        if (!dataobj.isNull("notification_id")){
                                            ID.add(dataobj.getString("notification_id"));
                                        }else{
                                            ID.add("");

                                        }
                                        if (!dataobj.isNull("notification_message")){
                                            NOTIFICATION_TEXT.add(dataobj.getString("notification_message"));
                                        }else{
                                            NOTIFICATION_TEXT.add("");

                                        }
                                        if (!dataobj.isNull("notification_schedule_date")){
                                            DATE_ADDED.add(dataobj.getString("notification_schedule_date"));
                                        }else{
                                            DATE_ADDED.add("");

                                        }


                                    }
                                    show_yesdata();
                                    setup_notification();
                                }else{
                                    Toast.makeText(getApplicationContext(), "No Notification !!", Toast.LENGTH_LONG).show();
                                    show_nodata();
                                }
                            }
                        }

                    } catch (Exception e) {
                        show_progress();
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    show_progress();
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    show_progress();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                show_progress();
                Toast.makeText(getApplicationContext(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setup_notification() {

        Adapter_notification adapter_notification = new Adapter_notification(this);
        recycler_notification.setLayoutManager(new GridLayoutManager(this, 1));
        recycler_notification.setAdapter(adapter_notification);

        adapter_notification.notifyDataSetChanged();

    }
    public void show_progress() {
        ShimmerFrameLayout container,container1,container2,container3;
        container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer);


        container.startShimmer();

        ll_progress.setVisibility(View.VISIBLE);
        ll_yesdata.setVisibility(View.GONE);
        ll_nodata.setVisibility(View.GONE);
        //  ll_noresult.setVisibility(View.GONE);

    }

    public void show_yesdata() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        ll_progress.setVisibility(View.GONE);
                        ll_yesdata.setVisibility(View.VISIBLE);
                        // ll_noresult.setVisibility(View.GONE);

                    }
                }, 0);

    } public void show_nodata() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        ll_progress.setVisibility(View.GONE);
                        ll_yesdata.setVisibility(View.GONE);
                        ll_nodata.setVisibility(View.VISIBLE);
                        // ll_noresult.setVisibility(View.GONE);

                    }
                }, 0);

    }



    @Override
    public void onBackPressed() {
        finish();
    }

    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("getnotification")
        @FormUrlEncoded
        Call<ResponseBody> getnotification(@Field("user_login_token") String user_login_token,
                                           @Field("user_id") String user_id

        );
        @Headers("Authkey:APPCBSBDMPL")
        @POST("clearnotification")
        @FormUrlEncoded
        Call<ResponseBody> clearnotification(@Field("user_login_token") String user_login_token,
                                             @Field("user_id") String user_id

        );

    }

}
