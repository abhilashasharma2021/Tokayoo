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

public class FaqActivity extends AppCompatActivity {
    TextView tx_faq,txt1;
    ProgressBar spin_kit;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        tx_faq = findViewById(R.id.tx_faq);
        spin_kit = findViewById(R.id.spin_kit);
        imgBack = findViewById(R.id.imgBack);
        txt1 = findViewById(R.id.txt1);

        /*SpannableString content = new SpannableString(getResources().getString(R.string.faq));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt1.setText(content);
*/

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        show_faq();
    }

    public void show_faq(){
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_faqs")
        AndroidNetworking.post(API.BASEURL+API.show_faqs)
                .setTag("About us")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("erfrdgf",response.toString());



                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id=jsonObject.getString("id");
                                String title=jsonObject.getString("title");
                                String description=jsonObject.getString("description");


                                tx_faq.setText(description);
                                txt1.setText(title);
                                spin_kit.setVisibility(View.GONE);
                            }
                        }catch (JSONException e) {
                            Log.e("tyhtgyh",e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fdrefg",anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }
}