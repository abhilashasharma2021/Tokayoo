package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.tokayoapp.R;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webview);
        //  webView.loadUrl("https://librofutbol.com/root/login/accessPanelbyapp?usu=jitu.maestros@gmail.com&pass=Jitu@7415&productname=ARMANI KIDS&productid=24480");
        webView.loadUrl("https://www.tracking.my/");

    }
}