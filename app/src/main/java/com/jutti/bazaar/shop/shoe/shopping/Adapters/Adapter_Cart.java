package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jutti.bazaar.shop.shoe.shopping.Activities.My_cart;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Cart;
import com.jutti.bazaar.shop.shoe.shopping.Product_details.Product_Details;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;


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


public class Adapter_Cart extends RecyclerView.Adapter<Adapter_Cart.MyViewHolder> {

    Context mContext;
    Model_Cart model_cart;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String user_id = "";
    int count = 0;
    String countnew = "";
    public static String   qty = "";
    public static ArrayList<String> stock_count = new ArrayList();
//    public static ArrayList<String> product_total_stock = new ArrayList<>();
//    public static ArrayList<String> product_min_order_qty = new ArrayList<>();
     int product_total_stock = 0, product_min_order_qty = 0;
    public Adapter_Cart(Context context) {
        this.mContext = context;
    }

    @Override
    public Adapter_Cart.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_cart, parent, false);
        return new Adapter_Cart.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        model_cart = My_cart.product.get(position);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        ////set text
        holder.tv_cart_product.setText(model_cart.getTITLE());
        holder.size.setText(model_cart.getvariant_size());
        holder.tv_type.setText(model_cart.gettype());
        holder.cart_product_price.setText(model_cart.getPRICE());
        holder.cart_product_total.setText(model_cart.getTOTAL_PRICE());
        holder.et.setText(model_cart.getCart_quantity());
        holder.tv_qty.setText(model_cart.getCart_quantity());

        count= Integer.parseInt(holder.et.getText().toString());
//        countnew= holder.et.getText().toString();

        ////set image


//        holder.add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                model_cart = My_cart.product.get(position);
////                count= Integer.parseInt(holder.et.getText().toString());
////                countnew= holder.et.getText().toString();
////
////                count++;
////                holder.et.setText(""+count);
////
////                if (mContext instanceof My_cart) {
////                    ((My_cart)mContext).add(
////                            ""+ sharedPreferences.getString("user_login_token", "")
////                            , "" + sharedPreferences.getString("user_id", "")
////                            ,""+model_cart.getProductid()
////                            ,""+holder.et.getText().toString()
////                            ,""+model_cart.getVariant_id()
////
////                    );
////                }
//
//            }
//        });

//        holder.sub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                model_cart = My_cart.product.get(position);
//                count= Integer.parseInt(holder.et.getText().toString());
//
//                count--;
//
//                holder.et.setText(""+count);
//                if (count<1){
//                    removefromcart("" + sharedPreferences.getString("user_login_token", ""),
//                            "" + sharedPreferences.getString("user_id", ""),
//                            "" + model_cart.getProductid()
//                            , "" + model_cart.getCart_id());
//
//                }else {
//                    if (mContext instanceof My_cart) {
//                        ((My_cart) mContext).add(
//                                "" + sharedPreferences.getString("user_login_token", ""),
//                                "" + sharedPreferences.getString("user_id", "")
//                                , "" + model_cart.getProductid()
//                                , "" +holder.et.getText().toString()
//                                , "" + model_cart.getVariant_id()
//
//                        );
//                    }
//                }
//
//            }
//        });

        Glide.with(mContext)
                .load(model_cart.getTHUMBNAIL())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(holder.iv_cart_product);



        holder.tv_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                model_cart = My_cart.product.get(position);
                if (mContext instanceof My_cart) {
                    model_cart = My_cart.product.get(position);
                    ((My_cart)mContext).change_qty(
                                    Integer.parseInt(model_cart.getCart_quantity()),
                                    Integer.parseInt(model_cart.getProduct_total_stock()),
                                    Integer.parseInt(model_cart.getProduct_min_order_qty())
                                    ,""+ sharedPreferences.getString("user_login_token", "")
                                    ,""+ sharedPreferences.getString("user_id", "")
                                    ,"" +model_cart.getProductid()
                                    ,""+model_cart.getVariant_id()
                            );
                }


            }
        });

        holder.ll_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model_cart = My_cart.product.get(position);
                mContext.startActivity(new Intent(mContext, Product_Details.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("product_id", My_cart.product_id.get(position)));


            }
        });
        holder.iv_clear_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model_cart = My_cart.product.get(position);
                if (mContext instanceof My_cart) {
                    ((My_cart)mContext).remove(
                            "" +model_cart.getProductid()
                            ,""+model_cart.getCart_id()
                    );
                }
            }
        });








        }







    @Override
    public int getItemCount() {
        return My_cart.product.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_clickable;
        ImageView iv_cart_product, iv_clear_item;
        Button add, sub;
        Spinner spinner;

        TextView tv_qty,et, size, tv_cart_product, cart_product_price, cart_product_total, tv_type;

        public MyViewHolder(View itemView) {
            super(itemView);
            //  ll_clickable=itemView.findViewById(R.id.ll_clickable);
            iv_cart_product = itemView.findViewById(R.id.iv_cart_product);
            iv_clear_item = itemView.findViewById(R.id.iv_clear_item);
            ll_clickable = itemView.findViewById(R.id.ll_clickable);
            et = itemView.findViewById(R.id.et);
            tv_qty = itemView.findViewById(R.id.tv_qty);


            size = (TextView) itemView.findViewById(R.id.tv_size);
            tv_cart_product = (TextView) itemView.findViewById(R.id.tv_cart_product);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            cart_product_price = (TextView) itemView.findViewById(R.id.cart_product_price);
            cart_product_total = (TextView) itemView.findViewById(R.id.cart_product_total);

            add = (Button) itemView.findViewById(R.id.add);
            sub = (Button) itemView.findViewById(R.id.sub);
            spinner = (Spinner) itemView.findViewById(R.id.spinner);

            cart_product_total = (TextView) itemView.findViewById(R.id.cart_product_total);


        }
    }




}
