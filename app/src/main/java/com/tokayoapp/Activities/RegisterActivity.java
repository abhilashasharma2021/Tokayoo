package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tokayoapp.Fcm.Config;
import com.tokayoapp.Fcm.NotificationUtils;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextView txtAlreadyHaveAccount;
    ImageView img_singup;
    EditText emailUserName, emailEmail, editpass, editCPass;
    ProgressBar spin_kit;
    String strFullName = "", strEmail = "", strMobile = "", strPassword = "", strConfirm = "";

    //////////////////////////////REGID//////////////////////////////////////////////////////////////
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String strregId = "";
    //////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spin_kit = findViewById(R.id.spin_kit);
        img_singup = findViewById(R.id.img_singup);
        txtAlreadyHaveAccount = findViewById(R.id.txtAlreadyHaveAccount);
        emailUserName = findViewById(R.id.emailUserName);
        emailEmail = findViewById(R.id.emailEmail);
        editpass = findViewById(R.id.editpass);
        editCPass = findViewById(R.id.editCPass);
        SpannableString content = new SpannableString(getResources().getString(R.string.already_have));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 25, 32, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtAlreadyHaveAccount.setText(content);

        txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        img_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strFullName = emailUserName.getText().toString().trim();
                strEmail = emailEmail.getText().toString().trim();
                strPassword = editpass.getText().toString().trim();
                strConfirm = editCPass.getText().toString().trim();


                if (strFullName.equals("")) {

                    Toast.makeText(RegisterActivity.this, "Please Enter Full Name", Toast.LENGTH_LONG).show();

                } else if (strEmail.equals("")) {

                    Toast.makeText(RegisterActivity.this, "Please Enter Valid Email", Toast.LENGTH_LONG).show();

                } else if (strPassword.equals("")) {

                    Toast.makeText(RegisterActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();

                } else if (!strPassword.equals(strConfirm)) {

                    Toast.makeText(RegisterActivity.this, "Password and Confirm Password Not Matched,Please Enter Correct Password", Toast.LENGTH_LONG).show();

                } else {


                    if (Patterns.EMAIL_ADDRESS.matcher(emailEmail.getText().toString().trim()).matches()){
                        signUp();
                    }else  {
                        Toast.makeText(RegisterActivity.this, "Please Enter Valid Email", Toast.LENGTH_LONG).show();

                    }



                }


            }
        });


        ////////////////////////// //*USING FIREBASE SAVE ""REGDID"" ON LOGIN*///////////////////////////////////////////////

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                }
            }
        };
        /*using firebase save  regid on login screen*/
        displayFirebaseRegId();


    }
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        strregId = pref.getString("regId", null);
        Log.e("gtfrhgfh", "Firebase reg id: " + strregId);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    ////////// /*USING FIREBASE SAVE ""REGDID"" END HERE ON LOGIN*///////////////////////////////////////////////



    public void signUp() {

        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        Log.e("sdhjhdj",strEmail);
        Log.e("sdhjhdj",strPassword);
        Log.e("sdhjhdj",strFullName);
        Log.e("sdhjhdj",strregId);
      //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=signup")
       AndroidNetworking.post(API.BASEURL+API.RegisterUser)
               .addBodyParameter( "regid",strregId )
               .addBodyParameter( "os_type","1" )
                .addBodyParameter("username", strFullName)
                .addBodyParameter("email", strEmail)
                .addBodyParameter("password", strPassword)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        spin_kit.setVisibility(View.GONE);
                        Log.e("dsfdv", response.toString());
                        try {
                            if (response.getString("result").equals("successfully Insert")) {

                                String id=response.getString("id");
                                String username=response.getString("username");
                                String email=response.getString("email");
                                String password=response.getString("password");

                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.Id, id);
                                editor.putString(AppConstant.UserName, username);
                                editor.putString(AppConstant.UserEmail, email);
                                editor.commit();

                                startActivity(new Intent(RegisterActivity.this, MobileOTPActivity.class));
                                Animatoo.animateZoom(RegisterActivity.this);
                                Toast.makeText(RegisterActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();


                            }else {
                                Toast.makeText(RegisterActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("rtrtt", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("yujyhj", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }

}