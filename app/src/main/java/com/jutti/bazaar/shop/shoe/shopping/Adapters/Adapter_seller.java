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
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jutti.bazaar.shop.shoe.shopping.Fragment.Homefrag;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Brand;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
import com.jutti.bazaar.shop.shoe.shopping.R;

import com.skydoves.androidribbon.ShimmerRibbonView;

public class Adapter_seller extends RecyclerView.Adapter<Adapter_seller.MyViewHolder> {
    Context mContext;
    Model_Brand complainModel;
    SharedPreferences sharedPref;
    ProgressDialog progressDialog;
    String user_id = "";

    public Adapter_seller(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_seller, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        holder.name.setText(Homefrag.seller_name.get(position));
        holder.loc.setText(Homefrag.seller_add.get(position));

        Glide.with(mContext)
                .load(Homefrag.seller_photo.get(position))
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img);
        if (Homefrag.seller_add.get(position).equals("1")) {
            holder.ribn.setVisibility(View.VISIBLE);
        }


        holder.ll_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Product_list.seller_id = Homefrag.seller_id.get(position);
                Product_list.title = Homefrag.seller_name.get(position);

                mContext.startActivity(new Intent(mContext, Product_list.class));

            }
        });




    }

    @Override
    public int getItemCount() {
        return Homefrag.seller_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_clickable;
        TextView  loc, name;
        ShimmerRibbonView ribn;
        ImageView img;


        public MyViewHolder(View itemView) {
            super(itemView);
            ll_clickable = itemView.findViewById(R.id.ll_clickable);
            ribn = itemView.findViewById(R.id.ribn);
            loc = itemView.findViewById(R.id.loc);
            name = itemView.findViewById(R.id.name);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

}