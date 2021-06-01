package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jutti.bazaar.shop.shoe.shopping.Activities.Orders_details;
import com.jutti.bazaar.shop.shoe.shopping.Product_details.Product_Details;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
import com.jutti.bazaar.shop.shoe.shopping.R;


public class Adapter_detail extends RecyclerView.Adapter<Adapter_detail.MyViewHolder> {


    SharedPreferences sharedPref;
    Context mContext;
    String spinner1_value_target = "", spinner1_value_result, spinner_value_source = "en", spinner_value_target = "en", inputtext = "";
    Dialog dialog_progress;

    Dialog dialog_translator;
    LinearLayout ll_translate_to, ll_translating;
    TextView tv_from_lang, tv_to_lang;

    Dialog dialog_report, dialog_adding;


    public Adapter_detail(Context context) {
        this.mContext = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_order_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        holder.tv_title.setText(Orders_details.prodtitle.get(position));
        holder.qty.setText(Orders_details.qty.get(position));
        holder.price.setText(Orders_details.price.get(position));
        holder.tv_sub_total.setText(Orders_details.subtot.get(position));
        holder.tv_type.setText(Orders_details.type.get(position));
        holder.tv_size.setText(Orders_details.product_size.get(position));

        Glide.with(mContext)
                .load(Orders_details.img.get(position))
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(holder.iv_img);


        holder.viewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext.startActivity(new Intent(mContext, Product_Details.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("product_id", Orders_details.prodID.get(position)));

                Log.d("product_id", "onClick: " + Orders_details.prodID.get(position));
            }
        });
        holder.viewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product_list.seller_id = Orders_details.prodShopID.get(position);
                Product_list.title = Orders_details.prodShopName.get(position);

                mContext.startActivity(new Intent(mContext, Product_list.class));
            }
        });

    }


    @Override
    public int getItemCount() {

        return Orders_details.prodtitle.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView viewShop, viewProduct, tv_title, qty, price, tv_sub_total, tv_type, tv_size;
        ImageView iv_img;
        LinearLayout ll_clickable, r;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            iv_img = itemView.findViewById(R.id.iv_img);
            qty = (TextView) itemView.findViewById(R.id.tv_qty);
            price = (TextView) itemView.findViewById(R.id.tv_per_price);
            tv_sub_total = (TextView) itemView.findViewById(R.id.tv_sub_total);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
            ll_clickable = itemView.findViewById(R.id.ll_clickablee);
            viewProduct = itemView.findViewById(R.id.viewProduct);
            viewShop = itemView.findViewById(R.id.viewShop);


        }
    }


}
