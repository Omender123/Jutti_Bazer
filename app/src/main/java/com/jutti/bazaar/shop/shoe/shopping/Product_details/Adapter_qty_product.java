package com.jutti.bazaar.shop.shoe.shopping.Product_details;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.R;


public class Adapter_qty_product extends RecyclerView.Adapter<Adapter_qty_product.MyViewHolder> {

    Context mContext;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;


    String user_id = "";

    public Adapter_qty_product(Context mContext) {
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
        holder.cbSelect.setText(Product_Details.stock.get(position));

    //    holder.cbSelect.setChecked(Product_Details.selectedPosition == position);

        if (Product_Details.selectedPosition!=-1){

            for (int j = 0; j<  Product_Details.stock.size(); j++) {

                if ( Product_Details.stock.get(position).equals(Product_Details.selectedPosition)){
                    holder.cbSelect.setChecked(true);
                    break;
                }
            }


        }






        //check cbSelect and uncheck previous selected button
        holder.cbSelect.setChecked(position == Product_Details.selectedPosition);
        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == Product_Details.selectedPosition) {
                    holder.cbSelect.setChecked(false);

                    Product_Details.selectedPosition = -1;
                } else {
                    Product_Details.selectedPosition = position;
                    Product_Details.Selected_qty=""+holder.cbSelect.getText();
                   // Toast.makeText(mContext, "select : "+position+holder.cbSelect.getText(), Toast.LENGTH_SHORT).show();
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
        return Product_Details.stock.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        CheckBox cbSelect;

        public MyViewHolder(View itemView) {
            super(itemView);


            cbSelect = itemView.findViewById(R.id.cb);


        }
    }


}
