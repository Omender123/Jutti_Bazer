package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.Activities.NotificationActivity;
import com.jutti.bazaar.shop.shoe.shopping.R;



public class Adapter_notification extends RecyclerView.Adapter<Adapter_notification.MyViewHolder> {

    Context mContext;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    public Adapter_notification(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);

        holder.tv_date.setText(NotificationActivity.DATE_ADDED.get(position));

        holder.tv_notification.setText(NotificationActivity.NOTIFICATION_TEXT.get(position));




    }

    @Override
    public int getItemCount() {
        return NotificationActivity.ID.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_notification,tv_date,tv_title;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_notification = (TextView) itemView.findViewById(R.id.tv_notification);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);



        }



        }







}

