package com.tokayoapp.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.FacebookSdk;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SplashActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_CALL_PHONE = 200;
    String strUserID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserID = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");

        Log.e("dfvhjkxv", strUserID);

        printHashKey();
     /*   new Handler().postDelayed(new Runnable() {


            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void run() {

                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        }, 3000);*/

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void run() {
                //  Intent i1 = new Intent(SplashActivity.this, HomeActivity .class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]
                                    {android.Manifest.permission.READ_EXTERNAL_STORAGE
                                            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                            , Manifest.permission.CAMERA
                                            , Manifest.permission.ACCESS_FINE_LOCATION
                                            , Manifest.permission.ACCESS_COARSE_LOCATION
                                            , Manifest.permission.INTERNET,
                                    },
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {
                    if (strUserID.equals("")) {

                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        Animatoo.animateZoom(SplashActivity.this);

                    } else {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        Animatoo.animateZoom(SplashActivity.this);
                    }
                }
            }
        }, 1000);


    }

    /*For Facebook genarte hashkey here*/
    public void printHashKey() {
// Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tokayoapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.e("dfasdfwfdf", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("dafadfadfadfadf", e.getMessage());

        } catch (NoSuchAlgorithmException e) {
            Log.e("dafadfadfadfadf", e.getMessage());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    , Manifest.permission.CAMERA
                                    , Manifest.permission.ACCESS_FINE_LOCATION
                                    , Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_CALL_PHONE);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                    //used one time Login,,,,,,
                } else {
                    if (strUserID.equals("")) {
                        //////get a intent,,,,,,,
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        Animatoo.animateZoom(SplashActivity.this);

                    } else {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        Animatoo.animateZoom(SplashActivity.this);
                    }
                }
            } else {
                Toast.makeText(this, "Existing User? Log in", Toast.LENGTH_SHORT).show();
            }
        }

    }

}