package com.tokayoapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.internal.DialogFeature;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Adapter.ProductDetailAdapter;
import com.tokayoapp.Adapter.ProductVideoAdapter;
import com.tokayoapp.Adapter.ScrollImagesAdapter;
import com.tokayoapp.Adapter.ShowReviewAdapter;
import com.tokayoapp.Modal.DataShowReview;
import com.tokayoapp.Modal.ModelReview;
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


public class ProductDetailsActivity extends AppCompatActivity {
    RelativeLayout rl_back;
    Button btn_add_cart;
    RecyclerView rec_product_detail;
    RecyclerView.LayoutManager layoutManager;
    ProductDetailAdapter productDetailAdapter;
    ArrayList<ProductDetailModal> productModelArrayList = new ArrayList<>();
    String strUserId = "", strProdutId = "", strColor = "", strModel = "", strSize = "";
    SparkButton spark_button, spark_fav_already;
    Spinner spin_model, spin_color, spin_size;
    ArrayList<String> arrayListColorID;
    ArrayList<String> arrayListSizeID;
    ArrayList<String> arrayListModelID;
    ArrayAdapter<String> adaptercolor;
    ArrayAdapter<String> adapterSize;
    ArrayAdapter<String> adapterModel;
    ArrayList<String> arrayListColor;
    ArrayList<String> arrayListSize;
    ArrayList<String> arrayListModel;
    ImageView img_plus, img_minus;
    public static ImageView imgProduct;
    TextView txt_itemCount, txtStock, tx_Weight, txt_product_name, txt_brandName, txt_catName, txt_price, txt_brand, txt_weight, txt_description;
    String strProdQ;
    String strProdutStatus = "";
    String strProductSubImage = "";
    RelativeLayout rl_3;
    RelativeLayout rl_4;
    ProgressBar spin_kit;
    ArrayList<ProductDetailModal> scrollList;
    int stock_count = 0;
    String ColorStatus = "", ModelStatus = "", SizeStatus = "";
    LinearLayout ll1, ll2, llReview, llDecription;
    ProductVideoAdapter productVideoAdapter;
    ArrayList<ProductVideoModal> videoModalArrayList = new ArrayList<>();
    RecyclerView rec_product_video;
    RecyclerView.LayoutManager videomanager;
    SliderView sliderView;
    View viewOfDecription, viewOfReview;
    RecyclerView recyclerviewReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strProdutId = AppConstant.sharedpreferences.getString(AppConstant.ProdutId, "");
        strProdutStatus = AppConstant.sharedpreferences.getString(AppConstant.ProdutStatus, "");

        Log.e("dskdf", strUserId);
        Log.e("dskdf", strProdutId);
        Log.e("dskdf", strProdutStatus);
        rl_back = findViewById(R.id.rl_back);
        rl_4 = findViewById(R.id.rl_4);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        recyclerviewReviews = findViewById(R.id.recyclerviewReviews);
        llReview = findViewById(R.id.llReview);
        llDecription = findViewById(R.id.llDecription);
        viewOfDecription = findViewById(R.id.viewOfDecription);
        viewOfReview = findViewById(R.id.viewOfReview);
        rec_product_video = findViewById(R.id.rec_product_video);
        tx_Weight = findViewById(R.id.tx_Weight);
        rl_3 = findViewById(R.id.rl_3);
        rec_product_detail = findViewById(R.id.rec_product_detail);
        txt_price = findViewById(R.id.txt_price);
        spin_size = findViewById(R.id.spin_size);
        spark_fav_already = findViewById(R.id.spark_fav_already);
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
        btn_add_cart = findViewById(R.id.btn_add_cart);
        txtStock = findViewById(R.id.txtStock);
        imgProduct = findViewById(R.id.imgProduct);

