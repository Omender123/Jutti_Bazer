package com.jutti.bazaar.shop.shoe.shopping.Product_list;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.jutti.bazaar.shop.shoe.shopping.Activities.Brand_list;
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

public class Product_list extends AppCompatActivity {
    LinearLayout filter, sorting, ll_progress, llyesproduct;
    GridLayoutManager mGridLayoutManager;
    Dialog dialog_category;
    public static ArrayList<String> brandIDList = new ArrayList<>();
    public static ArrayList<String> categoryIDList = new ArrayList<>();
    public static ArrayList<String> categoryNameList = new ArrayList<>();
    public static ArrayList<String> brandNameList = new ArrayList<>();
    public static ArrayList<String> brand_selected_IDList = new ArrayList<>();
    public static ArrayList<String> category_selected_IDList = new ArrayList<>();
    public static ArrayList<String> chcklst_Selections = new ArrayList<>();
    public static ArrayList<String> chcklst_Selections2 = new ArrayList<>();
    public static ArrayList<String> chcklst_id2 = new ArrayList<>();
    public static ArrayList<String> chcklst_id = new ArrayList<>();
    String final_brand_selections = "", final_category_selections = "", imageurlprod = "";
    Button apply, clear;
    View product_list, product_filter;
    RecyclerView recycle_result, recycle_resultcategory, recyclerproducts;
    RadioGroup radioSleected;
    RadioButton cb_brands, cb_category;
    String imageurlbrand = "";
    public static String selectedFiled = "brand", title = "";
    Adapter_filtered_list adapter_filtered_list;
    Adapter_filtered_category_list adapter_filtered_category_list;
    ImageView back, nores, iv_cancel_list;
    TextView producttitle;
    SharedPreferences sharedPreferences;
    GridLayoutManager gridLayoutManager;
    private ArrayList<Model_product_list> prodlist = new ArrayList<Model_product_list>();
    String from = "";
    Adapter_products_list adapter_products_list;
    ProgressDialog progressDialog;
    int scrolling_page = 0, PAGE_SIZE = 10;
    private boolean is_scrollig = false;

    boolean isLastPage = false, isFirst = false;
    boolean isLoading = false;
    public static String price_sorting = "", brandID = "", seller_id = "", categoryID = "", searchID = "", search_gender = "", selections_routine_checklist = "", selcted_category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.getString("user_login_token", "");

        //  Toast.makeText(this, "brand"+brandID+"cat"+categoryID, Toast.LENGTH_SHORT).show();


        filter = findViewById(R.id.filter);
        producttitle = findViewById(R.id.producttitle);
        back = findViewById(R.id.iv_back_list);
        apply = findViewById(R.id.btnapply);
        clear = findViewById(R.id.btn_clear);
        llyesproduct = findViewById(R.id.ll_yes_product);
        ll_progress = findViewById(R.id.ll_progressproduct);
        nores = findViewById(R.id.nores);
        sorting = findViewById(R.id.sorting);
        iv_cancel_list = findViewById(R.id.iv_cancel_list);
        product_list = (View) findViewById(R.id.listt);
        product_filter = (View) findViewById(R.id.filterr);
        recycle_result = (RecyclerView) findViewById(R.id.recycle_result);
        recycle_resultcategory = (RecyclerView) findViewById(R.id.recycle_resultcategory);
        radioSleected = (RadioGroup) findViewById(R.id.radioSleected);
        cb_brands = (RadioButton) findViewById(R.id.cb_brands);
        cb_category = (RadioButton) findViewById(R.id.cb_category);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selections_routine_checklist = "";
                selcted_category = "";


                Product_list.brandID = "";
                Product_list.categoryID = "";
                Product_list.searchID = "";
                chcklst_Selections2.clear();
                chcklst_Selections.clear();
                brandlist("");
                categorylist("");

                Toast.makeText(Product_list.this, "Filter Cleared", Toast.LENGTH_SHORT).show();
                if (Product_list.this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    scrolling_page = 0;
                }


                scrolling_page += 1;
                isFirst = false;

