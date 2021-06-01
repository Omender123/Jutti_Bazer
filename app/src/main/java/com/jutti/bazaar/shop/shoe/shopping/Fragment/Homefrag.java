package com.jutti.bazaar.shop.shoe.shopping.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.jutti.bazaar.shop.shoe.shopping.Activities.Brand_list;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_seller;
import com.jutti.bazaar.shop.shoe.shopping.Model.Banner_Model;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.SliderAdapterExample;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_Brand;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Brand;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_category;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Category;
import com.jutti.bazaar.shop.shoe.shopping.Adapters.Adapter_products;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Products;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;

import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

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

import static retrofit2.Retrofit.*;


public class Homefrag extends Fragment {
    /// no_connection
    View no_connectio;
    TextView tv_text, tv_try_later;
    Button btn_retry;
    ProgressBar pb;
    Adapter_seller adapter_cbs;
    RelativeLayout rl_all;
    LinearLayout men, women, kid, all, ll_seller, ll_search, ll_progress, ll_progress1, ll_progress2, ll_progress3, ll_noresult, ll_yes_result, llyesproduct, llyesbanner, llyescategory;
    ImageView brand1, img, iv_maha, iv_pun, iv_raja;
    View view_ad;
    TextView seeB, seeC, seeP, seeall_seller;
    private ViewPager viewPager;
    ShimmerFrameLayout container, container1, container2, container3;
    SliderView sliderView;
    View v;
    String imageurlcat = "", imageurlbrand = "", imageurlprod = "", imagecbs = "", imagebanner = "", image_path = "", YT_video = "", ph = "";
    public static String role = "";

    LinearLayoutManager linearLayoutManager, llc, llp, ll, lls;
    boolean isLoading = false;
    CardView cv_ad, cv_open_youtube, cv_call;

    ////adapters

    Adapter_Brand adapter_brand;
    Adapter_category adapter_category;
    Adapter_products adapter_products;
    SharedPreferences sharedPreferences;
    //// recyclers and array for listing in home screen
    RecyclerView recycler_cbs, recyclerbrand, recyclercategory, recyclerproducts, recyclerseller, recycler_view_banner;
    public static ArrayList<Model_Brand> Model_BrandArrayList = new ArrayList<>();
    public static ArrayList<Model_Category> Model_CategoryArrayList = new ArrayList<>();
    public static ArrayList<Model_Products> Model_ProductArrayList = new ArrayList<>();
    public static ArrayList<Banner_Model> Modelbanner = new ArrayList<>();
    public static ArrayList<String> al_banners = new ArrayList();
    public static ArrayList<String> tag = new ArrayList();
    public static ArrayList<String> banner_related_to = new ArrayList();
    public static ArrayList<String> banner_related_id = new ArrayList();
    public static ArrayList<String> id = new ArrayList();
    public static ArrayList<String> title = new ArrayList();
    public static ArrayList<String> photo = new ArrayList();
    public static ArrayList<Banner_Model> bannerlist = new ArrayList();
    public static ArrayList<String> seller_id = new ArrayList();
    public static ArrayList<String> seller_photo = new ArrayList();
    public static ArrayList<String> seller_name = new ArrayList();
    public static ArrayList<String> seller_popularity = new ArrayList();
    public static ArrayList<String> seller_add = new ArrayList();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_homefrag, container, false);
        init();
        Clicks();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        Banner();
