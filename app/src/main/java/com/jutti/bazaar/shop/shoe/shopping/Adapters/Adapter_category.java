package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jutti.bazaar.shop.shoe.shopping.Fragment.Homefrag;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Category;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
import com.jutti.bazaar.shop.shoe.shopping.R;

public class Adapter_category extends RecyclerView.Adapter<Adapter_category.MyViewHolder> {
    Context mContext;
   Model_Category category_Content;
    SharedPreferences sharedPref;
   ProgressDialog progressDialog;
   String user_id = "";

    public Adapter_category(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_category, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        category_Content = Homefrag.Model_CategoryArrayList.get(position);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        holder.tv_title.setText(category_Content.getcategory_name());
        if(category_Content.getParent_name().equals("")){
            holder.ll_cat.setVisibility(View.GONE);
        }else {
            holder.ll_cat.setVisibility(View.VISIBLE);
        }

        if (category_Content.getParent_name().equals("Men")){
            holder.ll_cat.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.men));
        }else  if (category_Content.getParent_name().equals("Women")){
            holder.ll_cat.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.womer));

        }else  if (category_Content.getParent_name().equals("Kids")){
            holder.ll_cat.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.kids));

        }
        holder.tv_cat.setText(category_Content.getParent_name());

        Glide.with(mContext)
                .load(category_Content.getcategory_image())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(holder.iv_image);

        holder.ll_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_Content = Homefrag.Model_CategoryArrayList.get(position);
                Product_list.title = category_Content.getcategory_name();
                Product_list.categoryID = category_Content.getcategory_id();
                mContext.startActivity(new Intent(mContext, Product_list.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return Homefrag.Model_CategoryArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_cat;
        ImageView iv_image;
        LinearLayout ll_clickable,ll_cat;
        public MyViewHolder(View itemView) {
            super(itemView);
            ll_clickable=itemView.findViewById(R.id.ll_clickable);
            ll_cat=itemView.findViewById(R.id.ll_cat);
            tv_cat=(TextView) itemView.findViewById(R.id.tv_cat);
            iv_image=(ImageView)itemView.findViewById(R.id.iv_model_category);
            tv_title=(TextView) itemView.findViewById(R.id.tv_model_category);


        }
    }

}