package com.jutti.bazaar.shop.shoe.shopping.Search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.Product_details.Product_Details;
import com.jutti.bazaar.shop.shoe.shopping.Product_list.Product_list;
import com.jutti.bazaar.shop.shoe.shopping.R;

public class Adapter_search extends RecyclerView.Adapter<Adapter_search.MyViewHolder> {

    Context mContext;

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String user_id = "";
    int count = 0;

    public Adapter_search(Context context) {
        this.mContext = context;
    }

    @Override
    public Adapter_search.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);
        return new Adapter_search.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        ////set text
        holder.tv_title.setText(Search.title.get(position));
        holder.tv_cat.setText(Search.cat.get(position));


        holder.ll_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (Search.cat.get(position).equals("PRODUCT")){
                   mContext.startActivity(new Intent(mContext, Product_Details.class)
                           .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                           .putExtra("product_id",Search.id.get(position)));
                   Log.d("product_id", "onClick: "+Search.id.get(position));

               }else {

                   Product_list.seller_id = Search.id.get(position);
                   Product_list.title = Search.title.get(position);
                   mContext.startActivity(new Intent(mContext, Product_list.class));
               }






            }
        });





    }


    @Override
    public int getItemCount() {
        return Search.id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_click;
        ImageView delete;
        TextView tv_title, tv_cat;

        public MyViewHolder(View itemView) {
            super(itemView);
            //  ll_clickable=itemView.findViewById(R.id.ll_clickable);
            ll_click = itemView.findViewById(R.id.ll_click);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_cat = (TextView) itemView.findViewById(R.id.tv_cat);



        }
    }


}
