package com.jutti.bazaar.shop.shoe.shopping.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_Brand_list;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_Sub_category_list;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_category_list;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_seller_brand;
import com.jutti.bazaar.shop.shoe.shopping.Fragment.Homefrag;
import com.jutti.bazaar.shop.shoe.shopping.Model.ModelBrand;
import com.jutti.bazaar.shop.shoe.shopping.Model.ModelSubCategory;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Brand;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Category;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
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
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class Brand_list extends AppCompatActivity {

    String imageurlbrand = "";
    Adapter_Brand_list adapter_brand;
    Adapter_category_list adapter_categoryList;
    Adapter_seller_brand adapter_notification;
    GridLayoutManager mGridLayoutManager;
    TextView category_name_list, seealll;
    String imageurlcat = "";
    ImageView back_from_list;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recycler;
    public static String tit = "", see = "", image_path = "", search = "", state = "";
    int scrolling_page = 0, PAGE_SIZE = 10;
    private boolean is_scrollig = false;

    boolean isLastPage = false, isFirst = false;
    boolean isLoading = false;
    public static ArrayList<Model_Brand> Model_BrandArrayList = new ArrayList<>();
    public static ArrayList<Model_Category> Model_CategoryArrayList = new ArrayList<>();
    public static ArrayList<ModelSubCategory> modelSubCategories = new ArrayList<>();


    public static ArrayList<String> seller_id = new ArrayList();
    public static ArrayList<String> seller_photo = new ArrayList();
    public static ArrayList<String> seller_name = new ArrayList();
    public static ArrayList<String> seller_popularity = new ArrayList();
    public static ArrayList<String> seller_add = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_list);

        category_name_list = findViewById(R.id.category_name_list);
        seealll = findViewById(R.id.seealll);
        back_from_list = findViewById(R.id.back_from_list);

        category_name_list.setText(tit);
