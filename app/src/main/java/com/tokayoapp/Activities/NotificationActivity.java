package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tokayoapp.Adapter.NotificationAdapter;
import com.tokayoapp.Modal.NotificationModal;
import com.tokayoapp.R;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    RelativeLayout rl_back;
    RecyclerView.LayoutManager layoutManager;
    NotificationAdapter notiAdapter;
    ArrayList<NotificationModal> notiArrayList= new ArrayList<>();
    RecyclerView rec_noti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        rl_back=findViewById(R.id.rl_back);
        rec_noti=findViewById(R.id.rec_noti);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        rec_noti.setLayoutManager(layoutManager);
        rec_noti.setHasFixedSize(true);
        notiAdapter=new NotificationAdapter(this,notiArrayList);
        rec_noti.setAdapter(notiAdapter);
        show_AddressList();
    }

    public void show_AddressList(){

        NotificationModal notiModal=new NotificationModal("Welcome Message from finishing app","lustrous black hair in minutes","22/05/2020","3.00 PM")   ;

        for (int i=0;i<3;i++){
            notiArrayList.add(notiModal);

        }






    }
}
