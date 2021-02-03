package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Adapter.AddressListAdapter;
import com.tokayoapp.Fragments.CartFragment;
import com.tokayoapp.Modal.AddressListModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressListActivity extends AppCompatActivity {

    RelativeLayout rl_back;
    ImageView iv_noaddress;
    Button btn_add;
    RecyclerView rec_address_list;
    RecyclerView.LayoutManager layoutManager;
    AddressListAdapter addressListAdapter;
    ArrayList<AddressListModal> addressArrayList= new ArrayList<>();
    ProgressBar spin_kit;
    String strUserId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        spin_kit = findViewById(R.id.spin_kit);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId =AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        Log.e("egtfrd", strUserId);

        rl_back=findViewById(R.id.rl_back);
        iv_noaddress=findViewById(R.id.iv_noaddress);
        btn_add=findViewById(R.id.btn_add);
        rec_address_list=findViewById(R.id.rec_address_list);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AddressListActivity.this,AddNewActivity.class));
                finish();
            }
        });

        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        rec_address_list.setLayoutManager(layoutManager);
        rec_address_list.setHasFixedSize(true);


        show_AddressList();
    }
    public void show_AddressList(){

        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);


        Log.e("fodpgvk",strUserId);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_address")
        AndroidNetworking.post(API.BASEURL+API.show_address)
                .addBodyParameter("user_id",strUserId)
                .setTag("Show Address")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("uifoufv",response.toString());

                        addressArrayList=new ArrayList<>();

                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);

                                    String id=jsonObject.getString("id");
                                    String user_id=jsonObject.getString("user_id");
                                    String name=jsonObject.getString("name");
                                    String contact=jsonObject.getString("contact");
                                    String address=jsonObject.getString("address");
                                    String status=jsonObject.getString("status");



                                    Log.e("efjdsjf",id);

                                    AddressListModal addressListModal=new AddressListModal();
                                    addressListModal.setId(jsonObject.getString("id"));
                                    addressListModal.setAddress(jsonObject.getString("address"));
                                    addressListModal.setName(jsonObject.getString("name"));
                                    addressListModal.setContact(jsonObject.getString("contact"));
                                    addressListModal.setStatus(jsonObject.getString("status"));
                                 //   addressListModal.setCountry(jsonObject.getString("country_name"));
                                    addressArrayList.add(addressListModal);
                                }

                                addressListAdapter=new AddressListAdapter(AddressListActivity.this,addressArrayList);
                                rec_address_list.setAdapter(addressListAdapter);
                                spin_kit.setVisibility(View.GONE);

                                if(addressArrayList.size()==0){

                                    iv_noaddress.setVisibility(View.VISIBLE);

                                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                    editor.putString(AppConstant.defaultIdAddress, "");
                                    editor.commit();
                                }

                            }catch (JSONException e) {
                                Log.e("efrdegf",e.getMessage());
                                spin_kit.setVisibility(View.GONE);
                            }
                        }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("etfgr",anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });




    }
}
