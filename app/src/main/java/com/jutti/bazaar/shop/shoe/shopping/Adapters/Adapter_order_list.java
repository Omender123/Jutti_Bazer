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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.Activities.OrdersList;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_Order;
import com.jutti.bazaar.shop.shoe.shopping.Activities.Orders_details;
import com.jutti.bazaar.shop.shoe.shopping.R;


public class Adapter_order_list extends RecyclerView.Adapter<Adapter_order_list.MyViewHolder> {

    Context mContext;
    Model_Order model_order;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String user_id = "";

    public Adapter_order_list(Context context) {
        this.mContext = context;
    }

    @Override
    public Adapter_order_list.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_order_list, parent, false);
        return new Adapter_order_list.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        model_order = OrdersList.order.get(position);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        ////set text
        holder.tv_id.setText(model_order.getDisplay_id());
        holder.tv_status.setText(model_order.getSTATUS());
        holder.tv_date.setText(model_order.getORDER_DATE());
        holder.tv_amount.setText(model_order.getORDER_AMOUNT());
        holder.tv_seller.setText(model_order.getSELLER_NAME());
        ////set image

        if (model_order.getSTATUS().equals("Cancelled")){
            holder.remove.setVisibility(View.VISIBLE);
        }else {
            holder.remove.setVisibility(View.GONE);
        }

       holder.ll_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model_order = OrdersList.order.get(position);
                mContext.startActivity(new Intent(mContext, Orders_details.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("order_id",model_order.getORDER_ID()));

            }
        });
       holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model_order = OrdersList.order.get(position);
                ((OrdersList)mContext).removeOrder(
                        model_order.getORDER_ID()

                );


            }
        });


    }




    @Override
    public int getItemCount() {

        return OrdersList.order.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

           LinearLayout ll_clickable;
        ImageView iv_cart_product, iv_clear_item;
        TextView remove,tv_id,tv_date,tv_status,tv_amount,tv_seller;
        public MyViewHolder(View itemView) {
            super(itemView);
            //  ll_clickable=itemView.findViewById(R.id.ll_clickable);

            ll_clickable=itemView.findViewById(R.id.ll_clickable);


            tv_id=(TextView) itemView.findViewById(R.id.tv_id);
            remove=(TextView) itemView.findViewById(R.id.remove);
            tv_date=(TextView) itemView.findViewById(R.id.tv_date);
            tv_status=(TextView) itemView.findViewById(R.id.tv_status);
            tv_amount=(TextView) itemView.findViewById(R.id.tv_ammount);
            tv_seller=(TextView) itemView.findViewById(R.id.tv_seller);


        }
    }



}
