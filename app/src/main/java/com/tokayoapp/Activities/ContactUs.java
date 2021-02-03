package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactUs extends AppCompatActivity {
    ProgressBar spin_kit;
    EditText edt_number,edt_email,edt_msg;
    Button btn_submit;
    ImageView imgBack;
    String strEmail="",strNumber="",strMsg="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        spin_kit = findViewById(R.id.spin_kit);
        edt_number = findViewById(R.id.edt_number);
        edt_email = findViewById(R.id.edt_email);
        btn_submit = findViewById(R.id.btn_submit);
        edt_msg = findViewById(R.id.edt_msg);
        imgBack = findViewById(R.id.imgBack);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail=edt_email.getText().toString().trim();
                strNumber=edt_number.getText().toString().trim();
                strMsg=edt_msg.getText().toString().trim();

                if (strEmail.equals("")){

                    Toast.makeText(ContactUs.this,"Please Enter Valid Email",Toast.LENGTH_LONG).show();

                }

                else if (strNumber.equals("")){

                    Toast.makeText(ContactUs.this,"Please Enter Valid Mobile Number",Toast.LENGTH_LONG).show();

                }

                else if (strMsg.equals("")){

                    Toast.makeText(ContactUs.this,"Please Enter Message Here",Toast.LENGTH_LONG).show();

                }

                else
                {
                    show_contact();
                }


            }
        });

    }

    public void show_contact (){
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=contact_us")
        AndroidNetworking.post(API.BASEURL+API.contact_us)
                .addBodyParameter("email",strEmail)
                .addBodyParameter("mobile",strNumber)
                .addBodyParameter("fullname","Xyz user")
                .addBodyParameter("message",strMsg)
                .setTag("Contact us")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ijfdivj",response.toString());

                        try {
                            if (response.getString("result").equals("Successfully")){
                                Toast.makeText(ContactUs.this, "Submitted", Toast.LENGTH_SHORT).show();
                                spin_kit.setVisibility(View.GONE);

                            }


                        } catch (JSONException e) {
                            Log.e("tgrhth",e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fdgd",anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }
}