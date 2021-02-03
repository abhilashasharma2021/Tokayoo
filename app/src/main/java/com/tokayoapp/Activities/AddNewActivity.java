package com.tokayoapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Fragments.CartFragment;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class AddNewActivity extends AppCompatActivity {
    RelativeLayout rl_back;
    EditText edt_address, edt_contact, edt_name;
    Button btn_save;
    String strName = "", strContact = "", strAddress = "", strUserId = "", strAddAddressType = "", strAddressId = "";

    TextView txt_country;
    String strDefaultStatus = "",strSelectedCountryCode="";
    ProgressBar spin_kit;
    CheckBox check_default;
    String country_name ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strAddAddressType = AppConstant.sharedpreferences.getString(AppConstant.AddAddressType, "");
        strAddressId = AppConstant.sharedpreferences.getString(AppConstant.AddressId, "");
        strSelectedCountryCode = AppConstant.sharedpreferences.getString(AppConstant.SelectedCountryCode, "");
        Log.e("fdfvd", strUserId);
        Log.e("fdfvd", strSelectedCountryCode);
        Log.e("fdfvd", strAddAddressType);
        Log.e("fdfvd", strAddressId);
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else {

            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Geocoder geocoder = new Geocoder(getApplicationContext());
            for (String provider : lm.getAllProviders()) {
                @SuppressWarnings("ResourceType") Location location = lm.getLastKnownLocation(provider);
                if (location != null) {
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {
                            country_name = addresses.get(0).getCountryName();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Toast.makeText(getApplicationContext(), country_name, Toast.LENGTH_LONG).show();
            Log.e("asfdsafafasfas", "name" + country_name);

        }


        rl_back = findViewById(R.id.rl_back);
        txt_country = findViewById(R.id.txt_country);
        check_default = findViewById(R.id.check_default);
        spin_kit = findViewById(R.id.spin_kit);
        btn_save = findViewById(R.id.btn_save);
        edt_address = findViewById(R.id.edt_address);
        edt_contact = findViewById(R.id.edt_contact);
        edt_name = findViewById(R.id.edt_name);

        txt_country.setText(strSelectedCountryCode);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        check_default.setChecked(false);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*check box per koi value save kisi button ke click per hoti hain*/
                if (check_default.isChecked()) {
                    strDefaultStatus = "1";
                    Log.e("fdlskjhfvk", strDefaultStatus);

                } else {


                }

                strName = edt_name.getText().toString().trim();
                strContact = edt_contact.getText().toString().trim();
                strAddress = edt_address.getText().toString().trim();

                if (strName.equals("")) {
                    Toast.makeText(AddNewActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();

                } else if (strContact.equals("")) {
                    Toast.makeText(AddNewActivity.this, "Please Enter Your Valid Mobile Number", Toast.LENGTH_SHORT).show();

                } else if (strAddress.equals("")) {
                    Toast.makeText(AddNewActivity.this, "Please Enter Your Valid Address", Toast.LENGTH_SHORT).show();

                } else {

                    if (strAddAddressType.equals("0")) {
                        add_Newaddress();
                    }
                    else {

                        add_Newaddress();
                    }

                }

            }
        });
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddNewActivity.this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void add_Newaddress() {

        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType") Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        country_name = addresses.get(0).getCountryName();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Toast.makeText(getApplicationContext(), country_name, Toast.LENGTH_LONG).show();
        Log.e("asfdsafafasfas", "name" + country_name);

        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);



        Log.e("efdsergfd", strUserId);
        Log.e("efdsergfd", strName);
        Log.e("efdsergfd", strContact);
        Log.e("efdsergfd", strAddress);
        Log.e("sdfdvfc", strDefaultStatus);
        Log.e("sdfdvfc", "dfgdfg"+country_name);


        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=add_address")
        AndroidNetworking.post(API.BASEURL + API.add_address)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("name", strName)
                .addBodyParameter("contact", strContact)
                .addBodyParameter("address", strAddress)
                .addBodyParameter("status", strDefaultStatus)
                .addBodyParameter("country_name", country_name)
                .setTag("Add New Address")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("fiifod", response.toString());
                        try {
                            if (response.getString("result").equals("Successfully")) {

                                String id = response.getString("id");
                                String user_id = response.getString("user_id");
                                final String name = response.getString("name");
                                final String contact = response.getString("contact");
                                final String address = response.getString("address");
                                final String status = response.getString("status");

                                Toast.makeText(AddNewActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddNewActivity.this, AddressListActivity.class));
                                spin_kit.setVisibility(View.GONE);
                                finish();
                            }
                            else {

                                Toast.makeText(AddNewActivity.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("irjtgi", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rdthgft", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });
    }
}