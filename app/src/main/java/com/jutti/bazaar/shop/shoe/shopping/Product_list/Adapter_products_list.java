package com.jutti.bazaar.shop.shoe.shopping.Product_list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import com.jutti.bazaar.shop.shoe.shopping.Product_details.Product_Details;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.skydoves.androidribbon.ShimmerRibbonView;

import java.util.ArrayList;

public class Adapter_products_list extends RecyclerView.Adapter<Adapter_products_list.MyViewHolder> {
    Context mContext;
    Model_product_list modelcontent;
    SharedPreferences sharedPref;
    private ArrayList<Model_product_list> prodlist;


    public Adapter_products_list(Context context, ArrayList<Model_product_list> prodlist) {
        this.mContext = context;
        this.prodlist = prodlist;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_product, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        modelcontent = prodlist.get(position);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);

        holder.seller.setText(modelcontent.getSeller_name());
        holder.name.setText(modelcontent.getproducts_name());
        int a=  Math.round(Float.parseFloat(modelcontent.getVariant_selling_price()));

        holder.tv_product_price.setText("₹" + a);
        holder.qty.setText("MOQ: " + modelcontent.getProduct_min_order_qty());
        holder.tv_product_price_mrp.setText(modelcontent.getVariant_mrp_price());

        if (modelcontent.getVariant_regular_price().equals("")) {
            holder.tv_product_price.setVisibility(View.GONE);
            holder.llpricemrp.setBackgroundResource(0);
        }

        if (modelcontent.getProduct_min_order_qty().equals("")) {
            holder.qty.setVisibility(View.GONE);
        }

        if (modelcontent.getCategory_name().equals("Men")) {
            holder.ll_men.setVisibility(View.VISIBLE);
            holder.ll_women.setVisibility(View.GONE);
            holder.ll_kid.setVisibility(View.GONE);
        }else if (modelcontent.getCategory_name().equals("Women")) {
            holder.ll_women.setVisibility(View.VISIBLE);
            holder.ll_men.setVisibility(View.GONE);
            holder.ll_kid.setVisibility(View.GONE);
        }else if (modelcontent.getCategory_name().equals("Kids")) {
            holder.ll_kid.setVisibility(View.VISIBLE);
            holder.ll_men.setVisibility(View.GONE);
            holder.ll_women.setVisibility(View.GONE);

        }


        if (modelcontent.getVariant_mrp_price().equals("")) {
            holder.llpricemrp.setVisibility(View.GONE);
        }

        if (modelcontent.getPopularity().equals("1")) {
            holder.rbn.setVisibility(View.VISIBLE);
        }


        RequestOptions requestOptions = new RequestOptions();


        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(modelcontent.getproducts_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.05f)
                .placeholder(R.drawable.placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pb.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        holder.pb.setVisibility(View.GONE);
                        return false;
                    }

                })
                .into(holder.img);


        holder.ll_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modelcontent = prodlist.get(position);
                mContext.startActivity(new Intent(mContext, Product_Details.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("product_id", modelcontent.getproducts_id()));
                Log.d("product_id", "onClick: " + modelcontent.getproducts_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return prodlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llpricemrp, ll_men, ll_women, ll_kid;
        CardView ll_clickable;
        ImageView img;
        ShimmerRibbonView rbn;
        TextView seller,name, tv_product_price, qty, tv_product_price_mrp;
        ProgressBar pb;

        public MyViewHolder(View itemView) {
            super(itemView);

            ll_clickable = itemView.findViewById(R.id.llproduct);
            seller = itemView.findViewById(R.id.seller);
            ll_men = itemView.findViewById(R.id.ll_men);
            ll_women = itemView.findViewById(R.id.ll_women);
            ll_kid = itemView.findViewById(R.id.ll_kid);
            llpricemrp = itemView.findViewById(R.id.llpricemrp);
            pb = itemView.findViewById(R.id.pb);
            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
            tv_product_price = itemView.findViewById(R.id.tv_product_price);
            tv_product_price_mrp = itemView.findViewById(R.id.tv_product_price_mrp);
            qty = itemView.findViewById(R.id.qty);
            rbn = itemView.findViewById(R.id.rbn);

        }
    }
}