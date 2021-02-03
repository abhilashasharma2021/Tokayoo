package com.tokayoapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.FavDetailsActivity;
import com.tokayoapp.Activities.ProductDetailsActivity;
import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.R;

import java.util.ArrayList;


public class FavouriteDetailAdapter extends RecyclerView.Adapter<FavouriteDetailAdapter.ViewHolder> {

    Context context;
    ArrayList<ProductDetailModal>productListModals;
    String imgs="",paths="";
    public FavouriteDetailAdapter(Context context, ArrayList<ProductDetailModal>getDataAdapter) {
        this.context = context;
        this.productListModals=getDataAdapter;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_product_detail_layout, parent, false);

        FavouriteDetailAdapter.ViewHolder viewHolder = new FavouriteDetailAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ProductDetailModal productDetailModal=productListModals.get(position);

        try {
            Picasso.with(context).load(productDetailModal.getPath()+productDetailModal.getImage()).into(holder.img_product1);
        }catch (Exception e){


        }

        holder.img_product1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgs = productDetailModal.getImage();
                paths = productDetailModal.getPath();
                Log.e("sbjsb", imgs + paths);

                try {
                    Picasso.with(context).load(paths + imgs).into(ProductDetailsActivity.imgProduct);
                    notifyDataSetChanged();

                } catch (Exception e) {


                }

            }
        });

        FavDetailsActivity.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.enlarge_slider_image_layout);

                ViewPager viewPager = dialog.findViewById(R.id.myViewpager);
                ViewpagerAdapter viewpagerAdapter=new ViewpagerAdapter(context,productListModals);
                viewPager.setAdapter(viewpagerAdapter);

                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return productListModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product1;
        TextView tx_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product1 = itemView.findViewById(R.id.img_product1);
            tx_img = itemView.findViewById(R.id.tx_img);




        }
    }



}