        videomanager = new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.HORIZONTAL, false);
        rec_product_video.setLayoutManager(videomanager);
        rec_product_video.setHasFixedSize(true);


        AvailableStock(strProdutId);


        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProductDetailsActivity.this);
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

                AndroidNetworking.post(API.BASEURL + API.product_details)
                        .addBodyParameter("product_id", strProdutId)
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


                                    ScrollImagesAdapter productDetailAdapter = new ScrollImagesAdapter(ProductDetailsActivity.this, scrollList);
                                    sliderView.setSliderAdapter(productDetailAdapter);
                                  /*  if (!ProductSubImagesPosition.equals("")) {
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
                    Picasso.with(ProductDetailsActivity.this).load(strProductSubImage).into(imageView);

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
                String weight = tx_Weight.getText().toString().trim();
                if (weight.equals("")) {
                    Toast.makeText(ProductDetailsActivity.this, "Please select Variation", Toast.LENGTH_SHORT).show();
                } else {
                    add_to_cart(strQuantity);
                }

            }
        });

        Log.e("ProductDetailsActivity", "onCreate: " + strProdutStatus);

        if (strProdutStatus.equals("Category")) {
            show_ProductDetail();
            show_video();
        } else {
            newArrival_details();
            show_arrivalVideo();


        }


        change_ModelAll();
        change_colorAll();
        change_SizeAll();

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        viewOfReview.setVisibility(View.GONE);
        llReview.setVisibility(View.GONE);
        viewOfDecription.setVisibility(View.VISIBLE);
        llDecription.setVisibility(View.VISIBLE);

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOfReview.setVisibility(View.GONE);
                llReview.setVisibility(View.GONE);
                viewOfDecription.setVisibility(View.VISIBLE);
                llDecription.setVisibility(View.VISIBLE);

            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOfDecription.setVisibility(View.GONE);
                llDecription.setVisibility(View.GONE);
                viewOfReview.setVisibility(View.VISIBLE);
                llReview.setVisibility(View.VISIBLE);

            }
        });
showReview();
    }

    @Override
    protected void onResume() {
        super.onResume();


        spin_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    if (ColorStatus.equals("1")) {
                        if (arrayListSizeID.get(i).equals("0")) {
                            strModel = "";
                        } else {
                            strModel = arrayListModelID.get(i);
                        }
                        Log.i("hfgfhghgh", "onItemSelected: " + strModel);

                    } else if (SizeStatus.equals("1")) {
                        Log.i("hfgfhghgh", "onItemSelected: " + strModel);
                        if (arrayListSizeID.get(i).equals("0")) {
                            strModel = "";
                        } else {
                            strModel = arrayListModelID.get(i);
                        }
                    } else {
                        Log.e("dcadsdsdssda", "else");
                    }

                } else {
                    String produtId = AppConstant.sharedpreferences.getString(AppConstant.ProdutId, "");
                    strModel = arrayListModelID.get(i);
                    Log.e("dgfvbg", strModel);
                    new Handler().postDelayed(new Runnable() {
                                                  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                                  public void run() {
                                                      AvailableStockFilter();
                                                  }
                                              }, 700
                    );


                    ModelStatus = "1";
                    if (ModelStatus.equals("1")) {

                        change_color(strModel);
                        change_SizeModel(strColor, strModel);
                        Log.e("dgffgfgf", strModel);
                        Log.e("dgffgfgf", strColor);
                        Log.e("dgffgfgf", strSize);
                        // AvailableStock(strColor, strModel,strSize);
                    } else {

                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        spin_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    if (ModelStatus.equals("1")) {
                        if (arrayListColorID.get(i).equals("0")) {
                            strColor = "";
                            Log.e("dvfbfbdfbdfbdf", "elseififffcheck" + strColor);
                        } else {
                            strColor = arrayListColorID.get(i);
                            Log.e("dvfbfbdfbdfbdf", "elseifelsecheck" + strColor);
                        }

                    } else if (SizeStatus.equals("1")) {
                        if (arrayListColorID.get(i).equals("0")) {
                            strColor = "";
                            Log.e("dvfbfbdfbdfbdf", "elseififffcheck" + strColor);
                        } else {
                            strColor = arrayListColorID.get(i);
                            Log.e("dvfbfbdfbdfbdf", "elseifelsecheck" + strColor);
                        }
                    } else {
                        Log.e("dvfbfbdfbdfbdf", "checkelse" + strColor);
                    }
                } else {

                    Log.e("ddjbd", strColor);
                    Log.e("ddjbd", strModel);
                    Log.e("dvfbfbdfbdfbdf", "check" + strColor + "elsefinal");
                    ColorStatus = "1";
                    if (ColorStatus.equals("1")) {
                        strColor = arrayListColorID.get(i);
                        // String strmodel = arrayListModelID.get(i);
                        Log.e("dvfbfbdfbdfbdf", "check" + strColor + "iffinal");
                        Log.e("dgffgfgf", strModel);
                        Log.e("dgffgfgf", strColor);

                        change_Model(strColor);
                        change_Size(strColor, strModel);
                        new Handler().postDelayed(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void run() {
                                AvailableStockFilter();
                            }
                        }, 700);

                        Log.e("dgffgfgf", "iffff");
                    } else {
                        Log.e("dgffgfgf", "elsee");
                    }


                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spin_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // strSize = arrayListSizeID.get(i);


                Log.e("ProductDetailsActivity", "strSize:1 " + strSize);
                if (i == 0) {
                    if (ModelStatus.equals("1")) {

                        Log.e("ProductDetailsActivity", "strColor: " + strColor);
                        if (arrayListSizeID.get(i).equals("0")) {
                            strSize = "";
                        } else {
                            strSize = arrayListSizeID.get(i);
                        }

                    } else if (ColorStatus.equals("1")) {
                        if (arrayListSizeID.get(i).equals("0")) {
                            strSize = "";
                        } else {
                            strSize = arrayListSizeID.get(i);
                        }
                    } else {

                    }
                } else {
                    strSize = arrayListSizeID.get(i);
                    new Handler().postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        public void run() {
                            AvailableStockFilter();
                        }
                    }, 700);
                    SizeStatus = "1";
                    if (SizeStatus.equals("1")) {
                        change_filterSize(strSize);
                        change_FilterModel(strSize);

                    }

                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


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
                .addBodyParameter("type", "0")/*For type=0 means product will added in favourite and For type=1 means reward will added in favourite*/
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
                                    Toast.makeText(ProductDetailsActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(ProductDetailsActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
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


    public void add_to_cart(String strQuantity) {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);

        Log.e("efrfggdgbgdgngngnf", "strUserId: " + strUserId);
        Log.e("efrfggdgbgdgngngnf", "strProdutId: " + strProdutId);
        Log.e("efrfggdgbgdgngngnf", "strColor: " + strColor);
        Log.e("efrfggdgbgdgngngnf", "strModel: " + strModel);
        Log.e("efrfggdgbgdgngngnf", "strSize: " + strSize);
        Log.e("efrfggdgbgdgngngnf", "strQuantity: " + strQuantity);

        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=add_cart")
        AndroidNetworking.post(API.BASEURL + API.add_cart)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("color_id", strColor)
                .addBodyParameter("model_id", strModel)
                .addBodyParameter("size_id", strSize)
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
                                Toast.makeText(ProductDetailsActivity.this, response.getString("result"), Toast.LENGTH_LONG).show();
                                spin_kit.setVisibility(View.GONE);
                            } else {


                                Toast.makeText(ProductDetailsActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
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

        Log.e("dsfsdfsdfs", strProdutId);
        Log.e("dsfsdfsdfs", strUserId);


        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=product_details")
        AndroidNetworking.post(API.BASEURL + API.product_details)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("user_id", strUserId)
                .setTag("show Product Detail")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("fghgf", response.toString());

                        productModelArrayList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                final String id_new = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                final String brand_id = jsonObject.getString("brand_id");
                                String description = jsonObject.getString("description");
                                String subbrand_id = jsonObject.getString("subbrand_id");
                                String price = jsonObject.getString("price");
                                String weight = jsonObject.getString("weight");
                                final String stock = jsonObject.getString("stock");
                                String image_new = jsonObject.getString("images");
                                final String path = jsonObject.getString("path");
                                String video_path = jsonObject.getString("video_path");
                                String cart_status = jsonObject.getString("cart_status");
                                String cart_quantity = jsonObject.getString("cart_quantity");

                                Log.e("dsfdkof", image_new);
                                Log.e("dsfdkof", path);
                                Log.e("dsfdkof", cart_status);


                                txt_description.setText(description);
                                //  txtStock.setText("Available stock"+" "+stock);
                                //  txt_weight.setText(weight);
                                txt_product_name.setText(name);
                                //     txt_price.setText(price);
                                // txt_itemCount.setText(cart_quantity);

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
                                                Add_Favorite();
                                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                                editor.putString(AppConstant.Favorite, "0");
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

                                String sub_brand_name = jsonObject.getString("Sub_brand_name");
                                Log.e("dsfkdslk", sub_brand_name);
                                String color = jsonObject.getString("color");

                                txt_brand.setText(sub_brand_name);
                                txt_catName.setText(brand_name);

                                String size_new = jsonObject.getString("size");


                                String video_new = jsonObject.getString("video");


                                if (!stock.equals("")) {
                                    stock_count = Integer.parseInt(stock);
                                }


                                img_plus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {


                                        strProdQ = txt_itemCount.getText().toString();

                                        final String new_str = String.valueOf(Integer.parseInt(strProdQ) + 1);

                                        txt_itemCount.setText(new_str);

                                        int qty = Integer.parseInt(new_str);

                                        if (qty > stock_count) {

                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProductDetailsActivity.this);
                                            alertDialogBuilder.setMessage("Can't purchase more than stock Please select under the stock ");
                                            alertDialogBuilder.setPositiveButton("ok",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface arg0, int arg1) {

                                                            Intent plusActivity = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                                                            startActivity(plusActivity);


                                                        }
                                                    });

                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();

                                        } else {

                                            update_quantity(id_new, new_str);

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

                                            txt_itemCount.setText("1");

                                        } else {

                                            txt_itemCount.setText(new_str);

                                            update_quantity(id_new, new_str);

                                        }
                                    }
                                });


                                String image = jsonObject.getString("image");
                                JSONArray jsonArray = new JSONArray(image);
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    ProductDetailModal productDetailModal = new ProductDetailModal();
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                    JSONObject object = jsonArray.getJSONObject(0);

                                    String id = jsonObject1.getString("id");


                                    final String image2 = object.getString("image");

                                    try {
                                        if (object != null) {
                                            Picasso.with(ProductDetailsActivity.this).load(path + image2).into(imgProduct);

                                        }
                                    } catch (Exception ignored) {

                                    }

                                    productDetailModal.setImage(jsonObject1.getString("image"));
                                    productDetailModal.setPath(jsonObject.getString("path"));
                                    productModelArrayList.add(productDetailModal);
                                }

                            }
                            layoutManager = new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.HORIZONTAL, false);
                            rec_product_detail.setLayoutManager(layoutManager);
                            rec_product_detail.setHasFixedSize(true);
                            productDetailAdapter = new ProductDetailAdapter(ProductDetailsActivity.this, productModelArrayList);
                            rec_product_detail.setAdapter(productDetailAdapter);
                            spin_kit.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("fdgfdbgf", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("trytrh", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }


    public void change_color(String Model) {
        Log.e("yhjhjh", strProdutId);
        Log.e("yhjhjh", strModel);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_color")
        ColorStatus = "0";
        ModelStatus = "1";
        SizeStatus = "0";
        AndroidNetworking.post(API.BASEURL + API.show_color)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("model_id", Model)
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


                                if (!color_info.equals("null")) {
                                    //  Log.e("ProductDetailsActivity", "Null nhi h: " +color_info);
                                    JSONObject object = new JSONObject(color_info);


                                    String id_new = object.getString("id");
                                    String color_new = object.getString("color");

                                    arrayListColorID.add(id_new);
                                    Log.e("dkjfkdj", id_new);
                                    arrayListColor.add(color_new);


                                } else {
                                    //  Log.e("ProductDetailsActivity", "Null nhi h: " +color_info);
                                    arrayListColorID.add("");
                                    ;
                                    arrayListColor.add("Not Available");
                                }


                            }

                            adaptercolor = new ArrayAdapter<>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListColor);
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
        Log.e("serghhbfgnbgf", strProdutId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_color")
        AndroidNetworking.post(API.BASEURL + API.show_color)
                .addBodyParameter("product_id", strProdutId)
                .setTag("Choose")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("rehrthfg", response.toString());

                        arrayListColor = new ArrayList<>();
                        arrayListColorID = new ArrayList<>();
                        arrayListColorID.add("");
                        ;
                        arrayListColor.add("Select Color");
                        Log.e("ProductDetailsActivity", "onResponse:one time " + arrayListColorID.size());

                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                if (!jsonObject.getString("id").equals("null")) {

                                    // Log.e("fdbhfn","null nhi h");
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

                                } else {
                                    // Log.e("fdbhfn","null h");

                                }

                            }
                            if (arrayListColorID.size() == 1) {
                                arrayListColor.set(0, "Not Available");
                            } else {
                                arrayListColor.set(0, "Select Color");
                            }

                            Log.e("ProductDetailsActivity", "onResponse:after addtion time " + arrayListColorID.size());
                            adaptercolor = new ArrayAdapter<>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListColor);
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
        Log.e("hfvkjvk", strProdutId);
        Log.e("hfvkjvk", colorID);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_model")
        ColorStatus = "1";
        ModelStatus = "0";
        SizeStatus = "0";
        AndroidNetworking.post(API.BASEURL + API.show_model)
                .addBodyParameter("product_id", strProdutId)
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
                                Log.e("dfsdfsf", model_info);

                                if (!model_info.equals("null")) {
                                    Log.e("xnsjnskl", "null h");
                                    JSONObject object = new JSONObject(model_info);
                                    String id_new = object.getString("id");
                                    String name_new = object.getString("name");
                                    Log.e("retyr", id_new);
                                    Log.e("retyr", name_new);
                                    arrayListModelID.add(id_new);
                                    arrayListModel.add(name_new);

                                    if (i == 0) {
                                        // strModel = object.getString("id");
                                    }
                                } else {
                                    //   Log.e("xnsjnskl","null nhi h");
                                    arrayListModelID.add("");
                                    arrayListModel.add("Not Available");
                                }


                            }
                            adapterModel = new ArrayAdapter<>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListModel);
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
        Log.e("hfvkjvk", strProdutId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_model")
        AndroidNetworking.post(API.BASEURL + API.show_model)
                .addBodyParameter("product_id", strProdutId)
                .setTag("Choose Modal")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("erytruyh", response.toString());

                        arrayListModel = new ArrayList<>();
                        arrayListModelID = new ArrayList<>();
                        arrayListModelID.add("");
                        arrayListModel.add("Select Model");

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String product_id = jsonObject.getString("product_id");
                                String model = jsonObject.getString("model");
                                String model_info = jsonObject.getString("model_info");
                                Log.e("dfsdfsf", model_info);

                                if (!model_info.equals("null")) {

                                    JSONObject object = new JSONObject(model_info);
                                    String id_new = object.getString("id");
                                    String name_new = object.getString("name");
                                    Log.e("fdfdfdfdfdfd", id_new);
                                    Log.e("retyr", name_new);
                                    arrayListModelID.add(id_new);
                                    arrayListModel.add(name_new);


                                }

                            }

                            if (arrayListModelID.size() == 1) {
                                arrayListModel.set(0, "Not Available");
                            } else {
                                arrayListModel.set(0, "Select Model");
                            }
                            adapterModel = new ArrayAdapter<>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListModel);
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


    public void change_SizeAll() {
        Log.e("ergrthgth", strProdutId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_size")
        AndroidNetworking.post(API.BASEURL + API.show_size)
                .addBodyParameter("product_id", strProdutId)
                .setTag("Choose Size")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("erytruyh", response.toString());

                        arrayListSize = new ArrayList<>();
                        arrayListSizeID = new ArrayList<>();
                        arrayListSizeID.add("");
                        arrayListSize.add("Select Size");

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String product_id = jsonObject.getString("product_id");
                                String size = jsonObject.getString("size");
                                String size_info = jsonObject.getString("size_info");
                                Log.e("gregr", size_info);

                                if (!size_info.equals("null")) {

                                    JSONObject object = new JSONObject(size_info);
                                    String id_new = object.getString("id");
                                    String size_new = object.getString("size");
                                    Log.e("ergrf", id_new);
                                    Log.e("rgfg", size_new);
                                    arrayListSizeID.add(id_new);
                                    arrayListSize.add(size_new);


                                }

                            }

                            if (arrayListSizeID.size() == 1) {
                                arrayListSize.set(0, "Not available");
                            } else {
                                arrayListSize.set(0, "Select Size");
                            }

                            adapterSize = new ArrayAdapter<>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListSize);
                            adapterSize.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spin_size.setAdapter(adapterSize);
                          /*  if(arrayListModel.size()==0){

                                rl_4.setVisibility(View.GONE);
                            }
                            else {
                                rl_4.setVisibility(View.VISIBLE);
                            }*/

                        } catch (JSONException e) {
                            Log.e("frghfdg", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tryht", anError.getMessage());
                    }
                });

    }

    public void change_SizeModel(String strColor, String strModel) {
        Log.e("ytjyuj", strColor + "color");
        Log.e("ytjyuj", strModel + "Model");
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_size")
        ColorStatus = "0";
        ModelStatus = "1";
        SizeStatus = "0";
        AndroidNetworking.post(API.BASEURL + API.show_size)
                .addBodyParameter("product_id", strProdutId)
                //.addBodyParameter("color_id", strColor)
                .addBodyParameter("model_id", strModel)
                .setTag("Choose Size")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("trhhj", response.toString());
                        arrayListSize = new ArrayList<>();
                        arrayListSizeID = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String product_id = jsonObject.getString("product_id");
                                String size = jsonObject.getString("size");
                                String size_info = jsonObject.getString("size_info");
                                Log.e("dfsdfsf", size_info);

                                if (!size_info.equals("null")) {

                                    JSONObject object = new JSONObject(size_info);
                                    String id_new = object.getString("id");
                                    String name_size = object.getString("size");
                                    Log.e("retyr", id_new);
                                    Log.e("retyr", name_size);
                                    arrayListSizeID.add(id_new);
                                    arrayListSize.add(name_size);

                                    if (i == 0) {
                                        strSize = object.getString("id");
                                    }


                                } else {
                                    Log.e("xnsjnskl", "null nhi h");
                                    arrayListSizeID.add("");
                                    arrayListSize.add("Not Available");
                                }
                            }
                            adapterSize = new ArrayAdapter<>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListSize);
                            adapterSize.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spin_size.setAdapter(adapterSize);

                        } catch (JSONException e) {
                            Log.e("thtyhth", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("trhrtht", anError.getMessage());
                    }
                });


    }


    public void change_Size(String strColor, String strModel) {
        Log.e("ytjyuj", strColor + "color");
        Log.e("ytjyuj", strModel + "Model");
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_size")
        ColorStatus = "1";
        ModelStatus = "0";
        SizeStatus = "0";
        AndroidNetworking.post(API.BASEURL + API.show_size)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("color_id", strColor)
                //.addBodyParameter("model_id", strModel)
                .setTag("Choose Size")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("trhhj", response.toString());
                        arrayListSize = new ArrayList<>();
                        arrayListSizeID = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String product_id = jsonObject.getString("product_id");
                                String size = jsonObject.getString("size");
                                String size_info = jsonObject.getString("size_info");
                                Log.e("dfsdfsf", size_info);

                                if (!size_info.equals("null")) {

                                    JSONObject object = new JSONObject(size_info);
                                    String id_new = object.getString("id");
                                    String name_size = object.getString("size");
                                    Log.e("retyr", id_new);
                                    Log.e("retyr", name_size);
                                    arrayListSizeID.add(id_new);
                                    arrayListSize.add(name_size);

                                    if (i == 0) {
                                        //  strSize = object.getString("id");
                                    }


                                } else {
                                    //  Log.e("ProductDetailsActivity", "Null nhi h: " +color_info);
                                    arrayListSizeID.add("");
                                    arrayListSize.add("Not Available");
                                }


                            }
                            adapterSize = new ArrayAdapter<>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListSize);
                            adapterSize.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spin_size.setAdapter(adapterSize);

                        } catch (JSONException e) {
                            Log.e("thtyhth", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("trhrtht", anError.getMessage());
                    }
                });


    }

    public void AvailableStock(String strProdutId) {

        Log.e("trhngfn", strProdutId);


        AndroidNetworking.post(API.BASEURL + API.available_stock)
                .addBodyParameter("product_id", strProdutId)
                //.addBodyParameter("color_id", strColor)
                //.addBodyParameter("model_id", strModel)
                //  .addBodyParameter("size_id", strSize)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("etrfdeg", response.toString());
                        try {
                            String total_stock = (response.getString("total_stock"));
                            String price = (response.getString("price"));
                            String weight = (response.getString("Weight"));

                            Log.e("ProductDetailsActivity", "total_stock: " + total_stock);
                            Log.e("ProductDetailsActivity", "price: " + price);
                            Log.e("ProductDetailsActivity", "weight: " + weight);
                            txtStock.setText(total_stock);
                            txt_price.setText(price);
                            // tx_Weight.setText("");

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

    public void AvailableStockFilter() {
        Log.e("trhngfn", strProdutId + "product");
        Log.e("trhngfn", strColor + "colorid");
        Log.e("trhngfn", strModel + "model");
        Log.e("trhngfn", strSize + "size");

        AndroidNetworking.post(API.BASEURL + API.available_stock)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("color_id", strColor)
                .addBodyParameter("model_id", strModel)
                .addBodyParameter("size_id", strSize)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("etrfdeg", response.toString());
                        try {
                            String total_stock = (response.getString("total_stock"));
                            String price = (response.getString("price"));
                            String weight = (response.getString("Weight"));

                            Log.e("ProductDetailsActivity", "total_stock: " + total_stock);
                            Log.e("ProductDetailsActivity", "price: " + price);
                            Log.e("ProductDetailsActivity", "weight: " + weight);

                            stock_count = Integer.parseInt(total_stock);

                            txtStock.setText(total_stock);
                            txt_price.setText(price);
                            tx_Weight.setText(weight);

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
        Log.e("tgfdhg", strColor);
        Log.e("tgfdhg", strModel);
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


    public void change_filterSize(String sizeId) {
        Log.e("yhjhjh", strProdutId);
        Log.e("yhjhjh", strModel);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_color")
        ColorStatus = "0";
        ModelStatus = "0";
        SizeStatus = "1";
        AndroidNetworking.post(API.BASEURL + API.show_color)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("size_id", sizeId)
                .setTag("Choose")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("gvdfgvbcfv", response.toString());

                        arrayListColor = new ArrayList<>();
                        arrayListColorID = new ArrayList<>();


                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String product_id = jsonObject.getString("product_id");
                                String color = jsonObject.getString("color");

                                String color_info = jsonObject.getString("color_info");


                                if (!color_info.equals("null")) {
                                    //  Log.e("ProductDetailsActivity", "Null nhi h: " +color_info);
                                    JSONObject object = new JSONObject(color_info);


                                    String id_new = object.getString("id");
                                    String color_new = object.getString("color");

                                    arrayListColorID.add(id_new);
                                    Log.e("dkjfkdj", id_new);
                                    arrayListColor.add(color_new);


                                } else {
                                    //  Log.e("ProductDetailsActivity", "Null nhi h: " +color_info);
                                    arrayListColorID.add("");
                                    ;
                                    arrayListColor.add("Not Available");
                                }


                            }

                            adaptercolor = new ArrayAdapter<>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListColor);
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


    public void change_FilterModel(String sizeId) {
        Log.e("hfvkjvk", strProdutId);
        Log.e("hfvkjvk", sizeId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_model")
        SizeStatus = "1";
        ColorStatus = "0";
        ModelStatus = "0";
        AndroidNetworking.post(API.BASEURL + API.show_model)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("size_id", sizeId)
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
                                Log.e("dfsdfsf", model_info);

                                if (!model_info.equals("null")) {
                                    Log.e("xnsjnskl", "null h");
                                    JSONObject object = new JSONObject(model_info);
                                    String id_new = object.getString("id");
                                    String name_new = object.getString("name");
                                    Log.e("retyr", id_new);
                                    Log.e("retyr", name_new);
                                    arrayListModelID.add(id_new);
                                    arrayListModel.add(name_new);

                                    if (i == 0) {
                                        strModel = object.getString("id");
                                    }


                                } else {
                                    //   Log.e("xnsjnskl","null nhi h");
                                    arrayListModelID.add("");
                                    arrayListModel.add("Not Available");
                                }


                            }
                            adapterModel = new ArrayAdapter<>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListModel);
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


    public void newArrival_details() {

        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=product_details")
        AndroidNetworking.post(API.BASEURL + API.show_arrival_product)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("user_id", strUserId)
                .setTag("show Product Detail")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("ytuj", response.toString());

                        productModelArrayList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                final String id_new = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                final String brand_id = jsonObject.getString("brand_id");
                                String description = jsonObject.getString("description");
                                String subbrand_id = jsonObject.getString("subbrand_id");
                                String price = jsonObject.getString("price");
                                String weight = jsonObject.getString("weight");
                                final String stock = jsonObject.getString("stock");


                                Log.e("dkhkjsdfhj", stock);
                                final String path = jsonObject.getString("path");
                                String video_path = jsonObject.getString("video_path");
                                String cart_status = jsonObject.getString("cart_status");
                                String cart_quantity = jsonObject.getString("cart_quantity");
                                String image_new = jsonObject.getString("images");
                                Log.e("yuyuy", image_new);
                                Log.e("yuyuy", path);
                                Log.e("yuyuy", cart_status);


                                txt_description.setText(description);
                                // txt_weight.setText(weight);
                                txt_product_name.setText(name);
                                txt_price.setText(price);
                                // txt_itemCount.setText(cart_quantity);

                                String brand_name = jsonObject.getString("brand_name");
                                String fav_status = jsonObject.getString("fav_status");
                                String sub_brand_name = jsonObject.getString("Sub_brand_name");
                                Log.e("ikilkj", sub_brand_name);
                                String color = jsonObject.getString("color");


                                if (fav_status.equals("0")) {


                                    Log.e("dfkjdxjv", fav_status);
                                    spark_button.setVisibility(View.VISIBLE);
                                    spark_fav_already.setVisibility(View.GONE);

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

                                } else {

                                    spark_button.setVisibility(View.GONE);
                                    spark_fav_already.setVisibility(View.VISIBLE);
                                    Log.e("sedfgvd", fav_status);
                                    spark_fav_already.setEventListener(new SparkEventListener() {
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


                                txt_brand.setText(sub_brand_name);
                                txt_catName.setText(brand_name);
                                JSONArray jsonArray1 = new JSONArray(color);


                                String size_new = jsonObject.getString("size");

                                JSONArray jsonArray2 = new JSONArray(size_new);


                                String video_new = jsonObject.getString("video");
                                JSONArray jsonArray3 = new JSONArray(video_new);


                                //  final int stock_count = Integer.parseInt(stock);

                                if (!stock.equals("")) {
                                    stock_count = Integer.parseInt(stock);
                                }


                                img_plus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {


                                        strProdQ = txt_itemCount.getText().toString();

                                        final String new_str = String.valueOf(Integer.parseInt(strProdQ) + 1);

                                        txt_itemCount.setText(new_str);

                                        int qty = Integer.parseInt(new_str);

                                        if (qty > stock_count) {

                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProductDetailsActivity.this);
                                            alertDialogBuilder.setMessage("Can't purchase more than stock Please select under the stock ");
                                            alertDialogBuilder.setPositiveButton("ok",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface arg0, int arg1) {

                                                            Intent plusActivity = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                                                            startActivity(plusActivity);


                                                        }
                                                    });

                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();

                                        } else {

                                            update_quantity(id_new, new_str);

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

                                            txt_itemCount.setText("1");

                                        } else {

                                            txt_itemCount.setText(new_str);

                                            update_quantity(id_new, new_str);

                                        }
                                    }
                                });


                                String image = jsonObject.getString("image");
                                Log.e("sajfckdjsvj", image);
                                JSONArray jsonArray = new JSONArray(image);

                                Log.e("dcsl", jsonArray.length() + "");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    ProductDetailModal productDetailModal = new ProductDetailModal();
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                    JSONObject object = jsonArray.getJSONObject(0);


                                    final String image2 = object.getString("image");
                                    Log.e("dshkfvdhvh", image2);

                                    try {
                                        if (object != null) {
                                            Picasso.with(ProductDetailsActivity.this).load(path + image2).into(imgProduct);

                                        }
                                    } catch (Exception ignored) {

                                    }

                                    productDetailModal.setImage(jsonObject1.getString("image"));
                                    productDetailModal.setPath(jsonObject.getString("path"));
                                    productModelArrayList.add(productDetailModal);
                                }


                            }
                            layoutManager = new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.HORIZONTAL, false);
                            rec_product_detail.setLayoutManager(layoutManager);
                            rec_product_detail.setHasFixedSize(true);
                            productDetailAdapter = new ProductDetailAdapter(ProductDetailsActivity.this, productModelArrayList);
                            rec_product_detail.setAdapter(productDetailAdapter);
                            spin_kit.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("khjlkjl", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("rtyfhg", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }


    public void show_video() {
        AndroidNetworking.post(API.BASEURL + API.product_details)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("user_id", strUserId)
                .setTag("show Video")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("awefsfds", response.toString());
                        videoModalArrayList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String video = jsonObject.getString("video");
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


                            }

                            productVideoAdapter = new ProductVideoAdapter(ProductDetailsActivity.this, videoModalArrayList);
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


    public void show_arrivalVideo() {
        AndroidNetworking.post(API.BASEURL + API.show_arrival_product)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("user_id", strUserId)
                .setTag("show Video")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("awefsfds", response.toString());
                        videoModalArrayList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String video = jsonObject.getString("video");
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


                            }

                            productVideoAdapter = new ProductVideoAdapter(ProductDetailsActivity.this, videoModalArrayList);
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
    public void showReview() {
        AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=view_review")
                .addBodyParameter("product_id", strProdutId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        DataShowReview dataShowReview = gson.fromJson(response.toString(), DataShowReview.class);
                        if (dataShowReview.getData()!=null){
                            ArrayList arrayList = new ArrayList<DataShowReview.Data>();
                            arrayList.addAll(dataShowReview.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false);
                            recyclerviewReviews.setLayoutManager(linearLayoutManager);
                            recyclerviewReviews.setHasFixedSize(true);
                            recyclerviewReviews.setAdapter(new ShowReviewAdapter(ProductDetailsActivity.this, arrayList ));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

}
