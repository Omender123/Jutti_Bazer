package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.Activities.Add_Address;
import com.jutti.bazaar.shop.shoe.shopping.Activities.AddressActivity;
import com.jutti.bazaar.shop.shoe.shopping.Model.Model_address;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.api.SavedPrefManager;

public class Adapter_address extends RecyclerView.Adapter<Adapter_address.MyViewHolder> {

    Context mContext;
    Model_address model_cart;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String user_id = "";
    int count = 0;

    public Adapter_address(Context context) {
        this.mContext = context;
    }

    @Override
    public Adapter_address.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_address, parent, false);
        return new Adapter_address.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        model_cart = AddressActivity.address.get(position);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        ////set text
        holder.name.setText(model_cart.getName());
        holder.address.setText(model_cart.getLandmark()+ " "+ model_cart.getLocality() + " " + model_cart.getAddress() + " " + model_cart.getCity() + " " + model_cart.getState() + " " + model_cart.getPincode());
        holder.mobile.setText(model_cart.getMobile());

        ////set image


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model_cart = AddressActivity.address.get(position);
                Add_Address.name=model_cart.getName();
                Add_Address.From="";
                Add_Address.address_id=model_cart.getAddress_id();
                Add_Address.pin=model_cart.getPincode();
                Add_Address.locality=model_cart.getLocality();
                Add_Address.landmark=model_cart.getLandmark();
                Add_Address.city=model_cart.getCity();
                Add_Address.state=model_cart.getState();
                Add_Address.add=model_cart.getAddress();
                Add_Address.add1=model_cart.getAddress1();
                Add_Address.phone=model_cart.getMobile();
                mContext.startActivity(new Intent(mContext, Add_Address.class));
            }
        });


        if (AddressActivity.address_id.equals(model_cart.getAddress_id())){
            holder.ll_click.setBackgroundResource(R.drawable.rect_green);


        }else {
            holder.ll_click.setBackgroundResource(R.drawable.rect_grey);
        }

        holder.ll_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model_cart = AddressActivity.address.get(position);

                AddressActivity.address_id=model_cart.getAddress_id();
                SavedPrefManager.saveStringPreferences(mContext,"addSelected",""+model_cart.getAddress_id());


                if (mContext instanceof AddressActivity) {
                    ((AddressActivity)mContext).courierCharge(
                          ""+model_cart.getAddress_id());
                }


                notifyDataSetChanged();
                notifyItemChanged(holder.getAdapterPosition());
                Log.d("clicked", "onClick: "+  AddressActivity.address_id);
                // Toast.makeText(mContext, ""+    Checkout.address_id, Toast.LENGTH_SHORT).show();




            }
        });






        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model_cart = AddressActivity.address.get(position);
                if (mContext instanceof AddressActivity) {
                    ((AddressActivity)mContext).delete(""+model_cart.getAddress_id());
                } }
        });




    }


    @Override
    public int getItemCount() {
        return AddressActivity.address.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_click;
        ImageView delete, iv_clear_item;
        Button add, sub;

        TextView name, address, mobile, edit;

        public MyViewHolder(View itemView) {
            super(itemView);
            //  ll_clickable=itemView.findViewById(R.id.ll_clickable);
            ll_click = itemView.findViewById(R.id.ll_click);
            delete = itemView.findViewById(R.id.delete);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            mobile = (TextView) itemView.findViewById(R.id.mobile);
            edit = (TextView) itemView.findViewById(R.id.edit);


        }
    }


}
