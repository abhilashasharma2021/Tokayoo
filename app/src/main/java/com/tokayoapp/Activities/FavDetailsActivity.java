package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Adapter.FavouriteDetailAdapter;
import com.tokayoapp.Adapter.ProductDetailAdapter;
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

public class FavDetailsActivity extends AppCompatActivity {
    RelativeLayout rl_back;
    Button btn_add_cart;

    RecyclerView rec_fav_detail;
    RecyclerView.LayoutManager layoutManager;
    FavouriteDetailAdapter productDetailAdapter;
    ArrayList<ProductDetailModal> productModelArrayList = new ArrayList<>();
    String strUserId = "", strProdutId = "", strColor = "", strModel = "",strfavouriteId="";
    SparkButton spark_button;
    SparkButton spark_fav_already;
    Spinner spin_model, spin_color;
    ArrayAdapter<String> adaptercolor;
    ArrayAdapter<String> adapterModel;
    ArrayList<String> arrayListColor;
    ArrayList<String> arrayListModel;
    ImageView img_plus, img_minus;
    TextView txt_itemCount,txt_product_name,txt_brandName,txt_catName,txt_price,txt_brand,txt_weight,txt_description;
    String strProdQ;
    ProgressBar spin_kit;
    ArrayList<String> arrayListColorID;
    ArrayList<String> arrayListModelID;
    public static ImageView imgProduct;

    String strProductSubImage = "";
    ProductVideoAdapter productVideoAdapter;
    ArrayList<ProductVideoModal>videoModalArrayList=new ArrayList<>();
    RecyclerView rec_product_video;
    RecyclerView.LayoutManager videomanager;
    ArrayList<ProductDetailModal> scrollList;
    String strProdutStatus = "";
    TextView txtStock;
    SliderView sliderView;
    String ColorStatus="",ModelStatus="",strFavorite="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_details);
         spin_kit = findViewById(R.id.spin_kit);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strfavouriteId = AppConstant.sharedpreferences.getString(AppConstant.ProdutId, "");
        strProdutStatus = AppConstant.sharedpreferences.getString(AppConstant.ProdutStatus, "");
        strFavorite = AppConstant.sharedpreferences.getString(AppConstant.Favorite, "");

