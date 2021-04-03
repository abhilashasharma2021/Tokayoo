package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Adapter.OrderProductDetailAdapter;
import com.tokayoapp.Adapter.ProductVideoAdapter;
import com.tokayoapp.Adapter.ScrollImagesAdapter;
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
    TextView txt_product_name, txt_price, txt_description, txt_catName, txt_color, txt_brand, txt_weight, txt_qty, textModel, textSize;
    String strUserId = "", strOrderId = "", strProdutId = "";
    ProductVideoAdapter productVideoAdapter;
    ArrayList<ProductVideoModal> videoModalArrayList = new ArrayList<>();
    RecyclerView rec_product_video;
    RecyclerView.LayoutManager videomanager;
    SliderView sliderView;
    String strProductSubImage = "";
    ProgressBar simpleProgressBar;
Button btnReview;
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
        //     Toast.makeText(OrderProductDetails.this,strUserId,Toast.LENGTH_LONG).show();
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
        textModel = findViewById(R.id.textModel);
        textSize = findViewById(R.id.textSize);
        btnReview = findViewById(R.id.btnReview);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });


        show_orderProduct_detail();


        show_video();

        videomanager = new LinearLayoutManager(OrderProductDetails.this, RecyclerView.HORIZONTAL, false);
        rec_product_video.setLayoutManager(videomanager);
        rec_product_video.setHasFixedSize(true);


        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(OrderProductDetails.this, "check", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderProductDetails.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_enlarge_image_layout, null);
                dialogBuilder.setView(dialogView);
                sliderView = dialogView.findViewById(R.id.imageSlider);
                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                strProductSubImage = AppConstant.sharedpreferences.getString(AppConstant.ProductSubImages, "");
                final String ProductSubImagesPosition = AppConstant.sharedpreferences.getString(AppConstant.ProductSubImagesPosition, "");

                sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                sliderView.startAutoCycle();


                Log.e("OrderProductDetails", "onClick: " + strUserId);
                Log.e("OrderProductDetails", "strOrderId: " + strOrderId);
                Log.e("OrderProductDetails", "strProdutId: " + strProdutId);
                AndroidNetworking.post(API.BASEURL + API.show_orderProduct_detail)
                        .addBodyParameter("user_id", strUserId)
                        .addBodyParameter("order_id", strOrderId)
                        .addBodyParameter("cart_id", strProdutId)
                        .setTag("single order detail")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("dsbvxcbcx", response.toString());
                                productModelArrayList = new ArrayList<>();
                                try {
                                    String img = response.getString("img");
                                    String path = response.getString("path");
                                    Log.e("OrderProductDetails", "onResponse: " + img);
                                    JSONArray jsonArray = new JSONArray(img);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String image = jsonObject.getString("image");
                                        ProductDetailModal productDetailModal = new ProductDetailModal();
                                        productDetailModal.setImage(jsonObject.getString("image"));
                                        productDetailModal.setPath(response.getString("path"));
                                        productModelArrayList.add(productDetailModal);


                                    }

                                    ScrollImagesAdapter productDetailAdapter = new ScrollImagesAdapter(OrderProductDetails.this, productModelArrayList);
                                    sliderView.setSliderAdapter(productDetailAdapter);


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


                ImageView imageView = dialogView.findViewById(R.id.my_image);

                try {
                    Picasso.with(OrderProductDetails.this).load(strProductSubImage).into(imageView);

                } catch (Exception e) {

                }
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });


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

    public void popup(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rate_popup);
        Button btn_add = dialog.findViewById(R.id.btn_add);
        EditText ratingText = dialog.findViewById(R.id.etEdittext);
        RatingBar ratingBar = dialog.findViewById(R.id.rating_Star);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleProgressBar.setVisibility(View.VISIBLE);
                String ratingBarText = ratingText.getText().toString()+ "Rating: "
                        + ratingBar.getRating();

                if (ratingBar.getRating()>0){
                    addReview( ""+ratingBar.getRating(),ratingText.getText().toString(),dialog);
                }else {
                    Toast.makeText(OrderProductDetails.this, "Please rate the product", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void show_orderProduct_detail() {


        Log.e("djfvdl", strOrderId);
        Log.e("djfvdl", strProdutId);
        Log.e("djfvdl", strUserId);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_orderProduct_detail")
        AndroidNetworking.post(API.BASEURL + API.show_orderProduct_detail)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("order_id", strOrderId)
                .addBodyParameter("cart_id", strProdutId)
                .setTag("single order detail")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("dffdsgfdfxfd", "onResponse: " + response);

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
                            String purchased_weight = response.getString("purchased_weight");
                            String purchased_color = response.getString("purchased_color");
                            String purchased_model = response.getString("purchased_model");
                            String purchased_size = response.getString("purchased_size");
                            String category = response.getString("category");
                            String brand = response.getString("brand");
                            String path = response.getString("path");


                            txt_description.setText(description);
                            txt_weight.setText(purchased_weight);
                            txt_product_name.setText(name);


                            txt_price.setText(price_NEW);


                            if (purchased_color.equals("null")) {
                                txt_color.setText("Not available");
                            } else {
                                txt_color.setText(purchased_color);
                            }

                            if (purchased_model.equals("null")) {
                                textModel.setText("Not available");
                            } else {
                                textModel.setText(purchased_model);
                            }

                            if (purchased_size.equals("null")) {
                                textSize.setText("Not available");
                            } else {
                                textSize.setText(purchased_size);
                            }


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
                .addBodyParameter("cart_id", strProdutId)
                .setTag("show Video")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("nxnccmnmn", "onResponse: "+response.toString());
                        try {
                            String video = response.getString("video");
                            JSONArray jsonArray = new JSONArray(video);

                            for (int j = 0; j < jsonArray.length(); j++) {

                                ProductVideoModal videoModal = new ProductVideoModal();
                                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                String status = jsonObject1.getString("status");
                                videoModal.setVideo(jsonObject1.getString("link"));
                                videoModal.setStatus(jsonObject1.getString("status"));
                                Log.e("xzlclxzklvkc", jsonObject1.getString("link"));
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

    public void addReview(String rating, String text, Dialog dialog){
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
      String  newID = AppConstant.sharedpreferences.getString(AppConstant.newID, "");
        AndroidNetworking.post(API.add_review)
                .addBodyParameter("product_id", newID)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("score", rating)
                .addBodyParameter("comment", text)
                .setTag("show Video")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        simpleProgressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                        Toast.makeText(OrderProductDetails.this, "Review Added Succcessfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfghfh", anError.getMessage());
                        simpleProgressBar.setVisibility(View.GONE);
                    }
                });
    }

}



