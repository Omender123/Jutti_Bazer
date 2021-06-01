package com.jutti.bazaar.shop.shoe.shopping.Profile;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    View v;
    ImageView dp, add_image;
    SharedPreferences sharedPreferences;
    TextInputEditText et_name, et_email, et_phone, et_shop_name, et_otp;
    TextView mobile, edit;
    LinearLayout ll_otp, ll_mob;
    Bitmap bitmap = null;




    private Uri picUri;
    String img_path = "", mCurrentPhotoPath, photo_name, converted_path = "", converted_path_featured = "";
    Button btn_save;
    String MobilePattern = "[0-9]{10}";
    ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
        init();
        profile(""+sharedPreferences.getString("user_id", ""));



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        AllowPermissions();

        return v;
    }

    private void init() {

        add_image = v.findViewById(R.id.add_image);
        dp = v.findViewById(R.id.dp);
        et_email = v.findViewById(R.id.et_email);
        et_name = v.findViewById(R.id.et_name);
        et_phone = v.findViewById(R.id.et_phone);
        et_shop_name = v.findViewById(R.id.et_shop_name);
        btn_save = v.findViewById(R.id.btn_save);
        edit = v.findViewById(R.id.edit);
        pb = v.findViewById(R.id.pb);
        mobile = v.findViewById(R.id.mobile);
        et_otp = v.findViewById(R.id.et_otp);
        ll_otp = v.findViewById(R.id.ll_otp);
        ll_mob = v.findViewById(R.id.ll_mob);
        clicks();

    }

    private void clicks() {
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();

                imagepickclick();

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_phone.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter valid Phone No.", Toast.LENGTH_SHORT).show();
                } else if (!et_phone.getText().toString().matches(MobilePattern)) {

                    Toast.makeText(getActivity(), "Please enter valid Phone No.", Toast.LENGTH_SHORT).show();

                } else if (et_email.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter valid email", Toast.LENGTH_SHORT).show();
                } else if (et_shop_name.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter valid shop name", Toast.LENGTH_SHORT).show();
                } else {

                    editprofile("" + et_otp.getText().toString(),
                            "" + et_shop_name.getText().toString(),
                            "" + et_email.getText().toString(),
                            "" + sharedPreferences.getString("user_id", ""),
                            "" + et_phone.getText().toString(),
                            "" + et_name.getText().toString());
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_otp.setVisibility(View.VISIBLE);
                ll_mob.setVisibility(View.GONE);
            }
        });

    }

    private void AllowPermissions() {
        int hasRECEIVE_SMS = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasREAD_SMS = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasCameraPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

        List<String> permissions = new ArrayList<String>();
        if (hasRECEIVE_SMS != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (hasREAD_SMS != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }

        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissions.toArray(new String[permissions.size()]), 101);
        } else {
            ///////
            imagepickclick();
            //////for further process

            //////
        }

        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissions.toArray(new String[permissions.size()]), 101);
        } else {


            imagepickclick();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);

                        return;
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }


    }


    public void imagepickclick() {


        picUri = null;


        Intent intent = CropImage.activity(picUri)
                .setAspectRatio(512, 512)
                .getIntent(getContext());

        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //   ((ImageView) findViewById(R.id.img_user)).setImageURI(result.getUri());

                try {

                    picUri = result.getUri();
                    Log.d("resp_picUri", "onActivityResult: "+picUri);

                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), result.getUri());
                    //  bitmap = getResizedBitmap(bitmap, 100);
