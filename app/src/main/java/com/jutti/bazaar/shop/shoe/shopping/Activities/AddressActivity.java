package com.jutti.bazaar.shop.shoe.shopping.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_address;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_address;
import com.jutti.bazaar.shop.shoe.shopping.Login_Register.AppConstant;
import com.jutti.bazaar.shop.shoe.shopping.PaymodeActivity;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.api.ApiCallBack;
import com.jutti.bazaar.shop.shoe.shopping.api.ApiManager;
import com.jutti.bazaar.shop.shoe.shopping.api.ApiResponseListener;
import com.jutti.bazaar.shop.shoe.shopping.api.Model.KycDetailsResponse;
import com.jutti.bazaar.shop.shoe.shopping.api.SavedPrefManager;
import com.jutti.bazaar.shop.shoe.shopping.databinding.ActivityAddressBinding;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import needle.Needle;
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

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

public class AddressActivity extends AppCompatActivity implements PaymentResultListener {
    RadioButton online, advance;
    public static ArrayList<Model_address> address = new ArrayList<>();
    Dialog dialog;
    RecyclerView recyclerView;
    RelativeLayout base_view;
    String flag = "";
    Uri picUri, picUrismall;
    Bitmap bitmap;
    String mCurrentPhotoPath, photo_name, img_path = "", img_path_banner = "";
    TextView tvRS, onlineText;
    RadioButton adhar, gst;
    EditText etGst, etAdhaar;
    SharedPreferences sharedPreferences;
    public static String address_id = "", order_id = "";
    Dialog dialogue_success;
    ProgressDialog progressDialog;
    String type = "", COD = "0";
    ActivityAddressBinding binding;
    double res = 0, deliveryCharge = 0;
    Context context;
    boolean gstVerified = false;
    String verifiedGst = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address);
        context = AddressActivity.this;
        //paymentlogs("1","test","test");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddressActivity.this);
        Checkout.preload(getApplicationContext());
        init();
        clicks();
        Needle.onMainThread().execute(new Runnable() {
            @Override
            public void run() {
                check();

            }
        });
        Needle.onMainThread().execute(new Runnable() {
            @Override
            public void run() {
                hit();
            }
        });


        Needle.onMainThread().execute(new Runnable() {
            @Override
            public void run() {
                kycDetails();
            }
        });


//        etAdhaar.setText(SavedPrefManager.getStringPreferences(this, "ADHAR_PAN_NO"));
//        etGst.setText(SavedPrefManager.getStringPreferences(this, "GST_NO"));

