package com.tokayoapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.OrderDetailsActivity;
import com.tokayoapp.Modal.HistoryModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.List;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder> {

   Context context;
   List<HistoryModal>historyModalList;

   public PurchaseHistoryAdapter(Context context, List<HistoryModal>getDataAdpter){

       this.context=context;
       this.historyModalList=getDataAdpter;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_purchasedhistory_layout,parent,false);
       ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final HistoryModal historyModal=historyModalList.get(position);

        Log.e("yefgfd",historyModal.getCount());

      //  holder.txt_name.setText(historyModal.getName());
        holder.txt_name.setText("Date"+" "+historyModal.getDate());
        holder.txt_qty.setText(historyModal.getQuantity());
        holder.txt_orderNo.setText(historyModal.getOrderId());
        holder.txt_price.setText(historyModal.getPrice());
        holder.txt_color.setText(historyModal.getColor());
        holder.txt_model.setText(historyModal.getModel());

        String tracking_status=historyModal.getOrder_deliver_status();
        Log.e("sdgtfhb", historyModal.getOrder_deliver_status()+"");

       /* try {
            Picasso.with(context).load(historyModal.getPath()+historyModal.getImage()).into(holder.img_product);
        }
        catch (Exception e){
        }*/

        if (tracking_status.equals("0")){



            holder.txt_progress.setText("In Process");
            holder.txt_progress.setTextColor(context.getResources().getColor(R.color.pink));
          /*  holder.txt_name.setText(historyModal.getName());
            holder.txt_qty.setText(historyModal.getQuantity());
            holder.txt_orderNo.setText(historyModal.getOrderId());
            holder.txt_price.setText(historyModal.getPrice());
            holder.txt_model.setText(historyModal.getModel());*/

          /*  try {
                Picasso.with(context).load(historyModal.getPath()+historyModal.getImage()).into(holder.img_product);
            }
            catch (Exception e){

            }*/

                /*if (position==0){

                    holder.txt_progress.setText("In Progress");
                    holder.txt_progress.setTextColor(context.getResources().getColor(R.color.pink));
                    holder.txt_name.setText(historyModal.getName());
                    holder.txt_qty.setText(historyModal.getQuantity());
                    holder.txt_orderNo.setText(historyModal.getOrderId());
                    holder.txt_price.setText(historyModal.getPrice());
                    holder.txt_model.setText(historyModal.getModel());

                    try {
                        Picasso.with(context).load(historyModal.getPath()+historyModal.getImage()).into(holder.img_product);
                    }
                    catch (Exception e){

                    }
                }else {

                    holder.txt_progress.setText("Ship out");
                    holder.txt_progress.setTextColor(context.getResources().getColor(R.color.blue));
                    holder.rl_count.setVisibility(View.GONE);
                    holder.txt_name.setText(historyModal.getName());
                    holder.txt_qty.setText(historyModal.getQuantity());
                    holder.txt_orderNo.setText(historyModal.getOrderId());
                    holder.txt_price.setText(historyModal.getPrice());
                    holder.txt_model.setText(historyModal.getModel());

                    try {
                        Picasso.with(context).load(historyModal.getPath()+historyModal.getImage()).into(holder.img_product);
                    }
                    catch (Exception e){


                    }
*/
        }



        else {

        holder.txt_progress.setText("Ship out");
        holder.txt_progress.setTextColor(context.getResources().getColor(R.color.blue));
       /* holder.txt_name.setText(historyModal.getName());
        holder.txt_qty.setText(historyModal.getQuantity());
        holder.txt_orderNo.setText(historyModal.getOrderId());
        holder.txt_price.setText(historyModal.getPrice());
        holder.txt_model.setText(historyModal.getModel());
*/
      /*  try {
            Picasso.with(context).load(historyModal.getPath()+historyModal.getImage()).into(holder.img_product);
        }
        catch (Exception e){

        }*/

    }


        holder.rl_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });




        holder.ll_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("rrgf", historyModal.getId()+"");
                Log.e("rrgf", historyModal.getOrderId()+"");
                AppConstant.sharedpreferences =context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.ORDERID, historyModal.getOrderId());
                editor.putString(AppConstant.ProdutId,historyModal.getId());
                editor.commit();
                context.startActivity(new Intent(context,OrderDetailsActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView img_product;
       LinearLayout ll_history;
       RelativeLayout rl_count;
       TextView txt_count,txt_sym,txt_model;
       TextView txt_name,txt_color,txt_price,txt_qty,txt_orderNo,txt_progress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_color = itemView.findViewById(R.id.txt_color);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_qty = itemView.findViewById(R.id.txt_qty);
            ll_history = itemView.findViewById(R.id.ll_history);
            txt_orderNo = itemView.findViewById(R.id.txt_orderNo);
            txt_progress = itemView.findViewById(R.id.txt_progress);
            rl_count = itemView.findViewById(R.id.rl_count);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_sym = itemView.findViewById(R.id.txt_sym);
            txt_model = itemView.findViewById(R.id.txt_model);
        }
    }
}