        Log.e("reyru76i7yk", strFavorite);
        Log.e("dskdf", strUserId);
        Log.e("dskdf", strfavouriteId);
        rl_back = findViewById(R.id.rl_back);
        spark_fav_already = findViewById(R.id.spark_fav_already);
        rec_fav_detail = findViewById(R.id.rec_fav_detail);
        txt_price = findViewById(R.id.txt_price);
        txt_product_name = findViewById(R.id.txt_product_name);
        txt_brandName = findViewById(R.id.txt_brandName);
        txt_catName = findViewById(R.id.txt_catName);
        txt_brand = findViewById(R.id.txt_brand);
        spin_kit = findViewById(R.id.spin_kit);
        txt_weight = findViewById(R.id.txt_weight);
        txt_description = findViewById(R.id.txt_description);
        spin_color = findViewById(R.id.spin_color);
        spin_model = findViewById(R.id.spin_model);
        img_minus = findViewById(R.id.img_minus);
        img_plus = findViewById(R.id.img_plus);
        txt_itemCount = findViewById(R.id.txt_itemCount);
        spark_button = findViewById(R.id.spark_button);
        txtStock = findViewById(R.id.txtStock);
        btn_add_cart = findViewById(R.id.btn_add_cart);
        imgProduct = findViewById(R.id.imgProduct);
        rec_product_video = findViewById(R.id.rec_product_video);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strQuantity=txt_itemCount.getText().toString().trim();
                add_to_cart(strQuantity);
            }
        });


        show_ProductDetail();

        show_video();


        videomanager = new LinearLayoutManager(FavDetailsActivity.this, RecyclerView.HORIZONTAL, false);
        rec_product_video.setLayoutManager(videomanager);
        rec_product_video.setHasFixedSize(true);



        AvailableStock(strColor,strModel);
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FavDetailsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_enlarge_image_layout, null);
                dialogBuilder.setView(dialogView);

                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                strProductSubImage = AppConstant.sharedpreferences.getString(AppConstant.ProductSubImages, "");
                final String ProductSubImagesPosition = AppConstant.sharedpreferences.getString(AppConstant.ProductSubImagesPosition, "");
                sliderView = dialogView.findViewById(R.id.imageSlider);
                sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                sliderView.startAutoCycle();

                AndroidNetworking.post(API.BASEURL + API.product_details)
                        .addBodyParameter("product_id", strfavouriteId)
                        .addBodyParameter("user_id", strUserId)
                        .setTag("show Product Detail")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.e("fghgf", response.toString());

                                scrollList = new ArrayList<>();
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        String image = jsonObject.getString("image");
                                        JSONArray jsonArray = new JSONArray(image);

                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            ProductDetailModal productDetailModal = new ProductDetailModal();
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                            productDetailModal.setImage(jsonObject1.getString("image"));
                                            productDetailModal.setPath(jsonObject.getString("path"));
                                            scrollList.add(productDetailModal);
                                        }
                                    }

                                    ScrollImagesAdapter productDetailAdapter = new ScrollImagesAdapter(FavDetailsActivity.this, scrollList);
                                    sliderView.setSliderAdapter(productDetailAdapter);
                                    /*layoutManager = new LinearLayoutManager(FavDetailsActivity.this, RecyclerView.HORIZONTAL, false);
                                    recyclerviewSroll.setLayoutManager(layoutManager);
                                    recyclerviewSroll.setHasFixedSize(true);
                                    ScrollImagesAdapter productDetailAdapter = new ScrollImagesAdapter(FavDetailsActivity.this, scrollList);
                                    recyclerviewSroll.setAdapter(productDetailAdapter);
                                    if (!ProductSubImagesPosition.equals("")) {
                                        int pos = Integer.parseInt(ProductSubImagesPosition);
                                        recyclerviewSroll.scrollToPosition(pos);
                                    }*/

                                } catch (JSONException e) {
                                    Log.e("fdgfdbgf", e.getMessage());
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("trytrh", anError.getMessage());

                            }
                        });


                ImageView imageView = dialogView.findViewById(R.id.my_image);

                try {
                    Picasso.with(FavDetailsActivity.this).load(strProductSubImage).into(imageView);

                } catch (Exception e) {

                }
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strQuantity = txt_itemCount.getText().toString().trim();
                if (strColor.equals("")){
                    Toast.makeText(FavDetailsActivity.this, "Please select Color", Toast.LENGTH_SHORT).show();
                }else  if (strModel.equals("")){
                    Toast.makeText(FavDetailsActivity.this, "Please select Model", Toast.LENGTH_SHORT).show();
                } else {
                    add_to_cart(strQuantity);
                }

            }
        });

        if (strProdutStatus.equals("Category")) {
            show_ProductDetail();
        } else {
            //newArrival_details();
        }



        change_ModelAll();
        change_colorAll();

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });



        spin_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

              //  strModel = arrayListModel.get(i);
                strModel = arrayListModelID.get(i);

                Log.e("dgfvbg",strModel);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

               // strColor = arrayListColor.get(i);

                strColor = arrayListColorID.get(i);

                Log.e("ddjbd",strColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (strFavorite.equals("0")){

            spark_button.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if (buttonState) {
                        // Button is active
                        Add_Favorite_product();
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


        }else{
       /*     spark_button.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if (buttonState) {
                        // Button is active
                        Add_Favorite_reward();
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
            });*/

        }





        btn_add_cart=findViewById(R.id.btn_add_cart);
        ImageView imgBack=findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();


        arrayListModel = new ArrayList<>();
        arrayListModelID = new ArrayList<>();
        arrayListModelID.add("0");
        arrayListModel.add("Select Model");
        spin_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    if (ColorStatus.equals("1")){
                        strModel = arrayListModelID.get(i);
                        Log.e("dcadsdsdssda", "if");
                    }else {
                        Log.e("dcadsdsdssda", "else");
                    }

                }else {
                    String produtId = AppConstant.sharedpreferences.getString(AppConstant.ProdutId, "");
                    strModel = arrayListModelID.get(i);
                    Log.e("dgfvbg", strModel);


                    ModelStatus="1";
                    if (ModelStatus.equals("1")){
                        change_color(strModel);
                    }

                }

                AvailableStock(strColor,strModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        arrayListColor = new ArrayList<>();
        arrayListColorID = new ArrayList<>();
        arrayListColorID.add("0");
        arrayListColor.add("Select Color");
        spin_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    if (ModelStatus.equals("1")){
                        strColor = arrayListColorID.get(i);
                    }else {

                    }
                }else {
                    strColor = arrayListColorID.get(i);
                    Log.e("ddjbd", strColor);
                    Log.e("ddjbd", strModel);

                    ColorStatus="1";
                    if (ColorStatus.equals("1")){
                        change_Model(strColor);
                    }

                }
                AvailableStock(strColor,strModel);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    public void Add_Favorite_product() {
        Log.e("sdjsjh", strfavouriteId);
        Log.e("sdjsjh", strUserId);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=fav_products")
        AndroidNetworking.post(API.BASEURL+API.fav_products)
                .addBodyParameter("user_id",strUserId)
                .addBodyParameter("product_id",strfavouriteId)
                .addBodyParameter("type","0")/*Type 0= Normal product and 1 means reward product*/
                .setTag("Add Favourite")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dsfdfd", response.toString());
                        try {
                            if (response.has("result")) {
                                if (response.getString("result").equals("Delete from Favourite list")) {

                                    Toast.makeText(FavDetailsActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(FavDetailsActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();

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
    public void Add_Favorite_reward() {
        Log.e("tgrehuyrt", strfavouriteId);
        Log.e("tgrehuyrt", strUserId);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=fav_products")
        AndroidNetworking.post(API.BASEURL+API.fav_products)
                .addBodyParameter("user_id",strUserId)
                .addBodyParameter("product_id",strfavouriteId)
                .addBodyParameter("type","1")/*Type 0= Normal product and 1 means reward product*/
                .setTag("Add Favourite")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dsfdfd", response.toString());
                        try {
                            if (response.has("result")) {
                                if (response.getString("result").equals("Delete from Favourite list")) {

                                    Toast.makeText(FavDetailsActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(FavDetailsActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();

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
  /*  public void add_to_cart(String strQuantity) {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        Log.e("dsflkd", strUserId);
        Log.e("dsflkd", strfavouriteId);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=add_cart")
        AndroidNetworking.post(API.BASEURL+API.add_cart)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("product_id", strfavouriteId)
                .addBodyParameter("color_id", strColor)
                .addBodyParameter("model_id", strModel)
                .addBodyParameter("quantity", strQuantity)
                .setTag("Add To Cart Product")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dgfdg", response.toString());
                        try {
                            if (response.getString("result").equals("successfully")) {
                                String id = response.getString("id");
                                String name = response.getString("name");
                                String brand_id = response.getString("brand_id");
                                String subbrand_id = response.getString("subbrand_id");
                                String description = response.getString("description");
                                String weight = response.getString("weight");
                                String price = response.getString("price");
                                String stock = response.getString("stock");
                                String tax = response.getString("tax");
                                String image = response.getString("image");
                                String product_image = response.getString("product_image");
                                String path = response.getString("path");

                                Log.e("kdslk", id);
                                Log.e("kdslk", price);

                                *//*holder.btnAddToCat.setVisibility(View.GONE);
                                holder.btnViewToCat.setVisibility(View.VISIBLE);*//*
                               *//* holder.btnViewToCat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        context.startActivity(new Intent(context, MyCartActivity.class));
                                    }
                                });*//*
                                Toast.makeText(FavDetailsActivity.this, response.getString("result"), Toast.LENGTH_LONG).show();
                                spin_kit.setVisibility(View.GONE);
                            } else {


                                Toast.makeText(FavDetailsActivity.this, response.getString("result"), Toast.LENGTH_LONG).show();
                                spin_kit.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            Log.e("fgfhhghj", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfrhbf", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);

                    }
                });
    }*/
  public void add_to_cart(String strQuantity) {
      spin_kit.setVisibility(View.VISIBLE);
      Sprite chasingDots = new ChasingDots();
      spin_kit.setIndeterminateDrawable(chasingDots);
      Log.e("dsflkd", strUserId);
      Log.e("dsflkd", strfavouriteId);
      Log.e("dsflkd", strQuantity);
      Log.e("dsflkd", strColor);
      Log.e("dsflkd", strModel);

      // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=add_cart")
      AndroidNetworking.post(API.BASEURL + API.add_cart)
              .addBodyParameter("user_id", strUserId)
              .addBodyParameter("product_id", strfavouriteId)
              .addBodyParameter("color_id", strColor)
              .addBodyParameter("model_id", strModel)
              .addBodyParameter("quantity", strQuantity)
              .setTag("Add To Cart Product")
              .setPriority(Priority.HIGH)
              .build()
              .getAsJSONObject(new JSONObjectRequestListener() {
                  @Override
                  public void onResponse(JSONObject response) {
                      Log.e("dgfdg", response.toString());
                      try {
                          if (response.getString("result").equals("Successfully Added in cart.")) {
                              String id = response.getString("id");
                              String name = response.getString("name");
                              String brand_id = response.getString("brand_id");
                              String subbrand_id = response.getString("subbrand_id");
                              String description = response.getString("description");
                              String weight = response.getString("weight");
                              String price = response.getString("price");
                              String stock = response.getString("stock");
                              String tax = response.getString("tax");
                              String product_image = response.getString("product_image");
                              String path = response.getString("path");

                              Log.e("kdslk", id);
                              Log.e("kdslk", price);

                                /*holder.btnAddToCat.setVisibility(View.GONE);
                                holder.btnViewToCat.setVisibility(View.VISIBLE);*/
                               /* holder.btnViewToCat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        context.startActivity(new Intent(context, MyCartActivity.class));
                                    }
                                });*/
                              Toast.makeText(FavDetailsActivity.this, response.getString("result"), Toast.LENGTH_LONG).show();
                              spin_kit.setVisibility(View.GONE);
                          } else {


                              Toast.makeText(FavDetailsActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                              spin_kit.setVisibility(View.GONE);
                          }

                      } catch (JSONException e) {
                          Log.e("fgfhhghj", e.getMessage());
                          spin_kit.setVisibility(View.GONE);

                      }


                  }

                  @Override
                  public void onError(ANError anError) {
                      Log.e("dfrhbf", anError.getMessage());

                  }
              });
  }

    public void show_ProductDetail() {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=product_details")
        AndroidNetworking.post(API.BASEURL+API.product_details)
                .addBodyParameter("product_id", strfavouriteId)
                .addBodyParameter("user_id", strUserId)
                .setTag("show Product Detail")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("fghgf", response.toString());

                        productModelArrayList=new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id_new = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                final String brand_id = jsonObject.getString("brand_id");
                                String description = jsonObject.getString("description");
                                String subbrand_id = jsonObject.getString("subbrand_id");
                                String price = jsonObject.getString("price");
                                String weight = jsonObject.getString("weight");
                                final String stock = jsonObject.getString("stock");
                                String image_new = jsonObject.getString("image");
                                final String path = jsonObject.getString("path");
                                String video_path = jsonObject.getString("video_path");
                                String cart_status = jsonObject.getString("cart_status");
                                String cart_quantity = jsonObject.getString("cart_quantity");
                                Log.e("dsfdkof",image_new);
                                Log.e("dsfdkof",path);


                                txt_description.setText(description);
                                txt_weight.setText(weight);
                                txt_product_name.setText(name);
                                txt_price.setText(price);


                                String brand_name = jsonObject.getString("brand_name");
                                String fav_status = jsonObject.getString("fav_status");

                                if (fav_status.equals("0")) {


                                    Log.e("dfkjdxjv", fav_status);
                                    spark_button.setVisibility(View.VISIBLE);
                                    spark_fav_already.setVisibility(View.GONE);

                                    spark_button.setEventListener(new SparkEventListener() {
                                        @Override
                                        public void onEvent(ImageView button, boolean buttonState) {

                                            if (buttonState) {
                                                // Button is active
                                                Add_Favorite_product();
                                                AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor=AppConstant.sharedpreferences.edit();
                                                editor.putString(AppConstant.Favorite,"0");
                                                editor.commit();

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

                                } else {

                                    spark_button.setVisibility(View.GONE);
                                    spark_fav_already.setVisibility(View.VISIBLE);
                                    Log.e("sedfgvd", fav_status);
                                    spark_fav_already.setEventListener(new SparkEventListener() {
                                        @Override
                                        public void onEvent(ImageView button, boolean buttonState) {
                                            if (buttonState) {
                                                // Button is active
                                                Add_Favorite_product();
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


                                final int stock_count = Integer.parseInt(stock);

                                img_plus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {


                                        strProdQ = txt_itemCount.getText().toString();

                                        final String new_str = String.valueOf(Integer.parseInt(strProdQ) + 1);

                                        txt_itemCount.setText(new_str);

                                        int qty = Integer.parseInt(new_str);

                                        if (qty>stock_count) {

                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FavDetailsActivity.this);
                                            alertDialogBuilder.setMessage("Can't purchase more than stock Please select under the stock ");
                                            alertDialogBuilder.setPositiveButton("ok",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface arg0, int arg1) {

                                                            Intent plusActivity = new Intent(FavDetailsActivity.this, FavDetailsActivity.class);
                                                            startActivity(plusActivity);


                                                        }
                                                    });

                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();

                                        } else {

                                            update_quantity(brand_id, new_str);

                                        }


                                    }
                                });

                                img_minus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        strProdQ = txt_itemCount.getText().toString();

                                        String new_str = String.valueOf(Integer.parseInt(strProdQ) - 1);

                                        int xyz = Integer.parseInt(strProdQ) - 1;
                                        if (xyz < 1) {

                                            txt_itemCount.setText("0");

                                        } else {

                                            txt_itemCount.setText(new_str);

                                            update_quantity(brand_id, new_str);

                                        }
                                    }
                                });



                                JSONArray jsonArray = new JSONArray(image_new);
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    ProductDetailModal productDetailModal=new ProductDetailModal();
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                    JSONObject object=jsonArray.getJSONObject(0);

                                    String id = jsonObject1.getString("id");
                                    String product_id = jsonObject1.getString("product_id");
                                    String image = jsonObject1.getString("image");
                                    final String image2 = object.getString("image");
                                    try {
                                        if (object!=null){
                                            Picasso.with(FavDetailsActivity.this).load(path +image2).into(imgProduct);

                                        }
                                    } catch (Exception ignored) {

                                    }


                                    productDetailModal.setImage(jsonObject1.getString("image"));
                                    productDetailModal.setPath( jsonObject.getString("path"));
                                    productModelArrayList.add(productDetailModal);
                                }


                                String sub_brand_name = jsonObject.getString("Sub_brand_name");
                                Log.e("dsfkdslk",sub_brand_name);
                                String color = jsonObject.getString("color");

                                txt_brand.setText(sub_brand_name);
                                txt_catName.setText(brand_name);
                                JSONArray jsonArray1=new JSONArray(color);

                                for (int k=0;k<jsonArray1.length();k++){

                                    JSONObject object=jsonArray1.getJSONObject(k);

                                    String id_1 = object.getString("id");
                                    String product_id = object.getString("product_id");
                                    String color_new= object.getString("color");
                                }
                                String size_new = jsonObject.getString("size");

                                JSONArray jsonArray2=new JSONArray(size_new);

                                for (int l=0;l<jsonArray2.length();l++){
                                    JSONObject object1=jsonArray2.getJSONObject(l);
                                    String id2 = object1.getString("id");
                                    String product_id = object1.getString("product_id");
                                    String size = object1.getString("size");
                                }

                                String video_new = jsonObject.getString("video");
                                JSONArray jsonArray3=new JSONArray(video_new);

                                for (int m=0;m<jsonArray3.length();m++){
                                    JSONObject obj=jsonArray3.getJSONObject(m);
                                    String id3 = obj.getString("id");
                                    String product_id_3 = obj.getString("product_id");
                                    String video = obj.getString("video");
                                }




                            }
                            layoutManager = new LinearLayoutManager(FavDetailsActivity.this, RecyclerView.HORIZONTAL, false);
                            rec_fav_detail.setLayoutManager(layoutManager);
                            rec_fav_detail.setHasFixedSize(true);
                            productDetailAdapter = new FavouriteDetailAdapter( FavDetailsActivity.this,productModelArrayList);
                            rec_fav_detail.setAdapter(productDetailAdapter);
                            spin_kit.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("fdgfdbgf",e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("trytrh",anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }

    public void change_color(String Model) {
        Log.e("yhjhjh", strfavouriteId);
        Log.e("yhjhjh", strModel);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_color")
        ColorStatus="0";
        ModelStatus="1";
        AndroidNetworking.post(API.BASEURL + API.show_color)
                .addBodyParameter("product_id", strfavouriteId)
                .addBodyParameter("model_id",Model)
                .setTag("Choose")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("sdskfl", response.toString());

                        arrayListColor = new ArrayList<>();
                        arrayListColorID = new ArrayList<>();



                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String product_id = jsonObject.getString("product_id");
                                String color = jsonObject.getString("color");

                                String color_info = jsonObject.getString("color_info");

                                JSONObject object = new JSONObject(color_info);

                                String id_new = object.getString("id");
                                String color_new = object.getString("color");

                                arrayListColorID.add(id_new);
                                Log.e("dkjfkdj", id_new);
                                arrayListColor.add(color_new);



                            }

                            adaptercolor = new ArrayAdapter<>(FavDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListColor);
                            adaptercolor.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spin_color.setAdapter(adaptercolor);


                        } catch (JSONException e) {
                            Log.e("dsfkdsk", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rtfrgf", anError.getMessage());
                    }
                });

    }



    public void change_colorAll() {
        Log.e("serghhbfgnbgf", strfavouriteId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_color")
        AndroidNetworking.post(API.BASEURL + API.show_color)
                .addBodyParameter("product_id", strfavouriteId)
                .setTag("Choose")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("rehrthfg", response.toString());

                        arrayListColor = new ArrayList<>();
                        arrayListColorID = new ArrayList<>();
                        arrayListColorID.add("0");
                        arrayListColor.add("Select Color");


                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                if (! jsonObject.getString("id").equals("null")){

                                    String id = jsonObject.getString("id");
                                    String product_id = jsonObject.getString("product_id");
                                    String color = jsonObject.getString("color");

                                    String color_info = jsonObject.getString("color_info");

                                    JSONObject object = new JSONObject(color_info);

                                    String id_new = object.getString("id");
                                    String color_new = object.getString("color");

                                    arrayListColorID.add(id_new);
                                    Log.e("dkjfkdj", id_new);

                                    arrayListColor.add(color_new);

                                }else{



                                }

                            }

                            adaptercolor = new ArrayAdapter<>(FavDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListColor);
                            adaptercolor.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spin_color.setAdapter(adaptercolor);
                           /* if(arrayListColor.size()==0){

                                rl_3.setVisibility(View.GONE);
                            }
                            else {

                                rl_3.setVisibility(View.VISIBLE);
                            }
*/

                        } catch (JSONException e) {
                            Log.e("rfhrthrt", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rtujhtyjyt", anError.getMessage());
                    }
                });

    }

    public void change_Model(String colorID) {
        Log.e("hfvkjvk", strfavouriteId);
        Log.e("hfvkjvk", colorID);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_model")
        ColorStatus="1";
        ModelStatus="0";
        AndroidNetworking.post(API.BASEURL + API.show_model)
                .addBodyParameter("product_id", strfavouriteId)
                .addBodyParameter("color_id", colorID)
                .setTag("Choose Modal")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("erytruyh", response.toString());
                        arrayListModel = new ArrayList<>();
                        arrayListModelID = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String product_id = jsonObject.getString("product_id");
                                String model = jsonObject.getString("model");
                                String model_info = jsonObject.getString("model_info");
                                Log.e("dfsdfsf",model_info);

                                if(!model_info.equals("null")){

                                    JSONObject object = new JSONObject(model_info);
                                    String id_new = object.getString("id");
                                    String name_new = object.getString("name");
                                    Log.e("retyr", id_new);
                                    Log.e("retyr", name_new);
                                    arrayListModelID.add(id_new);
                                    arrayListModel.add(name_new);

                                    if (i==0){
                                        strModel =object.getString("id");
                                    }


                                }
                            }
                            adapterModel = new ArrayAdapter<>(FavDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListModel);
                            adapterModel.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spin_model.setAdapter(adapterModel);

                        } catch (JSONException e) {
                            Log.e("etfredg", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tryht", anError.getMessage());
                    }
                });

    }

    public void change_ModelAll() {
        Log.e("hfvkjvk", strfavouriteId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_model")
        AndroidNetworking.post(API.BASEURL + API.show_model)
                .addBodyParameter("product_id", strfavouriteId)
                .setTag("Choose Modal")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("erytruyh", response.toString());

                        arrayListModel = new ArrayList<>();
                        arrayListModelID = new ArrayList<>();
                        arrayListModelID.add("0");
                        arrayListModel.add("Select Model");

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String product_id = jsonObject.getString("product_id");
                                String model = jsonObject.getString("model");
                                String model_info = jsonObject.getString("model_info");
                                Log.e("dfsdfsf",model_info);

                                if(!model_info.equals("null")){

                                    JSONObject object = new JSONObject(model_info);
                                    String id_new = object.getString("id");
                                    String name_new = object.getString("name");
                                    Log.e("retyr", id_new);
                                    Log.e("retyr", name_new);
                                    arrayListModelID.add(id_new);
                                    arrayListModel.add(name_new);


                                }
                            }
                            adapterModel = new ArrayAdapter<>(FavDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListModel);
                            adapterModel.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spin_model.setAdapter(adapterModel);
                          /*  if(arrayListModel.size()==0){

                                rl_4.setVisibility(View.GONE);
                            }
                            else {
                                rl_4.setVisibility(View.VISIBLE);
                            }*/

                        } catch (JSONException e) {
                            Log.e("etfredg", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tryht", anError.getMessage());
                    }
                });

    }


    public void AvailableStock(String strColor,String strModel) {

        Log.e("dgddgdfgd",strfavouriteId);
        Log.e("dgddgdfgd",strColor);
        Log.e("dgddgdfgd",strModel);

        AndroidNetworking.post(API.BASEURL + API.available_stock)
                .addBodyParameter("product_id", strfavouriteId)
                .addBodyParameter("color_id", strColor)
                .addBodyParameter("model_id", strModel)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("etrfdeg", response.toString());
                        try {
                            String total_stock=(response.getString("total_stock"));
                            String price=(response.getString("price"));
                            txtStock.setText(total_stock) ;
                            txt_price.setText (price);

                        } catch (JSONException e) {
                            Log.e("tyhth", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ukuihj", anError.getMessage());
                    }
                });
    }

    public void update_quantity(String incrmntId, String new_str) {
        Log.e("tgfdhg", new_str);
        Log.e("tgfdhg", incrmntId);
        Log.e("tgfdhg", strUserId);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=update_quantity")
        AndroidNetworking.post(API.BASEURL + API.update_quantity)
                .addBodyParameter("product_id", incrmntId)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("color_id", strColor)
                .addBodyParameter("model_id", strModel)
                .addBodyParameter("quantity", new_str)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("etrfdeg", response.toString());
                        try {
                            if (response.getString("result").equals("sucessfull")) {
                                String id = response.getString("id");
                                String user_id = response.getString("user_id");
                                String product_id = response.getString("product_id");
                                String status = response.getString("status");
                                String quantity = response.getString("quantity");
                                String path = response.getString("path");
                                Log.e("gdfrgbf", quantity);
                                Log.e("gdfrgbf", user_id);

                                //  txt_itemCount.setText(quantity);

                                //  context.startActivity(new Intent(context,AddToCartActivity.class));

                            }
                        } catch (JSONException e) {
                            Log.e("tyhth", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ukuihj", anError.getMessage());
                    }
                });

    }

    public void show_video() {

        AndroidNetworking.post(API.BASEURL + API.product_details)
                .addBodyParameter("product_id", strfavouriteId)
                .addBodyParameter("user_id", strUserId)
                .setTag("show Video")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("awefsfds", response.toString() );
                        videoModalArrayList=new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String video = jsonObject.getString("video");
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


                            }
                            productVideoAdapter = new ProductVideoAdapter(FavDetailsActivity.this, videoModalArrayList);
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