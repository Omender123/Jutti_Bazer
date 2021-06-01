package com.jutti.bazaar.shop.shoe.shopping.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.api.SavedPrefManager;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public class About_Us extends AppCompatActivity {

    TextView text;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        text=findViewById(R.id.text);
        back=findViewById(R.id.viewback);

        back.setOnClickListener(view -> startActivity(new Intent(About_Us.this, Home.class)));
        aboutus();
    }







    public void aboutus() {

        final ProgressDialog progressDialog = new ProgressDialog(About_Us.this);
        progressDialog.setMessage(" Please wait...");
        progressDialog.setCancelable(false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.aboutus();
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
                        progressDialog.dismiss();

                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            Toast.makeText(About_Us.this, "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(About_Us.this, "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {

                            JSONObject objdata = obj.getJSONObject("data");


                            if (!objdata.isNull("page_content")) {

                                String s=(objdata.getString("page_content"));



                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    text.setText(Html.fromHtml(s, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    text.setText(Html.fromHtml(s));
                                }




                            } else {
                                text.setText("");
                            }


                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(About_Us.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Log.i("arr_response", "response.code  " + response.code());
                    Toast.makeText(About_Us.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(About_Us.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                   /* DialogueCustom.dialogue_custom(SplashActivity.this,"Something went Wrong!",
                            "Aw Snap! Error code:"+response.code(),
                            "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later",
                            "GOT IT","",false, R.drawable.error,
                            "exit","", Color.parseColor("#1EBEA5"),Color.parseColor("#FFA1A1A1"));*/
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("t", "Something went wrong at server!" + t);

                Toast.makeText(About_Us.this, "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void policies(View view) {
        openWebPage(SavedPrefManager.getStringPreferences(About_Us.this,"url_privacypolicy"));
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

    public void privacyPolicies(View view) {
        openWebPage(SavedPrefManager.getStringPreferences(About_Us.this,"url_cancel"));

    }

    public void returnPolicies(View view) {
        openWebPage(SavedPrefManager.getStringPreferences(About_Us.this,"url_return"));
    }


    public interface Service {

        @Headers("Authkey:APPCBSBDMPL")
        @GET("page/about-us")
        Call<ResponseBody> aboutus();
    }
}