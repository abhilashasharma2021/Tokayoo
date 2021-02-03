package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswod extends AppCompatActivity {
EditText edt_email;
String strEmail;
RelativeLayout relforgot;
    ProgressBar spin_kit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwod);
        edt_email=findViewById(R.id.edt_email);
        relforgot=findViewById(R.id.relforgot);
        spin_kit = findViewById(R.id.spin_kit);
        relforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail=edt_email.getText().toString().trim();

                if (edt_email.equals("")){
                    Toast.makeText(ForgotPasswod.this,"Please enter valid registered email id ",Toast.LENGTH_SHORT).show();
                }

                else {
                    forgot();
                }
            }
        });

    }

    public void forgot(){
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        Log.e("nfkjdsf",strEmail);
     //   AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=forget_password")
        AndroidNetworking.post(API.BASEURL+API.forget_password)
                .addBodyParameter("email",strEmail)
                .setTag("Forgot Password")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("fudyfh",response.toString());

                        try {
                            if (response.getString("result").equals("An email has been sent to you with instructions")){

                                String email=response.getString("email");
                                spin_kit.setVisibility(View.GONE);


                                final Dialog dialog = new Dialog(ForgotPasswod.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(true);
                                dialog.setContentView(R.layout.forgot_popup);

                                TextView txt_email=dialog.findViewById(R.id.txt_email);


                                txt_email.setText(email);

                                RelativeLayout rl_forgot = (RelativeLayout) dialog.findViewById(R.id.rl_forgot);

                                rl_forgot.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        startActivity(new Intent(ForgotPasswod.this,LoginActivity.class));
                                        Animatoo.animateZoom(ForgotPasswod.this);

                                    }
                                });
                                dialog.show();

                            }

else {
                                Toast.makeText(ForgotPasswod.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("gdfghb",e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("uhfhfcd",anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }

}