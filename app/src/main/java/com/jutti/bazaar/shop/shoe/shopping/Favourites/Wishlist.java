package com.jutti.bazaar.shop.shoe.shopping.Favourites;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

public class Wishlist extends Fragment {

    ImageView bt_back, bt_call;
    int cartitem = 0;
    Adapter_favourite adapter_favourite;
    RecyclerView recyclerfavourites;
    TextView cortcounts;
    LinearLayoutManager linearLayoutManager;
    ImageView Favourites, bt_search;
    LinearLayout ll_progress, yes,ll_nodata;

    ProgressBar progressBar;
    ProgressDialog progressDialog;






    public static ArrayList<String> product_id = new ArrayList<>();
    public static ArrayList<String> product_slug = new ArrayList<>();
    public static ArrayList<String> product_title = new ArrayList<>();
    public static ArrayList<String> product_image = new ArrayList<>();
    public static ArrayList<String> brand_id = new ArrayList<>();
    public static ArrayList<String> brand_name = new ArrayList<>();
    public static ArrayList<String> brand_slug = new ArrayList<>();
    public static ArrayList<String> category_id = new ArrayList<>();
    public static ArrayList<String> category_name = new ArrayList<>();
    public static ArrayList<String> category_slug = new ArrayList<>();
    View root;


    SharedPreferences sharedPreferences;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


         root = inflater.inflate(R.layout.fragment_wishlist, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter_favourite = new Adapter_favourite(getContext());
        bt_back = (ImageView) root.findViewById(R.id.back);
        recyclerfavourites = root.findViewById(R.id.recycler);
        ll_progress = root.findViewById(R.id.ll_progress);
        yes = root.findViewById(R.id.yes);
        ll_nodata = root.findViewById(R.id.ll_nodata);


        startactivty();

        favouriteproductlist("" +  sharedPreferences.getString("user_id", ""),
                "" + sharedPreferences.getString("user_login_token", ""));

        return root;
    }

    private void startactivty() {


//        bt_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.container1, new Homefrag());
//                fragmentTransaction.commit();
//            }
//        });
    }


    public void favouriteproductlist(final String user_id, String user_login_token) {
        show_progress();
        Log.i("user_id", "" + user_id);
        Log.i("user_login_token", "" + user_login_token);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.favouriteproductlist(user_id, user_login_token);
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

                        product_id.clear();
                        product_slug.clear();
                        product_title.clear();
                        product_image.clear();
                        brand_id.clear();
                        brand_name.clear();
                        brand_slug.clear();

                        category_id.clear();
                        category_name.clear();
                        category_slug.clear();





                        if (obj.has("data")) {
                            JSONArray arr_product = obj.optJSONArray("data");
                            if (arr_product.length() > 0) {


                                Log.d("resp", "" + arr_product);


                                for (int i = 0; i < arr_product.length(); i++) {
                                    JSONObject jsonObject_prod = arr_product.optJSONObject(i);
                                    //////////////    Adding to Datamodel   ////////////////


                                    product_id.add(jsonObject_prod.optString("product_id"));
                                    product_slug.add(jsonObject_prod.optString("product_slug"));
                                    product_title.add(jsonObject_prod.optString("product_title"));
                                    product_image.add(jsonObject_prod.optString("product_image"));
                                    brand_id.add(jsonObject_prod.optString("brand_id"));
                                    brand_name.add(jsonObject_prod.optString("brand_name"));
                                    brand_slug.add(jsonObject_prod.optString("brand_slug"));
                                    category_id.add(jsonObject_prod.optString("category_id"));
                                    category_name.add(jsonObject_prod.optString("category_name"));
                                    category_slug.add(jsonObject_prod.optString("category_slug"));


                                    show_yesdata();

                                    setup_cart_items();

                                }
                                show_yesdata();
                                setup_cart_items();
                            } else {
                                show_nodata();
                                //  Toast.makeText(getActivity(), "No favourites added", Toast.LENGTH_SHORT).show();

                            }




                        } else {
                            //  String err_message=obj.getString("message");
                            //x Toast.makeText(getActivity(), ""+err_message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {

                    Toast.makeText(getActivity(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getActivity(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void setup_cart_items() {


        recyclerfavourites.setLayoutManager(linearLayoutManager);
        recyclerfavourites.setAdapter(adapter_favourite);
    }




    public void show_progress() {
        ShimmerFrameLayout container,container1,container2,container3;
        container =
                (ShimmerFrameLayout) root.findViewById(R.id.shimmer);


        container.startShimmer();

        ll_progress.setVisibility(View.VISIBLE);
        yes.setVisibility(View.GONE);
        ll_nodata.setVisibility(View.GONE);
        //  ll_noresult.setVisibility(View.GONE);

    }

    public void show_yesdata() {

        ll_progress.setVisibility(View.GONE);
        yes.setVisibility(View.VISIBLE);
        // ll_noresult.setVisibility(View.GONE);




    } public void show_nodata() {



        ll_progress.setVisibility(View.GONE);
        yes.setVisibility(View.GONE);
        ll_nodata.setVisibility(View.VISIBLE);
        // ll_noresult.setVisibility(View.GONE);



    }


//    public void remove(String user_id, String user_login_token, String product_id) {
//
//        removefavourite("" + user_id, "" + user_login_token, ""+product_id);
//
//
//    }


    void dialog(String message) {

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
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



    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("favouriteproductlist")
        @FormUrlEncoded
        Call<ResponseBody> favouriteproductlist(
                @Field("user_id") String user_id,
                @Field("user_login_token") String user_login_token);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("removefavourite")
        @FormUrlEncoded
        Call<ResponseBody> removefavourite(
                @Field("user_id") String user_id,
                @Field("user_login_token") String user_login_token,
                @Field("product_id") String product_id);
    }
}