//        SavedPrefManager.saveStringPreferences(getActivity(), AppConstant.FROM,"");
        return v;
    }

    private void Clicks() {

        iv_raja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop("rajasthan");
            }
        });
        iv_pun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop("punjab");

            }
        });
        iv_maha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop("maharashtra");

            }
        });
        rl_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop("");

            }
        });
    }

    void shop(String from) {
        Product_list.brandID = "";
        Product_list.categoryID = "";
        Product_list.searchID = "";
        Brand_list.tit = "Sellers list";
        Brand_list.see = "yes";
//        SavedPrefManager.saveStringPreferences(getActivity(), AppConstant.FROM,from);
        Brand_list.state = from;
        role = "shop";

        Intent intent = new Intent(getActivity(), Brand_list.class);
        startActivity(intent);
    }

    private void init() {

        ///no connction
        no_connectio = v.findViewById(R.id.no_connectio);
        tv_text = (TextView) v.findViewById(R.id.tv_text);
        tv_try_later = (TextView) v.findViewById(R.id.tv_try_later);
        btn_retry = (Button) v.findViewById(R.id.btn_retry);
        pb = v.findViewById(R.id.pb);
        recyclerseller = v.findViewById(R.id.recyclerseller);
        cv_ad = v.findViewById(R.id.cv_ad);
        img = v.findViewById(R.id.img);
        iv_raja = v.findViewById(R.id.iv_raja);
        iv_pun = v.findViewById(R.id.iv_pun);
        iv_maha = v.findViewById(R.id.iv_maha);
        rl_all = v.findViewById(R.id.rl_all);
        cv_open_youtube = v.findViewById(R.id.cv_open_youtube);

        cv_call = v.findViewById(R.id.cv_call);
        view_ad = v.findViewById(R.id.view_ad);
        seeB = v.findViewById(R.id.seeall_brands);
        seeC = v.findViewById(R.id.seeall_categories);
        seeP = v.findViewById(R.id.seeallproducts);
        seeall_seller = v.findViewById(R.id.seeall_seller);

        men = (LinearLayout) v.findViewById(R.id.men);
        women = (LinearLayout) v.findViewById(R.id.women);
        kid = (LinearLayout) v.findViewById(R.id.kid);
        all = (LinearLayout) v.findViewById(R.id.all);
        ll_progress = (LinearLayout) v.findViewById(R.id.ll_progress);
        ll_seller = (LinearLayout) v.findViewById(R.id.ll_seller);
        ll_yes_result = (LinearLayout) v.findViewById(R.id.ll_yes_result);
        ll_progress1 = (LinearLayout) v.findViewById(R.id.ll_progressproduct);
        llyesproduct = (LinearLayout) v.findViewById(R.id.ll_yes_product);
        ll_progress2 = (LinearLayout) v.findViewById(R.id.ll_progresscategory);
        llyescategory = (LinearLayout) v.findViewById(R.id.ll_yes_resultcategory);
        ll_progress3 = (LinearLayout) v.findViewById(R.id.ll_progress_banner);
        llyesbanner = (LinearLayout) v.findViewById(R.id.ll_yes_resultbanner);
        adapter_cbs = new Adapter_seller(getContext());

        recyclerbrand = v.findViewById(R.id.recycler_brand);

        RecyclerView.ItemAnimator animator = recyclerbrand.getItemAnimator();

        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        ll_search = v.findViewById(R.id.ll_search);
        recyclercategory = v.findViewById(R.id.recycler_category);
        recyclerproducts = v.findViewById(R.id.recycler_Products);
        recycler_view_banner = (RecyclerView) this.v.findViewById(R.id.recycler_view_banner1);
        sliderView = v.findViewById(R.id.imageSlider);
        adapter_brand = new Adapter_Brand(getContext());
        adapter_category = new Adapter_category(getContext());
        adapter_products = new Adapter_products(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        llc = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        llp = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        ll = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        lls = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        if (Utils.isNetworkConnected(getActivity())) {

            productsearch("", "19", "", "", "");

        } else {
            no_connectio.setVisibility(View.VISIBLE);

            tv_text.setText("No Internet Connection");

            btn_retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productsearch("", "19", "", "", "");

                }
            });

        }

        onclick();


    }

    private void onclick() {


        seeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product_list.brandID = "";
                Product_list.categoryID = "";
                Product_list.searchID = "";
                Brand_list.tit = "Brand List";
                role = "1";
                Intent intent = new Intent(getActivity(), Brand_list.class);
                startActivity(intent);

            }
        });

        seeC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product_list.brandID = "";
                Product_list.categoryID = "";
                Product_list.searchID = "";
                Brand_list.tit = "Category List";
                role = "cat";
                Intent intent = new Intent(getActivity(), Brand_list.class);
                startActivity(intent);

            }
        });
        seeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Product_list.brandID = "";
