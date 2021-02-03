package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.kodmap.library.kmrecyclerviewstickyheader.KmHeaderItemDecoration;
import com.tokayoapp.Adapter.CompanyAdapter;
import com.tokayoapp.Adapter.CountryAdapter;
import com.tokayoapp.Modal.CompanyModal;
import com.tokayoapp.Modal.CountryModal;
import com.tokayoapp.Modal.ItemType;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowCompanies extends AppCompatActivity {

    String strCountryName = "";
    String strCountryId = "";
    String strZoneId = "";

    private CompanyAdapter adapter;
    private RecyclerView recyclerview;
    private LinearLayoutManager layoutManager;
    private KmHeaderItemDecoration kmHeaderItemDecoration;
    List<CompanyModal> modelList;
    public  static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_companies);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strCountryId = AppConstant.sharedpreferences.getString(AppConstant.CountryId, "");
        strCountryName = AppConstant.sharedpreferences.getString(AppConstant.CountryName, "");
        strZoneId = AppConstant.sharedpreferences.getString(AppConstant.ZoneId, "");

        TextView txtCompany = findViewById(R.id.txtCompany);
        txtCompany.setText(strCountryName);
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
         context=this;
        initAdapter();
        initData();
    }

    private void initAdapter() {
        adapter = new CompanyAdapter();
        layoutManager = new LinearLayoutManager(this);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(layoutManager);
        kmHeaderItemDecoration = new KmHeaderItemDecoration(adapter);
        recyclerview.setAdapter(adapter);
    }

    private void initData() {

        final ProgressDialog commandDialog = new ProgressDialog(ShowCompanies.this);
        commandDialog.setMessage("Please wait..");
        commandDialog.setIndeterminate(true);
        commandDialog.show();

        AndroidNetworking.post(API.BASEURL + API.show_company)
                .addBodyParameter("zone_id",strZoneId)
                .setTag("Show companies")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("uifoufv", response.toString());
                        modelList = new ArrayList<>();
                        try {
                            for (Integer i = 0; i < response.length(); i++) {
                                JSONObject jsonObject=response.getJSONObject(i);
                                CompanyModal headerModel=new CompanyModal();
                                headerModel.setType(ItemType.Header);
                                headerModel.setCom_title(jsonObject.getString("company"));
                                headerModel.setCom_id(jsonObject.getString("id"));
                                modelList.add(headerModel);

                                JSONArray jsonArray=new JSONArray(jsonObject.getString("delivery_fee"));
                                for (Integer j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject2=jsonArray.getJSONObject(j);
                                    CompanyModal itemModel=new CompanyModal();
                                    itemModel.setType(ItemType.Post);
                                    itemModel.setWeight(jsonObject2.getString("weight"));
                                    itemModel.setPrice(jsonObject2.getString("price"));

                                    modelList.add(itemModel);
                                }
                            }
                            adapter.submitList(modelList);
                            commandDialog.dismiss();


                        } catch (Exception e) {
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