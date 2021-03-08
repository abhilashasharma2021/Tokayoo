package com.tokayoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.Modal.RedemptionDetailModal;
import com.tokayoapp.R;

import java.util.ArrayList;

public class ScrollImagesRedemptiondetailsAdapter  extends SliderViewAdapter<ScrollImagesRedemptiondetailsAdapter.SliderAdapterVH> {

    Context context;
    ArrayList<RedemptionDetailModal> productListModals;

    String imgs="",paths="";

    public ScrollImagesRedemptiondetailsAdapter(Context context,ArrayList<RedemptionDetailModal>getDataAdapter) {
        this.context = context;
        this.productListModals=getDataAdapter;


    }

    @NonNull
    @Override
    public ScrollImagesRedemptiondetailsAdapter.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_adapter, null);
        return new ScrollImagesRedemptiondetailsAdapter.SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(ScrollImagesRedemptiondetailsAdapter.SliderAdapterVH viewHolder, int position) {
        final RedemptionDetailModal dataAdapterOBJ = productListModals.get(position);


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
