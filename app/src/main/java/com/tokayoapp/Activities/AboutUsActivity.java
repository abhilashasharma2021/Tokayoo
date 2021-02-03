package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;

import com.tokayoapp.R;
import com.tokayoapp.Utils.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutUsActivity extends AppCompatActivity {
    TextView tx_about,txt1;
    ProgressBar spin_kit;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        tx_about = findViewById(R.id.tx_about);
        spin_kit = findViewById(R.id.spin_kit);
        imgBack = findViewById(R.id.imgBack);
        txt1 = findViewById(R.id.txt1);

        SpannableString content = new SpannableString(getResources().getString(R.string.about_us));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt1.setText(content);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        show_aboutUs();
    }


    public void show_aboutUs (){
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=about_us")
        AndroidNetworking.post(API.BASEURL+API.about_us)
                .setTag("About us")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("jhhjjl",response.toString());



                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id=jsonObject.getString("id");
                                String about=jsonObject.getString("about");


                                tx_about.setText(about);
                                spin_kit.setVisibility(View.GONE);
                            }
                        }catch (JSONException e) {
                            Log.e("edfhjs",e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("sfcdsv",anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }
}