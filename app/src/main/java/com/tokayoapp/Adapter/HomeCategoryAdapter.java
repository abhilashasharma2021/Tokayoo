package com.tokayoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.SubCategoryActivity;
import com.tokayoapp.Modal.CartModal;
import com.tokayoapp.Modal.HomeCategoryModal;
import com.tokayoapp.Modal.ProductListModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    Context context;
    List<HomeCategoryModal> categoryAdapterList;
    ArrayList<HomeCategoryModal> categoryArrayList;

    public HomeCategoryAdapter(Context context, List<HomeCategoryModal> getDataAdpter) {
        this.context = context;
        this.categoryAdapterList = getDataAdpter;
        categoryArrayList = new ArrayList<>(categoryAdapterList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_category_layout, parent, false);
        HomeCategoryAdapter.ViewHolder viewHolder = new HomeCategoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final HomeCategoryModal categoryModal = categoryAdapterList.get(position);


        holder.tx_catName.setText(categoryModal.getName());
        Log.e("dlfkdk", categoryModal.getPath() + categoryModal.getImage());
        try {
            Picasso.with(context).load(categoryModal.getPath() + categoryModal.getImage()).into(holder.img_cat);
        } catch (Exception e) {
        }

        holder.cardRod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.BrandId, categoryModal.getBrandId());
                editor.putString(AppConstant.BrandName, categoryModal.getName());
                editor.putString(AppConstant.SearchQuery, "");
                editor.commit();
                context.startActivity(new Intent(context, SubCategoryActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_cat;
        TextView tx_catName;
        CardView cardRod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cat = itemView.findViewById(R.id.img_cat);
            cardRod = itemView.findViewById(R.id.cardRod);
            tx_catName = itemView.findViewById(R.id.tx_catName);

        }
    }



    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        categoryAdapterList.clear();

        if (charText.length() == 0) {
            categoryAdapterList.addAll(categoryArrayList);
        } else {
            for (HomeCategoryModal wp : categoryArrayList) {

                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    categoryAdapterList.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }
}
