package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Adapter.AddressListAdapter;
import com.tokayoapp.Adapter.CountryAdapter;
import com.tokayoapp.Modal.AddressListModal;
import com.tokayoapp.Modal.CountryModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowCountry extends AppCompatActivity {

    RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    CountryAdapter countryAdapter;
    ArrayList<CountryModal> addressArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_country);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerview = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
        showCountries();
    }


    public void showCountries() {

        final ProgressDialog commandDialog = new ProgressDialog(ShowCountry.this);
        commandDialog.setMessage("Please wait..");
        commandDialog.setIndeterminate(true);
        commandDialog.show();

        AndroidNetworking.post(API.BASEURL + API.show_country)
                .setTag("Show Countries")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("uifoufv", response.toString());
                        addressArrayList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                CountryModal countryModal = new CountryModal();
                                countryModal.setId(jsonObject.getString("country_id"));
                                countryModal.setName(jsonObject.getString("country"));
                                countryModal.setZone_id(jsonObject.getString("zone_id"));
                                countryModal.setZone_name(jsonObject.getString("zone"));
                                addressArrayList.add(countryModal);
                            }

                            countryAdapter = new CountryAdapter(addressArrayList, ShowCountry.this);
                            recyclerview.setAdapter(countryAdapter);
                            commandDialog.dismiss();
                        }
                        catch (JSONException e) {
                            Log.e("efrdegf", e.getMessage());
                            commandDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("etfgr", anError.getMessage());
                        commandDialog.dismiss();
                    }
                });
    }
}