//                Product_list.categoryID = "";
//                Product_list.searchID = "";
//                Brand_list.tit = "Select Brand";
//                Brand_list.see = "yes";
                Intent intent = new Intent(getActivity(), Product_list.class);
                startActivity(intent);
//                role = "afterproduct";


            }
        });

        seeall_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product_list.brandID = "";
                Product_list.categoryID = "";
                Product_list.searchID = "";
                Brand_list.tit = "Sellers list";
                Brand_list.see = "yes";
                role = "shop";
                Intent intent = new Intent(getActivity(), Brand_list.class);
                startActivity(intent);


            }
        });
        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                role = "men";
                startActivity(new Intent(getActivity(), Brand_list.class));

            }
        });
        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                role = "women";
                startActivity(new Intent(getActivity(), Brand_list.class));

            }
        });
        kid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Product_list.search_gender = "kids";
                role = "kids";
                startActivity(new Intent(getActivity(), Brand_list.class));

            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                role="all";
                startActivity(new Intent(getActivity(), Brand_list.class));

            }
        });
        cv_open_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("" + YT_video));
                startActivity(browserIntent);

            }
        });
        cv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ph));
                startActivity(intent);

            }
        });


    }

    public void Banner() {

        show_banner();
        ((Service) new Builder().baseUrl(Utils.base_url)
                .build().create(Service.class)).
                bannerlist().enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONObject obj = new JSONObject(((ResponseBody) response.body()).string());
                        Log.i("Resp Banner: ", obj + "");
                        String success = obj.optString("success");
                        String message = obj.optString("message");
                        Homefrag.bannerlist.clear();
                        al_banners.clear();
                        tag.clear();
                        banner_related_to.clear();
                        banner_related_id.clear();

                        if (obj.has("image_base_path")) {
                            imagebanner = obj.getString("image_base_path");
                            Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                        }

                        if (obj.has("ad_banner")) {

                            if (obj.getString("ad_banner").equals("")) {
                                cv_ad.setVisibility(View.GONE);
                            } else {

                                Log.d("resp_ad", "onResponse: " + imagebanner + obj.getString("ad_banner"));

                                Glide.with(getActivity())
                                        .load("" + imagebanner + obj.getString("ad_banner"))


                                        .placeholder(R.drawable.placeholder)

                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(img);
                            }
                        }
                        if (obj.has("youtube_video")) {

                            if (obj.getString("youtube_video").equals("")) {
                                cv_open_youtube.setVisibility(View.GONE);
                            } else {

                                Log.d("resp_ad", "onResponse: " + imagebanner + obj.getString("youtube_video"));

                                YT_video = obj.getString("youtube_video");

                            }
                        }


                        if (success.equals("true")) {
                            JSONArray data = obj.getJSONArray("data");


                            if (data.length() > 0) {
                                //   rl_banner.setVisibility(View.VISIBLE);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    banner_related_to.add((object.getString("banner_related_to")));
                                    banner_related_id.add((object.getString("banner_related_id")));
                                    al_banners.add(imagebanner + object.getString("banner_image"));
                                    yes();
                                    yesbanner();

                                }
                                yes();
                                yesbanner();

                                shopbyseller("", "");


                            } else {
                                // rl_banner.setVisibility(View.GONE);
                            }
                        }

                    } catch (Exception e) {
                        show_banner();
                        Log.i("exception : ", e + "");
                        e.printStackTrace();
                        Context activity = Homefrag.this.getActivity();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("");
                        stringBuilder.append(e.getMessage());
                        // Toast.makeText(activity, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 404) {
                    show_banner();

                    Log.i("arr_response", "response.code  " + response.code());
                    connection("Something went wrong at server ! \n Please try again latter");
                } else {
                    show_banner();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    connection("Something went wrong ! \n Please try again ");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                show_banner();

                Log.d("t", "Something went wrong at server!" + t);
                connection("Something Wrong with Network connection!\n please try again");

            }
        });
    }


    public void featuredbrandlist() {

        show_brand();
        Retrofit retrofit = new Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.featuredbrandlist();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // isLoading = false;
                if (response.isSuccessful()) {
                    try {
                        Model_BrandArrayList.clear();
                        featuredcategorylist();
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
                            show_brand();
                            String message = obj.getString("error_message");
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            show_brand();
                            String message = obj.getString("message");
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {

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
                                yesbrand();
                                setup_brands();

                                adapter_brand.notifyDataSetChanged();


                            } else {
//                                Toast.makeText(getActivity(), "Aw Snap! No Data Available for this Catalogue", Toast.LENGTH_LONG).show();


                            }
                        }
                    } catch (Exception e) {
                        show_brand();
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 404) {
                    show_brand();

                    Log.i("arr_response", "response.code  " + response.code());
                    connection("Something went wrong at server ! \n Please try again latter");
                } else {
                    show_brand();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    connection("Something went wrong ! \n Please try again ");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                show_brand();

                Log.d("t", "Something went wrong at server!" + t);
                connection("Something Wrong with Network connection!\n please try again");

            }
        });
    }


    public void featuredproductlist() {
        show_product();

        Retrofit retrofit = new Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.featuredproductlist();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // isLoading = false;
                if (response.isSuccessful()) {
                    try {
                        Model_ProductArrayList.clear();
                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        Log.i("result_products", "" + obj);
                        if (obj.has("image_base_path")) {
                            imageurlprod = obj.getString("image_base_path");
                            Log.i("imageurl", "" + obj.getString("image_base_path"));
                        }

                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {
                            contactdetails();
                            JSONArray arr_response = obj.getJSONArray("data");

                            Log.i("arr_response", "arr_response  " + arr_response);
                            //   Log.i("page", page+"");
                            Log.i("total_pages", obj.optString("total_pages") + "");


                            if (arr_response.length() > 0) {
                                //   nearbyPropertiesModelArrayList.clear();
                                for (int i = 0; i < arr_response.length(); i++) {
                                    JSONObject jsonObject = arr_response.optJSONObject(i);
                                    Model_Products droppointListModel = new Model_Products();
                                    //////////////    Adding to Datamodel   ////////////////


                                    droppointListModel.setproducts_id(jsonObject.getString("product_id"));
                                    droppointListModel.setproducts_name(jsonObject.getString("product_title"));
                                    droppointListModel.setproducts_image(imageurlprod + jsonObject.getString("product_image"));
                                    if (!jsonObject.isNull("popularity")) {
                                        droppointListModel.setPopularity(jsonObject.getString("popularity"));


                                    } else {
                                        droppointListModel.setPopularity("");

                                    }
                                    if (!jsonObject.isNull("variant_selling_price")) {
                                        droppointListModel.setVariant_selling_price(jsonObject.getString("variant_selling_price"));


                                    } else {
                                        droppointListModel.setVariant_selling_price("");

                                    }
                                    if (!jsonObject.isNull("variant_regular_price")) {
                                        droppointListModel.setVariant_regular_price(jsonObject.getString("variant_regular_price"));


                                    } else {
                                        droppointListModel.setVariant_regular_price("");

                                    }
                                    if (!jsonObject.isNull("product_min_order_qty")) {
                                        droppointListModel.setProduct_min_order_qty(jsonObject.getString("product_min_order_qty"));


                                    } else {
                                        droppointListModel.setProduct_min_order_qty("");

                                    }

                                    if (!jsonObject.isNull("category_name")) {
                                        droppointListModel.setCategory_name(jsonObject.getString("category_name"));
                                    } else {
                                        droppointListModel.setCategory_name("");

                                    }
                                    if (!jsonObject.isNull("seller_name")) {
                                        droppointListModel.setSeller_name(jsonObject.getString("seller_name"));
                                    } else {
                                        droppointListModel.setSeller_name("");

                                    }
                                    if (!jsonObject.isNull("variant_mrp_price")) {
                                        droppointListModel.setVariant_mrp_price(jsonObject.getString("variant_mrp_price"));


                                    } else {
                                        droppointListModel.setVariant_mrp_price("");

                                    }
                                    Model_ProductArrayList.add(droppointListModel);
                                }

                                //  no_connectio.setVisibility(View.GONE);
                                yesprod();
                                setupproduct();

                                adapter_products.notifyDataSetChanged();


                            } else {
//                                Toast.makeText(getActivity(), "Aw Snap! No Data Available for this Catalogue", Toast.LENGTH_LONG).show();


                            }
                        }
                    } catch (Exception e) {
                        show_product();
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        //   Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 404) {
                    show_product();

                    Log.i("arr_response", "response.code  " + response.code());
                    connection("Something went wrong at server ! \n Please try again latter");
                } else {
                    show_product();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    connection("Something went wrong ! \n Please try again ");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                show_product();

                Log.d("t", "Something went wrong at server!" + t);
                connection("Something Wrong with Network connection!\n please try again");

            }
        });
    }

    public void shopbyseller(String state, String search) {

        Retrofit retrofit = new Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.shopbyseller("1","" + state, "" + search);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // isLoading = false;
                if (response.isSuccessful()) {
                    try {
                        featuredbrandlist();
                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        Log.i("result_seller", "" + obj);
                        if (obj.has("image_base_path")) {
                            imageurlprod = obj.getString("image_base_path");
                            Log.i("imageurl", "" + obj.getString("image_base_path"));
                        }

                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "warning  " + message);

                        } else {

                            seller_add.clear();
                            seller_name.clear();
                            seller_photo.clear();
                            seller_popularity.clear();
                            seller_id.clear();

                            if (obj.has("image_base_path")) {

                                image_path = obj.getString("image_base_path");
                            }

                            JSONArray arr_response = obj.getJSONArray("data");

                            Log.i("arr_response", "arr_response  " + arr_response);
                            //   Log.i("page", page+"");
                            Log.i("total_pages", obj.optString("total_pages") + "");


                            if (arr_response.length() > 0) {
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

                                //  no_connectio.setVisibility(View.GONE);

                                setupseller();

                                adapter_cbs.notifyDataSetChanged();


                            } else {
//                                Toast.makeText(getActivity(), "Aw Snap! No Data Available for this Catalogue", Toast.LENGTH_LONG).show();
                                ll_seller.setVisibility(View.GONE);

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 404) {

                    Log.i("arr_response", "response.code  " + response.code());
                    connection("Something went wrong at server ! \n Please try again latter");
                } else {

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    connection("Something went wrong ! \n Please try again ");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("t", "Something went wrong at server!" + t);
                connection("Something Wrong with Network connection!\n please try again");

            }
        });
    }

    public void featuredcategorylist() {

        show_category();
        Retrofit retrofit = new Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.featuredcategorylist();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // isLoading = false;
                if (response.isSuccessful()) {
                    try {
                        Model_CategoryArrayList.clear();
                        featuredproductlist();

                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);
                        String success = obj.optString("success");
                        Log.i("result", "" + obj);

                        if (obj.has("image_base_path")) {
                            imageurlcat = obj.getString("image_base_path");
                            Log.i("imageurl", "" + obj.getString("image_base_path"));
                        }

                        if (obj.has("error")) {
//                            vibrate_alert(500);
                            String message = obj.getString("error_message");
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                            Log.i("arr_response", "error " + message);

                        } else if (obj.has("warning")) {
//                            vibrate_alert(500);
                            String message = obj.getString("message");
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
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

                                yescat();
                                setup_category();

                                adapter_category.notifyDataSetChanged();


                            } else {
//                                Toast.makeText(getActivity(), "Aw Snap! No Data Available for this Catalogue", Toast.LENGTH_LONG).show();


                            }
                        }
                    } catch (Exception e) {
                        show_category();
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 404) {
                    show_category();

                    Log.i("arr_response", "response.code  " + response.code());
                    connection("Something went wrong at server ! \n Please try again latter");
                } else {
                    show_category();

                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    connection("Something went wrong ! \n Please try again ");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                show_category();

                Log.d("t", "Something went wrong at server!" + t);
                connection("Something Wrong with Network connection!\n please try again");

            }
        });
    }


    public void productsearch(final String page, String brand_id, String category_id, String tag_search, String price_sorting) {


        Retrofit retrofit = new Builder()
                .baseUrl(Utils.base_url)
                .build();
        Product_list.Service service = retrofit.create(Product_list.Service.class);
        Call<ResponseBody> call;
        call = service.productsearch(page, brand_id, category_id, tag_search, price_sorting, "", "");

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoading = false;

                if (response.isSuccessful()) {

                    no_connectio.setVisibility(View.GONE);

                    try {

                        String result = response.body().string();

                        Log.i("result", "" + result);
                        JSONObject obj = new JSONObject(result);
                        Log.d("resp", "" + obj);
                        String success = obj.optString("success");
                        String message = obj.optString("message");
                        id.clear();
                        title.clear();
                        photo.clear();

                        if (obj.has("success")) {
                            success = obj.optString("success");

                            if (obj.has("image_base_path")) {
                                imageurlprod = obj.getString("image_base_path");
                                Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                            }

                            if (success.equals("true")) {
                                message = obj.optString("message");

                                //  Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                                if (obj.has("image_base_path")) {
                                    imagecbs = obj.getString("image_base_path");
                                    Log.i("imageurlbrand", "" + obj.getString("image_base_path"));
                                }


                                if (obj.has("data")) {


                                    JSONArray arr_order = obj.getJSONArray("data");


                                    if (arr_order.length() > 0) {
                                        Log.d("responseee", "" + arr_order);


                                        for (int i = 0; i < arr_order.length(); i++) {
                                            JSONObject jsonObject = arr_order.optJSONObject(i);

                                            if (!jsonObject.isNull("product_id")) {
                                                id.add(jsonObject.getString("product_id"));

                                            } else {
                                                id.add("");
                                            }
                                            if (!jsonObject.isNull("product_title")) {
                                                title.add(jsonObject.getString("product_title"));

                                            } else {
                                                title.add("");
                                            }
                                            if (!jsonObject.isNull("product_image")) {
                                                photo.add(imagecbs + jsonObject.getString("product_image"));

                                            } else {
                                                photo.add("");
                                            }


                                        }
                                    } else {


                                        //     Toast.makeText(Search_FilterActivity.this, "1 done", Toast.LENGTH_SHORT).show();


                                    }

                                } else {
                                    Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            String err_message = obj.getString("message");
                            Toast.makeText(getActivity(), "error" + err_message, Toast.LENGTH_SHORT).show();
                            Log.d("error", "" + err_message);
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                        Log.d("e", "" + e);
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else switch (response.code()) {
                    case 404:
                        connection("Something went wrong at Server! We are tryin to fix it soon.\n Please try again later");

                        // Toast.makeText(getActivity(), "Page not found", Toast.LENGTH_SHORT).show();
                        break;
                    case 500:
                        connection("Something went wrong at Server! We are tryin to fix it soon. \n Please try again later");
                        break;
                    default:
                        connection("Something went wrong ! \n Please try again ");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                Log.d("fail", "onFailure: " + t.getMessage());

                connection("Something Wrong with Network connection!\n please try again");
                // Toast.makeText(getActivity(), "Something went wrong at server!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void contactdetails() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(" Please wait...");
        progressDialog.setCancelable(false);

        Retrofit retrofit = new Builder()
                .baseUrl(Utils.base_url)
                .build();

        Service service = retrofit.create(Service.class);
        Call<ResponseBody> call;
        call = service.contactdetails();
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

                        } else if (obj.has("data")) {
//

                            JSONObject objdata = obj.getJSONObject("data");
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            if (!objdata.isNull("address")) {
                                editor.putString("address", objdata.getString("address"));

                                //  tv_add.setText(objdata.getString("address"));

                            }
                            if (!objdata.isNull("email")) {
                                editor.putString("email", objdata.getString("email"));

//                                tv_email.setText(objdata.getString("email"));

                            }
                            if (!objdata.isNull("phone_number")) {
                                editor.putString("phone_number", objdata.getString("phone_number"));
                                ph = "" + objdata.getString("phone_number");

                                //   tv_mob.setText(objdata.getString("phone_number"));

                            }


                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.i("arr_response", "e    " + e);

                        //   Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    progressDialog.dismiss();
                    Log.i("arr_response", "response.code  " + response.code());
                    // Toast.makeText(getActivity(), "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    //  Toast.makeText(getActivity(), "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

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

                //  Toast.makeText(getActivity(), "Something went wrong at server!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setup_brands() {
        adapter_brand = new Adapter_Brand(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerbrand.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) recyclerbrand.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerbrand.setAdapter(adapter_brand);

    }

    public void setup_category() {
        recyclercategory.setLayoutManager(llc);
        recyclercategory.setAdapter(adapter_category);
    }

    public void setupproduct() {
        recyclerproducts.setLayoutManager(llp);
        recyclerproducts.setAdapter(adapter_products);
    }

    public void setupseller() {
        recyclerseller.setLayoutManager(lls);
        recyclerseller.setAdapter(adapter_cbs);
    }


    public void show_brand() {
        container =
                (ShimmerFrameLayout) v.findViewById(R.id.shimmer_view);

        container.startShimmer();


        ll_progress.setVisibility(View.VISIBLE);
        ll_yes_result.setVisibility(View.GONE);
        //  ll_noresult.setVisibility(View.GONE);

    }

    public void show_product() {

        container3 =
                (ShimmerFrameLayout) v.findViewById(R.id.shimmer_viewproduct);
        container3.startShimmer();


        ll_progress1.setVisibility(View.VISIBLE);
        llyesproduct.setVisibility(View.GONE);
        //  ll_noresult.setVisibility(View.GONE);

    }

    public void show_category() {

        container2 = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_viewcategory);
        container2.startShimmer();

        ll_progress2.setVisibility(View.VISIBLE);
        llyescategory.setVisibility(View.GONE);
    }

    public void show_banner() {

        container1 = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_view_banner);
        container1.startShimmer();
        ll_progress3.setVisibility(View.VISIBLE);
        llyesbanner.setVisibility(View.GONE);

    }


    public void yesbanner() {
        ll_progress3.setVisibility(View.GONE);
        llyesbanner.setVisibility(View.VISIBLE);
    }

    public void yesbrand() {
        ll_progress.setVisibility(View.GONE);
        ll_yes_result.setVisibility(View.VISIBLE);

    }

    public void yescat() {
        ll_progress2.setVisibility(View.GONE);
        llyescategory.setVisibility(View.VISIBLE);
    }

    public void yesprod() {
        ll_progress1.setVisibility(View.GONE);
        llyesproduct.setVisibility(View.VISIBLE);
    }


    void yes() {
        SliderAdapterExample adapter = new SliderAdapterExample(getContext());
        adapter.setCount(al_banners.size());
        sliderView.setSliderAdapter(adapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);

            }
        });
    }


    void connection(String message) {

        tv_text.setText(message);


        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pb.setVisibility(View.VISIBLE);
                productsearch("", "19", "", "", "");

            }
        });

    }

    public void setupcbs() {
        recycler_cbs.setLayoutManager(ll);
        recycler_cbs.setAdapter(adapter_cbs);


        final int speedScroll = 900;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (count < photo.size()) {
                    recycler_cbs.smoothScrollToPosition(++count);
                    handler.postDelayed(this, speedScroll);
                    if (count == photo.size()) {
                        count = 0;
                    }
                }


            }
        };

        handler.postDelayed(runnable, speedScroll);
    }


    public interface Service {

        @Headers("Authkey:APPCBSBDMPL")
        @GET("bannerlist")
        Call<ResponseBody> bannerlist();

        @Headers("Authkey:APPCBSBDMPL")
        @GET("featuredbrandlist")
        Call<ResponseBody> featuredbrandlist();

        @Headers("Authkey:APPCBSBDMPL")
        @GET("featuredcategorylist")
        Call<ResponseBody> featuredcategorylist();

        @Headers("Authkey:APPCBSBDMPL")
        @GET("featuredproductlist")
        Call<ResponseBody> featuredproductlist();

        @Headers("Authkey:APPCBSBDMPL")
        @POST("shopbyseller")
        @FormUrlEncoded
        Call<ResponseBody> shopbyseller(@Field("page") String page,
                                        @Field("state") String state,
                                        @Field("search") String search);


        @Headers("Authkey:APPCBSBDMPL")
        @POST("seller_by_stat")
        Call<ResponseBody> seller_by_stat(@Field("state") String state);

        @Headers("Authkey:APPCBSBDMPL")
        @GET("contactdetails")
        Call<ResponseBody> contactdetails();

    }
}
