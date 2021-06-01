package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.jutti.bazaar.shop.shoe.shopping.Product_details.ZoomSliderImageActivivty;
import com.jutti.bazaar.shop.shoe.shopping.Product_details.Product_Details;
import com.jutti.bazaar.shop.shoe.shopping.R;


/**
 * Created by Gulshan on 5/09/2019.
 */

public class AndroidImageAdapter extends PagerAdapter {
    Context mContext;
    public String[] sliderImagesId;

    public AndroidImageAdapter(Context context, String array[]) {
        this.mContext = context;
        this.sliderImagesId=array;

    }

    @Override
    public int getCount() {
        return sliderImagesId.length;
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == ((ImageView) obj);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int i) {
        final ImageView mImageView = new ImageView(mContext);






        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();



        Glide.with(mContext)

                .load(sliderImagesId[i])
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder)
                .apply(options)
                .into(mImageView);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ZoomSliderImageActivivty.class);
                intent.putStringArrayListExtra("tutorialData", Product_Details.al_banners);
                mContext.startActivity(intent);
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ZoomSliderImageActivivty.class);
                intent.putStringArrayListExtra("tutorialData", Product_Details.al_banners);
                mContext.startActivity(intent);
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ZoomSliderImageActivivty.class);
                intent.putStringArrayListExtra("tutorialData", Product_Details.al_banners);
                mContext.startActivity(intent);
            }
        });

//        mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(mContext);
//                dialog.setContentView(R.layout.dialog_profile_photo);
//
//
//                // set the custom dialog components - text, image and button
//
//                PhotoView image = (PhotoView) dialog.findViewById(R.id.photo_dp);
//
//
//                Picasso.with(mContext)
//                        .load(Product_Details.al_banners.get(i))
//                        .error(R.drawable.add_image)
//                        .into(image, new com.squareup.picasso.Callback() {
//
//                                    @Override
//                                    public void onSuccess() {
//                                        //   holder.progress.setVisibility(View.GONE);
//                                    }
//
//                                    @Override
//                                    public void onError() {
//                                        //  holder.progress.setVisibility(View.GONE);
//                                        // myViewHolder.llclick.setImageResource(R.drawable.add_image);
//
//                                    }
//                                }
//                        );
//
//
//                PhotoView dialogButton = dialog.findViewById(R.id.photo_dp);
//                // if button is clicked, close the custom dialog
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.show();
//            }
//        });

        // if button is clicked, close the custom dialog


        ((ViewPager) container).addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        ((ViewPager) container).removeView((ImageView) obj);
    }
}