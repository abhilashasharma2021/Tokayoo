package com.tokayoapp.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.facebook.internal.DialogFeature;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Adapter.ProductDetailAdapter;
import com.tokayoapp.Adapter.ProductVideoAdapter;
import com.tokayoapp.Adapter.RedemptionDetailAdapter;
import com.tokayoapp.Adapter.ScrollImagesAdapter;
import com.tokayoapp.Adapter.ScrollImagesRedemptiondetailsAdapter;
import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.Modal.ProductVideoModal;
import com.tokayoapp.Modal.RedemptionDetailModal;
import com.tokayoapp.Modal.RewardDetailModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RedempItemDetailActivity extends AppCompatActivity {
    RelativeLayout rl_back;
    Button btn_track, btn_trackfad;
    public static ImageView imgProduct;
    ProgressBar spin_kit;
    RelativeLayout rl_process;
    TextView txt_cat_name, txt_brand, txt_progress;
    RecyclerView rec_redmptn_detail;
    String strUserId = "", strORDERID = "";
    TextView textSize, textModel, textColor, txt_product_name, txt_point, txt_weight, txt_description, txtName, txtNumber, txtAddress, txt_Netweight, txt_delivery, txt_Totalprice, txt_orderNo, txt_date, txt_time;
    RecyclerView.LayoutManager layoutManager;
    RedemptionDetailAdapter detailAdapter;
    ArrayList<RedemptionDetailModal> redemptionArrayList = new ArrayList<>();
    TextView txt_companyName, txt_shipi_time, txt_shipi_date, txt_tracking;

    ProductVideoAdapter productVideoAdapter;
    ArrayList<ProductVideoModal> videoModalArrayList = new ArrayList<>();
    RecyclerView rec_product_video;
    String strProdutId = "";
    RecyclerView.LayoutManager videomanager;
    ImageView iv_copy;
    String company_name = "", ship_time = "";

    SliderView sliderView;
    ArrayList<RedemptionDetailModal> scrollList;
    String strProductSubImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemp_item_detail);

        txt_progress = findViewById(R.id.txt_progress);
        rl_process = findViewById(R.id.rl_process);
        btn_trackfad = findViewById(R.id.btn_trackfad);
        txt_shipi_time = findViewById(R.id.txt_shipi_time);
        rl_back = findViewById(R.id.rl_back);
        iv_copy = findViewById(R.id.iv_copy);
        txt_shipi_date = findViewById(R.id.txt_shipi_date);
        txt_companyName = findViewById(R.id.txt_companyName);
        txt_product_name = findViewById(R.id.txt_product_name);
        textColor = findViewById(R.id.textColor);
        textSize = findViewById(R.id.textSize);
        textModel = findViewById(R.id.textModel);
        txt_point = findViewById(R.id.txt_point);
        txt_weight = findViewById(R.id.txt_weight);
        txt_cat_name = findViewById(R.id.txt_cat_name);
        txt_description = findViewById(R.id.txt_description);
        txtName = findViewById(R.id.txtName);
        txtNumber = findViewById(R.id.txtNumber);
        txtAddress = findViewById(R.id.txtAddress);
        txt_time = findViewById(R.id.txt_time);
        txt_Netweight = findViewById(R.id.txt_Netweight);
        txt_delivery = findViewById(R.id.txt_delivery);
        txt_orderNo = findViewById(R.id.txt_orderNo);
        txt_date = findViewById(R.id.txt_date);
        txt_brand = findViewById(R.id.txt_brand);
        txt_tracking = findViewById(R.id.txt_tracking);
        txt_Totalprice = findViewById(R.id.txt_Totalprice);
        spin_kit = findViewById(R.id.spin_kit);
        rec_redmptn_detail = findViewById(R.id.rec_redmptn_detail);
        imgProduct = findViewById(R.id.imgProduct);
        btn_track = findViewById(R.id.btn_track);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RedempItemDetailActivity.this, TrackActivity.class));
            }
        });

        layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rec_redmptn_detail.setLayoutManager(layoutManager);
        rec_redmptn_detail.setHasFixedSize(true);
        detail_RedemptionItem();


        rec_product_video = findViewById(R.id.rec_product_video);

        show_video();
        videomanager = new LinearLayoutManager(RedempItemDetailActivity.this, RecyclerView.HORIZONTAL, false);
        rec_product_video.setLayoutManager(videomanager);
        rec_product_video.setHasFixedSize(true);

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RedempItemDetailActivity.this);
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

                AndroidNetworking.post(API.BASEURL + API.show_reward_order_detail)
                        .addBodyParameter("user_id", strUserId)
                        .addBodyParameter("product_id", strProdutId)
                        .addBodyParameter("order_id", strORDERID)
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
                                        String reward = jsonObject.getString("reward");
                                        JSONArray jsonArray = new JSONArray(reward);
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject object = jsonArray.getJSONObject(j);
                                            String img = object.getString("img");

                                            Log.e("RedempItemDetailActivity", "onResponse: " + img);
                                            JSONArray jsonArray1 = new JSONArray(img);

                                            for (int k = 0; k < jsonArray1.length(); k++) {
                                                RedemptionDetailModal productDetailModal = new RedemptionDetailModal();
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                                                productDetailModal.setImage(jsonObject1.getString("image"));
                                                productDetailModal.setPath(object.getString("path"));
                                                scrollList.add(productDetailModal);
                                            }
                                        }

                                    }


                                    ScrollImagesRedemptiondetailsAdapter productDetailAdapter = new ScrollImagesRedemptiondetailsAdapter(RedempItemDetailActivity.this, scrollList);
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
                    Picasso.with(RedempItemDetailActivity.this).load(strProductSubImage).into(imageView);

                } catch (Exception e) {

                }
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
  /*      imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RedempItemDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_slider_pop_up_image);

                ImageView img = dialog.findViewById(R.id.img);

                *//*try {
                    Picasso.with(context).load(paths+imgs).into(img);
                } catch (Exception e) {


                }*//*

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });*/
    }


    public void detail_RedemptionItem() {

        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strORDERID = AppConstant.sharedpreferences.getString(AppConstant.ORDERID, "");
        strProdutId = AppConstant.sharedpreferences.getString(AppConstant.ProdutId, "");
        Log.e("rgfg", strUserId);
        Log.e("rgfg", strORDERID);
        Log.e("rgfg", strProdutId);

        //   AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_reward_order_detail")
        AndroidNetworking.post(API.BASEURL + API.show_reward_order_detail)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("order_id", strORDERID)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        redemptionArrayList = new ArrayList<>();
                        Log.e("kbvblv", response.toString());
                        String order_status = "";

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String user_id = jsonObject.getString("user_id");
                                String order_id = jsonObject.getString("order_id");
                                String product_id = jsonObject.getString("product_id");
                                String product_point = jsonObject.getString("product_point");
                                String total_point = jsonObject.getString("total_point");
                                String username = jsonObject.getString("username");
                                String contact = jsonObject.getString("contact");
                                String address = jsonObject.getString("address");
                                String order_type = jsonObject.getString("order_type");
                                String order_date = jsonObject.getString("order_date");
                                String order_time = jsonObject.getString("order_time");
                                order_status = jsonObject.getString("order_status");
                                String tracking_no = jsonObject.getString("tracking_no");
                                String tracking_status = jsonObject.getString("tracking_status");
                                String deliver_status = jsonObject.getString("deliver_status");
                                String delivery_charge = jsonObject.getString("delivery_charge");
                                String reward = jsonObject.getString("reward");
                                String courier_info = jsonObject.getString("courier_info");


                                if (courier_info.equals("")) {

                                } else {
                                    JSONObject jsonObject1 = new JSONObject(courier_info);
                                    company_name = jsonObject1.getString("company_name");
                                    String contact_track = jsonObject1.getString("contact");
                                    String address_track = jsonObject1.getString("address");
                                    ship_time = jsonObject1.getString("ship_time");
                                    //    String ship_date=jsonObject1.getString("ship_date");
                                   /* txt_companyName.setText(company_name);

                                    txt_shipi_time.setText(ship_time);*/

                                }

                                JSONArray jsonArray = new JSONArray(reward);
                                Log.e("dlkfvk", user_id);
                                Log.e("dlkfvk", username);

                                for (int j = 0; j < jsonArray.length(); j++) {

                                    JSONObject object = jsonArray.getJSONObject(j);
                                    String id_new = object.getString("id");
                                    String brand_id = object.getString("brand_id");
                                    String reward_name = object.getString("reward_name");
                                    String point = object.getString("point");
                                    String weight = object.getString("weight");
                                    String stock = object.getString("stock");
                                    String price = object.getString("price");
                                    String url = object.getString("url");
                                    String description = object.getString("description");
                                    String image = object.getString("image");
                                    String path = object.getString("path");

                                    Log.e("dfvlkcv", path);

                                    String purchased_color = object.getString("purchased_color");
                                    String purchased_model = object.getString("purchased_model");
                                    String purchased_weight = object.getString("purchased_weight");
                                    String category = object.getString("category");
                                    String brand = object.getString("brand");
                                    txt_product_name.setText(reward_name);

                         if (object.getString("purchased_color").equals("null")) {
                                        textColor.setText("Not available");
                                    } else {
                                        textColor.setText(object.getString("purchased_color"));
                                    }

                                    if (object.getString("purchased_model").equals("null")) {
                                        textModel.setText("Not available");
                                    } else {
                                        textModel.setText(object.getString("purchased_model"));

                                    }
                                    textSize.setText("Not available");

                                   if (object.getString("purchased_size").equals("null")) {
                                        textSize.setText("Not available");
                                    } else {
                                        textSize.setText(object.getString("purchased_size"));
                                      //  textSize.setText(object.getString("purchased_size"));
                                    }


                                    if (total_point.equals("null")){
                                        txt_point.setText("0");
                                    }else {
                                        txt_point.setText(total_point);
                                    }


                                    txt_cat_name.setText(category);
                                    txt_brand.setText(brand);
                                    Log.e("dfsfsdfsfsd", weight);
                                    txt_weight.setText(purchased_weight);
                                    txt_description.setText(description);
                                    txtName.setText(username);
                                    txtNumber.setText(contact);
                                    txt_delivery.setText(delivery_charge + ".00");
                                    txtAddress.setText(address);
                                    txt_Netweight.setText(purchased_weight);
                                    txt_Totalprice.setText(total_point + ".00");
                                    txt_orderNo.setText(order_id);
                                    txt_date.setText(order_date);
                                    txt_time.setText(order_time);
                                    if (tracking_status.equals("0")) {
                                        //   txt1.setVisibility(View.GONE);
                                        btn_track.setVisibility(View.GONE);
                                        btn_trackfad.setVisibility(View.VISIBLE);
                                        txt_tracking.setVisibility(View.GONE);
                                        txt_progress.setText("In Process");
                                        txt_progress.setTextColor(getResources().getColor(R.color.pink));
                                    } else {
                                        // txt1.setVisibility(View.VISIBLE);
                                        txt_tracking.setVisibility(View.VISIBLE);
                                        txt_tracking.setText(tracking_no);
                                        btn_track.setVisibility(View.VISIBLE);
                                        iv_copy.setVisibility(View.VISIBLE);
                                        btn_trackfad.setVisibility(View.GONE);
                                        txt_progress.setText("Ship out");
                                        txt_progress.setTextColor(getResources().getColor(R.color.blue));


                                        txt_companyName.setText(company_name);

                                        txt_shipi_time.setText(ship_time);

                                        btn_track.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(RedempItemDetailActivity.this, WebViewActivity.class));
                                            }
                                        });
                                    }
                                    iv_copy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ClipboardManager cm = (ClipboardManager) RedempItemDetailActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                                            cm.setText(txt_tracking.getText());
                                            Toast.makeText(RedempItemDetailActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    String img = object.getString("img");
                                    Log.e("mnvm", img);
                                    JSONArray jsonArray1 = new JSONArray(img);
                                    for (int k = 0; k < jsonArray1.length(); k++) {
                                        RedemptionDetailModal redemptionDetailModal = new RedemptionDetailModal();
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                                        Log.e("dcsl", jsonArray1.length() + "");

                                        JSONObject object1 = jsonArray1.getJSONObject(0);
                                        final String image2 = object1.getString("image");

                                        Log.e("sgdgvdfvb", image2);
                                        Log.e("sgdgvdfvb", path);
                                        try {
                                            if (object1 != null) {
                                                Picasso.with(RedempItemDetailActivity.this).load(path + image2).into(imgProduct);

                                            }
                                        } catch (Exception ignored) {

                                        }

                                        redemptionDetailModal.setImage(jsonObject1.getString("image"));
                                        redemptionDetailModal.setPath(path);
                                        redemptionArrayList.add(redemptionDetailModal);
                                    }


                                }

                            }

                            detailAdapter = new RedemptionDetailAdapter(RedempItemDetailActivity.this, redemptionArrayList);
                            rec_redmptn_detail.setAdapter(detailAdapter);
                            spin_kit.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            Log.e("nkvckmv", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("tuyiu", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }

    public void show_video() {

        Log.e("dskjdkjv", strProdutId);
        Log.e("dskjdkjv", strUserId);
        Log.e("dskjdkjv", strORDERID);
        AndroidNetworking.post(API.BASEURL + API.show_reward_order_detail)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("product_id", strProdutId)
                .addBodyParameter("order_id", strORDERID)
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
                                String reward = jsonObject.getString("reward");
                                JSONArray array = new JSONArray(reward);
                                for (int j = 0; j < array.length(); j++) {

                                    JSONObject object = array.getJSONObject(j);
                                    String video = object.getString("video");
                                    JSONArray jsonArray = new JSONArray(video);

                                    for (int k = 0; k < jsonArray.length(); k++) {

                                        ProductVideoModal videoModal = new ProductVideoModal();
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                                        String status = jsonObject1.getString("status");

                                        videoModal.setVideo(jsonObject1.getString("link"));
                                        videoModal.setStatus(jsonObject1.getString("status"));
                                        Log.e("xzlclxzklvkc", jsonObject1.getString("link"));
                                        videoModalArrayList.add(videoModal);
                                    }

                                }


                            }

                            productVideoAdapter = new ProductVideoAdapter(RedempItemDetailActivity.this, videoModalArrayList);
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
