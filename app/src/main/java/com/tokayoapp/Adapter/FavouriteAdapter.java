package com.tokayoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.FavDetailsActivity;
import com.tokayoapp.Activities.RewardsDetailsActivity;
import com.tokayoapp.Modal.FavouriteModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.List;


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private Context context;
    List<FavouriteModal> favouriteList ;

    public FavouriteAdapter(Context context, List<FavouriteModal>getDataAdapter) {
        this.context = context;
        this.favouriteList = getDataAdapter;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_favourite_layout, parent, false);

        FavouriteAdapter.ViewHolder viewHolder = new FavouriteAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final FavouriteModal favouriteModal=favouriteList.get(position);

        String type=favouriteModal.getType();/*type 0=product, 1=reward*/
        Log.e("sggdfg",favouriteList.size()+"");




        holder.txt_product_name.setText(favouriteModal.getName());
      //  holder.txt_price.setText(favouriteModal.getPrice());
      //  holder.img_product.setImageResource(favouriteModal.getImage());
        Log.e("efildui",favouriteModal.getImage()+"");

       try {
            Picasso.with(context).load(favouriteModal.getImage()).into(holder.img_product);
        }catch (Exception e){


        }
       if (type.equals("0")){
           holder.txt_price.setText(favouriteModal.getPrice());
           holder.rl_fav.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                   editor.putString(AppConstant.ProdutId, favouriteModal.getProductId());
                   editor.putString(AppConstant.FavStatus, favouriteModal.getStatus());
                   editor.commit();
                   context.startActivity(new Intent(context, FavDetailsActivity.class));/*/*status 0=no fav, 1=fav product*/
               }
           });
       }

       else{
           holder.rl_point.setVisibility(View.VISIBLE);
           holder.rl_point.setVisibility(View.VISIBLE);
           holder.rl1.setVisibility(View.GONE);
           holder.txt_point.setText(favouriteModal.getPrice());
           holder.rl_fav.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                   editor.putString(AppConstant.RewardId, favouriteModal.getProductId());
                   editor.putString(AppConstant.FavStatus, favouriteModal.getStatus());/*status 0=no fav, 1=fav product*/
                   editor.commit();
                   context.startActivity(new Intent(context, RewardsDetailsActivity.class));
               }
           });
       }


    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView txt_product_name, txt_price,txt_point;
        RelativeLayout rl_fav,rl_point,rl1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            rl_fav = itemView.findViewById(R.id.rl_fav);
            txt_point = itemView.findViewById(R.id.txt_point);
            rl_point = itemView.findViewById(R.id.rl_point);
            rl1 = itemView.findViewById(R.id.rl1);


        }
    }

}