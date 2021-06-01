package com.jutti.bazaar.shop.shoe.shopping.Product_details;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.R;


public class Adapter_selct_size extends RecyclerView.Adapter<Adapter_selct_size.MyViewHolder> {

    Context mContext;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    String user_id = "";


    public Adapter_selct_size(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Adapter_selct_size.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_size, parent, false);
        return new Adapter_selct_size.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        ////set text
        holder.tv_size.setText(Product_Details.variant_size.get(position));





        if (Product_Details.selectedsize.equals(Product_Details.variant_id.get(position))){
            holder.llclick_address.setBackgroundResource(R.drawable.rect_green);
            holder.tv_size.setTextColor(Color.WHITE);



        }else {
            holder.llclick_address.setBackgroundResource(R.drawable.btn_shapes_capsule_sh);
            holder.tv_size.setTextColor(Color.BLACK);

        }

        holder.llclick_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product_Details.selectedsize=Product_Details.variant_id.get(position);

                Product_Details.ind=Product_Details.variant_size.indexOf(Product_Details.variant_size.get(position));

                Log.d("clicked", "onClick: "+Product_Details.variant_size.get(position));
               // Toast.makeText(mContext, ""+    Checkout.address_id, Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                notifyItemChanged(holder.getAdapterPosition());


                //for refresh

                if (mContext instanceof Product_Details) {
                    ((Product_Details)mContext).yess();
                }


            }
        });







    }




    @Override
    public int getItemCount() {
        return Product_Details.variant_size.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

           CardView llclick_address;
           ProgressBar progressbar;
        ImageView im_edit, im_delete;
        TextView tv_size,tv_address ,tv_city,tv_phone,cart_product_total,tv_type;
        public MyViewHolder(View itemView) {
            super(itemView);
            llclick_address=itemView.findViewById(R.id.llclick);
//
            tv_size=itemView.findViewById(R.id.tv_size);



//            tv_cartproduct_quantity=(TextView) itemView.findViewById(R.id.order_qty);
//            cart_product_price=(TextView) itemView.findViewById(R.id.cart_product_price);
//            cart_product_total=(TextView) itemView.findViewById(R.id.cart_product_total);


        }
    }



}
