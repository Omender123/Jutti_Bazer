package com.jutti.bazaar.shop.shoe.shopping.Favourites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jutti.bazaar.shop.shoe.shopping.Activities.Home;
import com.jutti.bazaar.shop.shoe.shopping.Product_details.Product_Details;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Utils;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Adapter_favourite extends RecyclerView.Adapter<Adapter_favourite.MyViewHolder> {

    Context mContext;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    String user_id = "";

    public Adapter_favourite(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Adapter_favourite.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fav, parent, false);
        return new Adapter_favourite.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        holder.tv_name.setText(Wishlist.product_title.get(position));
        holder.brand.setText(Wishlist.brand_name.get(position));
        holder.tv_price.setText(Wishlist.category_name.get(position));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);

        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(Utils.imgurl+Wishlist.product_image.get(position))


                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressbar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        holder.progressbar.setVisibility(View.GONE);
                        return false;
                    }

                })
                .into(holder.iv_imagecart);


        holder.ll_clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mContext.startActivity(new Intent(mContext, Product_Details.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("product_id",Wishlist.product_id.get(position)));
                Log.d("product_id", "onClick: "+Wishlist.product_id.get(position));
            }
        });


//       holder.ll_clickable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, ProductDetails.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .putExtra("slug",Wishlist.SLUG.get(position)));
//
//            }
//        });
//       holder.bt_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, ProductDetails.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .putExtra("slug",Wishlist.SLUG.get(position)));
//
//            }
//        });
       holder.im_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext)
                        .setTitle("Remove favourite")
                        .setMessage("Are you sure you want to Remove favourite product?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
//                                if (mContext instanceof Wishlist) {
 //                                   ((Wishlist)mContext).remove(
//                                            "" +  sharedPreferences.getString("user_id", ""),
//                                            "" + sharedPreferences.getString("user_login_token", "")
//                                            ,""+Wishlist.product_id.get(position)
 //                                   );
//                                }

                                removefavourite("" +  sharedPreferences.getString("user_id", ""),
                                        "" + sharedPreferences.getString("user_login_token", "")
                                        ,""+Wishlist.product_id.get(position));
                                Log.d("iddddddd adater", "onClick: "+Wishlist.product_id.get(position));

                                notifyDataSetChanged();
                                notifyItemChanged(holder.getAdapterPosition());

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            }
        });


    }




    @Override
    public int getItemCount() {
        return Wishlist.product_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

           LinearLayout ll_clickable;
           ProgressBar progressbar;
        ImageView iv_imagecart, im_fav;
        Button bt_add;
        TextView tv_name,tv_price,tv_discout,brand,cart_product_total,tv_type;
        public MyViewHolder(View itemView) {
            super(itemView);
              ll_clickable=itemView.findViewById(R.id.llclick_fav);
           im_fav=itemView.findViewById(R.id.im_fav);
            progressbar=itemView.findViewById(R.id.progressbar);
            iv_imagecart=itemView.findViewById(R.id.imageView);
            brand=itemView.findViewById(R.id.brand);
            tv_name=(TextView) itemView.findViewById(R.id.tv_name);
            tv_price=(TextView) itemView.findViewById(R.id.price);



        }
    }

    public void removefavourite(final String user_id, String user_login_token, String product_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.base_url)
                .build();
        Wishlist.Service service = retrofit.create(Wishlist.Service.class);
        Call<ResponseBody> call;
        call = service.removefavourite(user_id, user_login_token, product_id);
        //     Toast.makeText(this, ""+product_id, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {


                    try {

                        String result = response.body().string();

                        JSONObject obj = new JSONObject(result);

                        Log.d("respp", "onResponse: " + result);
                        String success = obj.optString("success");
                        String message = obj.optString("message");
//                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction1.replace(R.id.container1, new Wishlist());
//                        fragmentTransaction1.commit();
//                        fragmentTransaction1.addToBackStack(null);
                         Toast.makeText(mContext, ""+message, Toast.LENGTH_SHORT).show();




                        ((Home)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.container1, new Wishlist(),"OptionsFragment").addToBackStack(null).commit();



                    } catch (Exception e) {

                        //  Toast.makeText(mContext, ""+e, Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {


                    // Toast.makeText(mContext, "Aww Snap. Error Code: 401. Please Try again later", Toast.LENGTH_LONG).show();
                } else {


                    Log.d("Message", "code..." + response.code() + " message..." + response.message() + " body..." + response.body());
                    //  Toast.makeText(mContext, "An unexpected error has occured. We're working to fix it! Sorry for inconvenience, Please Try Again later" + " message..." + response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Toast.makeText(mContext, "Something went wrong at server!", Toast.LENGTH_SHORT).show();


            }
        });
    }




}
