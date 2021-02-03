package com.tokayoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.RedempItemDetailActivity;
import com.tokayoapp.Modal.RedemptionHistoryModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.List;

public class RedemptionHistoryAdapter extends RecyclerView.Adapter<RedemptionHistoryAdapter.ViewHolder> {

   Context context;
   List<RedemptionHistoryModal> redemptionHistoryModals;

   public RedemptionHistoryAdapter(Context context, List<RedemptionHistoryModal> getDataAdpter){

       this.context=context;
       this.redemptionHistoryModals=getDataAdpter;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_redemptionhistory_layout,parent,false);
       ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final RedemptionHistoryModal historyModal=redemptionHistoryModals.get(position);

        holder.txt_name.setText(historyModal.getName());
        holder.txt_date.setText(historyModal.getDate());
        holder.txt_point.setText(historyModal.getPoints());
        holder.txt_time.setText(historyModal.getTime());
        holder.txt_redemNo.setText(historyModal.getRedemptionNumber());

        if(historyModal.getOrder_deliver_status().equals("0")){

            holder.txt_progress.setText("In Process");
        }
        else {

            holder.txt_progress.setText("Ship out");
        }


       final String id=historyModal.getId();
        Log.e("ihifujiv",historyModal.getId()+"");

        try {
            Picasso.with(context).load(historyModal.getPath()+historyModal.getImage()).into(holder.img_product);
        }
        catch (Exception e){


        }


        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("dkjsk", historyModal.getId()+"");
                Log.e("dkjsk", historyModal.getRedemptionNumber()+"");
                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.ORDERID, historyModal.getRedemptionNumber());
                editor.putString(AppConstant.ProdutId,historyModal.getProduct_id());
                editor.commit();
                context.startActivity(new Intent(context, RedempItemDetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return redemptionHistoryModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView img_product;
        LinearLayout ll;
       TextView txt_name,txt_point,txt_time,txt_date,txt_redemNo,txt_progress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_point = itemView.findViewById(R.id.txt_point);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);
            ll = itemView.findViewById(R.id.ll);
            txt_redemNo = itemView.findViewById(R.id.txt_redemNo);
            txt_progress = itemView.findViewById(R.id.txt_progress);
        }
    }
}
