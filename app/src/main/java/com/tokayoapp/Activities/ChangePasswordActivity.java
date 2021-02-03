package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {
RelativeLayout rl_back;
Button btn_change;
EditText edt_oldPass,edt_newPass,edt_confirmPass;
String stNewPass="",stConfirm="",strUserid="",strOld="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        rl_back=findViewById(R.id.rl_back);
        edt_oldPass=findViewById(R.id.edt_oldPass);
        btn_change=findViewById(R.id.btn_change);
        edt_newPass=findViewById(R.id.edt_newPass);
        edt_confirmPass=findViewById(R.id.edt_confirmPass);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserid=AppConstant.sharedpreferences.getString(AppConstant.UserId,"");

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stConfirm=edt_confirmPass.getText().toString().trim();
                stNewPass=edt_newPass.getText().toString().trim();
                strOld=edt_oldPass.getText().toString().trim();

                if (strOld.equals("")){
                    Toast.makeText(ChangePasswordActivity.this,"Please Enter Correct Old Password",Toast.LENGTH_LONG).show();

                }
               else if (stNewPass.equals("")){
                    Toast.makeText(ChangePasswordActivity.this,"Please Enter New Password",Toast.LENGTH_LONG).show();

                }else if (!stConfirm.equals(stNewPass)){
                    Toast.makeText(ChangePasswordActivity.this,"Password Not Matched Please Enter Correct Password",Toast.LENGTH_LONG).show();
                }
                else {
                    change_Password();
                }


            }
        });

    }


    public void change_Password(){

        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=change_password")
        AndroidNetworking.post(API.BASEURL+API.change_password)
                .addBodyParameter("user_id",strUserid)
                .addBodyParameter("old_password",strOld)
                .addBodyParameter("new_password",stNewPass)
                .addBodyParameter("confirm_password",stConfirm)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("r4gter",response.toString());
                        try {
                            if (response.getString("result").equals("Successful")){
                                Toast.makeText(ChangePasswordActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(ChangePasswordActivity.this,ChangePasswordActivity.class));
                                edt_newPass.setText("");
                                edt_confirmPass.setText("");
                                edt_oldPass.setText("");

                            }else {
                                Toast.makeText(ChangePasswordActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Log.e("egrdgh",e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("sdkljkjl",anError.getMessage());

                    }
                });

    }
}