package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.jutti.bazaar.shop.shoe.shopping.Model.ModelTracking;
import com.jutti.bazaar.shop.shoe.shopping.Activities.OrderTracking;
import com.jutti.bazaar.shop.shoe.shopping.R;


public class Adapter_tracking extends RecyclerView.Adapter<Adapter_tracking.MyViewHolder> {

    Context mContext;
    ModelTracking model_order;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String user_id = "";

    public Adapter_tracking(Context context) {
        this.mContext = context;
    }

    @Override
    public Adapter_tracking.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tracking, parent, false);
        return new Adapter_tracking.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        model_order = OrderTracking.tracking.get(position);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        ////set text
        holder.tvDate.setText(model_order.getCheckpoint_time());
        holder.tvLocation.setText(model_order.getCity());
        holder.tvMessage.setText(model_order.getMessage());

        ////set image



    }




    @Override
    public int getItemCount() {

        return OrderTracking.tracking.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

           LinearLayout ll_clickable;
        ImageView ivDot;
        TextView tvDate,tvLocation,tvMessage,tv_amount,tv_seller;
        public MyViewHolder(View itemView) {
            super(itemView);
            //  ll_clickable=itemView.findViewById(R.id.ll_clickable);

            tvDate=(TextView) itemView.findViewById(R.id.tvDate);
            ivDot=(ImageView) itemView.findViewById(R.id.ivDot);
            tvLocation=(TextView) itemView.findViewById(R.id.tvLocation);
            tvMessage=(TextView) itemView.findViewById(R.id.tvMessage);



        }
    }



}
