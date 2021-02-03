package com.tokayoapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.ProductDetailsActivity;
import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.ArrayList;

public class ScrollImagesAdapter extends RecyclerView.Adapter<ScrollImagesAdapter.ViewHolder> {

    Context context;
    ArrayList<ProductDetailModal> productListModals;

    String imgs="",paths="";

    public ScrollImagesAdapter(Context context,ArrayList<ProductDetailModal>getDataAdapter) {
        this.context = context;
        this.productListModals=getDataAdapter;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_adapter, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ProductDetailModal productDetailModal = productListModals.get(position);


        Log.e("sfdjoifv", productDetailModal.getPath() + productDetailModal.getImage() + "");

        try {
            Picasso.with(context).load(productDetailModal.getPath() + productDetailModal.getImage()).into(holder.img_product1);
        } catch (Exception e) {
            Log.e("hfjhvjc", e.getMessage());

        }
    }

    @Override
    public int getItemCount() {
        return productListModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product1,img_product2;
        TextView tx_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product1 = itemView.findViewById(R.id.img_product1);
            img_product2 = itemView.findViewById(R.id.img_product2);
            tx_img = itemView.findViewById(R.id.tx_img);




        }
    }



}
