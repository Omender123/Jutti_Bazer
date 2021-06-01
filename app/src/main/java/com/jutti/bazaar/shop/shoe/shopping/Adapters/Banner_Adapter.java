package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jutti.bazaar.shop.shoe.shopping.Fragment.Homefrag;
import com.jutti.bazaar.shop.shoe.shopping.Model.Banner_Model;
import com.jutti.bazaar.shop.shoe.shopping.R;

public class Banner_Adapter extends RecyclerView.Adapter<Banner_Adapter.MyViewHolder> {
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardview = (CardView) itemView.findViewById(R.id.cardview1);
            this.image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public Banner_Adapter(Context context) {
        this.context = context;
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.banner_items, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int postion) {
        Banner_Model banner_model = (Banner_Model) Homefrag.bannerlist.get(postion);
//        RequestManager with = Glide.with(this.context);
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("http://cbskitchenware.com/beta/uploads/");
//        stringBuilder.append(banner_model.getBanner_image());
//        with.load(stringBuilder.toString()).into(myViewHolder.image);


        Glide.with(context)
                .load(banner_model.getBanner_image())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder)
                .into(myViewHolder.image);



    }

    public int getItemCount() {
        return Homefrag.bannerlist.size();
    }
}
