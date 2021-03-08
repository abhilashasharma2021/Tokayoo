package com.tokayoapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.tokayoapp.Activities.ProductDetailsActivity;
import com.tokayoapp.Activities.RedempItemDetailActivity;
import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.Modal.RedemptionDetailModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.ArrayList;


public class RedemptionDetailAdapter extends RecyclerView.Adapter<RedemptionDetailAdapter.ViewHolder> {

    Context context;
    ArrayList<RedemptionDetailModal>redemptionDetailModals;

    String imgs="",paths="";

    public
    RedemptionDetailAdapter(Context context, ArrayList<RedemptionDetailModal>getDataAdapter) {
        this.context = context;
        this.redemptionDetailModals=getDataAdapter;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_redmptn_detail_layout, parent, false);

        RedemptionDetailAdapter.ViewHolder viewHolder = new RedemptionDetailAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final RedemptionDetailModal redemptionDetailModal = redemptionDetailModals.get(position);

      //  holder.img_product1.setImageResource(redemptionDetailModal.getImage());




       Log.e("sfdjoifv", redemptionDetailModal.getPath() + redemptionDetailModal.getImage() + "");

        try {
            Picasso.with(context).load(redemptionDetailModal.getPath() + redemptionDetailModal.getImage()).into(holder.img_product1);
        } catch (Exception e) {
            Log.e("hfjhvjc", e.getMessage());

        }


       holder.img_product1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgs = redemptionDetailModal.getImage();
                paths = redemptionDetailModal.getPath();
                Log.e("sbjsb", imgs + paths);

                AppConstant.sharedpreferences =context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.ProductSubImages, paths + imgs);
                editor.putString(AppConstant.ProductSubImagesPosition, position+"");
                editor.commit();

                try {
                    Picasso.with(context).load(paths + imgs).into(RedempItemDetailActivity.imgProduct);
                    notifyDataSetChanged();

                } catch (Exception e) {


                }
            }
        });
/*

        RedempItemDetailActivity.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.enlarge_slider_image_layout);

                ImageView img = dialog.findViewById(R.id.img);

                try {
                    Picasso.with(context).load(paths+imgs).into(img);
                } catch (Exception e) {


                }

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
*/

       /* RedempItemDetailActivity.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.enlarge_slider_image_layout);

                ViewPager viewPager = dialog.findViewById(R.id.myViewpager);
                ViewpagerAdapterRedemptionItemDetail viewpagerAdapter=new ViewpagerAdapterRedemptionItemDetail(context,redemptionDetailModals);
                viewPager.setAdapter(viewpagerAdapter);

                dialog.show();

            }
        });
*/

     /*   if (position==0){

            holder. img_product1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.activity_slider_pop_up_image);



                    ImageView img = dialog.findViewById(R.id.img);

                    try {
                        Picasso.with(context).load(productDetailModal.getPath()+productDetailModal.getImage()).into(img);
                    }catch (Exception e){


                    }

                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });





        }else  if (position==1){

            holder. img_product1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.activity_slider_pop_up_image);



                    ImageView img = dialog.findViewById(R.id.img);

                    try {
                        Picasso.with(context).load(productDetailModal.getPath()+productDetailModal.getImage()).into(img);
                    }catch (Exception e){


                    }

                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });



        } else if (position==2){

            holder. img_product1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.activity_slider_pop_up_image);



                    ImageView img = dialog.findViewById(R.id.img);

                    try {
                        Picasso.with(context).load(productDetailModal.getPath()+productDetailModal.getImage()).into(img);
                    }catch (Exception e){


                    }

                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });


        }

        else if (position==3){
           holder.tx_img.setVisibility(View.VISIBLE);
           holder.img_product2.setVisibility(View.VISIBLE);
            try {
                Picasso.with(context).load(productDetailModal.getPath()+productDetailModal.getImage()).into(holder.img_product2);
            }catch (Exception e){

            }


        }



        Log.e("ewfolkd",productDetailModal.getPath()+productDetailModal.getImage());

        try {
            Picasso.with(context).load(productDetailModal.getPath()+productDetailModal.getImage()).into(holder.img_product1);
        }catch (Exception e){


        }



*/


    }

    @Override
    public int getItemCount() {
        return redemptionDetailModals.size();
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
