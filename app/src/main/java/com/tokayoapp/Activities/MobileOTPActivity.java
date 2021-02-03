package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import static com.tokayoapp.Utils.API.resend_otp;


public class MobileOTPActivity extends AppCompatActivity {

    EditText editOne, editTwo, editThree, editFour, editMobile;
    TextView txtHintEnter, txt_counter;
    String strs = "", strsp = "", strspl = "", strsplite = "", strcombine, strMobile = "",strUserId="";
    ProgressBar progress, progressNew;
    LinearLayout ll_verify, ll_otp;
    RelativeLayout relVerify, relsend, rl_count;
    CountryCodePicker counntryPicker;
    String selected_country_code;
    TextView txtresend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_o_t_p);




        relVerify = findViewById(R.id.relVerify);
        counntryPicker = findViewById(R.id.counntryPicker);
        relsend = findViewById(R.id.relsend);
        txt_counter = findViewById(R.id.txt_counter);
        rl_count = findViewById(R.id.rl_count);
        txtHintEnter = findViewById(R.id.txtHintEnter);
        progress = findViewById(R.id.progress);
        txtresend = findViewById(R.id.txtresend);
        ll_otp = findViewById(R.id.ll_otp);
        editMobile = findViewById(R.id.editMobile);
        progressNew = findViewById(R.id.progressNew);

        txtresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend_Otp();
            }
        });

        relVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile_varification();

            }
        });
        relsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strMobile = editMobile.getText().toString().trim();

                if (editMobile.equals("")) {

                    Toast.makeText(MobileOTPActivity.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                } else {

                    genrate_otp();
                }

            }
        });

        editOne = findViewById(R.id.editOne);
        editTwo = findViewById(R.id.editTwo);
        editThree = findViewById(R.id.editThree);
        editFour = findViewById(R.id.editFour);


        final String countrycode = String.valueOf(counntryPicker.getDefaultCountryCodeWithPlus());


        selected_country_code = countrycode;
        Log.e("dsdjsb", countrycode);

        AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=AppConstant.sharedpreferences.edit();
        editor.putString(AppConstant.SelectedCountryCode, selected_country_code);
        editor.commit();


        counntryPicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                selected_country_code = counntryPicker.getSelectedCountryCodeWithPlus();
                Log.e("ygiyhgiuui", counntryPicker.getSelectedCountryCodeWithPlus());


            }
        });
    }



    public void resend_Otp(){
        AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strMobile = AppConstant.sharedpreferences.getString(AppConstant.UserMobile, "");
    //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=resend_otp")

        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API.BASEURL+resend_otp)
                .addBodyParameter("id",strUserId)
                .addBodyParameter("mobile",strMobile)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("sdkls",response.toString());

                        try {
                            if (response.getString("result").equals("Otp Sent Successfully"));

                            progress.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            Log.e("efer",e.getMessage());
                            progress.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ewdwefd",anError.getMessage());
                        progress.setVisibility(View.GONE);
                    }
                });





    }




    public void genrate_otp() {

        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        String  strUserID = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");

        progress.setVisibility(View.VISIBLE);
        Log.e("sfdsfg", strMobile);
        AndroidNetworking.post(API.BASEURL + API.genrate_otp)
                .addBodyParameter("mobile", selected_country_code +"-"+ strMobile)
                .addBodyParameter("id", strUserID)
                .setPriority(Priority.HIGH)
                .setTag("Please wait")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("fghhfg", response.toString());
                        try {
                            if (response.getString("result").equals("Otp Sent Successfully")) {

                                String otp = response.getString("otp");
                                final String id = response.getString("id");
                                final String mobile = response.getString("mobile");


                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.UserMobile, mobile);
                                editor.putString(AppConstant.UserId, id);
                                editor.putString(AppConstant.OTP, otp);
                                editor.commit();






                                // ll_verify.setVisibility(View.VISIBLE);
                                txtHintEnter.setVisibility(View.VISIBLE);
                                ll_otp.setVisibility(View.VISIBLE);
                                editOne.setVisibility(View.VISIBLE);
                                editTwo.setVisibility(View.VISIBLE);
                                editThree.setVisibility(View.VISIBLE);
                                editFour.setVisibility(View.VISIBLE);
                                relsend.setVisibility(View.GONE);
                                relVerify.setVisibility(View.VISIBLE);
                                rl_count.setVisibility(View.VISIBLE);

                                new CountDownTimer(60000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                        txt_counter.setText("Resend Code : " + millisUntilFinished / 1000);
                                    }

                                    public void onFinish() {
                                        txt_counter.setText("Done!!!!");
                                    }

                                }.start();


                                editOne.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }
                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        editTwo.requestFocus();
                                    }
                                });


                                editTwo.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                        editThree.requestFocus();

                                    }
                                });

                                editThree.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                        editFour.requestFocus();

                                    }
                                });

                                editFour.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                        strs = editOne.getText().toString().trim();
                                        Log.e("fgfgfg", strs);
                                        strsp = editTwo.getText().toString().trim();
                                        Log.e("fgfgfg", strsp);
                                        strspl = editThree.getText().toString().trim();
                                        Log.e("fgfgfg", strspl);
                                        strsplite = editFour.getText().toString().trim();
                                        Log.e("fgfgfg", strsplite);
                                        strcombine = strs + strsp + strspl + strsplite;
                                        Log.e("dkjfdfdljd", strcombine);

                                    }
                                });

                                Toast.makeText(MobileOTPActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);
                            } else {

                                Toast.makeText(MobileOTPActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            Log.e("dhfghg", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("reytrhfhgf", anError.getMessage());
                        progress.setVisibility(View.GONE);
                    }
                });


    }


    public void mobile_varification() {
        progressNew.setVisibility(View.VISIBLE);

        Log.e("jkjgfglg", strcombine);
        Log.e("jkjgfglg", selected_country_code + strMobile);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=mobile_varification")
        AndroidNetworking.post(API.BASEURL + API.mobile_varification)
                .addBodyParameter("mobile", selected_country_code+"-" + strMobile)
                .addBodyParameter("otp", strcombine)
                .setPriority(Priority.HIGH)
                .setTag("Please wait")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("fghfh", response.toString());
                        try {
                            if (response.getString("result").equals("Mobile Verfication successfully")) {
                                Toast.makeText(MobileOTPActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                String id = response.getString("id");
                                String mobile = response.getString("mobile");

                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.UserMobile, mobile);
                                editor.putString(AppConstant.UserId, id);
                                editor.commit();
                                startActivity(new Intent(MobileOTPActivity.this, MainActivity.class));
                                Animatoo.animateZoom(MobileOTPActivity.this);
                                finishAffinity();
                                progressNew.setVisibility(View.GONE);
                            }
                            else {

                                Toast.makeText(MobileOTPActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                progressNew.setVisibility(View.GONE);
                            }
                        }

                        catch (JSONException e) {
                            Log.e("dfgdg", e.getMessage());
                            progressNew.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tryghf", anError.getMessage());
                        progressNew.setVisibility(View.GONE);
                    }
                });


    }


}