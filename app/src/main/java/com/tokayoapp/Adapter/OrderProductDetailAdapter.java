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
import com.tokayoapp.Activities.OrderProductDetails;
import com.tokayoapp.Activities.ProductDetailsActivity;
import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.R;

import java.util.ArrayList;
import java.util.List;


public class OrderProductDetailAdapter extends RecyclerView.Adapter<OrderProductDetailAdapter.ViewHolder> {

    Context context;
    ArrayList<ProductDetailModal>productListModals;

    String imgs="",paths="";

    public OrderProductDetailAdapter(Context context, ArrayList<ProductDetailModal>getDataAdapter) {
        this.context = context;
        this.productListModals=getDataAdapter;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_product_detail_layout, parent, false);

        OrderProductDetailAdapter.ViewHolder viewHolder = new OrderProductDetailAdapter.ViewHolder(view);

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

    /*    ProductDetailsActivity.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_slider_pop_up_image);

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
        OrderProductDetails.imgProduct.setOnClickListener(new View.OnClickListener() {
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