//        state= SavedPrefManager.getStringPreferences(Brand_list.this, AppConstant.FROM);
        Log.e("TAG", "onCreate: " + state);
        recycler = findViewById(R.id.recycler);
        adapter_brand = new Adapter_Brand_list(this);
        adapter_categoryList = new Adapter_category_list(this);
        // linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapter_notification = new Adapter_seller_brand(this);


        if (see.contains("yes")) {

            seealll.setVisibility(View.GONE);
        }
        seealll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Product_list.class);
                intent.putExtra("title2", "Category List");  // pass your values and retrieve them in the other Activity using keyName
                startActivity(intent);
            }
        });

        Model_BrandArrayList.clear();
        Model_CategoryArrayList.clear();

        back_from_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        if (Homefrag.role.contains("1")) {
            brandlist();

        } else if (Homefrag.role.contains("cat")) {
            categorylist();
            //  Toast.makeText(this, "category", Toast.LENGTH_SHORT).show();
        } else if (Homefrag.role.contains("afterproduct")) {

            brandlist();

        } else if (Homefrag.role.contains("shop")) {
            scrolling_page += 1;
            isFirst = true;
            shopbyseller("" + scrolling_page, "" + state, "" + search);
        } else {
            category_name_list.setText(Homefrag.role.toUpperCase() + " CATEGORIES");
            subcategories("" + Homefrag.role);
        }
    }


    public void brandlist() {

        final ProgressDialog progressDialog = new ProgressDialog(Brand_list.this);
        progressDialog.setMessage(" Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.brandlist();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // isLoading = false;
                if (response.isSuccessful()) {
                    try {
                        Model_BrandArrayList.clear();
                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        Log.i("result", "" + obj);

                        if (obj.has("image_base_path")) {

                            imageurlbrand = obj.getString("image_base_path");
                            Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                        }


                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            progressDialog.dismiss();
                            String message = obj.getString("error_message");
                            Toast.makeText(Brand_list.this, "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            progressDialog.dismiss();
                            String message = obj.getString("message");
                            Toast.makeText(Brand_list.this, "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {
                            progressDialog.dismiss();

                            JSONArray arr_response = obj.getJSONArray("data");

                            Log.i("arr_response", "arr_response  " + arr_response);

                            if (arr_response.length() > 0) {
                                //   nearbyPropertiesModelArrayList.clear();
                                for (int i = 0; i < arr_response.length(); i++) {
                                    JSONObject jsonObject = arr_response.optJSONObject(i);
                                    Model_Brand droppointListModel = new Model_Brand();
                                    //////////////    Adding to Datamodel   ////////////////


                                    if (!jsonObject.isNull("brand_id")) {
                                        droppointListModel.setbrand_id(jsonObject.getString("brand_id"));
                                        Log.i("brand: ", (jsonObject.getString("brand_id")));
                                    } else {
                                        droppointListModel.setbrand_id("");
                                    }
                                    if (!jsonObject.isNull("brand_name")) {
                                        droppointListModel.setbrand_name(jsonObject.getString("brand_name"));
                                        Log.i("brand_name: ", (jsonObject.getString("brand_name")));

                                    } else {
                                        droppointListModel.setbrand_name("");
                                    }
                                    if (!jsonObject.isNull("brand_image")) {
                                        droppointListModel.setbrand_image(imageurlbrand + jsonObject.getString("brand_image"));
                                        Log.i("brand_image: ", (jsonObject.getString("brand_image")));

                                    } else {
                                        droppointListModel.setbrand_image("");
                                    }

                                    Model_BrandArrayList.add(droppointListModel);
                                }


                                adapter_brand.notifyDataSetChanged();
                                progressDialog.dismiss();

                                setup_brands();

                            } else {
//                                Toast.makeText(Brand_list.this, "Aw Snap! No Data Available for this Catalogue", Toast.LENGTH_LONG).show();


                            }
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(Brand_list.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Log.i("arr_response", "response.code  " + response.code());
                    Toast.makeText(Brand_list.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(Brand_list.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

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

                Toast.makeText(Brand_list.this, "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void categorylist() {

        final ProgressDialog progressDialog = new ProgressDialog(Brand_list.this);
        progressDialog.setMessage(" Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.categorylist();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // isLoading = false;
                if (response.isSuccessful()) {
                    try {
                        Model_CategoryArrayList.clear();
                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        Log.i("result", "" + obj);
                        progressDialog.dismiss();
                        if (obj.has("image_base_path")) {
                            imageurlcat = obj.getString("image_base_path");
                            Log.i("imageurl", "" + obj.getString("image_base_path"));
                        }

                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            Toast.makeText(Brand_list.this, "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(Brand_list.this, "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {

                            JSONArray arr_response = obj.getJSONArray("data");

                            Log.i("arr_response", "arr_response  " + arr_response);
                            //   Log.i("page", page+"");
                            Log.i("total_pages", obj.optString("total_pages") + "");

                            if (arr_response.length() > 0) {
                                //   nearbyPropertiesModelArrayList.clear();
                                for (int i = 0; i < arr_response.length(); i++) {
                                    JSONObject jsonObject = arr_response.optJSONObject(i);
                                    Model_Category droppointListModel = new Model_Category();
                                    //////////////    Adding to Datamodel   ////////////////


                                    if (!jsonObject.isNull("category_id")) {
                                        droppointListModel.setcategory_id(jsonObject.getString("category_id"));

                                    } else {
                                        droppointListModel.setcategory_id("");
                                    }
                                    if (!jsonObject.isNull("category_name")) {
                                        droppointListModel.setcategory_name(jsonObject.getString("category_name"));

                                    } else {
                                        droppointListModel.setcategory_name("");
                                    }
                                    if (!jsonObject.isNull("category_image")) {
                                        droppointListModel.setcategory_image(imageurlcat + jsonObject.getString("category_image"));

                                    } else {
                                        droppointListModel.setcategory_image("");
                                    }
                                    if (!jsonObject.isNull("parent_name")) {
                                        droppointListModel.setParent_name(jsonObject.getString("parent_name"));

                                    } else {
                                        droppointListModel.setParent_name("");
                                    }
                                    Model_CategoryArrayList.add(droppointListModel);
                                }


                                setup_category();

                                adapter_categoryList.notifyDataSetChanged();


                            } else {
//                                Toast.makeText(getActivity(), "Aw Snap! No Data Available for this Catalogue", Toast.LENGTH_LONG).show();


                            }
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(Brand_list.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Log.i("arr_response", "response.code  " + response.code());
                    Toast.makeText(Brand_list.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(Brand_list.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

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

                Toast.makeText(Brand_list.this, "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void subcategories(String cat) {

        final ProgressDialog progressDialog = new ProgressDialog(Brand_list.this);
        progressDialog.setMessage(" Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url2)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.subcategories("" + cat);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // isLoading = false;
                if (response.isSuccessful()) {
                    try {
                        modelSubCategories.clear();
                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        Log.i("result", "" + obj);
                        progressDialog.dismiss();
                        if (obj.has("image_base_path")) {
                            imageurlcat = obj.getString("image_base_path");
                            Log.i("imageurl", "" + obj.getString("image_base_path"));
                        }

                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            Toast.makeText(Brand_list.this, "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(Brand_list.this, "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {

                            JSONArray arr_response = obj.getJSONArray("data");

                            Log.i("arr_response", "arr_response  " + arr_response);
                            //   Log.i("page", page+"");
                            Log.i("total_pages", obj.optString("total_pages") + "");

                            if (arr_response.length() > 0) {
                                //   nearbyPropertiesModelArrayList.clear();
                                for (int i = 0; i < arr_response.length(); i++) {
                                    JSONObject jsonObject = arr_response.optJSONObject(i);
                                    ModelSubCategory droppointListModel = new ModelSubCategory();
                                    //////////////    Adding to Datamodel   ////////////////


                                    if (!jsonObject.isNull("category_id")) {
                                        droppointListModel.setcategory_id(jsonObject.getString("category_id"));

                                    } else {
                                        droppointListModel.setcategory_id("");
                                    }
                                    if (!jsonObject.isNull("category_slug")) {
                                        droppointListModel.setCategory_slug(jsonObject.getString("category_slug"));

                                    } else {
                                        droppointListModel.setCategory_slug("");
                                    }
                                    if (!jsonObject.isNull("category_name")) {
                                        droppointListModel.setcategory_name(jsonObject.getString("category_name"));

                                    } else {
                                        droppointListModel.setcategory_name("");
                                    }
                                    if (!jsonObject.isNull("category_image")) {
                                        droppointListModel.setcategory_image(imageurlcat + jsonObject.getString("category_image"));

                                    } else {
                                        droppointListModel.setcategory_image("");
                                    }
                                    if (!jsonObject.isNull("parent_name")) {
                                        droppointListModel.setParent_name(jsonObject.getString("parent_name"));

                                    } else {
                                        droppointListModel.setParent_name("");
                                    }

                                    modelSubCategories.add(droppointListModel);
                                }


                                setup_subcategory();


                            } else {
                             Toast.makeText(Brand_list.this, "Aw Snap! No Data Available.", Toast.LENGTH_LONG).show();


                            }
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(Brand_list.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Log.i("arr_response", "response.code  " + response.code());
                    Toast.makeText(Brand_list.this, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    Toast.makeText(Brand_list.this, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

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

                Toast.makeText(Brand_list.this, "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("WrongConstant")
    public void shopbyseller(final String page, String state, String seller) {

        Log.d("resp", "shopbyseller: " + state);
        Log.d("resp", "shopbyseller page: " + page);
        Log.d("resp", "shopbyseller page: search" + search);

        isLoading = true;
        final ProgressDialog progressDialog = new ProgressDialog(Brand_list.this);

        if (page.equals("1")) {
            progressDialog.setMessage(" Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else {
            isFirst = false;
            Snackbar.make(recycler, "Loading More.. Please Wait", BaseTransientBottomBar.LENGTH_LONG).show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();

        Homefrag.Service service = retrofit.create(Homefrag.Service.class);
        Call<ResponseBody> call;
        call = service.shopbyseller(page,"" + state, "" + seller);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    try {

                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);
                        Log.i("result_seller", "" + obj);
                        if (obj.has("image_base_path")) {
                            String imageurlprod = obj.getString("image_base_path");
                            Log.i("imageurl", "" + obj.getString("image_base_path"));
                        }

                        if (page.equals("1")) {
                            seller_add.clear();
                            seller_name.clear();
                            seller_photo.clear();
                            seller_popularity.clear();
                            seller_id.clear();

                        }


                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            progressDialog.dismiss();

                            String message = obj.getString("error_message");
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
                            progressDialog.dismiss();

//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {

                            progressDialog.dismiss();
                            if (obj.has("image_base_path")) {

                                image_path = obj.getString("image_base_path");
                            }

                            JSONArray arr_response = obj.getJSONArray("data");

                            Log.i("arr_response", "arr_response  " + arr_response);
                            //   Log.i("page", page+"");
                            Log.i("total_pages", obj.optString("totalpage") + "");


                            if (arr_response.length() > 0) {

                                if (obj.optString("currentpage").equals(obj.optString("totalpage"))) {
                                    isLastPage = true;
                                } else {
                                    isLastPage = false;

                                }


                                //   nearbyPropertiesModelArrayList.clear();
                                for (int i = 0; i < arr_response.length(); i++) {
                                    JSONObject jsonObject = arr_response.optJSONObject(i);
                                    //////////////    Adding to Datamodel   ////////////////

                                    if (!jsonObject.isNull("user_id")) {
                                        seller_id.add(jsonObject.getString("user_id"));

                                    } else {
                                        seller_id.add("");
                                    }
                                    if (!jsonObject.isNull("name")) {
                                        seller_name.add(jsonObject.getString("name"));

                                    } else {
                                        seller_name.add("");
                                    }
                                    if (!jsonObject.isNull("sellerLogo")) {
                                        seller_photo.add(image_path + jsonObject.getString("sellerLogo"));

                                    } else {
                                        seller_photo.add("");
                                    }
                                    if (!jsonObject.isNull("location")) {
                                        seller_add.add(jsonObject.getString("location"));

                                    } else {
                                        seller_add.add("");
                                    }

                                    if (!jsonObject.isNull("popularity")) {
                                        seller_popularity.add(jsonObject.getString("popularity"));

                                    } else {
                                        seller_popularity.add("");
                                    }


                                }

                                Log.d("resp", "onResponse: " + seller_id);

                                setup_address();

                                adapter_notification.notifyDataSetChanged();

                            } else {
                                Toast.makeText(Brand_list.this, "No Result", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();

                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 404) {

                    progressDialog.dismiss();

                    Log.i("arr_response", "response.code  " + response.code());
                } else {

                    progressDialog.dismiss();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();


                Log.d("t", "Something went wrong at server!" + t);

            }
        });
    }


    public void setup_brands() {


        // recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter_brand);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
    }


    public void setup_category() {
        // recycler.setLayoutManager();
        recycler.setAdapter(adapter_categoryList);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));


    }

    public void setup_subcategory() {
        // recycler.setLayoutManager();
        Adapter_Sub_category_list adapter_categoryList = new Adapter_Sub_category_list(this);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setAdapter(adapter_categoryList);


    }

    public void setup_address() {
        mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recycler.setLayoutManager(mGridLayoutManager);
        adapter_notification = new Adapter_seller_brand(Brand_list.this);
        recycler.setAdapter(adapter_notification);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            Toast.makeText(Brand_list.this, "No Internet, Reconnect and Retry", Toast.LENGTH_SHORT).show();

                        } else {
                            scrolling_page += 1;
                            isFirst = false;
                            shopbyseller("" + scrolling_page, "" + state, "" + search);
                            Log.d("Scroll", "onScrollStateChanged: " + scrolling_page);


                        }
                    }
                }


            }


        });
    }


    @Override
    public void onBackPressed() {
        Homefrag.role = "";
        Product_list.brandID = "";
        Product_list.categoryID = "";
        Product_list.searchID = "";
        Product_list.search_gender = "";
        finish();
        super.onBackPressed();
    }

    public interface Service {


        @Headers("Authkey:APPCBSBDMPL")
        @GET("brandlist")
        Call<ResponseBody> brandlist();

        @Headers("Authkey:APPCBSBDMPL")
        @GET("categorylist")
        Call<ResponseBody> categorylist();

        @Headers("Authkey:APPCBSBDMPL")
        @POST("subcategories")
        @FormUrlEncoded
        Call<ResponseBody> subcategories(@Field("parent_id") String parent_id);

    }


}
