package com.tokayoapp.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Adapter.RedemptionHistoryAdapter;
import com.tokayoapp.Modal.HistoryModal;
import com.tokayoapp.Modal.RedemptionHistoryModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedemptionHistoryActivity extends AppCompatActivity {
    ImageView imgBack;
    RelativeLayout rl_empty;
    RecyclerView rec_red_history;
    RecyclerView.LayoutManager layoutManager;
    RedemptionHistoryAdapter historyAdapter;
    String strUserId="";
    ArrayList<RedemptionHistoryModal> historyModalArrayList = new ArrayList<>();
    ProgressBar spin_kit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemption);
        AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId=AppConstant.sharedpreferences.getString(AppConstant.UserId,"");
        Log.e("fkjfk",strUserId);
        spin_kit = findViewById(R.id.spin_kit);
        rl_empty = findViewById(R.id.rl_empty);
        imgBack = findViewById(R.id.imgBack);
        rec_red_history = findViewById(R.id.rec_red_history);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rec_red_history.setLayoutManager(layoutManager);
        show_Hisstory();
    }

    public void show_Hisstory() {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        Log.e("flkldg",strUserId);
       // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_reward_orderHistory")
        AndroidNetworking.post(API.BASEURL+API.show_reward_orderHistory)
                .addBodyParameter("user_id",strUserId)
                .setTag("Show Reward Order Hsitory")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                     Log.e("jkjbkv",response.toString());
                        historyModalArrayList=new ArrayList<>();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);

                                    String id = jsonObject.getString("id");
                                    String order_id = jsonObject.getString("order_id");
                                    String total_point = jsonObject.getString("total_point");
                                    String product_id = jsonObject.getString("product_id");
                                    String username = jsonObject.getString("username");
                                    String contact = jsonObject.getString("contact");
                                    String address = jsonObject.getString("address");
                                    String order_type = jsonObject.getString("order_type");/*For 1=cash on delivery*/
                                    String tracking_status = jsonObject.getString("tracking_status");/*For 1=*/
                                    String order_date = jsonObject.getString("order_date");
                                    String order_time = jsonObject.getString("order_time");
                                    String count = jsonObject.getString("count");
                                    String products = jsonObject.getString("products");
                                    Log.e("sfdsf",order_date);
                                    Log.e("sfdsf",products);
                                    Log.e("sfdsf",username);
                                    Log.e("sfdsf",order_id);
                                    JSONArray jsonArray = new JSONArray(products);

                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject object = jsonArray.getJSONObject(j);

                                        if (object.has("id")){
                                            //String brand_id = object.getString("brand_id");
                                            String reward_name = object.getString("reward_name");
                                            String point = object.getString("point");
                                            String price = object.getString("price");
                                            String weight = object.getString("weight");
                                            String stock = object.getString("stock");
                                            String url = object.getString("url");
                                            String description = object.getString("description");
                                            String image = object.getString("image");
                                            String path = object.getString("path");
                                            String color_id = object.getString("color_id");
                                            String model_id = object.getString("model_id");

                                            RedemptionHistoryModal historyModal=new RedemptionHistoryModal();
                                            historyModal.setId( jsonObject.getString("id"));
                                            historyModal.setProduct_id(jsonObject.getString("product_id"));
                                            historyModal.setRedemptionNumber(jsonObject.getString("order_id"));
                                            historyModal.setName(object.getString("reward_name"));
                                            historyModal.setImage(object.getString("image"));
                                            historyModal.setPath(object.getString("path"));
                                            historyModal.setDate(jsonObject.getString("order_date"));
                                            historyModal.setTime(jsonObject.getString("order_time"));
                                            historyModal.setPoints(jsonObject.getString("total_point"));
                                            historyModal.setOrder_deliver_status(jsonObject.getString("tracking_status"));
                                            historyModalArrayList.add(historyModal);


                                        }


                                    }


                                }
                                if (response.length() == 0) {
                                    rl_empty.setVisibility(View.VISIBLE);
                                    Toast.makeText(RedemptionHistoryActivity.this, "Here is no Redemption History available yet!!!!", Toast.LENGTH_SHORT).show();
                                    spin_kit.setVisibility(View.GONE);
                                }
                                else {
                                    historyAdapter = new RedemptionHistoryAdapter(RedemptionHistoryActivity.this, historyModalArrayList);
                                    rec_red_history.setAdapter(historyAdapter);
                                    spin_kit.setVisibility(View.GONE);

                                }


                            }catch (JSONException e) {
                                spin_kit.setVisibility(View.GONE);
                                Log.e("fkvock",e.getMessage());
                            }
                        }


                    @Override
                    public void onError(ANError anError) {
                        spin_kit.setVisibility(View.GONE);
                        Log.e("gfdgf",anError.getMessage());
                    }
                });
    }
}
