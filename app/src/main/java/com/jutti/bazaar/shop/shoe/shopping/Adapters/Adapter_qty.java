package com.jutti.bazaar.shop.shoe.shopping.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.Activities.My_cart;
import com.jutti.bazaar.shop.shoe.shopping.R;


public class Adapter_qty extends RecyclerView.Adapter<Adapter_qty.MyViewHolder> {

    Context mContext;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;


    String user_id = "";

    public Adapter_qty(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_check, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        ////set text
        holder.cbSelect.setText(My_cart.stock_count.get(position));

    //    holder.cbSelect.setChecked(My_cart.selectedPosition == position);




        for (int j = 0; j < My_cart.stock_count.size(); j++) {

            if (My_cart.stock_count.get(j).equals(My_cart.Selected_qty)) {
                My_cart.selectedPosition = j;
                Log.e("TAGVselectedPosition", "onBindViewHolder: " + My_cart.selectedPosition);

            }
        }
        Log.e("TAGVselectedPosition", "onBindViewHolder: " + My_cart.selectedPosition);







        //check cbSelect and uncheck previous selected button
        holder.cbSelect.setChecked(position == My_cart.selectedPosition);
        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == My_cart.selectedPosition) {
                    holder.cbSelect.setChecked(false);

                    My_cart.selectedPosition = -1;
                } else {
                    My_cart.selectedPosition = position;

                    My_cart.Selected_qty=""+holder.cbSelect.getText();

                  //  Toast.makeText(mContext,   My_cart.Selected_qty+" pos "+My_cart.selectedPosition, Toast.LENGTH_SHORT).show();
                    if (mContext instanceof My_cart) {
                        ((My_cart)mContext).change_qty1(
                               ""+holder.cbSelect.getText()
                        );
                    }

                    notifyDataSetChanged();
                }
            }
        });





        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                if(isChecked)
                {

                    holder.cbSelect.setChecked(true);
//                    Log.d("resp sel", "onCheckedChanged: "+StaffActivity.selected_permissions);
                    //movieList.get(position).setSelected(true);
                }
                else
                {
//                    StaffActivity.selected_permissions.remove(StaffActivity.permission_values.get(position));
//                    Log.d("resp de", "onCheckedChanged: "+StaffActivity.selected_permissions);
                    holder.cbSelect.setChecked(false);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return My_cart.stock_count.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        CheckBox cbSelect;

        public MyViewHolder(View itemView) {
            super(itemView);


            cbSelect = itemView.findViewById(R.id.cb);


        }
    }


}