//        online = (RadioButton) findViewById(R.id.online);
//        cash = (RadioButton) findViewById(R.id.cash);

        //  TextView proceed = findViewById(R.id.proceed);


    }


    private void clicks() {
        adhar.setOnClickListener(view -> {
            binding.llGst.setVisibility(View.GONE);
            binding.llAdhar.setVisibility(View.VISIBLE);


        });
        gst.setOnClickListener(view -> {

            binding.llGst.setVisibility(View.VISIBLE);
            binding.llAdhar.setVisibility(View.GONE);
            // kycDetails();
        });
        binding.ivGStImage.setOnClickListener(v -> {
            flag = "0";
            imagepickclick();

        });
        binding.ivAadharCard.setOnClickListener(v -> {
            flag = "1";
            imagepickclick();

        });

        binding.verify.setOnClickListener(v -> gstcheck(binding.etGst.getText().toString()));
        final RadioGroup radio = (RadioGroup) findViewById(R.id.paymode);

        radio.setOnCheckedChangeListener((group, checkedId) -> {

            View radioButton = radio.findViewById(checkedId);
            int index = radio.indexOfChild(radioButton);


            boolean isChecked = online.isChecked();
            // If the radiobutton that has changed in check state is now checked...
            if (isChecked) {
                double a = Double.parseDouble(SavedPrefManager.getStringPreferences(AddressActivity.this, "total"));
                double b = Double.parseDouble(SavedPrefManager.getStringPreferences(AddressActivity.this, "online_payment_discount"));
                res = (a / 100.0f) * b;
                res = a - res;
                setTotalCharges(res, deliveryCharge);
                COD = "0";
                courierCharge("" + address_id);


            }

            boolean isChecked1 = advance.isChecked();
            // If the radiobutton that has changed in check state is now checked...
            if (isChecked1) {
                double aa = Double.parseDouble(SavedPrefManager.getStringPreferences(AddressActivity.this, "total"));
                double bb = Double.parseDouble(SavedPrefManager.getStringPreferences(AddressActivity.this, "cod_advance_payment"));
                res = (aa / 100.0f) * bb;
                setTotalCharges(res, deliveryCharge);
                COD = "1";
                courierCharge("" + address_id);

            }

        });

    }

    private void check() {
        String addId = SavedPrefManager.getStringPreferences(context, "addSelected");

        Double a = Double.parseDouble(SavedPrefManager.getStringPreferences(AddressActivity.this, "total"));
        Double b = Double.parseDouble(SavedPrefManager.getStringPreferences(AddressActivity.this, "online_payment_discount"));
        res = (a / 100.0f) * b;
        res = a - res;

        if (!addId.equals("")) {
            address_id = addId;
        } else {
            setTotalCharges(res, deliveryCharge);

        }
        COD = "0";
        courierCharge("" + address_id);


    }

    private void init() {

        binding.etGst.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        online = (RadioButton) findViewById(R.id.online);
        advance = (RadioButton) findViewById(R.id.advance);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        base_view = (RelativeLayout) findViewById(R.id.base_view);
        gst = (RadioButton) findViewById(R.id.gst);
        adhar = (RadioButton) findViewById(R.id.adhar);
        etGst = (EditText) findViewById(R.id.etGst);
        etAdhaar = (EditText) findViewById(R.id.etAdhaar);


        onlineText = (TextView) findViewById(R.id.onlineText);
        tvRS = (TextView) findViewById(R.id.tvRS);
        onlineText.setText(sharedPreferences.getString("online_payment_discount", "") + "% Extra Discount");

        if (!sharedPreferences.getString("cod_advance_payment", "").equals("")) {
            double a = Double.parseDouble(sharedPreferences.getString("cod_advance_payment", ""));
            double result = 100 - a;
            advance.setText(a + "% Advance | " + result + "% COD");
        }


    }


    void setTotalCharges(Double res, Double deliveryCharge) {

        Log.d("TAG", "setTotalCharges: " + res + " " + deliveryCharge);
        tvRS.setVisibility(View.VISIBLE);
        Double sum = res + deliveryCharge;
        String strDouble = String.format("%.2f", sum);
        tvRS.setText("₹ " + strDouble);
    }


    public void imagepickclick() {

        if (flag.contains("1")) {
            picUrismall = null;
            Intent intent = CropImage.activity(picUri)
//                    .setAspectRatio(512, 512)
                    .getIntent(AddressActivity.this);


            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

        } else if (flag.contains("0")) {
            picUri = null;
            Intent intentimage = CropImage.activity(picUri)
//                    .setAspectRatio(512, 512)
                    .getIntent(AddressActivity.this);


            startActivityForResult(intentimage, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            com.theartofdev.edmodo.cropper.CropImage.ActivityResult result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                try {

                    if (flag.equals("0")) {
                        picUri = result.getUri();
                    } else if (flag.equals("1")) {
                        picUrismall = result.getUri();
                    }
                    bitmap = MediaStore.Images.Media.getBitmap(AddressActivity.this.getContentResolver(), result.getUri());

                    String root = AddressActivity.this.getFilesDir().toString();


                    File myDir = new File(root + "/b2b/");
                    myDir.mkdirs();
                    Log.e("TAG,", "onActivityResult: " + myDir);

                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Img" + n + ".jpg";

                    mCurrentPhotoPath = root + "/b2b/" + fname;
                    File file1 = new File(myDir, fname);
                    saveFile(bitmap, file1);

                    File file = new File(mCurrentPhotoPath);
                    Log.i("TAG", "File" + file.getName());

                    Log.e("TAG", "onActivityResult: " + file);
                    photo_name = file.getName();


                    setImageviewPath(bitmap);
                    if (flag.equals("0")) {
                        img_path = "" + file.getPath();
                        Log.i("isavtar_true", "" + img_path);
                    } else if (flag.equals("1")) {
                        img_path_banner = "" + file.getPath();
                        Log.i("isavtar_false", "" + img_path_banner);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    img_path = "";
                    img_path_banner = "";

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(AddressActivity.this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                img_path = "";

            }
        }
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


    public void courierCharge(String address_id) {
        Log.d("TAG", "courierCharge: " + address_id + " " + COD);

        if (!address_id.equals("") && !COD.equals("")) {
            couriercharges("" + sharedPreferences.getString("user_id", ""), "" + address_id, "" + COD, SavedPrefManager.getStringPreferences(getApplicationContext(), "code"));
        } else {
            Log.d("TAG", "courierCharge: " + address_id + " " + COD);
        }


    }


    void setImageviewPath(Bitmap bitmap) {

        if (flag.equals("0")) {
            binding.ivGStImage.setImageBitmap(bitmap);
        } else if (flag.equals("1")) {
            binding.ivAadharCard.setImageBitmap(bitmap);
        }

    }

    public void buynow(View view) {


        startActivity(new Intent(getApplicationContext(), PaymodeActivity.class));
    }

    public void myaddresses(final String user_id) {
        Log.d("resp_user_id", "myaddresses: " + user_id);

        final ProgressDialog progressDialog = new ProgressDialog(AddressActivity.this);
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.myaddresses(user_id);


        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        address.clear();

                        String result = response.body().string();

                        Log.i("resp_address", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (success.equals("true")) {
                                message = obj.optString("message");

                                progressDialog.dismiss();

                                if (obj.has("data")) {

                                    JSONArray arr_product = obj.optJSONArray("data");
                                    if (arr_product.length() > 0) {
                                        Log.d("resp", "" + arr_product);


                                        for (int i = 0; i < arr_product.length(); i++) {
                                            JSONObject jsonObject_prod = arr_product.optJSONObject(i);
                                            //////////////    Adding to Datamodel   ////////////////

                                            Model_address model_cart = new Model_address();
                                            if (!jsonObject_prod.isNull("address_id")) {
                                                model_cart.setAddress_id(jsonObject_prod.optString("address_id"));

                                            } else {
                                                model_cart.setAddress_id("");
                                            }
                                            if (!jsonObject_prod.isNull("address_user_id")) {
                                                model_cart.setAddress_user_id(jsonObject_prod.optString("address_user_id"));
                                            } else {
                                                model_cart.setAddress_user_id("");
                                            }
                                            if (!jsonObject_prod.isNull("name")) {
                                                model_cart.setName(jsonObject_prod.optString("name"));
                                            } else {
                                                model_cart.setName("");
                                            }
                                            if (!jsonObject_prod.isNull("locality")) {
                                                model_cart.setLocality(jsonObject_prod.optString("locality"));
                                            } else {
                                                model_cart.setLocality("");
                                            }
                                            if (!jsonObject_prod.isNull("landmark")) {
                                                model_cart.setLandmark(jsonObject_prod.optString("landmark"));
                                            } else {
                                                model_cart.setLandmark("");
                                            }
                                            if (!jsonObject_prod.isNull("address")) {
                                                model_cart.setAddress(jsonObject_prod.optString("address"));

                                            } else {
                                                model_cart.setAddress1("");
                                            }
                                            if (!jsonObject_prod.isNull("address_line_2")) {
                                                model_cart.setAddress1(jsonObject_prod.optString("address_line_2"));

                                            } else {
                                                model_cart.setAddress("");
                                            }
                                            if (!jsonObject_prod.isNull("state")) {
                                                model_cart.setState(jsonObject_prod.optString("state"));

                                            } else {
                                                model_cart.setState("");
                                            }
                                            if (!jsonObject_prod.isNull("city")) {
                                                model_cart.setCity(jsonObject_prod.optString("city"));

                                            } else {
                                                model_cart.setCity("");
                                            }


                                            if (!jsonObject_prod.isNull("pincode")) {
                                                model_cart.setPincode(jsonObject_prod.optString("pincode"));
                                            } else {
                                                model_cart.setPincode("");
                                            }
                                            if (!jsonObject_prod.isNull("mobile")) {
                                                model_cart.setMobile(jsonObject_prod.optString("mobile"));
                                            } else {
                                                model_cart.setMobile("");
                                            }

                                            address.add(model_cart);

                                        }
                                        setup_address();
                                    }
                                }


                            } else {
                                progressDialog.dismiss();

                            }


                        } else {
                            progressDialog.dismiss();

                            String err_message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "" + err_message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "exception" + e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void deleteaddress(final String user_id, String address_id) {

        Log.d("resp_address", "deleteaddress: " + address_id);

        final ProgressDialog progressDialog = new ProgressDialog(AddressActivity.this);
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.deleteaddress(user_id, address_id);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();

                        Log.i("resp_delete", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (success.equals("true")) {
                                message = obj.optString("message");

                                progressDialog.dismiss();

                                if (message.equals("Address deleted.")) {

                                    hit();
                                }


                            } else {
                                progressDialog.dismiss();

                            }


                        } else {
                            progressDialog.dismiss();

                            String err_message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "" + err_message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "exception" + e.getMessage(), Toast.LENGTH_LONG).show();
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
    public void paymentlogs(final String user_id, String responsee,String status) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url4)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.paymentlogs(user_id, responsee,status);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();

                        Log.i("resp_delete", "" + result);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("resp_delete_exception", "" + e);


                    }
                } else if (response.code() == 401) {


                } else {

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }

    public void couriercharges(final String user_id, String address_id, String cod, String coupon) {
        Log.d("resp_address", "deleteaddress: " + address_id);
        Log.d("resp_address", "deleteaddress: " + coupon);
        final ProgressDialog progressDialog = new ProgressDialog(AddressActivity.this);
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.couriercharges(user_id, address_id, cod, "" + coupon);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        progressDialog.dismiss();
                        String result = response.body().string();

                        Log.i("resp_delete", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (success.equals("true")) {
                                binding.cvDelivery.setVisibility(View.VISIBLE);
                                deliveryCharge = Double.parseDouble(obj.optString("delivery_charges"));

                                binding.tvDelivery.setText("₹ " + deliveryCharge);

                                setTotalCharges(res, deliveryCharge);

                            } else {
                                binding.cvDelivery.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                binding.proceed.setClickable(false);
                                binding.proceed.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            progressDialog.dismiss();
                            binding.cvDelivery.setVisibility(View.GONE);

                            String err_message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "" + err_message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        binding.cvDelivery.setVisibility(View.GONE);
                        binding.proceed.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "exception" + e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void gstcheck(final String gstnumber) {
        Log.d("resp", "gstnumber: " + gstnumber);
        final ProgressDialog progressDialog = new ProgressDialog(AddressActivity.this);
        progressDialog.setMessage("Loading, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url3)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.gstcheck(gstnumber);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();

                        Log.i("resp_delete", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("flag")) {
                            progressDialog.dismiss();

                            success = obj.optString("flag");

                            if (success.equals("true")) {
                                gstVerified = true;
                                Toast.makeText(AddressActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                JSONObject jsonObject = obj.optJSONObject("data");

                                binding.llTrade.setVisibility(View.VISIBLE);
                                verifiedGst = jsonObject.optString("gstin");
                                binding.tvTrade.setText(jsonObject.optString("tradeNam"));

                            } else {
                                gstVerified = false;
                                verifiedGst = "";

                                Toast.makeText(AddressActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                            }
                        } else {

                            progressDialog.dismiss();


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        binding.cvDelivery.setVisibility(View.GONE);
                        binding.proceed.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "exception" + e.getMessage(), Toast.LENGTH_LONG).show();
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


    @Override
    public void onPaymentSuccess(final String razorpayPaymentID) {
        try {
            paymentlogs("" + sharedPreferences.getString("user_id", ""),""+razorpayPaymentID,"success");
            Needle.onMainThread().execute(new Runnable() {
                @Override
                public void run() {

                    payment_update("" + sharedPreferences.getString("user_id", ""),
                            "" + SavedPrefManager.getStringPreferences(context, "razorpayOrder")
                            , "" + razorpayPaymentID,
                            "success"
                    );

                }
            });


        } catch (Exception e) {
            Log.e("resp", "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        paymentlogs("" + sharedPreferences.getString("user_id", ""),""+response,"failed code"+code);

        try {

            Log.e("TAG", "Payment failed: " + code + " " + response);
            showDialog();
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentError", e);
        }


    }


    public void startPayment(String orderid, String razorpayOrder, String orderamount) {

        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        int finalresult_razorpayOrder = Integer.parseInt(orderid);
        Formatter result_razorpayOrder = formatter.format("%06d", finalresult_razorpayOrder);
        String neworderid = "Order# OD" + result_razorpayOrder;
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = AddressActivity.this;
        String email = sharedPreferences.getString("user_email", "");
        String mobilenumber = sharedPreferences.getString("user_mobile", "");
        String name = sharedPreferences.getString("user_name", "");

        final com.razorpay.Checkout co = new com.razorpay.Checkout();
        co.setFullScreenDisable(true);

        co.setKeyID(sharedPreferences.getString("razorpay_key", ""));
        long fin = (long) (Double.valueOf(orderamount) * 100);

        try {
            JSONObject options = new JSONObject();
            options.put("name", name);
            options.put("description", orderid);
            options.put("order_id", razorpayOrder);
            options.put("currency", "INR");
            options.put("send_sms_hash", true);
            options.put("amount", fin);//pass amount in currency subunits
            options.put("payment_capture", true);
            options.put("prefill.email", email);
            options.put("prefill.contact", mobilenumber);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            co.open(activity, options);

        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    public void payment_update(final String user_id, String rzr_order_id, String txn_id,
                               String status) {


        Log.i("Resp user_id ", user_id + "");
        Log.i("Resp rzr_order_id: ", rzr_order_id + "");
        Log.i("Resp status: ", status + "");
        Log.i("Resp txn_id: ", txn_id + "");

        progressDialog = new ProgressDialog(AddressActivity.this);
        progressDialog.setMessage("Please Wait..");
        // progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.payment_update(user_id, rzr_order_id, txn_id, status);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String result = response.body().string();
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");
                        Log.i("Resp success Process: ", obj + "");
                        progressDialog.dismiss();
                        if (obj.has("success")) {

                            if (success.equals("true")) {
                                String code = obj.getString("message");
                                String order_placed = obj.getString("order_placed");

                                if (order_placed.equals("0")) {

                                } else {
                                    dialog_pay_stts("Process Complete", "" + code, true);
                                }
                                Log.d("respp", "onResponse: " + code);
                                Snackbar.make(base_view, "" + message, Snackbar.LENGTH_LONG).show();

                            } else {
                                Snackbar.make(base_view, "" + message, Snackbar.LENGTH_LONG).show();
                            }
                        }


                    } catch (Exception ee) {
                        Log.i("Resp ee: ", ee.getMessage() + "");
                        progressDialog.dismiss();
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


    public void dialog_pay_stts(String title, String desc, final boolean success) {
        dialogue_success = new Dialog(AddressActivity.this);
        dialogue_success.setContentView(R.layout.dialogue_pay_status);
        dialogue_success.show();
        dialogue_success.setCancelable(false);

        TextView tv_title = (TextView) dialogue_success.findViewById(R.id.tv_title);
        TextView tv_desc = (TextView) dialogue_success.findViewById(R.id.tv_desc);
        TextView tv_submit = (TextView) dialogue_success.findViewById(R.id.tv_submit);
        ImageView img_tick = (ImageView) dialogue_success.findViewById(R.id.img_tick);

        tv_title.setText("" + title);
        tv_desc.setText("" + desc);

        if (success) {
            img_tick.setImageResource(R.drawable.success);
        } else {
            img_tick.setImageResource(R.drawable.nointernet);
        }


        if (success) {
            tv_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (success) {
                        dialogue_success.dismiss();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    } else {
                        dialogue_success.dismiss();
                    }
                }
            });

        } else {

            tv_submit.setText("Try Again!");
            dialogue_success.dismiss();
        }
    }


    public void delete(final String address_id) {


        new AlertDialog.Builder(AddressActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm")
                .setMessage("Are you sure you want to delete address?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteaddress("" + sharedPreferences.getString("user_id", "'"),
                                "" + address_id);
                    }

                })
                .setNegativeButton("No", null)
                .show();


    }

    public void setup_address() {

        Adapter_address adapter_notification = new Adapter_address(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter_notification);

        adapter_notification.notifyDataSetChanged();

    }


    void hit() {
        myaddresses("" + sharedPreferences.getString("user_id", ""));
    }

    public void add_address(View view) {
        Add_Address.From = "add";
        startActivity(new Intent(AddressActivity.this, Add_Address.class));
    }

    public void goback(View view) {
        onBackPressed();
    }

    public void proceed(View view) {


        if (address_id.equals("")) {
            Snackbar.make(base_view, "Please select delivery address", Snackbar.LENGTH_LONG).show();
        } else if (binding.gst.isChecked()) {

            Log.d("TAG", "proceed: " + binding.etGst.getText().toString());
            Log.d("TAG", "proceed: " + verifiedGst);

            if (binding.etGst.getText().toString().equals("")) {
                Toast.makeText(context, "Please enter and verify GST no.", Toast.LENGTH_SHORT).show();
            }
//            else if (!hasImage(binding.ivGStImage)) {
//                Toast.makeText(context, "Please add  GST Image", Toast.LENGTH_SHORT).show();
//
//            }
            else {
                if (binding.etGst.getText().toString().equals(verifiedGst)) {
                    binding.etAdhaar.setText("");
                    binding.ivAadharCard.setImageDrawable(null);
                    img_path_banner = "";

                    if (online.isChecked()) {
                        hit_placeorder("ONLINE");
                        type = "ONLINE";
                        // startActivity(new Intent(getApplicationContext(), PaygatewayActivity.class));
                    } else {
                        //   startActivity(new Intent(getApplicationContext(), MyOrdersActivity.class));
                        hit_placeorder("CASH");
                        type = "CASH";
                    }
                } else {
                    Toast.makeText(context, "Please verify GST number", Toast.LENGTH_SHORT).show();
                }

            }


        } else if (binding.adhar.isChecked()) {

            if (binding.etAdhaar.getText().toString().equals("")) {
                Toast.makeText(context, "Please enter Aadhar/PAN no.", Toast.LENGTH_SHORT).show();

            }
//            else if (!hasImage(binding.ivAadharCard)) {
//                Toast.makeText(context, "Please add  Aadhar/PAN Image", Toast.LENGTH_SHORT).show();
//
//            }
            else {

                binding.etGst.setText("");
                binding.ivGStImage.setImageDrawable(null);
                img_path = "";
                if (online.isChecked()) {
                    hit_placeorder("ONLINE");
                    type = "ONLINE";
                    // startActivity(new Intent(getApplicationContext(), PaygatewayActivity.class));
                } else {
                    //   startActivity(new Intent(getApplicationContext(), MyOrdersActivity.class));
                    hit_placeorder("CASH");
                    type = "CASH";
                }
            }


        }

    }


    void hit_placeorder(String type) {
//        placeorder("" + sharedPreferences.getString("user_id", ""), "" + address_id,"" + sharedPreferences.getString("note", ""), "" + type);
        editplaceorder("" + sharedPreferences.getString("user_id", ""), "" + address_id, "" + sharedPreferences.getString("note", ""), "" + type, "" + etGst.getText().toString(), "" + etAdhaar.getText().toString(), binding.referral.getText().toString(), SavedPrefManager.getStringPreferences(getApplicationContext(), "code"));
        Log.e("TAG", "hit_placeorder: " + sharedPreferences.getString("note", ""));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void editplaceorder(final String user_id, String address_id, String order_note, final String payment_type, String GST_NO, String ADHAR_PAN_NO, String referral_code, String promo_code) {

        final ProgressDialog progressDialog = new ProgressDialog(AddressActivity.this);
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
            builder.addFormDataPart("user_id", user_id);
            builder.addFormDataPart("address_id", address_id);
            builder.addFormDataPart("order_note", order_note);
            builder.addFormDataPart("payment_type", payment_type);
            builder.addFormDataPart("gst_no", GST_NO);
            builder.addFormDataPart("adhar_pan_number", ADHAR_PAN_NO);
            builder.addFormDataPart("referral_code", referral_code);
            builder.addFormDataPart("promo_code", promo_code);


//            gst_no, gst_image, adharpan_no, adharpan_image


            if (!img_path.equals("")) {
                File image = new File(img_path);
                Log.i("img_path2 :", "" + img_path);
                builder.addFormDataPart("gst_image", image.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), image));
            }
            if (!img_path_banner.equals("")) {
                File banner = new File(img_path_banner);
                Log.i("img_path_banner :", "" + img_path_banner);
                builder.addFormDataPart("adhar_pan_image", banner.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), banner));
            }

            MultipartBody requestBody = builder.build();
            Call<ResponseBody> call;
            call = uploadService.editplaceorder(requestBody);

            call.enqueue(new Callback<ResponseBody>() {
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
                                if (success.equals("true")) {
                                    message = obj.optString("message");
                                    progressDialog.dismiss();
                                    if (message.equals("Successfully")) {
                                        if (obj.has("order_id")) {
                                            order_id = "" + obj.getString("order_id");
                                            SavedPrefManager.saveStringPreferences(context, "razorpayOrder", obj.getString("razorpayOrder"));

                                            SavedPrefManager.saveStringPreferences(context, "order_id", obj.getString("order_id"));
                                            SavedPrefManager.saveStringPreferences(context, "razorpayOrder", obj.getString("razorpayOrder"));
                                            SavedPrefManager.saveStringPreferences(context, "amount", obj.getString("amount"));
                                            startPayment(obj.getString("order_id"), "" + obj.getString("razorpayOrder"), "" + obj.getDouble("amount"));
                                        }
                                    }
                                } else {
                                    progressDialog.dismiss();

                                }


                            } else {
                                progressDialog.dismiss();

                                String err_message = obj.getString("message");
                                Toast.makeText(getApplicationContext(), "" + err_message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "exception" + e.getMessage(), Toast.LENGTH_LONG).show();
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
                    Log.i("obj onFailure", "" + t.getMessage());
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "" +
                            "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception ee) {
            progressDialog.dismiss();

            Log.i("obj Exc", "" + ee.getMessage());
        }


    }


    public void showDialog() {
        final Dialog dialog = new Dialog(AddressActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_payment);

        Button cancel = dialog.findViewById(R.id.cancel_payment);
        Button ok = dialog.findViewById(R.id.ok_payment);

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            Needle.onMainThread().execute(() -> editplaceorder("" + sharedPreferences.getString("user_id", ""), "" + address_id, "" + sharedPreferences.getString("note", ""), "" + type, "" + etGst.getText().toString(), "" + etAdhaar.getText().toString(), binding.referral.getText().toString(), SavedPrefManager.getStringPreferences(getApplicationContext(), "code")));


        });
        ok.setOnClickListener(v -> {
            Needle.onMainThread().execute(new Runnable() {
                @Override
                public void run() {
                    payment_update("" + sharedPreferences.getString("user_id", ""),
                            "" + SavedPrefManager.getStringPreferences(context, "razorpayOrder")
                            , "",
                            "failed"
                    );

                }
            });


            dialog.dismiss();
        });


        dialog.show();

    }

    public void kycDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(AddressActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiManager apiManager = ApiManager.getInstance(AddressActivity.this);
        ApiCallBack<KycDetailsResponse> call = new ApiCallBack<KycDetailsResponse>(new ApiResponseListener<KycDetailsResponse>() {
            @Override
            public void onApiSuccess(KycDetailsResponse response, String apiName) {

                if (response != null) {
                    progressDialog.dismiss();
                    if (response.getSuccess()) {

                        if (response.getData().getGstNumber() != null) {
                            binding.adhar.setChecked(false);
                            binding.gst.setChecked(true);
                            etGst.setText(response.getData().getGstNumber());
                            verifiedGst = response.getData().getGstNumber();
                            binding.llAdhar.setVisibility(View.GONE);
                            binding.llGst.setVisibility(View.VISIBLE);
                            if (response.getData().getGstImage() != null) {
                                Glide.with(AddressActivity.this)
                                        .load(response.getData().getGstImage())
                                        .into(binding.ivGStImage);
                                binding.ivGStImage.setVisibility(View.VISIBLE);

                            }


                        } else if (response.getData().getAdharPanNumber() != null) {
                            binding.adhar.setChecked(true);
                            binding.gst.setChecked(false);
                            etAdhaar.setText(response.getData().getAdharPanNumber());
                            binding.llGst.setVisibility(View.GONE);
                            binding.llAdhar.setVisibility(View.VISIBLE);
                            if (response.getData().getAdharPanImage() != null) {
                                Glide.with(AddressActivity.this)
                                        .load(response.getData().getAdharPanImage())
                                        .into(binding.ivAadharCard);
                            } else {

                            }
                        }


                    } else {
                        //  Toast.makeText(AddressActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
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
        }, AppConstant.KYC_DETAILS, AddressActivity.this);
        RequestBody user_id = RequestBody.create(MediaType.parse("text"), sharedPreferences.getString("user_id", ""));
        apiManager.getKycDetails(call, user_id);
    }


    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }

    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("myaddresses")
        @FormUrlEncoded
        Call<ResponseBody> myaddresses(@Field("user_id") String user_id);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("deleteaddress")
        @FormUrlEncoded
        Call<ResponseBody> deleteaddress(@Field("user_id") String user_id,
                                         @Field("address_id") String address_id);
        @Headers("Authkey:APPCBSBDMPL")
        @POST("paymentlogs")
        @FormUrlEncoded
        Call<ResponseBody> paymentlogs(@Field("user_id") String user_id,
                                         @Field("response") String response,
                                         @Field("status") String status);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("couriercharges")
        @FormUrlEncoded
        Call<ResponseBody> couriercharges(@Field("user_id") String user_id,
                                          @Field("address_id") String address_id,
                                          @Field("cod") String cod,
                                          @Field("coupon") String coupon);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("placeorder")
        Call<ResponseBody> editplaceorder(@Body RequestBody file);


        @Headers("Authkey:APPCBSBDMPL")
        @POST("gstcheck")
        @FormUrlEncoded
        Call<ResponseBody> gstcheck(@Field("gstnumber") String gstnumber);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("payment_update")
        @FormUrlEncoded
        Call<ResponseBody> payment_update(@Field("user_id") String user_id,
                                          @Field("rzr_order_id") String rzr_order_id,
                                          @Field("txn_id") String txn_id,
                                          @Field("status") String status);


    }


}
