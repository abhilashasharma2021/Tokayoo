package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReedeemPopUpActivity extends AppCompatActivity {
    Button btn_Submit, btn_select_add;
    EditText edt_name, edt_contact, edt_address;
    String st_defaultIdAddress = "",st_SelectedWeightName="";
    String st_RewardWeight = "", strUserId = "", st_RewardPoints = "", st_delivery_charge = "",strRewardSelectedWeightName="", st_paymentStatus = "", st_RewardId = "";
    TextView txt_weight, txt_deliv, txt_Totalprice, txt_CatName, txt_brand;
    ProgressBar spin_kit;
    TextView txt_name, txt_contact, txt_address;
    String strModel="",strColor="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reedeem_pop_up);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, 0);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        st_paymentStatus = AppConstant.sharedpreferences.getString(AppConstant.paymentStatus, "");
        st_RewardId = AppConstant.sharedpreferences.getString(AppConstant.RewardId, "");
        st_RewardPoints = AppConstant.sharedpreferences.getString(AppConstant.singleRewardPoints, "");
        st_RewardWeight = AppConstant.sharedpreferences.getString(AppConstant.RewardWeight, "");
        st_delivery_charge = AppConstant.sharedpreferences.getString(AppConstant.delivery_charge, "");
        st_defaultIdAddress = AppConstant.sharedpreferences.getString(AppConstant.defaultIdAddress, "");
        st_SelectedWeightName = AppConstant.sharedpreferences.getString(AppConstant.SelectedWeightName, "");
        strColor = AppConstant.sharedpreferences.getString(AppConstant.RewardSelectedColorId, "");
        strModel = AppConstant.sharedpreferences.getString(AppConstant.RewardSelectedModelId, "");
        strRewardSelectedWeightName = AppConstant.sharedpreferences.getString(AppConstant.RewardSelectedWeightName, "");


        Log.e("sfgfgfggf", strModel );
        Log.e("sfgfgfggf", strColor );
        Log.e("sfgfgfggf", strRewardSelectedWeightName );

        Log.e("tryhyh", strUserId);
        Log.e("rgfd", st_SelectedWeightName);
        Log.e("tryhyh", st_defaultIdAddress);
        Log.e("tryhyh", st_paymentStatus);
        Log.e("tryhyh", st_RewardId);
        Log.e("dfbgfb", "asfasf"+st_RewardWeight);
        Log.e("tryhyh", st_RewardPoints);
        Log.e("tryhyh", st_delivery_charge);

        btn_Submit = findViewById(R.id.btn_Submit);
        txt_name = findViewById(R.id.txt_name);
        txt_address = findViewById(R.id.txt_address);
        txt_contact = findViewById(R.id.txt_contact);
        txt_brand = findViewById(R.id.txt_brand);
        txt_CatName = findViewById(R.id.txt_CatName);
        txt_Totalprice = findViewById(R.id.txt_Totalprice);
        txt_deliv = findViewById(R.id.txt_deliv);
        txt_weight = findViewById(R.id.txt_weight);
        spin_kit = findViewById(R.id.spin_kit);
        btn_select_add = findViewById(R.id.btn_select_add);
        edt_name = findViewById(R.id.edt_name);
        edt_contact = findViewById(R.id.edt_contact);
        edt_address = findViewById(R.id.edt_address);
        txt_weight.setText(strRewardSelectedWeightName+"."+"00");
        txt_Totalprice.setText(st_RewardPoints+".00");
        txt_deliv.setText(st_delivery_charge+".00");

        btn_select_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.SeletAddressStatus, "select");
                editor.commit();
                startActivity(new Intent(ReedeemPopUpActivity.this, RewardAddressList.class));
                Animatoo.animateZoom(ReedeemPopUpActivity.this);
            }
        });

        show_AddAddress();
    }
    public void show_AddAddress() {

        Log.e("rtgdftrhgb",strUserId);
        Log.e("rtgdftrhgb",st_defaultIdAddress);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_address")
        AndroidNetworking.post(API.BASEURL + API.show_address)
                .addBodyParameter("user_id",strUserId)
                .addBodyParameter("id",st_defaultIdAddress)
                .setTag("Show Address")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("rgdfrg", response.toString());


                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                /*if (i==0){}*/
                                String id = jsonObject.getString("id");
                                String user_id = jsonObject.getString("user_id");
                                final String name = jsonObject.getString("name");
                                final String contact = jsonObject.getString("contact");
                                final String address = jsonObject.getString("address");

                                Log.e("tgryt", id);
                                Log.e("tgryt", name);
                                Log.e("tgryt", contact);
                                Log.e("tgryt", address);

                                txt_name.setText(name);
                                txt_contact.setText(contact);
                                txt_address.setText(address);

                                btn_Submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                        editor.putString(AppConstant.RewardUserName, name);
                                        editor.putString(AppConstant.RewardUserMobile, contact);
                                        editor.putString(AppConstant.RewardUserAddress, address);

                                        editor.commit();

                                        startActivity(new Intent(ReedeemPopUpActivity.this, PaymentMode.class));
                                        Animatoo.animateZoom(ReedeemPopUpActivity.this);
                                        finish();

                                    }

                                });

                            }

                        } catch (JSONException e) {
                            Log.e("gdfgb", e.getMessage());

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("frgfdhb ", anError.getMessage());

                    }
                });

    }
}