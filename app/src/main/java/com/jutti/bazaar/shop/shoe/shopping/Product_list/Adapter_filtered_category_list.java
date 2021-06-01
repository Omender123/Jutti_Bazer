package com.jutti.bazaar.shop.shoe.shopping.Product_list;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.R;

public class Adapter_filtered_category_list extends RecyclerView.Adapter<Adapter_filtered_category_list.MyViewHolder> {

    Context mContext;

    public Adapter_filtered_category_list(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.common_filtered_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (Product_list.chcklst_Selections2.isEmpty()){
            holder.ll_bahrvala.setBackgroundResource(R.drawable.rect_grey_filter);
            holder.img_chck.setImageResource(R.drawable.unchecked_item);
        }
        holder.title.setText(Product_list.categoryNameList.get(position));


        if (Product_list.chcklst_Selections2.contains(Product_list.categoryIDList.get(position))){
            holder.img_chck.setImageResource(R.drawable.checked_item);
            holder.ll_bahrvala.setBackgroundResource(R.drawable.rect_green);
            Product_list.selcted_category= "" + Product_list.chcklst_Selections2.toString().replace("[", "").replace("]", "");

            holder.title.setTextColor(Color.WHITE);
        }


        holder.ll_bahrvala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// notifyDataSetChanged();

                if (Product_list.chcklst_Selections2.contains(Product_list.categoryIDList.get(position))){
                    Product_list.chcklst_Selections2.remove(Product_list.categoryIDList.get(position));

                    holder.img_chck.setImageResource(R.drawable.unchecked_item);
                    holder.ll_bahrvala.setBackgroundResource(R.drawable.rect_grey_filter);
                    holder.title.setTextColor(Color.parseColor("#777777"));

                }else {
                    Product_list.chcklst_Selections2.add(Product_list.categoryIDList.get(position));
                    holder.title.setTextColor(Color.WHITE);
                    holder.img_chck.setImageResource(R.drawable.checked_item);
                    holder.ll_bahrvala.setBackgroundResource(R.drawable.rect_green);
                }
                Product_list.selcted_category= "" + Product_list.chcklst_Selections2.toString().replace("[", "").replace("]", "");


                Log.i("position: ", position + "");
                Log.i("chcklst_Selections: ", Product_list.chcklst_Selections2 + "");
                Log.i("chcklst_id: ", Product_list.chcklst_id2 + "");
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return Product_list.categoryIDList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        ImageView img_chck;
        LinearLayout ll_bahrvala;
        CardView cv_selection;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_name);
            cv_selection = (CardView) itemView.findViewById(R.id.cv_selection);
            img_chck = (ImageView) itemView.findViewById(R.id.checkedimg);
            ll_bahrvala = (LinearLayout) itemView.findViewById(R.id.ll_item);
        }
    }

}