//                    Saving image to mobile internal memory for sometime

                    String root = getContext().getFilesDir().toString();
                    File myDir = new File(root + "/b2b/");
                    myDir.mkdirs();

                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Img" + n + ".jpg";

                    mCurrentPhotoPath = root + "/b2b/" + fname;
                    File file1 = new File(myDir, fname);
                    saveFile(bitmap, file1);

                    File file = new File(mCurrentPhotoPath);
                    Log.i(" ", "File" + file.getName());


                    photo_name = file.getName();

                    img_path = "" + file.getPath();
                    Log.i("isavtar_true", "" + img_path);
                    setImageviewPath(bitmap);


                } catch (Exception e) {
                    e.printStackTrace();
                    img_path = "";

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                img_path = "";

            }
        }
    }


    void setImageviewPath(Bitmap bitmap) {
        Log.d("resp_img", "setImageviewPath: "+bitmap);

        dp.setImageBitmap(bitmap);


        converted_path_featured = converted_path;

    }

    private void saveFile(Bitmap sourceUri, File destination) {
        if (destination.exists()) destination.delete();
        try {
            FileOutputStream out = new FileOutputStream(destination);
            sourceUri.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void profile(final String user_id) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.profile(user_id);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();

                        Log.i("resultca", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("success")) {
                            success = obj.optString("success");
                            String image_base_path = obj.optString("image_base_path");

                            if (success.equals("true")) {
                                message = obj.optString("message");

                                progressDialog.dismiss();

                                if (obj.has("data")) {

                                    JSONObject data = obj.optJSONObject("data");

                                    if (data.has("user_id")) {
                                    }

                                    if (data.has("user_name")) {
                                        et_shop_name.setText(data.getString("user_name"));
                                    }
                                    if (data.has("user_email")) {
                                        et_email.setText(data.getString("user_email"));
                                    }
                                    if (data.has("user_mobile")) {
                                        et_phone.setText(data.getString("user_mobile"));
                                        mobile.setText(data.getString("user_mobile"));
                                    }
                                    if (!data.isNull("contact_name")) {

                                        et_name.setText(data.getString("contact_name"));
                                    }
                                    if (!data.isNull("user_image")) {

                                        String img = (image_base_path + data.getString("user_image"));




                                        RequestOptions requestOptions = new RequestOptions();


                                        Glide.with(getActivity())
                                                .setDefaultRequestOptions(requestOptions)
                                                .load(img)

                                                .listener(new RequestListener<Drawable>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                        pb.setVisibility(View.GONE);
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                                        pb.setVisibility(View.GONE);
                                                        return false;
                                                    }

                                                })
                                                .into(dp);

                                    }


                                }


                            } else {
                                progressDialog.dismiss();

                            }


                        } else {
                            progressDialog.dismiss();

                            String err_message = obj.getString("message");
                            Toast.makeText(getActivity(), "" + err_message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();

                        Toast.makeText(getActivity(), "exception" + e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void editprofile(String otp, String name, String email, String user_id, String phone, String contact_name) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS).build();
            // Change base URL to your upload server URL.
            Service uploadService = new Retrofit.Builder()
                    .baseUrl(Utils.base_url2)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Service.class);


            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("name", name);
            builder.addFormDataPart("user_id", user_id);
            builder.addFormDataPart("otp", otp);
            builder.addFormDataPart("phone", phone);
            builder.addFormDataPart("email", email);
            builder.addFormDataPart("contact_name", contact_name);


//
            if (!img_path.equals("")) {
                File sign = new File(img_path);
                builder.addFormDataPart("image", sign.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), sign));
            }

            MultipartBody requestBody = builder.build();
            Call<ResponseBody> call;
            call = uploadService.editprofile(requestBody);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {

                            String result = response.body().string();

                            JSONObject obj = new JSONObject(result);
                            Log.i("resp", "" + obj);
                            String success = obj.optString("success");
                            String message = obj.optString("message");


                            Log.i("Resp obj:", obj + "");

                            if (success.equals("true")) {
                                progressDialog.dismiss();

                                if (obj.has("otpdata")){
                                    et_otp.setVisibility(View.VISIBLE);
                                    Snackbar.make(getView(), "OTP sent to your Phone No.", Snackbar.LENGTH_LONG).show();

                                }

                                if (obj.has("data")){
                                    JSONObject data = obj.getJSONObject("data");

                                    Snackbar.make(getView(), ""+message, Snackbar.LENGTH_LONG).show();


                                    SharedPreferences.Editor editor = sharedPreferences.edit();
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
                                    }if (!data.isNull("user_city")) {
                                        editor.putString("user_city", data.getString("user_city"));
                                    } else {
                                        editor.putString("user_city", "");
                                    }if (!data.isNull("user_state")) {
                                        editor.putString("user_state", data.getString("user_state"));
                                    } else {
                                        editor.putString("user_state", "");
                                    }if (!data.isNull("user_pincode")) {
                                        editor.putString("user_pincode", data.getString("user_pincode"));
                                    } else {
                                        editor.putString("user_pincode", "");
                                    }if (!data.isNull("user_image")) {
                                        editor.putString("user_image", data.getString("user_image"));
                                    } else {
                                        editor.putString("user_image", "");
                                    }if (!data.isNull("user_whatsapp_number")) {
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

                                }





                                // Editor of shared


                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(getView(), ""+message, Snackbar.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();

                             e.printStackTrace();
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
//

                    else if (response.code() == 404) {
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

                    Toast.makeText(getActivity(), "Something went wrong at server! " + t.getMessage(), Toast.LENGTH_SHORT).show();


                }
            });

        } catch (Exception ee) {
            Toast.makeText(getActivity(), "" + ee, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }


    public interface Service {
        @Headers("Authkey: APPCBSBDMPL")
        @FormUrlEncoded
        @POST("profile")
        Call<ResponseBody> profile(@Field("user_id") String user_id);


        @Headers("Authkey:APPCBSBDMPL")
        @POST("editprofile")
        Call<ResponseBody> editprofile(@Body RequestBody file);


    }

}
