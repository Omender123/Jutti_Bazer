package com.jutti.bazaar.shop.shoe.shopping.Product_details;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_Similarproducts;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Cart;
import com.jutti.bazaar.shop.shoe.shopping.Activities.My_cart;
import com.jutti.bazaar.shop.shoe.shopping.Favourites.Wishlist;
import com.jutti.bazaar.shop.shoe.shopping.Activities.Home;
import com.jutti.bazaar.shop.shoe.shopping.Model.Banner_Model;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Model_product_list;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;
import com.jutti.bazaar.shop.shoe.shopping.databinding.ActivityProductDetailsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.viewpager.widget.ViewPager;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class Product_Details extends AppCompatActivity {
    View no_connectio;
    TextView tv_lot, tv_text, tv_try_later, tv_gst;
    Button btn_retry;
    ProgressBar pb;
    Button btn_add, btn;
    Dialog dialog;
    private ScrollView llPdf;
    Bitmap bitmap;
    public static JSONArray data1;
    public static ArrayList IMAGE_PATH = new ArrayList<>();
    ProgressDialog progressDialog;
    Adapter_selct_size adapter_selct_size;
    Gallery_adapter gallery_adapter;
    LinearLayout ll_packaging, ll_size_selected, pricefields, llmaster, llinner, llprice, llprice_mrp, ll_addtocart, ll_out, lloverview, llsize, llweight, like, unlike, similar, share;
    EditText et_cart_count;
    private ImageView[] dots;
    public static String regular_p = "", d = "", product_id = "", imagegallery = "", qty = "";
    int currentPage = 0, NUM_PAGES = 0;
    Timer timer;
    RelativeLayout cart;
    JSONArray price;
    ImageView wishlist;
    LinearLayoutManager llp;
    Spinner spinner;
    ActivityProductDetailsBinding binding;
    private static int NUM_Page = 0;
    final long DELAY_MS = 10000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 10000; //
    private Handler mHandler = new Handler();
    private Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            if (currentPage == NUM_Page) {
                currentPage = 0;
            }

            binding.viewPager.setCurrentItem(currentPage++, false);
            mHandler.postDelayed(mHandlerTask, 8000);
        }
    };

    public static int ind = 0, slcted;
    public static String selectedsize = "";
    SharedPreferences.Editor editor;
    boolean stopslide = false;
    public static ArrayList<String> al_banners = new ArrayList();
    public static ArrayList<String> stock_count = new ArrayList();
    public static ArrayList<String> price_max_limit_type = new ArrayList();
    public static ArrayList<String> price_min_qty = new ArrayList();
    public static ArrayList<String> price_max_qty = new ArrayList();
    public static ArrayList<String> price_product = new ArrayList();
    public static ArrayList<String> price_grade = new ArrayList();
    public static ArrayList<String> stateTitle = new ArrayList<>();
    public static ArrayList<Banner_Model> gallerylist = new ArrayList();
    public static ArrayList<String> variant_id = new ArrayList();
    public static ArrayList<String> variant_size = new ArrayList();
    public static ArrayList<String> variant_weight = new ArrayList();
    public static ArrayList<String> variant_mrp_price = new ArrayList();
    public static ArrayList<String> variant_regular_price = new ArrayList();
    public static ArrayList<String> variant_selling_price = new ArrayList();
    public static ArrayList<String> variant_including_tax = new ArrayList();
    public static ArrayList<String> variant_master_packing = new ArrayList();
    public static ArrayList<String> variant_inner_case = new ArrayList();
    public static ArrayList<JSONArray> product_price_list = new ArrayList();
    public static ArrayList<String> stock = new ArrayList();
    public static  ArrayList<Model_product_list> prodlist = new ArrayList<Model_product_list>();


    String Token = "", user = "", imageurlprod = "";
    ImageView iv_back_details, swipe;
    SharedPreferences sharedPreferences;
    RecyclerView recycler_view_banner, rcv_selections, recycler;
    ;
    String Total_amt = "", product_idApi = "", product_slug = "", product_similar_tags = "", product_packing_type = "", brand_id = "", category_name = "", category_id = "";
    LinearLayout llSeller;
    public static String Selected_qty = "";
    public static int selectedPosition = -1;

    TextView tv_count, tv_packing, total, tv_avail, sizes_available, tax, count, tv_save, tv_percentage, tv_Masterpacking, tv_innerpacking, tv_product_name, tv_product_price, tv_product_price_mrp, tvSlellerName, tv_material, tv_product_brand, tv_product_weight,
            tv_product_size, tv_product_description, tv_quanty_type, moq;
    int product_total_stock = 0, product_min_order_qty = 0;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product__details);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product__details);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Token = sharedPreferences.getString("user_login_token", "");
        user = sharedPreferences.getString("user_id", "");


        Log.d("token", "onCreate: " + Token + "Userid" + user);
        if (getIntent().hasExtra("product_id")) {
            product_id = getIntent().getStringExtra("product_id");
            Log.d("product id", "onCreate: " + product_id);

            //  btn_add.setText("Out of stock");
            //  btn_add.setBackgroundColor(Color.parseColor("#000"));
        }


        init();

        if (Utils.isNetworkConnected(Product_Details.this)) {

            if (Token.equals("")) {
                ll_size_selected.setVisibility(View.GONE);
                cart.setVisibility(View.GONE);
                ll_addtocart.setVisibility(View.GONE);
                unlike.setVisibility(View.GONE);
                wishlist.setVisibility(View.GONE);

                pricefields.setVisibility(View.GONE);
            } else {

                cartdetails("" + Token, "" + user);
            }
            productdetails(product_id, "" + Token, "" + user);
            ind = 0;

            Log.d("token product id", "onCreate: " + product_id);


        } else {
            no_connection("No Internet Connection! Please retry");
        }


        Log.i("Get All Categories", "token" + Token + "user" + user);

    }

    void init() {

        ///no connction

        tv_count = findViewById(R.id.tv_count);
        recycler = findViewById(R.id.recycler);

        spinner = findViewById(R.id.spinner);
        no_connectio = findViewById(R.id.no_connectio);
        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_lot = (TextView) findViewById(R.id.tv_lot);
        tv_try_later = (TextView) findViewById(R.id.tv_try_later);
        tv_gst = (TextView) findViewById(R.id.tv_gst);
        btn_retry = (Button) findViewById(R.id.btn_retry);
        pb = findViewById(R.id.pb);
        btn = findViewById(R.id.btn);
        llPdf = findViewById(R.id.llpdf);
        cart = findViewById(R.id.cart);
        sizes_available = findViewById(R.id.sizes_available);
        tax = findViewById(R.id.tax);
        count = findViewById(R.id.count);
        tv_packing = findViewById(R.id.tv_packing);
        ll_packaging = findViewById(R.id.ll_packaging);
        similar = findViewById(R.id.similar);
        like = findViewById(R.id.like);
        share = findViewById(R.id.share);
        wishlist = findViewById(R.id.wishlist);
        unlike = findViewById(R.id.unlike);
        moq = findViewById(R.id.moq);
        rcv_selections = findViewById(R.id.rcv_selections);
        adapter_selct_size = new Adapter_selct_size(Product_Details.this);

        llp = new LinearLayoutManager(Product_Details.this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_banner = (RecyclerView) findViewById(R.id.recycler_view_banner1);
        tv_product_price = (TextView) findViewById(R.id.tv_product_price);
        tv_product_price_mrp = (TextView) findViewById(R.id.tv_product_price_mrp);
        tv_material = (TextView) findViewById(R.id.tv_material);
        tvSlellerName = (TextView) findViewById(R.id.tvSlellerName);
        tv_product_brand = (TextView) findViewById(R.id.tv_product_brand);
        tv_product_weight = (TextView) findViewById(R.id.tv_product_weight);
        tv_product_size = (TextView) findViewById(R.id.tv_product_size);
        tv_avail = (TextView) findViewById(R.id.tv_avail);
        llSeller = (LinearLayout) findViewById(R.id.llSeller);
        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_Masterpacking = (TextView) findViewById(R.id.tv_Masterpacking);
        tv_innerpacking = (TextView) findViewById(R.id.tv_innerpacking);
        tv_percentage = (TextView) findViewById(R.id.tv_percentage);
        total = (TextView) findViewById(R.id.total);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_product_description = (TextView) findViewById(R.id.tv_product_description);
        iv_back_details = (ImageView) findViewById(R.id.iv_back_details);
        swipe = findViewById(R.id.swipe);
        llmaster = findViewById(R.id.llmaster);
        llinner = findViewById(R.id.llinner);
        pricefields = findViewById(R.id.pricefields);
        llprice = findViewById(R.id.llprice);
        llprice_mrp = findViewById(R.id.llpricemrp);
        llsize = findViewById(R.id.llsize);
        llweight = findViewById(R.id.llweight);
        tv_quanty_type = findViewById(R.id.tv_quantiy_type);
        lloverview = findViewById(R.id.lloverview);
        btn_add = findViewById(R.id.btn_add_tocart);
        et_cart_count = findViewById(R.id.et_cartitem);
        ll_size_selected = findViewById(R.id.ll_size_selected);

        ll_out = findViewById(R.id.ll_out);

        ll_addtocart = findViewById(R.id.ll_addtocart);


        click();

    }


    void click() {

        tv_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_qty();
            }
        });


        iv_back_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });
        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Product_list.categoryID = category_id;
                Product_list.title = category_name;

                startActivity(new Intent(Product_Details.this, Product_list.class));
                finish();


            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Product_Details.this, Wishlist.class));
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CBS Kitchenware");
                    String shareMessage = "\nLet me recommend you this product\n\n";
                    shareMessage = shareMessage + Utils.base_urlprod + product_slug;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }

            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Product_Details.this, "like", Toast.LENGTH_SHORT).show();

                removefavourite("" + user, "" + Token, "" + product_id);

                unlike.setVisibility(View.VISIBLE);
                like.setVisibility(View.GONE);


            }
        });
        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Product_Details.this, "un", Toast.LENGTH_SHORT).show();
                addfavourite("" + user, "" + Token, "" + product_id);

                unlike.setVisibility(View.GONE);
                like.setVisibility(View.VISIBLE);


            }
        });


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                My_cart.from = "detail";

                editor = sharedPreferences.edit();
                editor.putString("proid", product_id);
                editor.commit();
                startActivity(new Intent(Product_Details.this, My_cart.class));


            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Utils.isNetworkConnected(Product_Details.this)) {

                    if (tv_count.getText().toString().trim().equals("")) {
                        Toast.makeText(Product_Details.this, "Please choose valid Quantity", Toast.LENGTH_SHORT).show();
                    } else {

                        int in = Integer.valueOf(product_min_order_qty);
                        int max = Integer.valueOf(product_total_stock);


                        Log.d("limit", " min" + in + "   max" + max);
                        Log.d("size", " selectedStatename" + selectedsize);


                        editor = sharedPreferences.edit();
                        editor.putString("proid", product_id);
                        editor.commit();
                        additem("" + Token,
                                "" + user,
                                "" + product_id,
                                "" + tv_count.getText().toString().trim()
                                , "" + selectedsize);
                        My_cart.from = "detail";


                        Log.d("cartitem", "onResponse: " + selectedsize);
//                    Log.d("cartitem", "onResponse: " + qty);
                        Log.d("cartitem", "onResponse: " + product_id);
                    }

                } else {
                    no_connection("No Internet Connection! Please retry");
                }


            }
        });


    }


    void focusOnView() {
        llPdf.post(new Runnable() {
            @Override
            public void run() {
                llPdf.scrollTo(0, et_cart_count.getBottom());
            }
        });

    }


    public void change_qty() {
        stock.clear();
        selectedPosition = -1;

        int c = product_min_order_qty * product_total_stock;

        for (int j = product_min_order_qty; j <= c; j += product_min_order_qty) {

            stock.add(String.valueOf(j));
        }


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

                dialog.dismiss();
                tv_count.setText("" + Selected_qty);
//                additem("" + login_token,
//                        "" + user_id
//                        , "" + product_id
//                        , "" + quantity
//                        , "" + varient_id);


            }
        });


    }


    private void setup_qty() {


        final GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recycler.setLayoutManager(mGridLayoutManager);
        Adapter_qty_product adapter_products_list = new Adapter_qty_product(Product_Details.this);

        recycler.setAdapter(adapter_products_list);
    }

    private void setup_similar() {


        final GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        binding.rcvSimilar.setLayoutManager(mGridLayoutManager);
        Adapter_Similarproducts adapter_products_list = new Adapter_Similarproducts(Product_Details.this);
        binding.rcvSimilar.setAdapter(adapter_products_list);
    }

    @SuppressLint("WrongConstant")
    public void productsearch(final String page, String brand_id, String category_id, String tag_search, String price_sorting, String seller_items, String filter_gender) {


        Log.i("result page", "" + page);
        Log.i("result brand_id", "" + brand_id);
        Log.i("result category_id", "" + category_id);
        Log.i("result tag_search", "" + tag_search);
        Log.i("result price_sorting", "" + price_sorting);
        Log.i("result seller_items", "" + seller_items);
        Log.i("result filter_gender", "" + filter_gender);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Product_list.Service service = retrofit.create(Product_list.Service.class);
        Call<ResponseBody> call;
        call = service.productsearch(page, brand_id, category_id, tag_search, price_sorting, seller_items, filter_gender);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    try {
                        prodlist.clear();
                        String result = response.body().string();

                        Log.i("result", "" + result);
                        JSONObject obj = new JSONObject(result);
                        Log.d("resp", "" + obj);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (obj.has("image_base_path")) {
                                imageurlprod = obj.getString("image_base_path");
                                Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                            }

                            if (success.equals("true")) {
                                message = obj.optString("message");


                                if (obj.has("data")) {


                                    JSONArray arr_order = obj.getJSONArray("data");


                                    if (arr_order.length() > 0) {


                                        Log.d("responseee", "" + arr_order);


                                        for (int i = 0; i < arr_order.length(); i++) {
                                            JSONObject jsonObject = arr_order.optJSONObject(i);
                                            //////////////    Adding to Datamodel   ////////////////

                                            Model_product_list droppointListModel = new Model_product_list();

                                            droppointListModel.setproducts_id(jsonObject.getString("product_id"));
                                            droppointListModel.setproducts_name(jsonObject.getString("product_title"));
                                            droppointListModel.setproducts_image(imageurlprod + jsonObject.getString("product_image"));

                                            if (!jsonObject.isNull("popularity")) {
                                                droppointListModel.setPopularity(jsonObject.getString("popularity"));


                                            } else {
                                                droppointListModel.setPopularity("");

                                            }
                                            if (!jsonObject.isNull("seller_name")) {
                                                droppointListModel.setSeller_name(jsonObject.getString("seller_name"));
                                            } else {
                                                droppointListModel.setSeller_name("");

                                            }

                                            if (!jsonObject.isNull("variant_regular_price")) {
                                                droppointListModel.setVariant_regular_price(jsonObject.getString("variant_regular_price"));


                                            } else {
                                                droppointListModel.setVariant_regular_price("");

                                            }
                                            if (!jsonObject.isNull("category_name")) {
                                                droppointListModel.setCategory_name(jsonObject.getString("category_name"));
                                            } else {
                                                droppointListModel.setCategory_name("");

                                            }
                                            if (!jsonObject.isNull("product_min_order_qty")) {
                                                droppointListModel.setProduct_min_order_qty(jsonObject.getString("product_min_order_qty"));


                                            } else {
                                                droppointListModel.setProduct_min_order_qty("");

                                            }
                                            if (!jsonObject.isNull("variant_mrp_price")) {
                                                droppointListModel.setVariant_mrp_price(jsonObject.getString("variant_mrp_price"));


                                            } else {
                                                droppointListModel.setVariant_mrp_price("");

                                            }
                                            if (!jsonObject.isNull("variant_selling_price")) {
                                                droppointListModel.setVariant_selling_price(jsonObject.getString("variant_selling_price"));


                                            } else {
                                                droppointListModel.setVariant_selling_price("");

                                            }

                                            prodlist.add(droppointListModel);
                                        }

                                        setup_similar();

                                    } else {

                                        // Toast.makeText(Product_list.this, "No Result", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(Product_Details.this, "No Result", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(Product_Details.this, "No Result", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            String err_message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "error" + err_message, Toast.LENGTH_SHORT).show();
                            Log.d("error", "" + err_message);
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                        Log.d("e", "" + e);
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 404) {

                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 404. Please Try again later", Toast.LENGTH_LONG).show();
                } else {

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void productdetails(final String product_id, String user_login_token, String user_id) {
        final ProgressDialog progressDialog = new ProgressDialog(Product_Details.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("TAG", "productdetails: "+product_id);
        Log.d("TAG", "productdetails: "+user_login_token);
        Log.d("TAG", "productdetails: "+user_id);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.productdetails(product_id, user_login_token, user_id);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    no_connectio.setVisibility(View.GONE);


                    try {

                        String result = response.body().string();

                        Log.i("Get All Categories", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");
                        al_banners.clear();
                        gallerylist.clear();
                        variant_selling_price.clear();
                        price_max_limit_type.clear();
                        price_max_qty.clear();
                        price_min_qty.clear();
                        price_product.clear();
                        stateTitle.clear();
                        IMAGE_PATH.clear();
                        variant_id.clear();
                        variant_inner_case.clear();
                        variant_master_packing.clear();
                        variant_weight.clear();
                        variant_size.clear();
                        price_grade.clear();
                        product_price_list.clear();
                        variant_mrp_price.clear();
                        variant_regular_price.clear();
                        variant_including_tax.clear();


                        if (message.equals("Do not have Sufficient Permissions to Access")) {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            progressDialog.dismiss();

                            startActivity(new Intent(Product_Details.this, Home.class));
                        }

                        if (obj.has("image_base_path")) {
                            imagegallery = obj.getString("image_base_path");
                            Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                        }


                        if (success.equals("true")) {

                            progressDialog.dismiss();
                            final JSONObject data = obj.optJSONObject("data");


                            if (data.has("seller")) {

                                JSONObject seller = data.optJSONObject("seller");
                                if (!seller.isNull("user_name")) {
                                    llSeller.setVisibility(View.VISIBLE);
                                    tvSlellerName.setText("Sold By : " + seller.optString("user_name"));
                                } else {
                                    tvSlellerName.setText("");
                                    llSeller.setVisibility(View.GONE);
                                }
                            }


                            if (!data.isNull("product_id")) {
                                product_idApi = data.getString("product_id");
                            } else {
                                product_idApi = "";
                            }


                            if (!data.isNull("product_slug")) {
                                product_slug = data.getString("product_slug");
                            } else {
                                product_slug = "";
                            }

                            if (!data.isNull("product_title")) {
                                tv_product_name.setText(data.getString("product_title"));
                            } else {
                                tv_product_name.setText("");
                            }
                            if (!data.isNull("product_favourite")) {

                                String fav = data.getString("product_favourite");

                                if (fav.equals("true")) {
                                    like.setVisibility(View.VISIBLE);
                                    unlike.setVisibility(View.GONE);
                                } else {
                                    like.setVisibility(View.GONE);
                                    unlike.setVisibility(View.VISIBLE);
                                }

                            } else {
                                tv_product_name.setText("");
                            }

                            if (!data.isNull("product_description")) {
                                String d = data.getString("product_description");


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    tv_product_description.setText(Html.fromHtml(d, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    tv_product_description.setText(Html.fromHtml(d));
                                }


                                if (d.equals("")) {
                                    lloverview.setVisibility(View.GONE);
                                }
                            } else {
                                tv_product_description.setText("");
                                lloverview.setVisibility(View.GONE);
                            }


                            if (!data.isNull("brand_name")) {
                                tv_product_brand.setText(data.getString("brand_name"));
                            } else {
                                tv_product_brand.setText("");
                            }
                            if (!data.isNull("product_variants")) {
                                tv_product_size.setText(data.getString("product_variants"));
                                llsize.setVisibility(View.GONE);
                            } else {
                                tv_product_size.setText("");
                                llsize.setVisibility(View.GONE);
                            }

                            if (!data.isNull("brand_id")) {
                                brand_id = data.getString("brand_id");
                            } else {
                                brand_id = "";
                            }
                            if (!data.isNull("product_packing_type")) {
                                ll_packaging.setVisibility(View.VISIBLE);
                                tv_packing.setText(data.getString("product_packing_type"));
                            } else {
                                ll_packaging.setVisibility(View.GONE);

                                tv_packing.setText("");
                            }
                            if (!data.isNull("product_similar_tags")) {
                                product_similar_tags = data.getString("product_similar_tags");
                            } else {
                                product_similar_tags = "";
                            }

                            if (!data.isNull("category_id")) {
                                category_id = data.getString("category_id");
                                productsearch("", "", "" + category_id, "", "", "", "");
                            } else {
                                category_id = "";
                            }
                            if (!data.isNull("category_name")) {
                                category_name = data.getString("category_name");
                            } else {
                                category_name = "";
                            }

//                            mcmdc

                            if (!data.isNull("product_min_order_qty")) {
                                product_min_order_qty = Integer.parseInt(data.getString("product_min_order_qty"));
                                moq.setText("MOQ:  " + data.getString("product_min_order_qty") + " Pairs");
                                tv_lot.setText("1 lot ( " + data.getString("product_min_order_qty") + " Pairs )");

                                if (product_min_order_qty == 0) {

                                    ll_out.setVisibility(View.VISIBLE);
                                    ll_addtocart.setVisibility(View.GONE);

                                }

                            } else {
                                product_min_order_qty = 0;
                            }
                            if (!data.isNull("product_stock_type")) {
                                tv_quanty_type.setText(data.getString("product_stock_type"));
                            } else {
                                tv_quanty_type.setText("");
                            }

                            if (!data.isNull("product_total_stock")) {
                                product_total_stock = Integer.parseInt(data.getString("product_total_stock"));
                                if (product_total_stock == 0) {

                                    ll_out.setVisibility(View.VISIBLE);
                                    ll_addtocart.setVisibility(View.GONE);

                                } else {

                                    set_spinner();
                                }
                            } else {
                                product_total_stock = 0;
                            }

                            if (!data.isNull("category_name")) {
                                tv_material.setText(data.getString("category_name"));
                            } else {
                                tv_material.setText("");
                            }


                            if (!data.isNull("product_gallery")) {
                                data1 = data.getJSONArray("product_gallery");
                                IMAGE_PATH.add(data.getJSONArray("product_gallery"));
                                Log.d("gallery", " " + data1);
                                if (data1.length() > 0) {


                                    //   rl_banner.setVisibility(View.VISIBLE);
                                    for (int j = 0; j < data1.length(); j++) {
                                        JSONObject object = data1.getJSONObject(j);
                                        Log.d("gallery images:", " " + object);
                                        Banner_Model bm = new Banner_Model();
                                        bm.setGallery_image(imagegallery + object.getString("product_image"));

                                        al_banners.add(imagegallery + object.getString("product_image"));
                                        Product_Details.gallerylist.add(bm);
//                                        viewpage_home();
                                        timerOnViewPager();

                                    }
                                    setData();
                                } else {
                                    // rl_banner.setVisibility(View.GONE);
                                }
                                progressDialog.dismiss();
                            }


                            if (!data.isNull("product_variant")) {
                                JSONArray product_size_array = data.getJSONArray("product_variant");

                                tv_avail.setText("(Available " + product_size_array.length() + ")");
                                if (product_size_array.length() > 0) {
                                    for (int i = 0; i < product_size_array.length(); i++) {
                                        JSONObject jsonObject_prod = product_size_array.optJSONObject(i);

                                        if (!jsonObject_prod.isNull("variant_id")) {
                                            variant_id.add(jsonObject_prod.optString("variant_id"));

                                        } else {
                                            variant_id.add("");
                                        }
                                        if (!jsonObject_prod.isNull("variant_size")) {
                                            variant_size.add(jsonObject_prod.optString("variant_size"));

                                            String s = variant_size.toString().replace("[", "").replace("]", "");


                                        } else {
                                            variant_size.add("");
                                        }


                                        if (!jsonObject_prod.isNull("variant_weight")) {
                                            variant_weight.add(jsonObject_prod.optString("variant_weight"));

                                            // tv_product_weight.setText(s);

                                        } else {
                                            variant_weight.add("");
                                        }
                                        if (!jsonObject_prod.isNull("variant_master_packing")) {
                                            variant_master_packing.add(jsonObject_prod.optString("variant_master_packing"));

                                        } else {
                                            variant_master_packing.add("");
                                        }
                                        if (!jsonObject_prod.isNull("variant_inner_case")) {
                                            variant_inner_case.add(jsonObject_prod.optString("variant_inner_case"));

                                        } else {
                                            variant_inner_case.add("");
                                        }

                                        if (!jsonObject_prod.isNull("variant_mrp_price")) {
                                            variant_mrp_price.add(jsonObject_prod.getString("variant_mrp_price"));
                                        } else {
                                            variant_mrp_price.add("");
                                        }

                                        if (!jsonObject_prod.isNull("variant_regular_price")) {
                                            variant_regular_price.add(jsonObject_prod.getString("variant_regular_price"));
                                        } else {
                                            variant_regular_price.add("");

                                        }
                                        if (!jsonObject_prod.isNull("variant_regular_price")) {
                                            variant_selling_price.add(jsonObject_prod.getString("variant_selling_price"));
                                        } else {
                                            variant_selling_price.add("");

                                        }


                                        if (!jsonObject_prod.isNull("variant_including_tax")) {
                                            variant_including_tax.add(jsonObject_prod.getString("variant_including_tax"));
                                            //   tv_product_price.setText(" ₹ "+data.getString("product_regular_price"));
                                        } else {


                                            variant_including_tax.add("");

                                        }


                                        if (!jsonObject_prod.isNull("product_price_list")) {

                                            product_price_list.add(jsonObject_prod.getJSONArray("product_price_list"));


                                            //  Toast.makeText(Product_Details.this, ""+data.getJSONArray("product_price_list").length(), Toast.LENGTH_SHORT).show();


                                        }


                                    }


                                    price = product_price_list.get(ind);

                                    selectedsize = variant_id.get(ind);

                                    // variant_size.indexOf(ind);
                                    sizes_available.setText(variant_size.get(ind));


                                    if (!variant_master_packing.get(ind).equals("")) {

                                        tv_Masterpacking.setText(variant_master_packing.get(ind));
                                        llmaster.setVisibility(View.VISIBLE);


                                    } else {
                                        llmaster.setVisibility(View.GONE);
                                    }


                                    if (!variant_inner_case.get(ind).equals("")) {
                                        llinner.setVisibility(View.VISIBLE);

                                        tv_innerpacking.setText(variant_inner_case.get(ind));
                                    } else {
                                        llinner.setVisibility(View.GONE);
                                    }


                                    if (!variant_weight.get(ind).equals("")) {

                                        llweight.setVisibility(View.GONE);

                                        tv_product_weight.setText(variant_weight.get(ind));
                                    } else {
                                        llweight.setVisibility(View.GONE);
                                    }


                                    if (!variant_regular_price.get(ind).equals("")) {
                                        regular_p = variant_regular_price.get(ind);
                                        llprice.setVisibility(View.VISIBLE);
                                        // Toast.makeText(Product_Details.this, ""+variant_regular_price.get(ind), Toast.LENGTH_SHORT).show();

                                        tv_product_price.setText(" ₹ " + variant_selling_price.get(ind));

                                        double a = Double.parseDouble(variant_selling_price.get(ind));
                                        double b = Double.parseDouble(variant_regular_price.get(ind));
                                        double res = a - b;
                                        tv_gst.setText("₹." + variant_regular_price.get(ind) + " + " + " ₹." + String.format("%.2f", res) + " GST");
                                    } else {
                                        regular_p = "";
                                        // llprice.setVisibility(View.GONE);
                                    }


                                    if (!variant_mrp_price.get(ind).equals("")) {

                                        llprice_mrp.setVisibility(View.VISIBLE);

                                        d = variant_mrp_price.get(ind);
                                        //  Toast.makeText(Product_Details.this, "" + variant_mrp_price.get(ind), Toast.LENGTH_SHORT).show();


                                        tv_product_price_mrp.setText(" ₹ " + variant_mrp_price.get(ind));
                                    } else {
                                        d = "";
                                        llprice_mrp.setVisibility(View.GONE);
                                    }


                                    if (!variant_including_tax.get(ind).equals("")) {

                                        //  Toast.makeText(Product_Details.this, "", Toast.LENGTH_SHORT).show();

                                        if (variant_including_tax.get(ind).equals("Yes")) {
                                            tax.setVisibility(View.VISIBLE);

                                        } else {
                                            tax.setVisibility(View.GONE);
                                        }

                                    } else {
                                        tax.setVisibility(View.GONE);

                                    }


                                    if (!d.equals("") && !regular_p.equals("")) {

                                        tv_save.setVisibility(View.VISIBLE);
                                        tv_percentage.setVisibility(View.VISIBLE);
                                        double amount = Double.parseDouble(d);
                                        double regular = Double.parseDouble(regular_p);
                                        long res = (long) Math.round((1 - regular / amount) * 100);
                                        int i = (int) res;

                                        tv_percentage.setText(i + "% OFF");
                                        int save = (int) (amount - regular);

                                        tv_save.setText("SAVE UPTO ₹" + save);

                                    } else {

                                        tv_save.setVisibility(View.GONE);
                                        tv_percentage.setVisibility(View.GONE);
                                    }

                                    //  Toast.makeText(Product_Details.this, "" + price, Toast.LENGTH_SHORT).show();

                                }
                                setsize();
                            }


                        } else {

                            ll_size_selected.setVisibility(View.GONE);

                            progressDialog.dismiss();
                            Toast.makeText(Product_Details.this, "" + message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "eeeeeeeee" + e, Toast.LENGTH_LONG).show();

                    }

                } else if (response.code() == 401) {
                    progressDialog.dismiss();

                    no_connection("Something  went wrong at server. Please Try again later");
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    progressDialog.dismiss();

                    no_connection("Something  went wrong at server. Please Try again later");

                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {

                    progressDialog.dismiss();
                    no_connection("An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later");


                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable error) {
                if (error instanceof SocketTimeoutException) {
                    // "Connection Timeout";
                    progressDialog.dismiss();

                    no_connection("Slow internet! Please retry");


                    // Toast.makeText(Product_Details.this, "Connection timeout"+error, Toast.LENGTH_SHORT).show();
                } else if (error instanceof IOException) {
                    no_connection("Connection internet! Please retry");

                    progressDialog.dismiss();

                    // "Timeout";
                } else {
                    //Call was cancelled by user
                    if (call.isCanceled()) {
                        // Toast.makeText(Product_Details.this, "Call was cancelled forcefully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        System.out.println("Call was cancelled forcefully");
                    } else {
                        //Generic error handling
                        no_connection("Network error! Please retry");

                        // Toast.makeText(Product_Details.this, "Network Error :: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        System.out.println("Network Error :: " + error.getLocalizedMessage());

                    }
                }
            }
        });
    }

    private void set_spinner() {
        stock_count.clear();

        int c = product_min_order_qty * product_total_stock;

        for (int j = product_min_order_qty; j <= c; j += product_min_order_qty) {

            stock_count.add(String.valueOf(j));
        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(Product_Details.this, android.R.layout.simple_spinner_item, stock_count);

        // Drop down layout style - list view with radio button
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter4);
        int spinnerPosition = 0;
        spinner.setSelection(spinnerPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int position, long id) {
                Log.v("SpinnerSelected Item",
                        "" + spinner.getSelectedItem());
                Log.v("Clicked position", "" + position);
                qty = "" + spinner.getSelectedItem();
                Log.e("TAG", "onItemSelected: " + qty);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Log.v("NothingSelected Item",
                        "" + spinner.getSelectedItem());
            }
        });
    }


    public void cartdetails(final String user_login_token, String user_id) {


        progressDialog = new ProgressDialog(Product_Details.this);
        progressDialog.setMessage("Please Wait..");
        // progressDialog.setCancelable(false);
        progressDialog.show();
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

                        String result = response.body().string();

                        Log.i("resultca", "" + result);
                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");


                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (success.equals("true")) {
                                message = obj.optString("message");

                                // Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                if (obj.has("data")) {
                                    JSONObject data_obj = obj.optJSONObject("data");


                                    if (data_obj.has("total_price")) {

                                        total.setText("₹ " + data_obj.getString("total_price"));
                                        Total_amt = data_obj.getString("total_price");

                                    } else {
                                        total.setText("₹ 0");

                                    }
                                    if (data_obj.has("product")) {

                                        JSONArray arr_product = data_obj.optJSONArray("product");
                                        if (arr_product.length() > 0) {

                                            Log.d("resp", "" + arr_product);

                                            count.setText("" + arr_product.length());


                                            for (int i = 0; i < arr_product.length(); i++) {
                                                JSONObject jsonObject_prod = arr_product.optJSONObject(i);
                                                //////////////    Adding to Datamodel   ////////////////

                                                Model_Cart model_cart = new Model_Cart();
                                                if (!jsonObject_prod.isNull("product_id")) {
                                                    model_cart.setProductid(jsonObject_prod.optString("product_id"));

                                                } else {
                                                    model_cart.setProductid("");
                                                }


                                            }

                                        } else {

                                            count.setText("0");

                                        }

                                    }
                                }


                            }


                        } else {
                            progressDialog.dismiss();
                            String err_message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "" + err_message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
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


    public void additem(final String user_login_token, String user_id, final String product_id, String quantity, String variant_id) {
        showloader();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.addtocart(user_login_token, user_id, product_id, quantity, variant_id);
        //Toast.makeText(this, ""+property_id, Toast.LENGTH_SHORT).show();

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

                            hideloader();
                            // Toast.makeText(Product_Details.this, ""+success, Toast.LENGTH_SHORT).show();
                            Log.d("cartitem", "onResponse: " + message);


                            dialog = new Dialog(Product_Details.this);
                            dialog.setContentView(R.layout.dialog_success);
                            dialog.show();
                            dialog.setCancelable(false);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            TextView tv_dial_subtitle = (TextView) dialog.findViewById(R.id.tv_dial_subtitle);
                            tv_dial_subtitle.setText("Order added to cart");
                            TextView tv_dial_desc = (TextView) dialog.findViewById(R.id.tv_dial_desc);
                            tv_dial_desc.setText("Please check your order in cart");
                            Button btn = (Button) dialog.findViewById(R.id.btn);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    et_cart_count.setText("0");
                                    qty = "";
                                    spinner.setSelection(0);

                                    cartdetails("" + Token, "" + user);

                                }
                            });


                        } else {
                            hideloader();
                            Toast.makeText(Product_Details.this, "" + message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        hideloader();

                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {
                    hideloader();


                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {

                    hideloader();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();

                hideloader();

            }
        });
    }


    public void


    removefavourite(final String user_id, String user_login_token, String product_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Wishlist.Service service = retrofit.create(Wishlist.Service.class);
        Call<ResponseBody> call;
        call = service.removefavourite(user_id, user_login_token, product_id);
        //     Toast.makeText(this, ""+product_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {

                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);

                        Log.d("respp", "onResponse: " + result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");
                        //  Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                        //  Toast.makeText(mContext, ""+e, Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {


                    // Toast.makeText(mContext, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    //  Toast.makeText(mContext, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Toast.makeText(mContext, "Something went wrong at server!", Toast.LENGTH_SHORT).show();


            }
        });
    }

    public void addfavourite(final String user_id, String user_login_token, String product_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.addfavourite(user_id, user_login_token, product_id);
        //     Toast.makeText(this, ""+product_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {

                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);

                        Log.d("respp", "onResponse: " + result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");
                        // Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                        //  Toast.makeText(mContext, ""+e, Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {


                    // Toast.makeText(mContext, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    //  Toast.makeText(mContext, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Toast.makeText(mContext, "Something went wrong at server!", Toast.LENGTH_SHORT).show();


            }
        });
    }


    public void showloader() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_to_cart);
        // dialog.getWindow().setBackgroundDrawableResource(R.drawable.back);
        dialog.show();


    }

    public void hideloader() {
        dialog.hide();
    }


    public void setsize() {
        adapter_selct_size = new Adapter_selct_size(Product_Details.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Product_Details.this, LinearLayoutManager.HORIZONTAL, false);
        rcv_selections.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) rcv_selections.getItemAnimator()).setSupportsChangeAnimations(false);
        rcv_selections.setAdapter(adapter_selct_size);


        rcv_selections.setAdapter(adapter_selct_size);// set adapter on recyclerview
        rcv_selections.scrollToPosition(ind); //use to focus the item with index
        adapter_selct_size.notifyDataSetChanged();
    }


    void yess() {

        productdetails(product_id, "" + Token, "" + user);

    }


    void no_connection(String message) {
        no_connectio.setVisibility(View.VISIBLE);

        tv_text.setText(message);

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productdetails(product_id, "" + Token, "" + user);
                cartdetails("" + Token, "" + user);

            }
        });

    }


    @SuppressLint("WrongConstant")
    private void setData() {
        this.gallery_adapter = new Gallery_adapter(Product_Details.this);
        this.recycler_view_banner.setLayoutManager(new LinearLayoutManager(Product_Details.this, 0, false));
        this.recycler_view_banner.setAdapter(this.gallery_adapter);
        this.gallery_adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {

        selectedsize = "";
        product_id = "";

        finish();
        super.onBackPressed();
    }


    private void timerOnViewPager() {
        ArrayList arrayList = al_banners;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            private static final int NUM_PAGES = 5;

            public void run() {

                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 0;
                }
                binding.viewPager.setCurrentItem(currentPage++, false);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


//            images.get(i).getImagePath();
        binding.viewPager.setAdapter(new ViewpagerAdapter(Product_Details.this, al_banners));
        binding.indicator.setViewPager(binding.viewPager);
        final float density = Product_Details.this.getResources().getDisplayMetrics().density;
        binding.indicator.setRadius(5 * density);
        NUM_Page = al_banners.size();
//        new Handler().
//                postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (currentPage == NUM_Page) {
//                            currentPage = 0;
//                        }
//                        binding.viewPager.setCurrentItem(currentPage++, true);
//                    }
//                }, 2000);

        binding.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }


    public interface Service {
        @Headers("Authkey: APPCBSBDMPL")
        @FormUrlEncoded
        @POST("productdetails")
        Call<ResponseBody> productdetails(@Field("product_id") String product_id,
                                          @Field("user_login_token") String user_login_token,
                                          @Field("user_id") String user_id);

        @Headers("Authkey:APPCBSBDMPL")
        @POST("addfavourite")
        @FormUrlEncoded
        Call<ResponseBody> addfavourite(
                @Field("user_id") String user_id,
                @Field("user_login_token") String user_login_token,
                @Field("product_id") String product_id);

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

