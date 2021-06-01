package com.jutti.bazaar.shop.shoe.shopping.Product_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.jutti.bazaar.shop.shoe.shopping.R;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewpagerAdapterZoomSlider extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflator;
    ArrayList<String> imageListType= Product_Details.al_banners;


    public ViewpagerAdapterZoomSlider(Context mContext, ArrayList<String> imageListType) {
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
        View view = mLayoutInflator.inflate(R.layout.viewpagerdesignpinch, container, false);
        PhotoView tutorialImage = view.findViewById(R.id.imageViewpger);
        Glide.with(mContext).load(imageListType.get(position)).into(tutorialImage);
        //tutorialImage.setImageResource(Integer.parseInt(String.valueOf(position)));
        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }


}


