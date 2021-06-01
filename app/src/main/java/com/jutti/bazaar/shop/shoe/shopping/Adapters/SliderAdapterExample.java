package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jutti.bazaar.shop.shoe.shopping.Fragment.Homefrag;

import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private int mCount;

    public SliderAdapterExample(Context context) {
        this.context = context;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAG", "onClick: "+Homefrag.banner_related_to.get(position));
                Log.d("TAG", "onClick: "+Homefrag.banner_related_id.get(position));


                //  Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();

                if (Homefrag.banner_related_to.get(position).equals("sub_category")){
                    Product_list.categoryID=""+Homefrag.banner_related_id.get(position);
                    context.startActivity(new Intent(context, Product_list.class));
                } else if (Homefrag.banner_related_to.get(position).equals("category")){
                    Product_list.categoryID=""+Homefrag.banner_related_id.get(position);
                    context.startActivity(new Intent(context, Product_list.class));
                }else  if (Homefrag.banner_related_to.get(position).equals("seller")){
                    Product_list.seller_id =""+Homefrag.banner_related_id.get(position);
                    context.startActivity(new Intent(context, Product_list.class));
                }else {

                }




            }
        });


        Glide.with(viewHolder.itemView)
                .load(Homefrag.al_banners.get(position))
                .thumbnail(0.05f)
                .placeholder(R.drawable.placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewHolder.imageViewBackground);


    }


    @Override
    public int getCount() {
        //slider view count could be dynamic size


        return mCount;
    }


    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }


}