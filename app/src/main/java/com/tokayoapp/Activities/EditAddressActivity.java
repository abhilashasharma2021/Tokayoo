package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class EditAddressActivity extends AppCompatActivity {
    Button btn_save;
    String strAddressId = "";
   RelativeLayout rl_back;
   TextView txt_country;
    EditText edt_address, edt_contact, edt_name;
    String strUserName = "", strUserAddress = "", strUserMobile = "",strdefaultStatus="";
    CheckBox check_default;
    String strDefaultStatus = "",strSelectedCountryCode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strAddressId = AppConstant.sharedpreferences.getString(AppConstant.AddressId, "");
        strUserName = AppConstant.sharedpreferences.getString(AppConstant.UserName, "");
        strUserAddress = AppConstant.sharedpreferences.getString(AppConstant.UserAddress, "");
        strUserMobile = AppConstant.sharedpreferences.getString(AppConstant.UserMobile, "");
        strdefaultStatus = AppConstant.sharedpreferences.getString(AppConstant.defaultStatus, "");
        strSelectedCountryCode = AppConstant.sharedpreferences.getString(AppConstant.SelectedCountryCode, "");
        Log.e("fdfvd", strAddressId);
        Log.e("fdfvd", strUserName);
        Log.e("fdfvd", strUserAddress);
        Log.e("fdfvd", strUserMobile);
        Log.e("dsffvd", strdefaultStatus);
        check_default = findViewById(R.id.check_default);
        btn_save = findViewById(R.id.btn_save);
        edt_contact = findViewById(R.id.edt_contact);
        edt_address = findViewById(R.id.edt_address);
        txt_country = findViewById(R.id.txt_country);
        edt_name = findViewById(R.id.edt_name);
        rl_back = findViewById(R.id.rl_back);

        edt_name.setText(strUserName);
        edt_contact.setText(strUserMobile);
        edt_address.setText(strUserAddress);
        txt_country.setText(strSelectedCountryCode);

        if (strdefaultStatus.equals("1")){
            check_default.setChecked(true) ;
        }

        //check_default.setChecked(false);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditAddressActivity.this, AddressListActivity.class));
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*check box per koi value save kisi button ke click per hoti hain*/
                if (check_default.isChecked()) {
                    strDefaultStatus = "1";
                    Log.e("fdlskjhfvk", strDefaultStatus);

                } else {


                }
                strUserName = edt_name.getText().toString().trim();
                strUserAddress = edt_address.getText().toString().trim();
                strUserMobile = edt_contact.getText().toString().trim();

                edit_Address();
            }
        });


    }

    public void edit_Address() {


        Log.e("dfldfk", strAddressId);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=update_address")
        AndroidNetworking.post(API.BASEURL + API.update_address)
                .addBodyParameter("id", strAddressId)
                .addBodyParameter("name", strUserName)
                .addBodyParameter("contact", strUserMobile)
                .addBodyParameter("address", strUserAddress)
                .addBodyParameter("status", strDefaultStatus)
                .setTag("Edit Address")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("efhfhdkjf", response.toString());
                        try {

                            if (response.getString("result").equals("successfully")) {

                                String id = response.getString("id");
                                String user_id = response.getString("user_id");
                                String name = response.getString("name");
                                String contact = response.getString("contact");
                                String address = response.getString("address");
                                String status = response.getString("status");/*1 means checked and 0 means not checked*/

                                Log.e("fudfvou", name);
                                Log.e("fudfvou", contact);
                                Log.e("fudfvou", address);
                                Log.e("yujy", status);

                                edt_name.setText(name);
                                edt_contact.setText(contact);
                                edt_address.setText(address);

                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.UserMobile, contact);
                                editor.putString(AppConstant.UserName, name);
                                editor.putString(AppConstant.UserAddress, address);
                                editor.putString(AppConstant.defaultStatus, status);
                                editor.commit();

                                startActivity(new Intent(EditAddressActivity.this, AddressListActivity.class));

                            }

                        } catch (JSONException e) {
                            Log.e("dojfdog", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("truyt", anError.getMessage());
                    }
                });


    }
}