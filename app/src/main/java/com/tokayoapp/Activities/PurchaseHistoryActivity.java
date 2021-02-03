package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Adapter.PurchaseHistoryAdapter;
import com.tokayoapp.Modal.HistoryModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PurchaseHistoryActivity extends AppCompatActivity {

    RecyclerView rec_history;
    RecyclerView.LayoutManager layoutManager;
    PurchaseHistoryAdapter historyAdapter;
    ImageView imgBack;
    RelativeLayout rl_empty;
    ArrayList<HistoryModal> historyModalArrayList = new ArrayList<>();
    String strUserId = "",strStatusPurchase="";
    ProgressBar spin_kit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strStatusPurchase = AppConstant.sharedpreferences.getString(AppConstant.StatusPurchase, "");
        Log.e("fkiljdsk", strUserId);
        spin_kit = findViewById(R.id.spin_kit);
        rl_empty = findViewById(R.id.rl_empty);
        rec_history = findViewById(R.id.rec_history);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (strStatusPurchase.equals("Profile")){
                    finish();
                }
                else {

                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                    editor.putString(AppConstant.Statusback,"Account");
                    editor.commit();
                    startActivity(new Intent(PurchaseHistoryActivity.this,MainActivity.class));

                }

            }
        });

        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rec_history.setLayoutManager(layoutManager);
        show_Hisstory();

    }

    public void show_Hisstory() {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        Log.e("gdfghf", strUserId);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_order")
        AndroidNetworking.post(API.BASEURL + API.show_order)
                .addBodyParameter("user_id", strUserId)
                .setTag("Order History")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("urirtug", response.toString());

                        historyModalArrayList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String order_id = jsonObject.getString("order_id");
                                String total_amount = jsonObject.getString("total_amount");
                                String order_type = jsonObject.getString("order_type");
                                String tracking_status = jsonObject.getString("tracking_status");/*0= not deliver 1=delivered*/
                                String order_date = jsonObject.getString("order_date");
                                String order_time = jsonObject.getString("order_time");
                                String count = jsonObject.getString("count");

                                HistoryModal historyModal = new HistoryModal();
                                historyModal.setId(order_id);
                                historyModal.setPrice(total_amount);
                                historyModal.setCount(count);
                                historyModal.setOrder_deliver_status(tracking_status);
                                historyModal.setDate(order_date);
                                historyModal.setOrderId(order_id);
                                historyModalArrayList.add(historyModal);


                              /*  String products = jsonObject.getString("products");
                                JSONArray jsonArray = new JSONArray(products);

                                for (int j = 0; j < jsonArray.length(); j++) {

                                    historyModalArrayList = new ArrayList<>();

                                    JSONObject object = jsonArray.getJSONObject(j);
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String brand_id = object.getString("brand_id");
                                    String subbrand_id = object.getString("subbrand_id");
                                    String description = object.getString("description");
                                    String price = object.getString("price");
                                    String weight = object.getString("weight");
                                    String stock = object.getString("stock");
                                    String url = object.getString("url");
                                    String image = object.getString("images");
                                    String arrival_status = object.getString("arrival_status");
                                    String purchased_quantity = object.getString("purchased_quantity");
                                    String path = object.getString("path");
                                    String colorss = object.getString("colorss");
                                    String modelss = object.getString("modelss");

                                    HistoryModal historyModal = new HistoryModal();
                                    historyModal.setId(object.getString("id"));
                                    historyModal.setName(object.getString("name"));
                                    historyModal.setImage(object.getString("images"));
                                    historyModal.setColor(object.getString("colorss"));
                                    historyModal.setPath(object.getString("path"));
                                    historyModal.setPrice(object.getString("price"));
                                    historyModal.setCount(jsonObject.getString("count"));
                                    historyModal.setModel(object.getString("modelss"));
                                    historyModal.setOrder_deliver_status(jsonObject.getString("order_deliver_status"));
                                    historyModal.setQuantity(object.getString("purchased_quantity"));
                                    historyModal.setOrderId(jsonObject.getString("order_id"));
                                    historyModalArrayList.add(historyModal);
                                }*/
                            }
                            if (response.length() == 0) {
                                rl_empty.setVisibility(View.VISIBLE);
                                Toast.makeText(PurchaseHistoryActivity.this, "Here is no history available yet!!!!", Toast.LENGTH_SHORT).show();
                                spin_kit.setVisibility(View.GONE);
                            }
                            else {
                                historyAdapter = new PurchaseHistoryAdapter(PurchaseHistoryActivity.this, historyModalArrayList);
                                rec_history.setAdapter(historyAdapter);
                                spin_kit.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            Log.e("jijigft", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("fdrgf", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }


}
