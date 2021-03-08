package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Adapter.ProductDetailAdapter;
import com.tokayoapp.Adapter.ProductVideoAdapter;
import com.tokayoapp.Adapter.RewardDetailAdapter;
import com.tokayoapp.Adapter.ScrollImagesAdapter;
import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.Modal.ProductVideoModal;
import com.tokayoapp.Modal.RewardDetailModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class RewardsDetailsActivity extends AppCompatActivity {

    RelativeLayout rl_back;

    Button btn_redeem;
    RecyclerView rec_rewardDetail;
    RecyclerView.LayoutManager layoutManager;
    RewardDetailAdapter rewardDetailAdapter;
    ArrayList<RewardDetailModal> rewardDetailArrayListArrayList = new ArrayList<>();
    String strUserId = "", strRewardId = "";
    Spinner spin_model, spin_color, spin_weight,spin_size;
    public static ImageView imgProduct;
    TextView txt_color, txt_model;
    TextView txt_product_name, txt_brandName, txt_CatName, txt_point,txtStock, txt_brand, txt_weight, txt_description,txt_available,tx_Weight;
    ProgressBar spin_kit;
    SparkButton spark_button, spark_fav_already;
    String strFavRewardType = "", strFavStatus = "";
    ArrayList<String> arrayListColorID;
    ArrayList<String> arrayListModelID;
    ArrayList<String> arrayListSizeID;
    ArrayList<String> arrayListWeightID;
    ArrayAdapter<String> adapterSize;
    ArrayList<String> arrayListSize;
    ArrayAdapter<String> adaptercolor;
    ArrayAdapter<String> adapterModel;
    ArrayAdapter<String> adapterWeight;
    ArrayList<String> arrayListColor;
    ArrayList<String> arrayListModel;
    ArrayList<String> arrayListWeight;
    String strModel = "", strWeight = "", strColor = "",strProductSubImage="",strSize="",SizeStatus="";
    ArrayList<ProductDetailModal> scrollList;

    String ColorStatus="",ModelStatus="";



    ProductVideoAdapter productVideoAdapter;
    ArrayList<ProductVideoModal>videoModalArrayList=new ArrayList<>();
    RecyclerView rec_reward_video;
    RecyclerView.LayoutManager videomanager;
    SliderView sliderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_details);
        AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strRewardId = AppConstant.sharedpreferences.getString(AppConstant.RewardId, "");
        //   strFavRewardType = AppConstant.sharedpreferences.getString(AppConstant.FavRewardType, "");
        strFavStatus = AppConstant.sharedpreferences.getString(AppConstant.FavStatus, "");

        Log.e("tryhyh", strUserId);
        Log.e("tryhyh", strRewardId);
        //Log.e("sfcds", strFavRewardType);
        Log.e("sfcds", strFavStatus);
        rl_back = findViewById(R.id.rl_back);
        spin_size = findViewById(R.id.spin_size);
      //  spin_weight = findViewById(R.id.spin_weight);
        rec_rewardDetail = findViewById(R.id.rec_rewardDetail);
        spin_kit = findViewById(R.id.spin_kit);
        txt_model = findViewById(R.id.txt_model);
        txt_product_name = findViewById(R.id.txt_product_name);
        tx_Weight = findViewById(R.id.tx_Weight);
        txt_brandName = findViewById(R.id.txt_brandName);
        txt_CatName = findViewById(R.id.txt_CatName);
        txt_color = findViewById(R.id.txt_color);
        txt_brand = findViewById(R.id.txt_brand);
        txt_weight = findViewById(R.id.txt_weight);
        txt_description = findViewById(R.id.txt_description);
        spin_color = findViewById(R.id.spin_color);
        spin_model = findViewById(R.id.spin_model);
        imgProduct = findViewById(R.id.imgProduct);
        txt_point = findViewById(R.id.txt_point);
        txtStock = findViewById(R.id.txtStock);
        rl_back = findViewById(R.id.rl_back);
        spark_button = findViewById(R.id.spark_button);
        btn_redeem = findViewById(R.id.btn_redeem);
        spark_fav_already = findViewById(R.id.spark_fav_already);
        txt_available = findViewById(R.id.txt_available);
        rec_reward_video = findViewById(R.id.rec_reward_video);

        show_video();
        videomanager = new LinearLayoutManager(RewardsDetailsActivity.this, RecyclerView.HORIZONTAL, false);
        rec_reward_video.setLayoutManager(videomanager);
        rec_reward_video.setHasFixedSize(true);


        AvailableStock(strRewardId);
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RewardsDetailsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_enlarge_image_layout, null);
                dialogBuilder.setView(dialogView);

                ImageView imageView = dialogView.findViewById(R.id.my_image);
                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                strProductSubImage = AppConstant.sharedpreferences.getString(AppConstant.ProductSubImages, "");
                final String ProductSubImagesPosition = AppConstant.sharedpreferences.getString(AppConstant.ProductSubImagesPosition, "");

                sliderView = dialogView.findViewById(R.id.imageSlider);
                sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
               //set scroll delay in seconds :
                sliderView.startAutoCycle();

                AndroidNetworking.post(API.BASEURL + API.show_reward_details)
                        .addBodyParameter("reward_id", strRewardId)
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
                                        String image = jsonObject.getString("img");
                                        JSONArray jsonArray = new JSONArray(image);

                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            ProductDetailModal productDetailModal = new ProductDetailModal();
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                            productDetailModal.setImage(jsonObject1.getString("image"));
                                            productDetailModal.setPath(jsonObject.getString("path"));
                                            scrollList.add(productDetailModal);
                                        }
                                    }
                                    ScrollImagesAdapter productDetailAdapter = new ScrollImagesAdapter(RewardsDetailsActivity.this, scrollList);
                                    sliderView.setSliderAdapter(productDetailAdapter);
                               /*     layoutManager = new LinearLayoutManager(RewardsDetailsActivity.this, RecyclerView.HORIZONTAL, false);
                                    recyclerviewSroll.setLayoutManager(layoutManager);
                                    recyclerviewSroll.setHasFixedSize(true);
                                    ScrollImagesAdapter productDetailAdapter = new ScrollImagesAdapter(RewardsDetailsActivity.this, scrollList);
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


                try {
                    Picasso.with(RewardsDetailsActivity.this).load(strProductSubImage).into(imageView);

                } catch (Exception e) {

                }
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });


      //  change_Weight();
        change_colorAll();
        change_ModelAll();
        change_SizeAll();
        show_RewardDetail();
       /* spark_button.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    // Button is active

                   *//* AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                    editor.putString(AppConstant.FavRewardType, "1");
                    editor.commit();*//*
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
*/

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



      /*  spin_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strWeight = arrayListWeightID.get(i);
                strWeightName = arrayListWeight.get(i);
                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.RewardSelectedWeightId, strWeightName);
                editor.putString(AppConstant.RewardSelectedWeightName, strWeightName);
                editor.commit();
                Log.e("thfgh", strWeightName);
                Log.e("thfgh", strWeight);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


 /*       spin_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strColor = arrayListColorID.get(i);
                Log.e("dsfcdfvc", strColor);
                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.RewardSelectedColorId, strColor);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spin_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strModel = arrayListModelID.get(i);
                Log.e("sdfcds", strModel);
                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.RewardSelectedModelId, strModel);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/

    }


    @Override
    protected void onResume() {
        super.onResume();

        arrayListModel = new ArrayList<>();
        arrayListModelID = new ArrayList<>();
        arrayListModelID.add("");
        arrayListModel.add("Select Model");
        spin_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    if (ColorStatus.equals("1")){
                        strModel = arrayListModelID.get(i);
                        Log.e("dcadsdsdssda", "if");
                    }else if (SizeStatus.equals("1")){
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
                        change_SizeModel(strColor,strModel);
                        AvailableStockFilter(strColor, strModel,strSize);
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        arrayListColor = new ArrayList<>();
        arrayListColorID = new ArrayList<>();
                arrayListColorID.add("");
        arrayListColor.add("Select Color");
        spin_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i==0){
                        if (ModelStatus.equals("1")){
                            strColor = arrayListColorID.get(i);
                        }else if (SizeStatus.equals("1")){
                            strColor = arrayListColorID.get(i);
                        } else {

                        }
                }else {

                    strColor = arrayListColorID.get(i);
                    Log.e("ddjbd", strColor);

                    ColorStatus="1";
                    if (ColorStatus.equals("1")){
                        change_Model(strColor);
                        change_Size(strColor);
                        AvailableStockFilter(strColor, strModel,strSize);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        arrayListSize = new ArrayList<>();
        arrayListSizeID = new ArrayList<>();
        arrayListSizeID.add("");
        arrayListSize.add("Select Size");
        spin_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e("ProductDetailsActivity", "strSize:1 " + strSize);
                if (i == 0) {
                    if (ModelStatus.equals("1")) {
                        strSize = arrayListSizeID.get(i);
                        Log.e("ProductDetailsActivity", "strColor: " + strColor);


                    } else if (ColorStatus.equals("1")) {
                        strSize = arrayListSizeID.get(i);
                        Log.e("ProductDetailsActivity", "strColor: " + strModel);
                    } else {

                    }
                } else {

                    strSize = arrayListSizeID.get(i);

                    SizeStatus = "1";
                    if (SizeStatus.equals("1")) {
                        change_filterSize(strSize);
                        change_FilterModel(strSize);
                        AvailableStockFilter(strColor, strModel, strSize);
                    }

                }

            }




            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });





    }
    public void Add_Favorite() {
        Log.e("sdjsjh", strRewardId);
        Log.e("sdjsjh", strUserId);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=fav_products")
        AndroidNetworking.post(API.BASEURL + API.fav_products)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("product_id", strRewardId)
                .addBodyParameter("type", "1")/*For type=0 means product will added in favourite and For type=1 means reward will added in favourite */
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

                                    Toast.makeText(RewardsDetailsActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();

                                    show_RewardDetail();

                                } else {

                                    Toast.makeText(RewardsDetailsActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
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
    public void show_RewardDetail() {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);

        Log.e("efjdkj",strUserId);
        Log.e("efjdkj",strRewardId);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_reward_details")
        AndroidNetworking.post(API.BASEURL + API.show_reward_details)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("reward_id", strRewardId)
                .setTag("show Reward Detail")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("slkjdfcdlk", response.toString());
                        rewardDetailArrayListArrayList = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                final String id_new = jsonObject.getString("id");
                                final String category_id = jsonObject.getString("category_id");
                                String brand_id = jsonObject.getString("brand_id");
                                String reward_name = jsonObject.getString("reward_name");
                                final String point = jsonObject.getString("point");
                                final String weight = jsonObject.getString("weight");
                                String stock = jsonObject.getString("stock");
                                String price = jsonObject.getString("price");
                                String url = jsonObject.getString("url");
                                String description = jsonObject.getString("description");
                                String image = jsonObject.getString("image");
                                final String path = jsonObject.getString("path");
                                String category = jsonObject.getString("category");
                                String brand = jsonObject.getString("brand");
                                String fav_status = jsonObject.getString("fav_status");
                                final String delivery_charge = jsonObject.getString("delivery_charge");
                                String img1 = jsonObject.getString("img");





                                if (fav_status.equals("0")) {


                                    Log.e("dfkjdxjv",strFavStatus);
                                    spark_button.setVisibility(View.VISIBLE);
                                    spark_fav_already.setVisibility(View.GONE);

                                    spark_button.setEventListener(new SparkEventListener() {
                                        @Override
                                        public void onEvent(ImageView button, boolean buttonState) {

                                            if (buttonState) {
                                                // Button is active
                                                Add_Favorite();

                                                AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor=AppConstant.sharedpreferences.edit();
                                                editor.putString(AppConstant.Favorite,"1");
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
                                    Log.e("sedfgvd",strFavStatus);
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





                                Log.e("sdsfcx", id_new);
                                Log.e("tyut", point);
                                Log.e("tyut", "weight"+weight);
                                Log.e("tyut", delivery_charge);

                               /* String weights = jsonObject.getString("weights");
                                JSONArray jsonArray1=new JSONArray(weights);

                                for (int j = 0; j <jsonArray1.length() ; j++) {
                                    JSONObject object=jsonArray1.getJSONObject(j);
                                    final String weight_name=object.getString("weight_name");

                                }
*/
                                /*your total amount will be deductaed in your get reward points Only you have to pay delivery fee*/

                                btn_redeem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String weight=tx_Weight.getText().toString().trim();
                                        if (weight.equals("")){
                                            Toast.makeText(RewardsDetailsActivity.this, "Please select variation", Toast.LENGTH_SHORT).show();

                                        }else {
                                            Log.e("sfgfgfggf", strModel );
                                            Log.e("sfgfgfggf", strColor );
                                            Log.e("RewardsDetailsActivity", "strSize: " +strSize);
                                            AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                            editor.putString(AppConstant.CheckoutStatus, "OrderReward");
                                            editor.putString(AppConstant.RewardId, id_new);
                                            editor.putString(AppConstant.singleRewardPoints, point);
                                           // editor.putString(AppConstant.RewardWeight, weight);
                                            //editor.putString(AppConstant.RewardWeight,weight_name);
                                            editor.putString(AppConstant.delivery_charge, delivery_charge);
                                            editor.putString(AppConstant.RewardSelectedModelId, strModel);
                                            editor.putString(AppConstant.RewardSelectedColorId, strColor);
                                            editor.putString(AppConstant.RewardSelectedSizeId, strSize);
                                            editor.putString(AppConstant.RewardSelectedWeightId, tx_Weight.getText().toString().trim());
                                            editor.commit();
                                            startActivity(new Intent(RewardsDetailsActivity.this, ReedeemPopUpActivity.class));
                                            Animatoo.animateZoom(RewardsDetailsActivity.this);

                                        }


                                    }
                                });

                                txt_product_name.setText(reward_name);
                                txt_description.setText(description);
                              //  txt_point.setText(point);
                                txt_CatName.setText(category);
                                txt_brand.setText(brand);


                                JSONArray jsonArray = new JSONArray(img1);
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    RewardDetailModal rewardDetailModal = new RewardDetailModal();
                                    JSONObject object1 = jsonArray.getJSONObject(j);
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    String img = object1.getString("image");
                                    final String img2 = object1.getString("image");


                                    Log.e("rgdfghb", img);
                                    Log.e("dsfvdxc", img2);
                                    try {
                                        if (object != null) {
                                            Picasso.with(RewardsDetailsActivity.this).load(path + img2).into(imgProduct);

                                        }
                                    } catch (Exception ignored) {

                                    }

                                    rewardDetailModal.setImage(object1.getString("image"));
                                    rewardDetailModal.setPath(jsonObject.getString("path"));

                                    rewardDetailArrayListArrayList.add(rewardDetailModal);
                                }

                            }



                            layoutManager = new LinearLayoutManager(RewardsDetailsActivity.this, RecyclerView.HORIZONTAL, false);
                            rec_rewardDetail.setLayoutManager(layoutManager);
                            rec_rewardDetail.setHasFixedSize(true);
                            rewardDetailAdapter = new RewardDetailAdapter(RewardsDetailsActivity.this, rewardDetailArrayListArrayList);
                            rec_rewardDetail.setAdapter(rewardDetailAdapter);
                            spin_kit.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("fjlejf", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("rgdfg", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }
    public void change_color(String strModel) {
        Log.e("yhjhjh", strRewardId);
        Log.e("yhjhjh", strModel);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_reward_color")
        ColorStatus="0";
        ModelStatus="1";
        SizeStatus="0";
        AndroidNetworking.post(API.BASEURL + API.show_reward_color)
                .addBodyParameter("product_id", strRewardId)
                .addBodyParameter("model_id",strModel)
                .setTag("Choose")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("rthtgfnvbvb", response.toString());

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
                                    JSONObject object = new JSONObject(color_info);

                                    String id_new = object.getString("id");
                                    String color_new = object.getString("color");

                                    arrayListColorID.add(id_new);
                                    Log.e("rgrthbtr", id_new);

                                    arrayListColor.add(color_new);


                                } else {
                                    //  Log.e("ProductDetailsActivity", "Null nhi h: " +color_info);
                                            arrayListColorID.add("");
                                    arrayListColor.add("Not Available");

                                }
                            }

                            adaptercolor = new ArrayAdapter<>(RewardsDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListColor);
                            adaptercolor.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spin_color.setAdapter(adaptercolor);


                        } catch (JSONException e) {
                            Log.e("bhbhfgbcv", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rtfrgf", anError.getMessage());
                    }
                });

    }
    public void change_colorAll() {
        Log.e("yhjhjh", strRewardId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_color")
        AndroidNetworking.post(API.BASEURL + API.show_reward_color)
                .addBodyParameter("product_id", strRewardId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("sdskfl", response.toString());

                        arrayListColor = new ArrayList<>();
                        arrayListColorID = new ArrayList<>();
                                arrayListColorID.add("");
                        arrayListColor.add("Select Color");


                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                if (!jsonObject.getString("id").equals("null")){
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
                            }

                            if (arrayListColorID.size() == 1) {
                                arrayListColor.set(0, "Not Available");
                            } else {
                                arrayListColor.set(0, "Select Color");
                            }

                            adaptercolor = new ArrayAdapter<>(RewardsDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListColor);
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
                            Log.e("dsfkdsk", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rtfrgf", anError.getMessage());
                    }
                });

    }
    public void change_Model(String colorID) {
        Log.e("hfvkjvk", strRewardId);
        Log.e("hfvkjvk", colorID);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_reward_model")
        ColorStatus="1";
        ModelStatus="0";
        SizeStatus="0";
        AndroidNetworking.post(API.BASEURL + API.show_reward_model)
                .addBodyParameter("product_id", strRewardId)
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


                                }
                                else{
                                    Log.e("xnsjnskl","null nhi h");
                                    arrayListModelID.add("");
                                    arrayListModel.add("Not Available");
                                }
                            }
                            adapterModel = new ArrayAdapter<>(RewardsDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListModel);
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
        Log.e("hfvkjvk", strRewardId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_model")
        AndroidNetworking.post(API.BASEURL + API.show_reward_model)
                .addBodyParameter("product_id", strRewardId)
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

                            if (arrayListModelID.size() == 1) {
                                arrayListModel.set(0, "Not Available");
                            } else {
                                arrayListModel.set(0,"Select Model");
                            }
                            adapterModel = new ArrayAdapter<>(RewardsDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListModel);
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
    public void change_filterSize(String strSizeId) {
        Log.e("yhjhjh", strRewardId);
        Log.e("yhjhjh", strModel);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_reward_color")
        ColorStatus="0";
        SizeStatus="1";
        ModelStatus="0";
        AndroidNetworking.post(API.BASEURL + API.show_reward_color)
                .addBodyParameter("product_id", strRewardId)
                .addBodyParameter("size_id",strSizeId)
                .setTag("Choose")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("rthtgfnvbvb", response.toString());

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
                                    JSONObject object = new JSONObject(color_info);

                                    String id_new = object.getString("id");
                                    String color_new = object.getString("color");

                                    arrayListColorID.add(id_new);
                                    Log.e("rgrthbtr", id_new);

                                    arrayListColor.add(color_new);


                                } else {
                                    //  Log.e("ProductDetailsActivity", "Null nhi h: " +color_info);
                                            arrayListColorID.add("");
                                    arrayListColor.add("Not Available");

                                }
                            }

                            adaptercolor = new ArrayAdapter<>(RewardsDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListColor);
                            adaptercolor.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spin_color.setAdapter(adaptercolor);


                        } catch (JSONException e) {
                            Log.e("bhbhfgbcv", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rtfrgf", anError.getMessage());
                    }
                });

    }
    public void change_FilterModel(String sizeid) {
        Log.e("hfvkjvk", strRewardId);
        Log.e("hfvkjvk", sizeid);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_reward_model")
        ColorStatus="0";
        SizeStatus="1";
        ModelStatus="0";
        AndroidNetworking.post(API.BASEURL + API.show_reward_model)
                .addBodyParameter("product_id", strRewardId)
                .addBodyParameter("size_id", sizeid)
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


                                }
                                else{
                                    Log.e("xnsjnskl","null nhi h");
                                    arrayListModelID.add("");
                                    arrayListModel.add("Not Available");
                                }
                            }
                            adapterModel = new ArrayAdapter<>(RewardsDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListModel);
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
    public void change_Size(String strColor) {
        Log.e("sfgddg", strColor+"color");
        ColorStatus="1";
        ModelStatus="0";
        SizeStatus="0";
        AndroidNetworking.post(API.BASEURL + API.show_reward_size)
                .addBodyParameter("product_id", strRewardId)
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
                                String size = jsonObject.getString("Size");
                                String Reward_info = jsonObject.getString("Reward_info");
                                Log.e("dfsdfsf", Reward_info);

                                if (!Reward_info.equals("null")) {

                                    JSONObject object = new JSONObject(Reward_info);
                                    String id_new = object.getString("id");
                                    String name_size = object.getString("size");
                                    Log.e("retyr", id_new);
                                    Log.e("retyr", name_size);
                                    arrayListSizeID.add(id_new);
                                    arrayListSize.add(name_size);

                                    if (i == 0) {
                                        strSize = object.getString("id");
                                    }


                                }
                                else {
                                    //  Log.e("ProductDetailsActivity", "Null nhi h: " +color_info);
                                    arrayListSizeID.add("");
                                    arrayListSize.add("Not Available");
                                }


                            }
                            adapterSize = new ArrayAdapter<>(RewardsDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListSize);
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
    public void AvailableStock(String strRewardId) {

        Log.e("RewardsDetailsActivity", "strRewardId: " +strRewardId);
        Log.e("RewardsDetailsActivity", "strColor: " +strColor);
        Log.e("RewardsDetailsActivity", "strModel: " +strModel);
        Log.e("RewardsDetailsActivity", "strSize: " +strSize);


        AndroidNetworking.post(API.BASEURL + API.available_reward_stock)
                .addBodyParameter("product_id",strRewardId)
               // .addBodyParameter("color_id",strColor)
              //  .addBodyParameter("model_id",strModel)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("hgdgthghdg", response.toString());
                        try {
                            txt_available.setText("Available Stock "+response.getString("total_stock"));
                            txt_point.setText(response.getString("price"));



                        } catch (JSONException e) {
                            Log.e("fgdfgfgfgffg", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fgdfgfgfgffg", anError.getMessage());
                    }
                });
    }
    public void AvailableStockFilter(String strColor, String strModel,String strSize) {


        Log.e("RewardsDetailsActivity", "strRewardId: " +strRewardId);
        Log.e("RewardsDetailsActivity", "strColor: " +strColor);
        Log.e("RewardsDetailsActivity", "strModel: " +strModel);
        Log.e("RewardsDetailsActivity", "strSize: " +strSize);

        AndroidNetworking.post(API.BASEURL + API.available_reward_stock)
                .addBodyParameter("product_id",strRewardId)
                .addBodyParameter("color_id",strColor)
                .addBodyParameter("model_id",strModel)
                .addBodyParameter("size_id",strSize)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("hgdgthghdg", response.toString());
                        try {
                            txt_available.setText("Available Stock "+response.getString("total_stock"));
                            txt_point.setText(response.getString("price"));
                            tx_Weight.setText(response.getString("Weight"));



                        } catch (JSONException e) {
                            Log.e("fgdfgfgfgffg", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fgdfgfgfgffg", anError.getMessage());
                    }
                });
    }
    public void change_SizeAll() {
        Log.e("ergrthgth", strRewardId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_reward_size")
        AndroidNetworking.post(API.BASEURL + API.show_reward_size)
                .addBodyParameter("product_id", strRewardId)
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
                                String size = jsonObject.getString("Size");
                                String Reward_info = jsonObject.getString("Reward_info");
                                Log.e("gregr", Reward_info);

                                if (!Reward_info.equals("null")) {

                                    JSONObject object = new JSONObject(Reward_info);
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
                            adapterSize = new ArrayAdapter<>(RewardsDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListSize);
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
        Log.e("fbgfgdgggd", strModel+"Model");
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_size")
        ColorStatus="0";
        ModelStatus="1";
        SizeStatus="0";
        AndroidNetworking.post(API.BASEURL + API.show_reward_size)
                .addBodyParameter("product_id", strRewardId)
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
                                String size = jsonObject.getString("Size");
                                String Reward_info = jsonObject.getString("Reward_info");
                                Log.e("dfsdfsf", Reward_info);

                                if (!Reward_info.equals("null")) {

                                    JSONObject object = new JSONObject(Reward_info);
                                    String id_new = object.getString("id");
                                    String name_size = object.getString("size");
                                    Log.e("retyr", id_new);
                                    Log.e("retyr", name_size);
                                    arrayListSizeID.add(id_new);
                                    arrayListSize.add(name_size);

                                    if (i == 0) {
                                        strSize = object.getString("id");
                                    }


                                }
                                else{
                                   // Log.e("xnsjnskl","null nhi h");
                                    arrayListSizeID.add("");
                                    arrayListSize.add("Not Available");
                                }
                            }
                            adapterSize = new ArrayAdapter<>(RewardsDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListSize);
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
    public void change_Weight() {
        Log.e("hfvkjvk", strRewardId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_reward_details")
        AndroidNetworking.post(API.BASEURL + API.show_reward_details)
                .addBodyParameter("reward_id",strRewardId)
                .addBodyParameter("user_id",strUserId)
                .setTag("Choose Modal")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("fcdvcx", response.toString());

                        arrayListWeight = new ArrayList<>();
                        arrayListWeightID = new ArrayList<>();


                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String weights_new = jsonObject.getString("weights");
                                Log.e("dsvfdv", weights_new);

                                JSONArray jsonArray = new JSONArray(weights_new);

                                for (int j = 0; j < jsonArray.length(); j++) {

                                    JSONObject object = jsonArray.getJSONObject(j);
                                    String id = object.getString("id");
                                    String product_id = object.getString("product_id");
                                    String weight = object.getString("weight");
                                    String type = object.getString("type");
                                    String weight_name = object.getString("weight_name");
                                    Log.e("fghbgfnh", weight_name);
                                    Log.e("fghbgfnh", id);
                                    arrayListWeightID.add(id);
                                    Log.e("dvfcv c", id);

                                    arrayListWeight.add(weight_name);

                                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                    editor.putString(AppConstant.SelectedWeightName, weight_name);
                                    editor.commit();
                                }


                            }

                            adapterWeight = new ArrayAdapter<>(RewardsDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListWeight);
                            adapterWeight.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spin_weight.setAdapter(adapterWeight);


                        }  catch (JSONException e) {
                            Log.e("etfredg", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("gfvb", anError.getMessage());
                    }
                });
    }
    public void show_video() {
        AndroidNetworking.post(API.BASEURL + API.show_reward_details)
                .addBodyParameter("reward_id",strRewardId)
                .addBodyParameter("user_id", strUserId)
                .setTag("show Video")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("thtrhftg", response.toString() );
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

                            productVideoAdapter = new ProductVideoAdapter(RewardsDetailsActivity.this, videoModalArrayList);
                            rec_reward_video.setAdapter(productVideoAdapter);

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