package com.jutti.bazaar.shop.shoe.shopping.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;

import org.json.JSONObject;

import java.net.URLEncoder;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class Contact extends Fragment {

    EditText et_query_text;
    String callno = "", whatsappno = "", email_address = "";
    TextInputEditText et_contact_name, et_contact_phone, et_contact_email, et_contact_description;
    Button submit_contact;
    ImageView img_whatsapp,sms,call;
    RelativeLayout container;

    String MobilePattern = "[0-9]{10}";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    View v;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_contact, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        init();
        settings();
        return v;
    }

    private void init() {
        et_contact_name = v.findViewById(R.id.et_contact_name);
        et_contact_phone = v.findViewById(R.id.et_contact_phone);
        et_contact_email = v.findViewById(R.id.et_contact_email);
        submit_contact = v.findViewById(R.id.submit_contact);
        et_contact_description = v.findViewById(R.id.et_contact_description);
        container = v.findViewById(R.id.container);
        call = v.findViewById(R.id.call);
        sms = v.findViewById(R.id.sms);
        img_whatsapp = v.findViewById(R.id.img_whatsapp);
        click();
    }

    private void click() {

        img_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatsapp(container);
            }
        });
            call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(container);
            }
        });
            sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email(container);
            }
        });



        submit_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_contact_name.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter your name", Toast.LENGTH_SHORT).show();
                } else if (et_contact_phone.getText().toString().equals("") && !et_contact_phone.getText().toString().matches(MobilePattern)) {
                    Toast.makeText(getActivity(), "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                } else if (!et_contact_email.getText().toString().matches(emailPattern)) {
                    Toast.makeText(getActivity(), "Enter valid email", Toast.LENGTH_SHORT).show();
                } else if (et_contact_description.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Specify Description", Toast.LENGTH_SHORT).show();
                } else {
                    contact_us(et_contact_name.getText().toString(),
                            et_contact_phone.getText().toString(),
                            et_contact_email.getText().toString(),
                            et_contact_description.getText().toString());
                }

            }
        });

    }





    public void whatsapp(View view) {
        PackageManager packageManager = getActivity().getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String countrycode = "91";
        String phone = countrycode + "" + whatsappno;
        String NAME = "Kunal";
        String message = "Hello *JuttiBazzar Support* \n I am : *" + NAME + "* " + "\n ";
        try {
            String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                getActivity().startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void email(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "" + email_address, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Support- CBS App");
        intent.putExtra(Intent.EXTRA_TEXT, "CBS Support");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + callno));
        startActivity(intent);
    }


    public void settings() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(" Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

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
                        progressDialog.dismiss();

                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {

                            JSONObject objdata = obj.getJSONObject("data");


                            if (!objdata.isNull("call_us")) {

                                callno = objdata.getString("call_us");

                            } else {
                                callno = "";
                            }
                            if (!objdata.isNull("whatsapp_number")) {

                                whatsappno = objdata.getString("whatsapp_number");

                            } else {
                                whatsappno = "";
                            }
                            if (!objdata.isNull("email_address")) {

                                email_address = objdata.getString("email_address");

                            } else {
                                email_address = "";
                            }


                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Log.i("arr_response", "response.code  " + response.code());
                    Toast.makeText(getActivity(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getActivity(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

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

                Toast.makeText(getActivity(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void contact_us(String name, String mobile, String email, final String message) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.contactus(name, mobile, email, message);

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
                            Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getActivity(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("contactus")
        @FormUrlEncoded
        Call<ResponseBody> contactus(@Field("name") String name,
                                     @Field("mobile") String mobile,
                                     @Field("email") String email,
                                     @Field("message") String message);


        @Headers("Authkey:APPCBSBDMPL")
        @GET("settings")
        Call<ResponseBody> settings();


    }

}
