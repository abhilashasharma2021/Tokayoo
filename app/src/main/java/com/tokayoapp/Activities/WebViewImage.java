package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

public class WebViewImage extends AppCompatActivity {
    WebView webView;
    String strImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_image);
        webView = findViewById(R.id.webview);
        AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strImageUrl = AppConstant.sharedpreferences.getString(AppConstant.ImageUrl, "");
        //  webView.loadUrl("https://librofutbol.com/root/login/accessPanelbyapp?usu=jitu.maestros@gmail.com&pass=Jitu@7415&productname=ARMANI KIDS&productid=24480");
        webView.loadUrl(strImageUrl);
    }
}