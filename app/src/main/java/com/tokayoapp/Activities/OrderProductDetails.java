package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Adapter.OrderDetailAdapter;
import com.tokayoapp.Adapter.OrderProductDetailAdapter;
import com.tokayoapp.Adapter.ProductDetailAdapter;
import com.tokayoapp.Adapter.ProductVideoAdapter;
import com.tokayoapp.Modal.OrderDetailModal;
import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.Modal.ProductVideoModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderProductDetails extends AppCompatActivity {
    RelativeLayout rl_back;
    public static ImageView imgProduct;
    SparkButton spark_button;
    ProgressBar spin_kit;
    RecyclerView rec_product_detail;
    RecyclerView.LayoutManager layoutManager;
    OrderProductDetailAdapter productDetailAdapter;
    ArrayList<ProductDetailModal> productModelArrayList = new ArrayList<>();
    TextView txt_product_name, txt_price, txt_description, txt_catName, txt_color, txt_brand, txt_weight, txt_qty;
    String strUserId = "", strOrderId = "", strProdutId = "";


    ProductVideoAdapter productVideoAdapter;
    ArrayList<ProductVideoModal> videoModalArrayList = new ArrayList<>();
    RecyclerView rec_product_video;
    RecyclerView.LayoutManager videomanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product_details);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strOrderId = AppConstant.sharedpreferences.getString(AppConstant.ORDERID, "");
        strProdutId = AppConstant.sharedpreferences.getString(AppConstant.ProdutId, "");

        Log.e("gffbv", strUserId);
        Log.e("gffbv", strOrderId);
        Log.e("gffbv", strProdutId);

        rl_back = findViewById(R.id.rl_back);
        txt_qty = findViewById(R.id.txt_qty);
        txt_color = findViewById(R.id.txt_color);
        rec_product_detail = findViewById(R.id.rec_product_detail);
        spark_button = findViewById(R.id.spark_button);
        imgProduct = findViewById(R.id.imgProduct);
        txt_price = findViewById(R.id.txt_price);
        txt_catName = findViewById(R.id.txt_catName);
        txt_brand = findViewById(R.id.txt_brand);
        rec_product_video = findViewById(R.id.rec_product_video);
        spin_kit = findViewById(R.id.spin_kit);
        txt_weight = findViewById(R.id.txt_weight);
        txt_description = findViewById(R.id.txt_description);
        txt_product_name = findViewById(R.id.txt_product_name);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        show_orderProduct_detail();


        show_video();


        videomanager = new LinearLayoutManager(OrderProductDetails.this, RecyclerView.HORIZONTAL, false);
        rec_product_video.setLayoutManager(videomanager);
        rec_product_video.setHasFixedSize(true);

        spark_button.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    // Button is active
                    Add_Favorite();
                } else {
                    // Button is inactive

                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

    }

    public void show_orderProduct_detail() {


        Log.e("djfvdl", strOrderId);
        Log.e("djfvdl", strProdutId);
        Log.e("djfvdl", strUserId);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_orderProduct_detail")
        AndroidNetworking.post(API.BASEURL + API.show_orderProduct_detail)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("order_id", strOrderId)
                .addBodyParameter("product_id", strProdutId)
                .setTag("single order detail")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("fdgvvg", response.toString());

                        try {
                            String id = response.getString("id");
                            String name = response.getString("name");
                            String description = response.getString("description");
                            String price_NEW = response.getString("price");
                            String weight = response.getString("weight");
                            String images = response.getString("images");
                            String delete_status = response.getString("delete_status");
                            String img = response.getString("img");
                            String purchased_quantity = response.getString("purchased_quantity");
                            String purchased_weight = response.getString("weight");
                            String purchased_color = response.getString("purchased_color");
                            String purchased_model = response.getString("purchased_model");
                            String category = response.getString("category");
                            String brand = response.getString("brand");
                            String path = response.getString("path");


                            txt_description.setText(description);
                            txt_weight.setText(purchased_weight);
                            txt_product_name.setText(name);
                            txt_price.setText(price_NEW);
                            txt_color.setText(purchased_color);
                            txt_qty.setText(purchased_quantity);

                            txt_brand.setText(brand);
                            txt_catName.setText(category);
                            JSONArray jsonArray = new JSONArray(img);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                ProductDetailModal productDetailModal = new ProductDetailModal();
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                JSONObject object = jsonArray.getJSONObject(0);

                                String id_new = jsonObject1.getString("id");
                                String product_id = jsonObject1.getString("product_id");

                                final String image2 = object.getString("image");

                                try {
                                    if (object != null) {
                                        Picasso.with(OrderProductDetails.this).load(path + image2).into(imgProduct);

                                    }
                                } catch (Exception ignored) {

                                }
                                productDetailModal.setImage(jsonObject1.getString("image"));
                                productDetailModal.setPath(response.getString("path"));
                                productModelArrayList.add(productDetailModal);
                            }

                            layoutManager = new LinearLayoutManager(OrderProductDetails.this, RecyclerView.HORIZONTAL, false);
                            rec_product_detail.setLayoutManager(layoutManager);
                            rec_product_detail.setHasFixedSize(true);
                            productDetailAdapter = new OrderProductDetailAdapter(OrderProductDetails.this, productModelArrayList);
                            rec_product_detail.setAdapter(productDetailAdapter);
                            spin_kit.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            Log.e("gfdgh", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("grtygh", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }

    public void Add_Favorite() {
        Log.e("sdjsjh", strProdutId);
        Log.e("sdjsjh", strUserId);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=fav_products")
        AndroidNetworking.post(API.BASEURL + API.fav_products)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("type", "0")/*For type=0 means product will add in product*/
                .setTag("Add Favourite")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dsfdfd", response.toString());
                        try {
                            if (response.has("result")) {
                                if (response.getString("result").equals("successfully Add In Favourite list")) {

                                    Toast.makeText(OrderProductDetails.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(OrderProductDetails.this, response.getString("result"), Toast.LENGTH_SHORT).show();

                                }
                            }

                        } catch (JSONException e) {
                            Log.e("sfadsg", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfgdg", anError.getMessage());
                    }
                });
    }



    public void show_video() {
        Log.e("sdaljds", strProdutId);
        Log.e("sdaljds", strUserId);
        Log.e("sdaljds", strOrderId);
        AndroidNetworking.post(API.BASEURL + API.show_orderProduct_detail)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("order_id", strOrderId)
                .addBodyParameter("product_id", strProdutId)
                .setTag("show Video")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String video = response.getString("video");
                            JSONArray jsonArray = new JSONArray(video);

                            for (int j = 0; j < jsonArray.length(); j++) {

                                ProductVideoModal videoModal = new ProductVideoModal();
                                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                String status=jsonObject1.getString("status");

                                videoModal.setVideo(jsonObject1.getString("link"));
                                videoModal.setStatus(jsonObject1.getString("status"));
                                Log.e("xzlclxzklvkc", jsonObject1.getString("link") );
                                videoModalArrayList.add(videoModal);
                            }

                            productVideoAdapter = new ProductVideoAdapter(OrderProductDetails.this, videoModalArrayList);
                            rec_product_video.setAdapter(productVideoAdapter);
                        } catch (JSONException e) {
                            Log.e("fdgfdbgf", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfghfh", anError.getMessage());
                    }
                });


    }
}



