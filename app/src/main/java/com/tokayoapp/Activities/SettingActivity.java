package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tokayoapp.R;

public class SettingActivity extends AppCompatActivity {

    RelativeLayout rl_back,rl_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        rl_back=findViewById(R.id.rl_back);
        rl_pass=findViewById(R.id.rl_pass);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(SettingActivity.this,ChangePasswordActivity.class));
            }
        });
    }
}