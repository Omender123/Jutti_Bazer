package com.jutti.bazaar.shop.shoe.shopping;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Gulshan thakur 04-09-2019
 */

public class Utils {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 235;
    public static String url = "https://juttibazar.com/api/";
    public static String base_url = url+"v1/";
    public static String base_url2 = url+"v2/";
    public static String base_url3 = url+"v3/";
    public static String base_url4 = url+"v4/";
    public static String base_urlweb = "https://juttibazar.com/user/index.php?id=";
    public static String base_urlprod = "https://juttibazar.com/product/";
  //  public static String pdf_url = "https://cbskitchenware.com/create-cart-pdf?id=";
    public static String imgurl = "https://juttibazar.com/uploads/";




    public static boolean isNetworkConnected(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static void conDialog(final Context c) {
        AlertDialog.Builder alert = new AlertDialog.Builder(c);
        alert.setTitle("Internet connection unavailable.");
        alert.setMessage("You must be connected to an internet connection via WI-FI or Mobile Connection.");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                c.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
            }
        });
        alert.show();
    }

    @TargetApi(19)
    public static boolean checkPermissionLocation(final Context context) {
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.ACCESS_FINE_LOCATION")) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Location permission is required to calculated your distance from lab");
            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, Utils.MY_PERMISSIONS_REQUEST_LOCATION);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        return false;
    }

    public static String streamToString(InputStream is) throws IOException {
        String str = "";
        if (is == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String readLine = reader.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        } finally {
            is.close();
        }
    }




    public static void updatedtoken(final Context context,String user_login_token,String user_id ,String token) {
       /* final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.updatedtoken(""+user_login_token,""+user_id,""+token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // progressDialog.dismiss();
                    try {
                        String resultnoti = response.body().string();
                        JSONObject obj = new JSONObject(resultnoti);
                        String success = obj.optString("success");
                       

                   //     String role="";

                        if (obj.has("error")) {
// vibrate_alert(500);
                            String error_message = obj.getString("error_message");
                         // Toast.makeText(context, "Error : "+error_message, Toast.LENGTH_SHORT).show();


                        } else if (obj.has("warning")) {

// vibrate_alert(500);
                            String error_message = obj.getString("error_message");
                       //  Toast.makeText(context, "warning : "+error_message, Toast.LENGTH_SHORT).show();


                        } else {
                            if(obj.has("message")){
                                String message=obj.optString("message");
                          //    Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            }

                          // Toast.makeText(context,obj.optString("message"),Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        //  progressDialog.dismiss();
                        e.printStackTrace();
                    //    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
//                    progressDialog.dismiss();
                    Toast.makeText(context, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
//                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(context, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                progressDialog.dismiss();
             //   Toast.makeText(context, "Something went wrong at server!"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("updatedevicetoken")
        @FormUrlEncoded
        Call<ResponseBody>updatedtoken(@Field("user_login_token") String user_login_token,
                                       @Field("user_id") String user_id,
                                       @Field("token") String token);
    }
}