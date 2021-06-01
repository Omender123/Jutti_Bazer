package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jutti.bazaar.shop.shoe.shopping.Model.Brand_selction;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
import com.jutti.bazaar.shop.shoe.shopping.R;


public class Adapter_select_brand extends RecyclerView.Adapter<Adapter_select_brand.MyViewHolder> {
    Context context;
    SharedPreferences sharedPref;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_clickable;
        ImageView iv_model_brand;
        public MyViewHolder(View itemView) {
            super(itemView);
            ll_clickable=itemView.findViewById(R.id.ll_clickable);

            iv_model_brand=(ImageView)itemView.findViewById(R.id.iv_model_brand);


        }
    }

    public Adapter_select_brand(Context context) {
        this.context = context;
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.model_brand, viewGroup, false));
    }


    public void onBindViewHolder(@NonNull MyViewHolder holder, final int postion) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);


        Glide.with(context)
                .load(Brand_selction.brand_image.get(postion))
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_model_brand);

        holder.ll_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Product_list.brandID=Brand_selction.brand_id.get(postion);
                Product_list.categoryID=""+Brand_selction.cat_id;
                Product_list.title=Brand_selction.brand_name.get(postion);

                context.startActivity(new Intent(context, Product_list.class));

    }

});}

    @Override
    public int getItemCount() {
        return Brand_selction.brand_id.size();    }
}
