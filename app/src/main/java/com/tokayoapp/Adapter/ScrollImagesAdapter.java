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

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.ProductDetailsActivity;
import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.Modal.SliderModel;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.ArrayList;

public class ScrollImagesAdapter extends SliderViewAdapter<ScrollImagesAdapter.SliderAdapterVH> {

    Context context;
    ArrayList<ProductDetailModal> productListModals;

    String imgs="",paths="";

    public ScrollImagesAdapter(Context context,ArrayList<ProductDetailModal>getDataAdapter) {
        this.context = context;
        this.productListModals=getDataAdapter;


    }

    @NonNull
    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_adapter, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(ScrollImagesAdapter.SliderAdapterVH viewHolder, int position) {
        final ProductDetailModal dataAdapterOBJ = productListModals.get(position);


        try {
            Picasso.with(context).load(dataAdapterOBJ.getPath() + dataAdapterOBJ.getImage()).into(viewHolder.img_product1);
        } catch (Exception e) {


        }

    }

    @Override
    public int getCount() {
        return productListModals.size();
    }



    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        ImageView img_product1,img_product2;
        TextView tx_img;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            img_product1 = itemView.findViewById(R.id.img_product1);
            img_product2 = itemView.findViewById(R.id.img_product2);
            tx_img = itemView.findViewById(R.id.tx_img);




        }
    }



}
