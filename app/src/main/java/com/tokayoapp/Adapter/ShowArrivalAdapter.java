package com.tokayoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.ProductDetailsActivity;
import com.tokayoapp.Modal.HomeNewArrivalModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.List;

public class ShowArrivalAdapter extends RecyclerView.Adapter<ShowArrivalAdapter.ViewHolder> {

   Context context;
   List<HomeNewArrivalModal>arrivalAdapterList;

   public ShowArrivalAdapter(Context context, List<HomeNewArrivalModal> getDataAdpter){

       this.context=context;
       this.arrivalAdapterList=getDataAdpter;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_allnew_arrival_layout,parent,false);
       ShowArrivalAdapter.ViewHolder viewHolder=new ShowArrivalAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final HomeNewArrivalModal arrivalModal=arrivalAdapterList.get(position);


        holder.txt_price.setText(arrivalModal.getPrice());
        holder.txtOne.setText(arrivalModal.getName());
        try {
            Picasso.with(context).load(arrivalModal.getPath()+arrivalModal.getImage()).into(holder.img);
        }catch (Exception e){


        }


        holder.rl_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.ProdutId, arrivalModal.getId());
                editor.putString(AppConstant.ProdutStatus, "New Arrival");
                editor.commit();
                context.startActivity(new Intent(context, ProductDetailsActivity.class));

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrivalAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView img;
       RelativeLayout rl_new;
       TextView txt_price,txtOne;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            rl_new = itemView.findViewById(R.id.rl_new);
            txtOne = itemView.findViewById(R.id.txtOne);
            txt_price = itemView.findViewById(R.id.txt_price);

        }
    }
}
