package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Adapter.HomeArrivalAdapter;
import com.tokayoapp.Adapter.ShowArrivalAdapter;
import com.tokayoapp.Modal.HomeNewArrivalModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowNewArrival extends AppCompatActivity {
    ArrayList<HomeNewArrivalModal> arrivalArrayList= new ArrayList<>();
    ShowArrivalAdapter arrivalAdapter;
    RecyclerView rec_newArrival;
    ImageView imgBack;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar spin_kit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_new_arrival);

        spin_kit = findViewById(R.id.spin_kit);
        rec_newArrival =findViewById(R.id.rec_newArrival);
        imgBack =findViewById(R.id.imgBack);
        layoutManager = new GridLayoutManager(ShowNewArrival.this, 2, RecyclerView.VERTICAL, false);
        rec_newArrival.setLayoutManager(layoutManager);
        show_Arrival();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }




    public void show_Arrival() {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);

        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=arrival_products")
        AndroidNetworking.post(API.BASEURL+API.arrival_products)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("fdfgf",response.toString());
                        arrivalArrayList=new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);


                                String id=jsonObject.getString("id");
                                String title=jsonObject.getString("name");
                                String description=jsonObject.getString("description");
                                String image=jsonObject.getString("images");
                                String path=jsonObject.getString("path");
                                String price=jsonObject.getString("price");



                                HomeNewArrivalModal arrivalModal=new HomeNewArrivalModal();

                                arrivalModal.setImage(jsonObject.getString("images"));
                                arrivalModal.setId(jsonObject.getString("id"));
                                arrivalModal.setPrice(jsonObject.getString("price"));
                                arrivalModal.setName(jsonObject.getString("name"));
                                arrivalModal.setBrand(jsonObject.getString("description"));
                                arrivalModal.setPath(jsonObject.getString("path"));

                                arrivalArrayList.add(arrivalModal);


                            }


                            arrivalAdapter = new ShowArrivalAdapter(ShowNewArrival.this,arrivalArrayList);
                            rec_newArrival.setAdapter(arrivalAdapter);
                            spin_kit.setVisibility(View.GONE);

                        }catch (JSONException e) {
                            Log.e("dgfdg", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }



                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("frftr", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }

}