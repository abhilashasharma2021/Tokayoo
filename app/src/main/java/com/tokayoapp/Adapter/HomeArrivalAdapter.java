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
import com.tokayoapp.Activities.ProductDetailsActivity;
import com.tokayoapp.Modal.HomeNewArrivalModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeArrivalAdapter extends RecyclerView.Adapter<HomeArrivalAdapter.ViewHolder> {

    Context context;
    List<HomeNewArrivalModal> arrivalAdapterList;
    ArrayList<HomeNewArrivalModal> allproductyArrayList;
    ArrayList<String> listData;
    public HomeArrivalAdapter(Context context, List<HomeNewArrivalModal> getDataAdpter) {

        this.context = context;
        this.arrivalAdapterList = getDataAdpter;
        allproductyArrayList = new ArrayList<>(arrivalAdapterList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_new_arrival_layout, parent, false);
        HomeArrivalAdapter.ViewHolder viewHolder = new HomeArrivalAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final HomeNewArrivalModal arrivalModal = arrivalAdapterList.get(position);
        Log.e("dgdgdgd",arrivalModal.getPath() + arrivalModal.getImage());
        holder.txt_price.setText(arrivalModal.getPrice());

        if(arrivalModal.getName().length()>=16){
            String strData="";
            listData= new ArrayList<String>();
            for (int i = 0; i <arrivalModal.getName().length() ; i++) {
                if(i<15){
                      Character str=arrivalModal.getName().charAt(i);
                      String strGetData= String.valueOf(str);
                      listData.add(strGetData);
                }
            }

            String character="";
            for (int i = 0; i <listData.size() ; i++) {
               if(character.equals("")){
                   character=listData.get(i);
               }
               else {
                   character=character+listData.get(i);
               }
            }
            Log.e("Dfgdfgdfgdg",character+"...");
           holder.txtOne.setText(character+"...");
        }

        else {
            Log.e("sdfsdfsdffs","Else"+arrivalModal.getName());
            holder.txtOne.setText(arrivalModal.getName());
        }

        try {
            Picasso.with(context).load(arrivalModal.getPath() + arrivalModal.getImage()).into(holder.img);
        } catch (Exception e) {
        }

        holder.rl_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("fdgfdgdfd",arrivalModal.getId());

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
        TextView txt_price, txtOne;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            rl_new = itemView.findViewById(R.id.rl_new);
            txtOne = itemView.findViewById(R.id.txtOne);
            txt_price = itemView.findViewById(R.id.txt_price);

        }
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrivalAdapterList.clear();

        if (charText.length() == 0) {
            arrivalAdapterList.addAll(allproductyArrayList);
        } else {
            for (HomeNewArrivalModal wp : allproductyArrayList) {

                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrivalAdapterList.add(wp);
                }
              /*
                else if (wp.getDescription().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataAdapters.add(wp);
                } else if (wp.getLocation().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataAdapters.add(wp);
                }*/

            }
        }
        notifyDataSetChanged();
    }

}
