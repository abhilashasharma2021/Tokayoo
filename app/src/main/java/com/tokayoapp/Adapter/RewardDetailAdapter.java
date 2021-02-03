package com.tokayoapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.ProductDetailsActivity;
import com.tokayoapp.Activities.RewardsDetailsActivity;
import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.Modal.RewardDetailModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.ArrayList;


public class RewardDetailAdapter extends RecyclerView.Adapter<RewardDetailAdapter.ViewHolder> {

    Context context;
    ArrayList<RewardDetailModal>productListReward;
    String imgs="",paths="";
    public RewardDetailAdapter(Context context, ArrayList<RewardDetailModal>getDataAdapter) {
        this.context = context;
        this.productListReward=getDataAdapter;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_product_detail_layout, parent, false);

        RewardDetailAdapter.ViewHolder viewHolder = new RewardDetailAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final RewardDetailModal rewardDetailModal=productListReward.get(position);


   /*     if (position==0) {

            holder.img_product1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.activity_slider_pop_up_image);


                    ImageView img = dialog.findViewById(R.id.img);

                    try {
                        Picasso.with(context).load(rewardDetailModal.getPath() + rewardDetailModal.getImage()).into(holder.img_product1);
                    } catch (Exception e) {


                    }

                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });

*/



            Log.e("fgdrgv",rewardDetailModal.getPath()+rewardDetailModal.getImage());

        try {
            Picasso.with(context).load(rewardDetailModal.getPath()+rewardDetailModal.getImage()).into(holder.img_product1);
        }catch (Exception e){


        }



        holder.img_product1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgs = rewardDetailModal.getImage();
                paths = rewardDetailModal.getPath();
                Log.e("dfgbhn", imgs + paths);
                AppConstant.sharedpreferences =context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.ProductSubImages, paths + imgs);
                editor.commit();

                try {
                    Picasso.with(context).load(paths + imgs).into(RewardsDetailsActivity.imgProduct);
                    notifyDataSetChanged();

                } catch (Exception e) {


                }

            }
        });

       /* RewardsDetailsActivity.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.enlarge_slider_image_layout);

                ViewPager viewPager = dialog.findViewById(R.id.myViewpager);
                ViewpagerAdapterRewardDetail viewpagerAdapter=new ViewpagerAdapterRewardDetail(context,productListReward);
                viewPager.setAdapter(viewpagerAdapter);

                dialog.show();

            }
        });
*/
    }






    @Override
    public int getItemCount() {
        return productListReward.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product1;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product1 = itemView.findViewById(R.id.img_product1);




        }
    }



}
