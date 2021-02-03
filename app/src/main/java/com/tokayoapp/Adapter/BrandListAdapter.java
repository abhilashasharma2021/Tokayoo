package com.tokayoapp.Adapter;

import android.content.Context;
import android.support.v4.media.session.IMediaControllerCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.SubCategoryActivity;
import com.tokayoapp.Modal.BrandNameModal;
import com.tokayoapp.Modal.ProductListModal;
import com.tokayoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.tokayoapp.Utils.API.show_filter_brands;


public class BrandListAdapter extends RecyclerView.Adapter<BrandListAdapter.ViewHolder> {

    private Context context;
    List<BrandNameModal> brandNameModalList ;
    ProductListAdapter productListAdapter;
    RecyclerView rec_product_list;
    ArrayList<ProductListModal> arrayProductList = new ArrayList<>();
    String subBrandId="";
    public BrandListAdapter(Context context, List<BrandNameModal> getDataAdapter) {
        this.context = context;
        this.brandNameModalList = getDataAdapter;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_brandslide_layout, parent, false);

        BrandListAdapter.ViewHolder viewHolder = new BrandListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final BrandNameModal brandNameModal=brandNameModalList.get(position);

         subBrandId=brandNameModal.getId();
         Log.e("dfdlgl",brandNameModal.getId());

      //  holder.tx_name.setText(brandNameModal.getName());

        try{
            Picasso.with(context).load(brandNameModal.getPath()+brandNameModal.getImage()).into(holder.imgBrand);
        }
        catch (Exception e){
        }


        holder. imgBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("asdfadfadf", brandNameModal.getId() );
                if (context instanceof SubCategoryActivity) {
                    ((SubCategoryActivity)context).show_filter_brands(brandNameModal.getId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return brandNameModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBrand;
        TextView tx_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBrand = itemView.findViewById(R.id.imgBrand);
            tx_name = itemView.findViewById(R.id.tx_name);

        }
    }



}