                hit();
            }
        });

        iv_cancel_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                product_list.setVisibility(View.VISIBLE);
                product_filter.setVisibility(View.GONE);
                brandlist("clear");
                categorylist("clear");

                selections_routine_checklist = "";
                selcted_category = "";
                chcklst_Selections.clear();
                chcklst_Selections2.clear();


            }
        });
        sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_sort();

            }
        });

        radioSleected.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.cb_brands) {

                    selectedFiled = "brand";
                    recycle_result.setVisibility(View.VISIBLE);
                    recycle_resultcategory.setVisibility(View.GONE);
                } else if (i == R.id.cb_category) {

                    selectedFiled = "category";
                    recycle_resultcategory.setVisibility(View.VISIBLE);
                    recycle_result.setVisibility(View.GONE);
                }
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.i("comm_final_brands", "" + selections_routine_checklist + "---- final category " + selcted_category);

                brandID = selections_routine_checklist;
                categoryID = selcted_category;
                searchID = "";


                product_list.setVisibility(View.VISIBLE);
                product_filter.setVisibility(View.GONE);
//                scrolling_page = 0;
//                scrolling_page += 1;
//                isFirst = true;
//                prodlist.clear();
                if (Product_list.this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    scrolling_page = 0;
                }


                scrolling_page += 1;
                isFirst = false;
                hit();
            }
        });


        recyclerproducts = (RecyclerView) findViewById(R.id.recycler_Products);
        adapter_products_list = new Adapter_products_list(this, prodlist);

        if (title.contains("")) {
            producttitle.setText("Product List");
        } else {
            producttitle.setText(title);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                brandlist("");


                nores.setVisibility(View.GONE);

                from = "yes";

                product_filter.setVisibility(View.VISIBLE);
                product_list.setVisibility(View.GONE);
            }
        });


        if (!brandID.equals("")) {
            chcklst_Selections.add(brandID);

        }
        if (!categoryID.equals("")) {
            chcklst_Selections2.add(categoryID);

        }


        if (Product_list.this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            scrolling_page = 0;
        }

        scrolling_page += 1;
        isFirst = false;
        hit();

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


        isLoading = true;

        if (page.equals("1")) {
            isFirst = true;
        } else {
            isFirst = false;
        }


        if (page.equals("1")) {

        } else {
            Snackbar.make(recyclerproducts, "Loading More.. Please Wait", BaseTransientBottomBar.LENGTH_LONG).show();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.productsearch(page, brand_id, category_id, tag_search, price_sorting, seller_items, filter_gender);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoading = false;

                if (response.isSuccessful()) {

                    try {

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

                                        if (page.equals("1")) {
                                            prodlist.clear();
                                        }
                                        if (obj.optString("currentpage").equals(obj.optString("totalpage"))) {
                                            isLastPage = true;
                                        } else {
                                            isLastPage = false;

                                        }


                                        Log.d("responseee", "" + arr_order);

                                        nores.setVisibility(View.GONE);

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

                                            } if (!jsonObject.isNull("category_name")) {
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

                                            }  if (!jsonObject.isNull("variant_selling_price")) {
                                                droppointListModel.setVariant_selling_price(jsonObject.getString("variant_selling_price"));


                                            } else {
                                                droppointListModel.setVariant_selling_price("");

                                            }

                                            prodlist.add(droppointListModel);
                                        }
                                        if (scrolling_page == 1) {
                                            show_yesdata();
                                            setup_cart_items();
                                        } else {

                                            adapter_products_list.notifyDataSetChanged();

                                        }


                                    } else {
                                        scrolling_page = 0;
                                        isFirst = true;
                                        nores.setVisibility(View.VISIBLE);
                                        // Toast.makeText(Product_list.this, "No Result", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(Product_list.this, "No Result", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(Product_list.this, "No Result", Toast.LENGTH_SHORT).show();
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


    public void brandlist(final String valueFrom) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(" Please wait...");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();

        Brand_list.Service service = retrofit.create(Brand_list.Service.class);

        Call<ResponseBody> call;
        call = service.brandlist();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // isLoading = false;
                if (response.isSuccessful()) {
                    try {

                        String result = response.body().string();

                        brandIDList.clear();
                        brandNameList.clear();
                        brand_selected_IDList.clear();

                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        Log.i("result_response_obj", "" + obj);

                        if (obj.has("image_base_path")) {
                            imageurlbrand = obj.getString("image_base_path");
                            Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                        }

                        if (obj.has("error")) {
//                            vibrate_alert(500);

                            String message = obj.getString("error_message");
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);

                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {

                            if (valueFrom.equals("clear")) {
                                // Toast.makeText(Product_list.this, "Your Selection Has Been Cleared", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                            categorylist("");
                            JSONArray arr_response = obj.getJSONArray("data");

                            Log.i("arr_response", "arr_response  " + arr_response);

                            if (arr_response.length() > 0) {
                                //   nearbyPropertiesModelArrayList.clear();
                                for (int i = 0; i < arr_response.length(); i++) {
                                    JSONObject jsonObject = arr_response.optJSONObject(i);
                                    //////////////    Adding to Datamodel   ////////////////

                                    brand_selected_IDList.add("");

                                    if (!jsonObject.isNull("brand_id")) {
                                        brandIDList.add(jsonObject.getString("brand_id"));
                                    } else {
                                        brandIDList.add("");
                                    }

                                    if (!jsonObject.isNull("brand_name")) {
                                        brandNameList.add(jsonObject.getString("brand_name"));
                                        Log.i("brand_name: ", (jsonObject.getString("brand_name")));
                                    } else {
                                        brandNameList.add("");
                                    }
                                }
                                progressDialog.dismiss();
                                setup_brands();

                            } else {
                                progressDialog.dismiss();
//                                Toast.makeText(Brand_list.this, "Aw Snap! No Data Available for this Catalogue", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();

                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Log.i("arr_response", "response.code  " + response.code());
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
                Log.d("t", "Something went wrong at server!" + t);

                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void categorylist(final String valueFrom) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();

        Brand_list.Service service = retrofit.create(Brand_list.Service.class);

        Call<ResponseBody> call;
        call = service.categorylist();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // isLoading = false;
                if (response.isSuccessful()) {

                    try {

                        String result = response.body().string();

                        categoryIDList.clear();
                        categoryNameList.clear();
                        category_selected_IDList.clear();

                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        Log.i("result_response_obj", "" + obj);

                        if (obj.has("image_base_path")) {
                            imageurlbrand = obj.getString("image_base_path");
                            Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                        }

                        if (obj.has("error")) {
//                            vibrate_alert(500);

                            String message = obj.getString("error_message");
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);

                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {

                            if (valueFrom.equals("clear")) {
                                // Toast.makeText(Product_list.this, "Your Selection Has Been Cleared", Toast.LENGTH_SHORT).show();
                            } else {

                            }

                            JSONArray arr_response = obj.getJSONArray("data");

                            Log.i("arr_response", "arr_response  " + arr_response);

                            if (arr_response.length() > 0) {
                                //   nearbyPropertiesModelArrayList.clear();
                                for (int i = 0; i < arr_response.length(); i++) {
                                    JSONObject jsonObject = arr_response.optJSONObject(i);
                                    //////////////    Adding to Datamodel   ////////////////


                                    category_selected_IDList.add("");
                                    if (!jsonObject.isNull("category_id")) {
                                        categoryIDList.add(jsonObject.getString("category_id"));
                                    } else {
                                        categoryIDList.add("");
                                    }
                                    if (!jsonObject.isNull("category_name")) {
                                        categoryNameList.add(jsonObject.getString("category_name"));
                                    } else {
                                        categoryNameList.add("");
                                    }

                                }

                                setup_category();

                            } else {
//                                Toast.makeText(Brand_list.this, "Aw Snap! No Data Available for this Catalogue", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {


                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {


                    Log.i("arr_response", "response.code  " + response.code());
                    Toast.makeText(getApplicationContext(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(getApplicationContext(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                Log.d("t", "Something went wrong at server!" + t);

                Toast.makeText(getApplicationContext(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    void setup_cart_items() {
        mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerproducts.setLayoutManager(mGridLayoutManager);
        adapter_products_list = new Adapter_products_list(Product_list.this, prodlist);
        recyclerproducts.setAdapter(adapter_products_list);
//        gridViewAdapter.notifyDataSetChanged();

        recyclerproducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = mGridLayoutManager.getChildCount();
                int totalItemCount = mGridLayoutManager.getItemCount();
                int firstVisibleItemPosition = mGridLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        if (!Utils.isNetworkConnected(getApplicationContext())) {
                            Toast.makeText(Product_list.this, "No Internet, Reconnect and Retry", Toast.LENGTH_SHORT).show();

                        } else {
                            scrolling_page += 1;
                            isFirst = false;
                            hit();
                            Log.d("Scroll", "onScrollStateChanged: " + scrolling_page);


                        }
                    }
                }


            }


        });
    }


    public void setup_brands() {

        adapter_filtered_list = new Adapter_filtered_list(this);
        recycle_result.setLayoutManager(new GridLayoutManager(this, 1));
        recycle_result.setAdapter(adapter_filtered_list);

        adapter_filtered_list.notifyDataSetChanged();

    }

    public void setup_category() {

        adapter_filtered_category_list = new Adapter_filtered_category_list(this);
        recycle_resultcategory.setLayoutManager(new GridLayoutManager(this, 1));
        recycle_resultcategory.setAdapter(adapter_filtered_category_list);

        adapter_filtered_category_list.notifyDataSetChanged();

    }


    public void show_progress() {
        ShimmerFrameLayout container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view);


        container.startShimmer();

        ll_progress.setVisibility(View.VISIBLE);
        llyesproduct.setVisibility(View.GONE);
        //  ll_noresult.setVisibility(View.GONE);

    }


    public void show_yesdata() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        ll_progress.setVisibility(View.GONE);

                        llyesproduct.setVisibility(View.VISIBLE);


                        // ll_noresult.setVisibility(View.GONE);

                    }
                }, 0);

    }


    public void dialog_sort() {
        dialog_category = new Dialog(this);
        dialog_category.setContentView(R.layout.dialog_sort);
        dialog_category.show();
        dialog_category.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        TextView button_select = (TextView) dialog_category.findViewById(R.id.button_select);
        final CheckBox ltoh = (CheckBox) dialog_category.findViewById(R.id.ltoh);
        final CheckBox htol = (CheckBox) dialog_category.findViewById(R.id.htol);

        LinearLayout ll_select = (LinearLayout) dialog_category.findViewById(R.id.ll_select);


        if (price_sorting.equals("low_to_high")) {
            ltoh.setChecked(true);
        } else if (price_sorting.equals("high_to_low")) {
            htol.setChecked(true);
        }

        ltoh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (ltoh.isChecked()) {
                    htol.setChecked(false);

                    price_sorting = "low_to_high";
                } else {
                    price_sorting = "";
                }


            }
        });
        htol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (htol.isChecked()) {

                    ltoh.setChecked(false);
                    price_sorting = "high_to_low";

                } else {
                    price_sorting = "";
                }

            }
        });


        ll_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Product_list.this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    scrolling_page = 0;
                }
                scrolling_page += 1;
                isFirst = false;
                hit();
                dialog_category.dismiss();
            }

        });


    }

    void hit() {

        productsearch("" + scrolling_page, brandID, categoryID, searchID, "" + price_sorting, "" + seller_id, "" + search_gender);

    }


    public interface Service {
        @Headers("Authkey:APPCBSBDMPL")
        @POST("productsearch")
        @FormUrlEncoded
        Call<ResponseBody> productsearch(@Field("page") String page,
                                         @Field("brand_id") String brand_id,
                                         @Field("category_id") String category_id,
                                         @Field("tag_search") String tag_search,
                                         @Field("price_sorting") String price_sorting,
                                         @Field("seller_items") String seller_items,
                                         @Field("filter_gender") String filter_gender);

    }

    @Override
    public void onBackPressed() {

        brandID = "";
        categoryID = "";
        searchID = "";
        price_sorting = "";
        seller_id = "";
        title = "";
        search_gender = "";
        chcklst_Selections.clear();
        chcklst_Selections2.clear();
        finish();
        super.onBackPressed();

    }


}
