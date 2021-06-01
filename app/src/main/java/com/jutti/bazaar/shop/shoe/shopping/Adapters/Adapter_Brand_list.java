package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jutti.bazaar.shop.shoe.shopping.Activities.Brand_list;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Brand;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
import com.jutti.bazaar.shop.shoe.shopping.R;

public class Adapter_Brand_list extends RecyclerView.Adapter<Adapter_Brand_list.MyViewHolder> {
    Context mContext;
   Model_Brand complainModel;
    SharedPreferences sharedPref;
   ProgressDialog progressDialog;
   String user_id = "";

    public Adapter_Brand_list(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_brand, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        complainModel = Brand_list.Model_BrandArrayList.get(position);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);


        Glide.with(mContext)
                .load(complainModel.getbrand_image())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(holder.iv_model_brand);

        holder.ll_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complainModel = Brand_list.Model_BrandArrayList.get(position);

                Product_list.brandID=complainModel.getbrand_id();
                Product_list.title=complainModel.getbrand_name();

                mContext.startActivity(new Intent(mContext, Product_list.class));


                //    Toast.makeText(mContext, "proid"+modelcontent.getproducts_id(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return Brand_list.Model_BrandArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_clickable;
        ImageView iv_model_brand;
        public MyViewHolder(View itemView) {
            super(itemView);
            ll_clickable=itemView.findViewById(R.id.ll_clickable);

            iv_model_brand=(ImageView)itemView.findViewById(R.id.iv_model_brand);


        }
    }

}