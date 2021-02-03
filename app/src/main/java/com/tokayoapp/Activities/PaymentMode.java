package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Activities.stripepayment.AddCardDetailPayment;
import com.tokayoapp.Activities.stripepayment.ShowCardDetailPayment;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentMode extends AppCompatActivity {
    ImageView img_back;
    LinearLayout ll_cod,ll_stripe;
    Button btn_checkout;
    ProgressBar spin_kit;
    String st_paymentStatus="",st_user_id="",st_delivery_charge="",st_TotalAmount="",st_address_id="",st_CheckoutStatus="",st_NetDeliveryCharge="";
    String st_RewardId="",st_RewardPoints="",strMobile="",strFullName="",strAddress="";
    String st_WeightId="",st_ColorId="",st_ModelId="",strOrderItems="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);
        spin_kit = findViewById(R.id.spin_kit);
        img_back = findViewById(R.id.img_back);
        ll_stripe = findViewById(R.id.ll_stripe);
        ll_cod = findViewById(R.id.ll_cod);
        btn_checkout = findViewById(R.id.btn_checkout);

        ll_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_cod.setBackgroundColor(getResources().getColor(R.color.grey_light));
                AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,0);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.paymentStatus, "1");
                editor.commit();
            }
        });
        ll_stripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_stripe.setBackgroundColor(getResources().getColor(R.color.grey_light));
                AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,0);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.paymentStatus, "2");
                editor.commit();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,0);
                st_paymentStatus= AppConstant.sharedpreferences.getString(AppConstant.paymentStatus,"");
                st_user_id= AppConstant.sharedpreferences.getString(AppConstant.UserId,"");
                st_NetDeliveryCharge= AppConstant.sharedpreferences.getString(AppConstant.NetDeliveryCharge,"");
                st_TotalAmount= AppConstant.sharedpreferences.getString(AppConstant.CartGrandTotal,"");
                st_address_id= AppConstant.sharedpreferences.getString(AppConstant.defaultIdAddress,"");
                st_CheckoutStatus= AppConstant.sharedpreferences.getString(AppConstant.CheckoutStatus,"");
                st_RewardId= AppConstant.sharedpreferences.getString(AppConstant.RewardId,"");
                st_RewardPoints= AppConstant.sharedpreferences.getString(AppConstant.singleRewardPoints,"");
                strFullName= AppConstant.sharedpreferences.getString(AppConstant.RewardUserName,"");
                st_delivery_charge = AppConstant.sharedpreferences.getString(AppConstant.delivery_charge, "");
                strMobile= AppConstant.sharedpreferences.getString(AppConstant.RewardUserMobile,"");
                strAddress= AppConstant.sharedpreferences.getString(AppConstant.RewardUserAddress,"");
                st_ColorId = AppConstant.sharedpreferences.getString(AppConstant.RewardSelectedColorId, "");
                st_ModelId = AppConstant.sharedpreferences.getString(AppConstant.RewardSelectedModelId, "");
                st_WeightId = AppConstant.sharedpreferences.getString(AppConstant.RewardSelectedWeightId, "");
                strOrderItems = AppConstant.sharedpreferences.getString(AppConstant.OrderItems, "");
                Log.e("hsdkajhvklj", strOrderItems);
                Log.e("dfjnfgmjf", st_NetDeliveryCharge);

                Log.e("dsgfvd", st_ModelId);
                Log.e("dsgfvd", st_ColorId);
                Log.e("dsgfvd", "WeightId"+st_WeightId);
                Log.e("sdksck",st_RewardId);
                Log.e("sdksck",strFullName);
                Log.e("sdksck",strAddress);
                Log.e("sdksck",st_RewardPoints);
                Log.e("sdksck",st_paymentStatus);
                Log.e("sdksck",strMobile);
                Log.e("sdksck",st_user_id);
                Log.e("sdksck",st_TotalAmount);
                Log.e("efddfgvg",st_address_id);
                Log.e("sdksck",st_CheckoutStatus);

                if (st_CheckoutStatus.equals("OrderProduct")){


                    if (st_paymentStatus.equals("1")){

                        chekout_product();
                    }
                    else if (st_paymentStatus.equals("2")){

                           //startActivity(new Intent(PaymentMode.this, ShowCardDetailPayment.class));
                           startActivity(new Intent(PaymentMode.this, AddCardDetailPayment.class));

                    }
                }else {

                    if (st_paymentStatus.equals("1")){
                        checkout_Reward();
                       // startActivity(new Intent(PaymentMode.this, ReedeemPopUpActivity.class));
                    }
                    else if (st_paymentStatus.equals("2")){
                        startActivity(new Intent(PaymentMode.this, AddCardDetailPayment.class));

                    }

                }




            }

        });


    }


    public void chekout_product() {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);

        Log.e("rtytyt", st_user_id);
        Log.e("rtytyt", st_paymentStatus);
        Log.e("rtytyt", st_TotalAmount);
        Log.e("rtytyt", st_address_id);
        Log.e("dsgbv", st_NetDeliveryCharge);

        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=checkout")
        AndroidNetworking.post(API.BASEURL+API.checkout)
                .addBodyParameter("user_id",st_user_id)
                .addBodyParameter("address_id",st_address_id)
                .addBodyParameter("total_amount",st_TotalAmount)
                .addBodyParameter("order_type",st_paymentStatus)
                .addBodyParameter("order_item",strOrderItems)
                .addBodyParameter("delevery_charge",st_NetDeliveryCharge)
                .setTag("Order")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("trtyghh", response.toString());

                        try {

                            if(response.getString("result").equals("Placed order successfully")){

                                String id = response.getString("id");
                                String user_id = response.getString("user_id");
                                String order_id = response.getString("order_id");
                                String total_amount = response.getString("total_amount");
                                String status = response.getString("status");
                                String address_id = response.getString("address_id");
                                String rewards_point = response.getString("rewards_point");
                                Log.e("dldf",id);
                                Log.e("dldf",address_id);
                                Log.e("dldf",order_id);
                                Log.e("dldf",total_amount);
                                Log.e("dldf",rewards_point);

                                AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.order_no, order_id);
                                editor.putString(AppConstant.CartGrandTotal,total_amount );
                                editor.putString(AppConstant.RewardPoints,rewards_point );
                                editor.commit();
                                spin_kit.setVisibility(View.GONE);

                                Toast.makeText(PaymentMode.this,response.getString("result"),Toast.LENGTH_SHORT).show();
                                final Dialog dialog = new Dialog(PaymentMode.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.paymentsucessful);


                                RelativeLayout rl = (RelativeLayout) dialog.findViewById(R.id.rl);

                                rl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        startActivity(new Intent(PaymentMode.this, PurchaseHistoryActivity.class));
                                        finish();
                                    }
                                });
                                dialog.show();
                            }

                            else {

                                Toast.makeText(PaymentMode.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                spin_kit.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            Log.e("sdlksfk", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("trytr", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }


    public void checkout_Reward(){

        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);

        Log.e("dgfdvg", st_user_id);
        Log.e("dgfdvg", st_paymentStatus);
        Log.e("dgfdvg", st_RewardId);
        Log.e("dgfdvg", st_RewardPoints);
        Log.e("dgfdvg", strFullName);
        Log.e("dgfdvg", strMobile);
        Log.e("dgfdvg", "sadfasfa"+st_WeightId);
        Log.e("dgfdvg", "sadfasfa"+st_ColorId);
        Log.e("dgfdvg", "sadfasfa"+st_ModelId);

        Log.e("sdhvcklsdhjvc", st_delivery_charge );
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=reward_checkout")
        AndroidNetworking.post(API.BASEURL+API.reward_checkout)
                .addBodyParameter("user_id", st_user_id)
                .addBodyParameter("product_id",st_RewardId)
                .addBodyParameter("color_id",st_ColorId)
                .addBodyParameter("model_id",st_ModelId)
                .addBodyParameter("weight",st_WeightId)
                .addBodyParameter("delevery_charge",st_delivery_charge)
                .addBodyParameter("username",strFullName)
                .addBodyParameter("total_point",st_RewardPoints)
                .addBodyParameter("order_type",st_paymentStatus)
                .addBodyParameter("order_status","0")
                .addBodyParameter("contact",strMobile)
                .addBodyParameter("address",strAddress)
                .setTag("Reward Checkout")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("gdfgg",response.toString());

                        try {
                            if (response.getString("result").equals("Placed order successfully")){
                                spin_kit.setVisibility(View.GONE);

                                final Dialog dialog = new Dialog(PaymentMode.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.rewardssucessful);

                                RelativeLayout rl = (RelativeLayout) dialog.findViewById(R.id.rl);
                                rl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        startActivity(new Intent(PaymentMode.this,RedemptionHistoryActivity.class));
                                        Animatoo.animateZoom(PaymentMode.this);
                                        finish();
                                    }
                                });

                                dialog.show();
                            }
                            else {
                                spin_kit.setVisibility(View.GONE);
                                Toast.makeText(PaymentMode.this, "Dont have enough coins", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("dgfvdg",e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fgdgf",anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });
    }
}