package com.tokayoapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.places.model.PlaceFields;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EditAddressActivity extends AppCompatActivity {
    Button btn_save;
    String strAddressId = "";
    RelativeLayout rl_back;
   TextView txt_country,edt_address;
    EditText  edt_contact, edt_name;
    String strUserName = "", strUserAddress = "", strUserMobile = "",strdefaultStatus="";
    CheckBox check_default;
    String strDefaultStatus = "",strSelectedCountryCode="";
    private static final int AUTOCOMPLETE_REQUEST_CODE_SEARCH = 1111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strAddressId = AppConstant.sharedpreferences.getString(AppConstant.AddressId, "");
        strUserName = AppConstant.sharedpreferences.getString(AppConstant.UserName, "");
        strUserAddress = AppConstant.sharedpreferences.getString(AppConstant.UserAddress, "");
        strUserMobile = AppConstant.sharedpreferences.getString(AppConstant.UserMobile, "");
        strdefaultStatus = AppConstant.sharedpreferences.getString(AppConstant.defaultStatus, "");
        strSelectedCountryCode = AppConstant.sharedpreferences.getString(AppConstant.SelectedCountryCode, "");
        Log.e("fdfvd", strAddressId);
        Log.e("fdfvd", strUserName);
        Log.e("fdfvd", strUserAddress);
        Log.e("fdfvd", strUserMobile);
        Log.e("dsffvd", strdefaultStatus);
        check_default = findViewById(R.id.check_default);
        btn_save = findViewById(R.id.btn_save);
        edt_contact = findViewById(R.id.edt_contact);
        edt_address = findViewById(R.id.edt_address);
        txt_country = findViewById(R.id.txt_country);
        edt_name = findViewById(R.id.edt_name);
        rl_back = findViewById(R.id.rl_back);



        if (strdefaultStatus.equals("1")){
            check_default.setChecked(true) ;
        }

        //check_default.setChecked(false);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        if (!Places.isInitialized()){

           Places.initialize(EditAddressActivity.this,getString(R.string.api_key), Locale.getDefault());
        }


        edt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(EditAddressActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_SEARCH);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*check box per koi value save kisi button ke click per hoti hain*/
                if (check_default.isChecked()) {
                    strDefaultStatus = "1";
                    Log.e("fdlskjhfvk", strDefaultStatus);

                } else {


                }
                strUserName = edt_name.getText().toString().trim();
                strUserAddress = edt_address.getText().toString().trim();
                String code = txt_country.getText().toString().trim();
                if (strSelectedCountryCode.equals("")){
                    strUserMobile="+60-"+edt_contact.getText().toString().trim();
                }else {
                    strUserMobile=code+"-"+edt_contact.getText().toString().trim();
                }

                edit_Address();
            }
        });

        show_address();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_SEARCH) {
            if (resultCode == RESULT_OK) {

                if (data != null) {

                    Place place = Autocomplete.getPlaceFromIntent(data);
                    Log.e("oireuftoe", "Place: " + place.getName() + ", " + place.getLatLng() +
                            place.getAddress());

                    LatLng pickUplatlng = place.getLatLng();

                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                        if (pickUplatlng != null) {

                            double st_ClinicLat = pickUplatlng.latitude;
                            double st_ClinicLong = pickUplatlng.longitude;
                            Log.e("Scflkl", st_ClinicLat + "");
                            Log.e("Scflkl", st_ClinicLong + "");

                            AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                            editor.putString(AppConstant.SEARCH_USER_LAT, pickUplatlng.latitude + "");
                            editor.putString(AppConstant.SEARCH_USER_LONG, pickUplatlng.longitude + "");
                            editor.commit();
                         /*   SharedHelper.putKey(getApplicationContext(), AppConstats.SEARCH_LOCATION_LAT, pickUplatlng.latitude + "");
                            SharedHelper.putKey(getApplicationContext(), AppConstats.SEARCH_LOCATION_LNG, pickUplatlng.longitude + "");*/

                            Log.e("rtrere", "latlng" + pickUplatlng.latitude + "," + pickUplatlng.longitude);
                            List<Address> addressList = geocoder.getFromLocation(pickUplatlng.latitude, pickUplatlng.longitude, 1);

                            if (addressList != null) {

                                strUserAddress = addressList.get(0).getAddressLine(0);
                                Log.e("rehhbfd", "msg : " + strUserAddress);

                                if (strUserAddress.equals("")) {
                                    edt_address.setText("");
                                } else {
                                    edt_address.setText(strUserAddress);
                                    edt_address.setTextColor(getResources().getColor(R.color.black));
                                }


                                //autoCompleteSearch.setText(address);

                            }
                        }


                    } catch (Exception e) {
                        Log.e("gfvdfrgvd", e.getMessage(), e);
                    }


                }


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                assert status.getStatusMessage() != null;
                Log.i("oireuftoe", status.getStatusMessage());
            }

        }


    }
    public void edit_Address() {


        Log.e("dfldfk", strAddressId);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=update_address")
        AndroidNetworking.post(API.BASEURL + API.update_address)
                .addBodyParameter("id", strAddressId)
                .addBodyParameter("name", strUserName)
                .addBodyParameter("contact", strUserMobile)
                .addBodyParameter("address", strUserAddress)
                .addBodyParameter("status", strDefaultStatus)
                .setTag("Edit Address")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("efhfhdkjf", response.toString());
                        try {

                            if (response.getString("result").equals("successfully")) {

                                String id = response.getString("id");
                                String user_id = response.getString("user_id");
                                String name = response.getString("name");
                                String contact = response.getString("contact");
                                String address = response.getString("address");
                                String status = response.getString("status");/*1 means checked and 0 means not checked*/

                                Log.e("fudfvou", name);
                                Log.e("fudfvou", contact);
                                Log.e("fudfvou", address);
                                Log.e("yujy", status);

                                edt_name.setText(name);
                                String [] code=contact.split("-");
                                txt_country.setText(code[0]);
                                edt_contact.setText(code[1]);
                                edt_address.setText(address);

                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                               /* editor.putString(AppConstant.UserMobile, contact);
                                editor.putString(AppConstant.UserName, name);
                                editor.putString(AppConstant.UserAddress, address);*/  // humne kiya
                                editor.putString(AppConstant.defaultStatus, status);
                                editor.commit();
                                finish();
                              //  startActivity(new Intent(EditAddressActivity.this, AddressListActivity.class));

                            }

                        } catch (JSONException e) {
                            Log.e("dojfdog", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("truyt", anError.getMessage());
                    }
                });


    }






    public void show_address() {


        Log.e("dfldfk", strAddressId);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=update_address")
        AndroidNetworking.post(API.BASEURL + API.update_address)
                .addBodyParameter("id", strAddressId)
                .setTag("Edit Address")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("efhfhdkjf", response.toString());
                        try {

                            if (response.getString("result").equals("successfully")) {

                                String id = response.getString("id");
                                String user_id = response.getString("user_id");
                                String name = response.getString("name");
                                String contact = response.getString("contact");
                                String address = response.getString("address");
                                String status = response.getString("status");/*1 means checked and 0 means not checked*/

                                Log.e("fudfvou", name);
                                Log.e("fudfvou", contact);
                                Log.e("fudfvou", address);
                                Log.e("yujy", status);

                                edt_name.setText(name);


                                String [] code=contact.split("-");
                                txt_country.setText(code[0]);
                                edt_contact.setText(code[1]);

                                edt_address.setText(address);

                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                               /* editor.putString(AppConstant.UserMobile, contact);
                                editor.putString(AppConstant.UserName, name);
                                editor.putString(AppConstant.UserAddress, address);*/  // humne kiya
                                editor.putString(AppConstant.defaultStatus, status);
                                editor.commit();

                            }

                        } catch (JSONException e) {
                            Log.e("dojfdog", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("truyt", anError.getMessage());
                    }
                });


    }
}