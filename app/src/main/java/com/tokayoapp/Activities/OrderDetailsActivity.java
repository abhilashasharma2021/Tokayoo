package com.tokayoapp.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Adapter.OrderDetailAdapter;
import com.tokayoapp.Modal.OrderDetailModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {
    RelativeLayout rl_back,rl_ship;
    TextView txt_orderNo,txt_companyName, txt_orderNo1, txtName, txtNumber, txt1, txtAddress, txt_pay_time, txt_orderstatus,txt_pay_date, txt_orderdate, txt_ordertime, txt_deliv, txt_price, txt_subtotal, txt_weight, txt_progress, txt_tracking;
    Button btn_track, btn_trackfad;
    TextView txt_shipi_time,txt_shipi_date,txt_country;
    String strUserId = "", strORDERID = "", strProdutId = "",strSelectedCountryCode="";
    ProgressBar spin_kit;
    RecyclerView rec_order_details;
    RecyclerView.LayoutManager layoutManager;
    OrderDetailAdapter orderDetailAdapter;
    ArrayList<OrderDetailModal> orderDetailModalArrayList = new ArrayList<>();
    ImageView iv_copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strORDERID = AppConstant.sharedpreferences.getString(AppConstant.ORDERID, "");
        strProdutId = AppConstant.sharedpreferences.getString(AppConstant.ProdutId, "");
        strSelectedCountryCode = AppConstant.sharedpreferences.getString(AppConstant.SelectedCountryCode, "");

        Log.e("drhndtrjntgn", strSelectedCountryCode);
        Log.e("dfvdv", strUserId);
        Log.e("fgdgv", strORDERID);
        Log.e("fgdgv", strProdutId);
        rec_order_details = findViewById(R.id.rec_order_details);
        iv_copy = findViewById(R.id.iv_copy);
        spin_kit = findViewById(R.id.spin_kit);
        txt_country = findViewById(R.id.txt_country);
        txt1 = findViewById(R.id.txt1);
        txt_shipi_time = findViewById(R.id.txt_shipi_time);
        rl_ship = findViewById(R.id.rl_ship);
        txt_shipi_date = findViewById(R.id.txt_shipi_date);
        txt_companyName = findViewById(R.id.txt_companyName);
        txt_tracking = findViewById(R.id.txt_tracking);
        txt_orderNo = findViewById(R.id.txt_orderNo);
        btn_trackfad = findViewById(R.id.btn_trackfad);
        txtNumber = findViewById(R.id.txtNumber);
        txt_pay_date = findViewById(R.id.txt_pay_date);
        txt_orderdate = findViewById(R.id.txt_orderdate);
        txt_pay_time = findViewById(R.id.txt_pay_time);
        txt_ordertime = findViewById(R.id.txt_ordertime);
        txtAddress = findViewById(R.id.txtAddress);
        txt_deliv = findViewById(R.id.txt_deliv);
        txt_weight = findViewById(R.id.txt_weight);
        txt_subtotal = findViewById(R.id.txt_subtotal);
        txt_price = findViewById(R.id.txt_price);
        txtName = findViewById(R.id.txtName);
        txt_orderNo1 = findViewById(R.id.txt_orderNo1);
        rl_back = findViewById(R.id.rl_back);
        btn_track = findViewById(R.id.btn_track);
        txt_orderstatus = findViewById(R.id.txt_orderstatus);

        txt_country.setText(strSelectedCountryCode);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderDetailsActivity.this, TrackActivity.class));
            }
        });



        show_Order();
    }


    public void show_Order(){
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        Log.e("fdshbdfnb",strUserId);
        Log.e("fdshbdfnb",strORDERID);

        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_order_detail")
        AndroidNetworking.post(API.BASEURL+API.show_order_detail)
                .addBodyParameter("user_id",strUserId)
                .addBodyParameter("order_id",strORDERID)
              //  .addBodyParameter("product_id",strProdutId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                       Log.e("ijiidf",response.toString());
                        try {
                                for (int i=0;i<response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);

                                    String order_id=jsonObject.getString("order_id");
                                    String courier_info=jsonObject.getString("courier_info");

                                    if (courier_info.equals("")){

                                    }
                                    else{
                                        JSONObject jsonObject1=new JSONObject(courier_info);
                                        String company_name=jsonObject1.getString("company_name");
                                        String contact_track=jsonObject1.getString("contact");
                                        String address_track=jsonObject1.getString("address");
                                        String ship_time=jsonObject1.getString("ship_time");
                                        String ship_date=jsonObject1.getString("ship_date");
                                        txt_companyName.setText(company_name);

                                        txt_shipi_time.setText(ship_time);
                                        txt_shipi_date.setText(ship_date);
                                    }

                                    String total_amount=jsonObject.getString("total_amount");
                                    String order_type=jsonObject.getString("order_type");
                                    String order_deliver_status=jsonObject.getString("order_deliver_status");
                                    String order_date=jsonObject.getString("order_date");
                                    String order_time=jsonObject.getString("order_time");
                                    String count=jsonObject.getString("count");
                                    String sub_total=jsonObject.getString("sub_total");
                                    String net_weight=jsonObject.getString("net_weight");
                                    String delivery_charge=jsonObject.getString("delivery_charge");
                                    String tracking_no=jsonObject.getString("tracking_no");
                                    String tracking_status=jsonObject.getString("tracking_status");
                                   /* String ship_time=jsonObject.getString("ship_time");
                                    String ship_date=jsonObject.getString("ship_date");*/



                                    String products=jsonObject.getString("products");
                                    Log.e("kcjvl",delivery_charge);
                                    Log.e("kcjvl",tracking_no);
                                    Log.e("kcjvl",order_id);
                                    //*tracking_no=0 means no tracking Number and 1= show trancking number admin add in it*//*

                                    if (tracking_status.equals("0")){
                                        //   txt1.setVisibility(View.GONE);
                                        btn_track.setVisibility(View.GONE);
                                        btn_trackfad.setVisibility(View.VISIBLE);
                                        txt_tracking.setVisibility(View.GONE);
                                        txt_orderstatus.setText("In Process");
                                        txt_orderstatus.setTextColor(getResources().getColor(R.color.pink));
                                    }else{
                                        // txt1.setVisibility(View.VISIBLE);
                                        txt_tracking.setVisibility(View.VISIBLE);
                                        iv_copy.setVisibility(View.VISIBLE);
                                        txt_tracking.setText(tracking_no);
                                        btn_track.setVisibility(View.VISIBLE);
                                        btn_trackfad.setVisibility(View.GONE);
                                        txt_orderstatus.setText("Ship out");
                                        txt_orderstatus.setTextColor(getResources().getColor(R.color.blue));

                                        btn_track.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                           startActivity(new Intent(OrderDetailsActivity.this,WebViewActivity.class));
                                            }
                                        });

                                    }

                                    iv_copy.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ClipboardManager cm = (ClipboardManager)OrderDetailsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                                            cm.setText(txt_tracking.getText());
                                            Toast.makeText(OrderDetailsActivity.this,"Copied to clipboard", Toast.LENGTH_SHORT).show();
                                        }
                                    });




                                    txt_orderNo.setText(order_id);
                                    txt_orderNo1.setText(order_id);
                                    txt_price.setText(total_amount);
                                    txt_weight.setText(net_weight);

                                    txt_subtotal.setText(sub_total);
                                    txt_deliv.setText(delivery_charge);
                                    txt_ordertime.setText(order_time);
                                    txt_orderdate.setText(order_date);
                                    txt_deliv.setText(delivery_charge);
                                    txt_pay_date.setText(order_date);
                                    txt_pay_time.setText(order_time);

                                    JSONArray jsonArray=new JSONArray(products);

                                    for (int j=0;j<jsonArray.length();j++){

                                        JSONObject object=jsonArray.getJSONObject(j);

                                        String id_new=object.getString("id");
                                        String name=object.getString("name");
                                        String brand_id=object.getString("brand_id");
                                        String description=object.getString("description");
                                        String price=object.getString("price");
                                        String weight=object.getString("weight");
                                        String stock=object.getString("stock");
                                        String image=object.getString("images");
                                        String purchased_quantity=object.getString("purchased_quantity");
                                        String purchased_weight=object.getString("purchased_weight");
                                        String purchased_color=object.getString("purchased_color");
                                        String purchased_price=object.getString("purchased_price");
                                        String purchased_model=object.getString("purchased_model");
                                        String path=object.getString("path");

                                        OrderDetailModal detailModal=new OrderDetailModal();
                                        detailModal.setId(object.getString("id"));
                                        detailModal.setName(object.getString("name"));
                                        detailModal.setQuantity(object.getString("purchased_quantity"));
                                        detailModal.setWeight(object.getString("purchased_weight"));
                                        detailModal.setImage(object.getString("images"));
                                        detailModal.setModel(object.getString("purchased_model"));
                                        detailModal.setColor(object.getString("purchased_color"));
                                        detailModal.setPath(object.getString("path"));
                                        detailModal.setPrice(object.getString("purchased_price"));
                                        detailModal.setOrder_id(jsonObject.getString("order_id"));


                                        orderDetailModalArrayList.add(detailModal);





                                    }

                                    String address=jsonObject.getString("address");
                                    JSONArray jsonArray1=new JSONArray(address);

                                    Log.e("dfideuido",address);

                                    for (int k=0;k<jsonArray1.length();k++){
                                        JSONObject obj=jsonArray1.getJSONObject(k);
                                        String id=obj.getString("id");
                                        String user_id_new=obj.getString("user_id");
                                        String name=obj.getString("name");
                                        String contact=obj.getString("contact");
                                        String address_new=obj.getString("address");


                                        txtName.setText(name);
                                        txtNumber.setText(contact);
                                        txtAddress.setText(address_new);
                                    }





                                }

                            layoutManager = new LinearLayoutManager(OrderDetailsActivity.this, RecyclerView.VERTICAL, false);
                            rec_order_details.setLayoutManager(layoutManager);
                            orderDetailAdapter = new OrderDetailAdapter(OrderDetailsActivity.this, orderDetailModalArrayList);
                            rec_order_details.setAdapter(orderDetailAdapter);
                            spin_kit.setVisibility(View.GONE);

                            }catch (JSONException e) {
                                Log.e("jijgfk",e.getMessage());
                                spin_kit.setVisibility(View.GONE);
                            }

                        }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("truyt",anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }

}

