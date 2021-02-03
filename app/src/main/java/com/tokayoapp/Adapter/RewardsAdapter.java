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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.RewardsDetailsActivity;
import com.tokayoapp.Modal.RewardModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.List;


public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.ViewHolder> {

    private Context context;
    List<RewardModal> favouriteList;

    public RewardsAdapter(Context context, List<RewardModal> getDataAdapter) {
        this.context = context;
        this.favouriteList = getDataAdapter;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_rewards_layout, parent, false);

        RewardsAdapter.ViewHolder viewHolder = new RewardsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final RewardModal rewardModal = favouriteList.get(position);

        holder.txt_product_name.setText(rewardModal.getName());
        holder.txt_point.setText(rewardModal.getPoint());

        try {

            Picasso.with(context).load(rewardModal.getPath() + rewardModal.getImage()).into(holder.img_product);
        }

        catch(Exception e) {


        }

        Log.e("dsfclkjdsj", rewardModal.getRewardId() + "");
        holder.rl_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.RewardId, rewardModal.getRewardId());
                editor.putString(AppConstant.FavStatus, rewardModal.getFav_status());/*status 0=no fav, 1=fav product*/
                editor.commit();
                Log.e("fvkjdj", rewardModal.getFav_status() + "");
                context.startActivity(new Intent(context, RewardsDetailsActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        RelativeLayout rl_reward;
        TextView txt_product_name, txt_point;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            rl_reward = itemView.findViewById(R.id.rl_reward);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_point = itemView.findViewById(R.id.txt_point);


        }
    }

}
