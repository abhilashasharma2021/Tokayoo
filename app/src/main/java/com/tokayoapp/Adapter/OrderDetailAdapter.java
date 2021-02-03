package com.tokayoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.OrderProductDetails;
import com.tokayoapp.Activities.ProductDetailsActivity;
import com.tokayoapp.Modal.OrderDetailModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.List;


public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

   Context context;
   List<OrderDetailModal> orderDetailModals;

   public OrderDetailAdapter(Context context, List<OrderDetailModal> getDataAdpter){

       this.context=context;
       this.orderDetailModals=getDataAdpter;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_order_detail_layout,parent,false);
       ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final OrderDetailModal orderDetailModal=orderDetailModals.get(position);
        holder.txt_name.setText(orderDetailModal.getName());
        holder.txt_qty.setText(orderDetailModal.getQuantity());
        holder.txt_price.setText(orderDetailModal.getPrice());
        holder.txt_model.setText(orderDetailModal.getModel());
        holder.txt_color.setText(orderDetailModal.getColor());

        try {
            Picasso.with(context).load(orderDetailModal.getPath() + orderDetailModal.getImage()).into(holder.img_product);
        } catch (Exception ignored) {

        }




        holder. ll_order.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
               editor.putString(AppConstant.ORDERID,orderDetailModal.getOrder_id());
               editor.putString(AppConstant.ProdutId, orderDetailModal.getId());
               editor.commit();
             context.startActivity(new Intent(context,OrderProductDetails.class));
           }
       });
    }

    @Override
    public int getItemCount() {
        return orderDetailModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView img_product;
       TextView txt_name,txt_price,txt_model,txt_qty,txt_color;
       LinearLayout ll_order;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_model = itemView.findViewById(R.id.txt_model);
            txt_color = itemView.findViewById(R.id.txt_color);
            txt_qty = itemView.findViewById(R.id.txt_qty);
            ll_order = itemView.findViewById(R.id.ll_order);

        }
    }
}
