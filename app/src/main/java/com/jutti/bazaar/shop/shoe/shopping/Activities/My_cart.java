package com.jutti.bazaar.shop.shoe.shopping.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_Cart;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_qty;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Cart;
import com.jutti.bazaar.shop.shoe.shopping.Product_details.Product_Details;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.Welcome.DialogueCustom;
import com.jutti.bazaar.shop.shoe.shopping.api.SavedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class My_cart extends AppCompatActivity {
    public static ArrayList<String> stock_count = new ArrayList();
    public static String Selected_qty = "",prod_id="",varientId="";
    public static int selectedPosition = -1;
    RecyclerView recycler;

    ImageView iv_back_mycart;
    LinearLayout llcartitem, llmain, lll, llempty, ll_discount, ll_sub_tot;
    RelativeLayout rl_yes;
    Button checkout, additem;
    LinearLayoutManager linearLayoutManager;
    Dialog dialog;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Adapter_Cart adapter_cart;
    TextView tv_sub_tot, tv_resp, tv_total_discount, apply, tv_tot_price, promo, tv_total_items;
    ProgressBar pb;
    EditText et_note, et_promo;
    ProgressDialog progressDialog;
    String applied = "";
    public static JSONArray arr_product;
    public static String from = "";
    String orderid, total = "", promoo = "", sub_total = "", discount = "", discount_percentage = "", totall = "";
    String Token = "", user = "", image = "", proid = "";
    Uri bmpUri;
    Context context;
    boolean appliedPromo = false;
//    public static ArrayList<String> stock_counts = new ArrayList();

    public static ArrayList<Model_Cart> product = new ArrayList<>();
    public static ArrayList<String> product_id = new ArrayList<>();
    public static ArrayList<String> cart_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        context = My_cart.this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        init();


    }


    void init() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Token = sharedPreferences.getString("user_login_token", "");
        user = sharedPreferences.getString("user_id", "");
        proid = sharedPreferences.getString("proid", "");

        // Toast.makeText(this, ""+user, Toast.LENGTH_SHORT).show();
        Log.d("resp uid ,token", " " + Token + ",,," + user);
        llcartitem = findViewById(R.id.llmycart);
        llempty = findViewById(R.id.llempty);
        pb = findViewById(R.id.pb);
        llmain = findViewById(R.id.llmain);
        checkout = findViewById(R.id.checkout);
        lll = findViewById(R.id.ll);
        apply = findViewById(R.id.apply);
        tv_sub_tot = findViewById(R.id.tv_sub_tot);
        tv_resp = findViewById(R.id.tv_resp);
        tv_total_discount = findViewById(R.id.tv_total_discount);
        et_note = findViewById(R.id.et_remark);
        rl_yes = findViewById(R.id.rl_yes);
        et_promo = findViewById(R.id.et_promo);
        ll_discount = findViewById(R.id.ll_discount);
        ll_sub_tot = findViewById(R.id.ll_sub_tot);
        promo = findViewById(R.id.promo);
        tv_total_items = findViewById(R.id.tv_total_items);
        additem = findViewById(R.id.add_item);
        iv_back_mycart = findViewById(R.id.iv_back_mycart);
        recyclerView = findViewById(R.id.cart_recycler);
        tv_tot_price = findViewById(R.id.tv_total_price);
        adapter_cart = new Adapter_Cart(My_cart.this);
        linearLayoutManager = new LinearLayoutManager(My_cart.this);


        if (Token.isEmpty() || user.isEmpty()) {

        } else {
            cartdetails("" + Token, "" + user, "");
        }
        clicks();
    }


    public void change_qty(int Selected, final int stock, final int qty, final String login_token, final String user_id, final String product_id, final String varient_id) {
        stock_count.clear();
        Selected_qty = "" + Selected;
        varientId=varient_id;
        prod_id=product_id;

        Log.e("TAGV", "onBindViewHolder: " + selectedPosition);
        int a = stock;
        final int b = qty;
        final int c = a * b;

        ExecutorService executors = Executors.newSingleThreadExecutor();
        executors.execute(new Runnable() {
            @Override
            public void run() {


                //// do background heavy task here
                for (int i = b; i <= c; i += b) {
                    stock_count.add(String.valueOf(i));
                    //  Log.e("TAGV", "onBindViewHolder: " + stock_count);

                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //// Ui thread work like

                    }
                });
            }
        });


        Log.e("TAGVselectedPosition", "onBindViewHolder: " + selectedPosition);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_recycler);
        dialog.show();
        dialog.setCancelable(true);

        final EditText et_promo = (EditText) dialog.findViewById(R.id.et_prom);
        recycler = (RecyclerView) dialog.findViewById(R.id.recycler);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        TextView apply = (TextView) dialog.findViewById(R.id.apply);

        setup_qty();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                additem("" + login_token,
//                        "" + user_id
//                        , "" + product_id
//                        , "" + Selected_qty
//                        , "" + varient_id);

                dialog.dismiss();
            }
        });


    }
    public void change_qty1(final String Selected_qty) {

                additem(""+ sharedPreferences.getString("user_login_token", "")
                        ,""+ sharedPreferences.getString("user_id", "")
                        , "" + prod_id
                        , "" + Selected_qty
                        , "" + varientId);

    }

    private void setup_qty() {
        final GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recycler.setLayoutManager(mGridLayoutManager);
        Adapter_qty adapter_products_list = new Adapter_qty(My_cart.this);
        recycler.setAdapter(adapter_products_list);
    }


    void clicks() {

        if (Utils.isNetworkConnected(this)) {

            iv_back_mycart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }

            });
            additem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(My_cart.this, Home.class));
                }
            });

            promo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showpromo();
                }
            });
            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (appliedPromo) {
                        SavedPrefManager.saveStringPreferences(getApplicationContext(), "code", et_promo.getText().toString());
                    } else {
                        SavedPrefManager.saveStringPreferences(getApplicationContext(), "code", "");
                    }

                    Log.d("resp_user_id", "onClick: " + sharedPreferences.getString("user_id", ""));
                    SharedPreferences.Editor editorlog = sharedPreferences.edit(); // Editor of shared
                    editorlog.putString("total", total);
                    editorlog.putString("note", et_note.getText().toString());
                    editorlog.commit();

                    startActivity(new Intent(My_cart.this, AddressActivity.class));


                }
            });
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (apply.getText().toString().equals("Remove")) {
                        applied = "";
                        et_promo.setVisibility(View.VISIBLE);
                        tv_resp.setVisibility(View.GONE);
                        ll_sub_tot.setVisibility(View.GONE);
                        ll_discount.setVisibility(View.GONE);
                        apply.setText("Apply");
                        cartdetails("" + Token, "" + user, "");

                    } else {


                        if (!et_promo.getText().toString().equals("")) {
                            applypromocode("" + Token,
                                    "" + user
                                    , "" + et_promo.getText().toString()
                                    , "" + total);
                        } else {
                            Toast.makeText(My_cart.this, "Enter Valid Promocode!", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            });


        } else {
            DialogueCustom.dialogue_custom(this, "Network Error!", "Uh-oh!", "Slow or No Internet Connection.", " Check Internet Settings ", "", false, R.drawable.nointernet, "gotointernetsettings", "", Color.parseColor("#488a99"), Color.parseColor("#488a99"));
        }


    }


    public void cartdetails(final String user_login_token, String user_id, final String from) {


        pb.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        My_cart.Service service = retrofit.create(My_cart.Service.class);
        Call<ResponseBody> call;
        call = service.cartdetails(user_login_token, user_id);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        product_id.clear();
                        cart_id.clear();
                        String result = response.body().string();

                        Log.i("resultca", "" + result);
                        Log.i("resultca", "" + from);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("image_base_path")) {
                            image = obj.getString("image_base_path");
                            Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                        }

                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (success.equals("true")) {
                                message = obj.optString("message");

                                // Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();

                                if (obj.has("data")) {
                                    JSONObject data_obj = obj.optJSONObject("data");

                                    if (data_obj.has("total_price")) {

                                        total = data_obj.getString("total_price");

                                        tv_tot_price.setText("₹ " + total);
                                    } else {
                                        tv_tot_price.setText("");
                                        lll.setVisibility(View.GONE);
                                        total = "";
                                        llmain.setVisibility(View.GONE);
                                        llempty.setVisibility(View.VISIBLE);

                                    }
                                    if (data_obj.has("total_cart_item")) {

                                        tv_total_items.setText(data_obj.getString("total_cart_item"));

                                        String s = data_obj.getString("total_cart_item");

                                        if (s.equals("0")) {
                                            llempty.setVisibility(View.VISIBLE);
                                        }


                                    } else {
                                        tv_total_items.setText("");
                                        lll.setVisibility(View.GONE);

                                        llmain.setVisibility(View.GONE);
                                        llempty.setVisibility(View.VISIBLE);

                                    }
                                    if (data_obj.has("product")) {

                                        arr_product = data_obj.optJSONArray("product");
                                        if (arr_product.length() > 0) {
                                            product.clear();
                                            Log.d("resp", "" + arr_product);


                                            for (int i = 0; i < arr_product.length(); i++) {
                                                JSONObject jsonObject_prod = arr_product.optJSONObject(i);
                                                //////////////    Adding to Datamodel   ////////////////

                                                Model_Cart model_cart = new Model_Cart();
                                                if (!jsonObject_prod.isNull("product_id")) {
                                                    model_cart.setProductid(jsonObject_prod.optString("product_id"));
                                                    product_id.add(jsonObject_prod.optString("product_id"));

                                                } else {
                                                    model_cart.setProductid("");
                                                }


                                                if (!jsonObject_prod.isNull("product_total_stock")) {
                                                    model_cart.setProduct_total_stock(jsonObject_prod.optString("product_total_stock"));

                                                } else {
                                                    model_cart.setProduct_total_stock("");
                                                }

                                                if (!jsonObject_prod.isNull("product_min_order_qty")) {
                                                    model_cart.setProduct_min_order_qty(jsonObject_prod.optString("product_min_order_qty"));

                                                } else {
                                                    model_cart.setProduct_min_order_qty("");
                                                }


                                                if (!jsonObject_prod.isNull("cart_id")) {
                                                    model_cart.setCart_id(jsonObject_prod.optString("cart_id"));
                                                    cart_id.add(jsonObject_prod.optString("cart_id"));

                                                } else {
                                                    model_cart.setCart_id("");
                                                }
                                                if (!jsonObject_prod.isNull("variant_size")) {
                                                    model_cart.setvariant_size(jsonObject_prod.optString("variant_size"));
                                                } else {
                                                    model_cart.setvariant_size("");
                                                }
                                                if (!jsonObject_prod.isNull("variant_id")) {
                                                    model_cart.setVariant_id(jsonObject_prod.optString("variant_id"));
                                                } else {
                                                    model_cart.setVariant_id("");
                                                }
                                                if (!jsonObject_prod.isNull("cart_quantity")) {
                                                    model_cart.setCart_quantity(jsonObject_prod.optString("cart_quantity"));
                                                } else {
                                                    model_cart.setCart_quantity("");
                                                }
                                                if (!jsonObject_prod.isNull("product_title")) {
                                                    model_cart.setTITLE(jsonObject_prod.optString("product_title"));

                                                } else {
                                                    model_cart.setTITLE("");
                                                }
                                                if (!jsonObject_prod.isNull("product_image")) {
                                                    model_cart.setTHUMBNAIL(image + jsonObject_prod.optString("product_image"));
                                                } else {
                                                    model_cart.setTHUMBNAIL("");
                                                }

                                                if (!jsonObject_prod.isNull("cart_quantity")) {
                                                    model_cart.setqty(jsonObject_prod.optString("cart_quantity"));
                                                } else {
                                                    model_cart.setqty("");
                                                }
                                                if (!jsonObject_prod.isNull("product_price")) {
                                                    model_cart.setPRICE("₹ " + jsonObject_prod.optString("product_price"));
                                                } else {
                                                    model_cart.setPRICE("");
                                                }
                                                if (!jsonObject_prod.isNull("product_stock_type")) {
                                                    model_cart.settype(jsonObject_prod.optString("product_stock_type"));
                                                } else {
                                                    model_cart.settype("");
                                                }

                                                if (!jsonObject_prod.isNull("price_sub_total")) {
                                                    model_cart.setTOTAL_PRICE("₹ " + jsonObject_prod.optString("price_sub_total"));
                                                } else {
                                                    model_cart.setTOTAL_PRICE("");
                                                }
                                                product.add(model_cart);
                                                adapter_cart.notifyDataSetChanged();

                                                setup_cart_items();

                                            }
                                            adapter_cart.notifyDataSetChanged();
                                            rl_yes.setVisibility(View.VISIBLE);

                                            setup_cart_items();
                                        } else {
                                            lll.setVisibility(View.GONE);
                                            llmain.setVisibility(View.GONE);
                                            llempty.setVisibility(View.VISIBLE);

                                        }

                                    }

                                    pb.setVisibility(View.GONE);
                                }

                            }


                        } else {

                            pb.setVisibility(View.GONE);
                            String err_message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "" + err_message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        pb.setVisibility(View.GONE);
                        if (!from.equals("")) {
                            progressDialog.dismiss();
                        }

                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    if (!from.equals("")) {
                        progressDialog.dismiss();
                    }

                    pb.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    pb.setVisibility(View.GONE);
                    if (!from.equals("")) {
                        progressDialog.dismiss();
                    }

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!from.equals("")) {
                    progressDialog.dismiss();
                }

                pb.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void applypromocode(final String user_login_token, String user_id, final String promocode, String totalamount) {


        final ProgressDialog progressDialog = new ProgressDialog(My_cart.this);
        progressDialog.setMessage("Please Wait..");
        //  progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.applypromocode(user_login_token, user_id, promocode, totalamount);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();

                        Log.i("result", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (success.equals("true")) {
                                message = obj.optString("message");
                                // cartDialog();
                                tv_resp.setText(et_promo.getText().toString() + " Successfully Applied ");
                                apply.setText("Remove");

                                applied = "yes";

                                tv_resp.setVisibility(View.VISIBLE);
                                et_promo.setVisibility(View.GONE);

                                if (obj.has("data")) {
                                    JSONObject data_obj = obj.optJSONObject("data");

                                    if (data_obj.has("promo_code")) {

                                        promoo = data_obj.getString("promo_code");

                                    } else {
                                        promoo = "";
                                    }
                                    if (data_obj.has("sub_total")) {

                                        tv_sub_tot.setText(data_obj.getString("sub_total"));
                                        ll_sub_tot.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_sub_tot.setText("");
                                    }
                                    if (data_obj.has("discount")) {

                                        tv_total_discount.setText("- ₹ " + data_obj.getString("discount"));

                                    } else {
                                        tv_total_discount.setText("");
                                    }
                                    if (data_obj.has("discount_percentage")) {
                                        ll_discount.setVisibility(View.VISIBLE);
                                        discount_percentage = data_obj.getString("discount_percentage");

                                    } else {
                                        discount_percentage = "";
                                    }
                                    if (data_obj.has("total")) {
                                        appliedPromo = true;
                                        total = data_obj.getString("total");
                                        tv_tot_price.setText("₹ " + data_obj.getString("total"));
                                    } else {
                                        totall = "";
                                    }

                                }

                                if (!obj.isNull("order_id")) {
                                    // Toast.makeText(My_cart.this, ""+obj.getString("order_id"), Toast.LENGTH_SHORT).show();

                                    orderid = obj.getString("order_id");
                                } else {
                                    orderid = "";
                                }


                                // Toast.makeText(My_cart.this, "" + message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(My_cart.this, "" + message, Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            progressDialog.dismiss();
                            String err_message = obj.getString("message");
                            tv_resp.setText(err_message);

                            Toast.makeText(My_cart.this, "" + err_message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(My_cart.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(My_cart.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(My_cart.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(My_cart.this, "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removefromcart(final String user_login_token, String user_id, String product_id, String cart_id) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Removing, Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.removetocart(user_login_token, user_id, product_id, cart_id);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();
        Log.e("TAGRe", "removefromcart:"+cart_id );
        Log.e("TAGRe", "removefromcart:"+user_login_token );
        Log.e("TAGRe", "removefromcart:"+product_id );
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();
                        Log.i("resultad", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (success.equals("true")) {
                                message = obj.optString("message");

                                progressDialog.dismiss();

                                cartdetails("" + Token, "" + user, "");



                            } else {
                                progressDialog.dismiss();
                                //  Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            progressDialog.dismiss();
                            String err_message = obj.getString("message");
                            Toast.makeText(context, "" + err_message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        //  Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(context, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void additem(final String user_login_token, String user_id, final String product_id, String quantity, String variant_id) {

        Log.i("Get", "" + user_id);
        Log.i("Get", "" + product_id);
        Log.i("Get", "" + quantity);
        Log.i("Get", "" + variant_id);


//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Updating cart Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();


        Service service = retrofit.create(Service.class);

//        Product_Details.Service service = retrofit.create(Product_Details.Service.class);
        Call<ResponseBody> call;
        call = service.addtocart(user_login_token, user_id, product_id, quantity, variant_id);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();
        Log.i("Get All Categories", "" + user_id);
        Log.i("Get All Categories", "" + product_id);


        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {

                        String result = response.body().string();


                        Log.i("Get All Categories", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (success.equals("true")) {
                            Log.d("cartitem", "onResponse: " + message);
                            cartdetails("" + Token, "" + user, "");
                        } else {
                           // progressDialog.dismiss();

                            Toast.makeText(My_cart.this, "" + message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                      //  progressDialog.dismiss();

                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {
                  //  progressDialog.dismiss();


                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                   // progressDialog.dismiss();


                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


            }
        });
    }


    void showpromo() {
        dialog = new Dialog(My_cart.this);
        dialog.setContentView(R.layout.dialog_promo);
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final EditText et_promo = (EditText) dialog.findViewById(R.id.et_prom);
        Button btn = (Button) dialog.findViewById(R.id.btn_submit);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_promo.getText().toString().equals("")) {

                    Toast.makeText(My_cart.this, "Enter valid  code", Toast.LENGTH_SHORT).show();


                } else {
                    dialog.dismiss();

                    applypromocode("" + Token,
                            "" + user
                            , "" + et_promo.getText().toString()
                            , "" + total);
                }


            }
        });
    }


    public void add(String login_token, String user_id, String product_id, String quantity, String varient_id) {
        additem("" + login_token,
                "" + user_id
                , "" + product_id
                , "" + quantity
                , "" + varient_id);


    }
    public void remove(String product_id, String cart_id) {
        removefromcart("" + sharedPreferences.getString("user_login_token", ""),
                "" + sharedPreferences.getString("user_id", ""),
                "" +product_id
                , "" + cart_id);


    }


    public void setup_cart_items() {


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter_cart);

        adapter_cart.notifyDataSetChanged();

    }


    @Override
    public void onBackPressed() {
        finish();


    }

    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("cartdetails")
        @FormUrlEncoded
        Call<ResponseBody> cartdetails(@Field("user_login_token") String user_login_token,
                                       @Field("user_id") String user_id);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("check_coupon")
        @FormUrlEncoded
        Call<ResponseBody> check_coupon(@Field("user_login_token") String user_login_token,
                                        @Field("user_id") String user_id,
                                        @Field("order_note") String order_note,
                                        @Field("promo_code") String promo_code);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("applypromocode")
        @FormUrlEncoded
        Call<ResponseBody> applypromocode(@Field("user_login_token") String user_login_token,
                                          @Field("user_id") String user_id,
                                          @Field("promocode") String promocode,
                                          @Field("totalamount") String totalamount);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("removetocart")
        @FormUrlEncoded
        Call<ResponseBody> removetocart(@Field("user_login_token") String user_login_token,
                                        @Field("user_id") String user_id,
                                        @Field("product_id") String product_id,
                                        @Field("cart_id") String cart_id);


        @Headers("Authkey: APPCBSBDMPL")
        @FormUrlEncoded
        @POST("addtocart")
        Call<ResponseBody> addtocart(@Field("user_login_token") String user_login_token,
                                     @Field("user_id") String user_id,
                                     @Field("product_id") String product_id,
                                     @Field("quantity") String quantity,
                                     @Field("variant_id") String vet_cartitemariant_id
                                     
                                     
        );
    }

}






