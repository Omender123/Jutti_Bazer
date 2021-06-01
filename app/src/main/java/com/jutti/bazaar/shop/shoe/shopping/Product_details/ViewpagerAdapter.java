package com.jutti.bazaar.shop.shoe.shopping.Product_details;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.jutti.bazaar.shop.shoe.shopping.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewpagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflator;
    ArrayList<String> imageListType= Product_Details.al_banners;


    public ViewpagerAdapter(Context mContext, ArrayList<String> imageListType) {
        this.mContext = mContext;
        this.imageListType = imageListType;
    }

    @Override
    public int getCount() {
        return imageListType.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        mLayoutInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflator.inflate(R.layout.viewpagerdesign, container, false);
        ImageView tutorialImage = view.findViewById(R.id.imageViewpger);
        Glide.with(mContext).load(imageListType.get(position))
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(tutorialImage);
        container.addView(view);

        tutorialImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ZoomSliderImageActivivty.class);
                intent.putStringArrayListExtra("tutorialData", Product_Details.al_banners);
                mContext.startActivity(intent);
            }
        });



        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }


}


