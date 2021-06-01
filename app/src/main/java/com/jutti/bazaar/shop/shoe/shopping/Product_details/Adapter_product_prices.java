package com.jutti.bazaar.shop.shoe.shopping.Product_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jutti.bazaar.shop.shoe.shopping.R;


public class Adapter_product_prices extends RecyclerView.Adapter<Adapter_product_prices.MyViewHolder> {

    Context mContext;

    public static String pos="";



    public Adapter_product_prices(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_price_table, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.price.setText(Product_Details.price_product.get(position));


       if (Product_Details.price_max_limit_type.get(position).equals("No") && Product_Details.price_max_qty.get(position).equals("")){
           holder.tv_maxqty.setText("");
           holder.tv_minqty.setText("");

           holder.tv_qty.setText(Product_Details.price_min_qty.get(position)+"+");
       }else{

           holder.tv_maxqty.setText(Product_Details.price_max_qty.get(position));
           holder.tv_minqty.setText(Product_Details.price_min_qty.get(position));
       }


//       holder.ll_clickable.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//
//
//               Toast.makeText(mContext, ""+position, Toast.LENGTH_SHORT).show();
//
//               notifyItemRemoved(position);
//           }
//       });

    }

    @Override
    public int getItemCount() {
       // Toast.makeText(mContext, ""+Product_Details.price_product.size(), Toast.LENGTH_SHORT).show();
        return Product_Details.price_product.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_maxqty,tv_minqty,tv_qty;
        ImageView iv_image;
        TextView price;
        LinearLayout ll_clickable;
        public MyViewHolder(View itemView) {
            super(itemView);
            ll_clickable=itemView.findViewById(R.id.ll_clickable);

            tv_maxqty=(TextView) itemView.findViewById(R.id.toqty);
            tv_qty=(TextView) itemView.findViewById(R.id.qtysoon);
            tv_minqty=(TextView) itemView.findViewById(R.id.fromqty);
            price=(TextView) itemView.findViewById(R.id.qtyprice);


        }
    }

}