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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.FavDetailsActivity;
import com.tokayoapp.Activities.ProductDetailsActivity;
import com.tokayoapp.Modal.HomeCategoryModal;
import com.tokayoapp.Modal.ProductListModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private Context context;
    List<ProductListModal> productList ;
    ArrayList<ProductListModal> categoryArrayList;
    ArrayList<String> listData;
    public ProductListAdapter(Context context, List<ProductListModal> getDataAdapter) {
        this.context = context;
        this.productList = getDataAdapter;
        categoryArrayList = new ArrayList<>(productList);
        this.categoryArrayList.addAll(productList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_productlist_layout, parent, false);

        ProductListAdapter.ViewHolder viewHolder = new ProductListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final ProductListModal productListModal=productList.get(position);


      //  holder.txt_dis.setText(productListModal.getDis());
        holder.txt_price.setText(productListModal.getBrandPrice());

        String productId= productListModal.getId();
        Log.e("slkdsfkv",productListModal.getId()+"");



        if(productListModal.getName().length()>=20){
            String strData="";
            listData= new ArrayList<String>();
            for (int i = 0; i <productListModal.getName().length() ; i++) {
                if(i<15){
                    Character str=productListModal.getName().charAt(i);
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
            holder.txt_product_name.setText(character+"...");
        }

        else {
            Log.e("sdfsdfsdffs","Else"+productListModal.getName());
            holder.txt_product_name.setText(productListModal.getName());
        }





        try {
            Picasso.with(context).load(productListModal.getPath()+productListModal.getImage()).into(holder.img_product);
        }catch (Exception e){


        }
        holder.rl_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.ProdutId, productListModal.getId());
                editor.putString(AppConstant.BrandId, productListModal.getBrandId());
                editor.putString(AppConstant.ProdutStatus, "Category");
           //     editor.putString(AppConstant.FavStatus, productListModal.getFav_status());/*status 0=no fav, 1=fav product*/
                editor.commit();
              context.startActivity(new Intent(context, ProductDetailsActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product;
        TextView txt_product_name, txt_dis,txt_price;
        RelativeLayout rl_product;
        RelativeLayout rl_new;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
           // txt_dis = itemView.findViewById(R.id.txt_dis);
            rl_product = itemView.findViewById(R.id.rl_product);
            txt_price = itemView.findViewById(R.id.txt_price);
            rl_new = itemView.findViewById(R.id.rl_new);


        }
    }




    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productList.clear();

        if (charText.length() == 0) {
            productList.addAll(categoryArrayList);
        } else {
            for (ProductListModal wp : categoryArrayList) {

                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    productList.add(wp);
                }

                else if (wp.getBrandPrice().toLowerCase(Locale.getDefault()).contains(charText)) {
                    productList.add(wp);
                } else if (wp.getDis().toLowerCase(Locale.getDefault()).contains(charText)) {
                    productList.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }

}
