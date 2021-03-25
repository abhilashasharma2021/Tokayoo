package com.tokayoapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.internal.DialogFeature;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.squareup.picasso.Picasso;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView profile_Img;
    private static final String IMAGE_DIRECTORY = "/directory";
    private int GALLERY = 1, CAMERA = 2;
    File f;
    TextView txt_email;
    EditText edt_country, edt_state, edt_pin, edt_name, edt_contact;
    String strUserId = "", strUserName = "", strUserMobile = "", strUserImage = "", strCountry = "", strState = "", strPostal = "", strUserEmail = "", strSelectedCountryCode = "";
    ProgressBar spin_kit;
    ImageView img_back;
    Button btn_update;
    EditText txt_countrycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        spin_kit = findViewById(R.id.spin_kit);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edt_pin = findViewById(R.id.edt_pin);
        edt_contact = findViewById(R.id.edt_contact);
        edt_name = findViewById(R.id.edt_name);
        txt_email = findViewById(R.id.txt_email);
        edt_country = findViewById(R.id.edt_country);
        edt_state = findViewById(R.id.edt_state);
        profile_Img = findViewById(R.id.profile_Img);
        btn_update = findViewById(R.id.btn_update);
        txt_countrycode = findViewById(R.id.txt_countrycode);


        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strUserName = AppConstant.sharedpreferences.getString(AppConstant.UserName, "");
        strUserMobile = AppConstant.sharedpreferences.getString(AppConstant.UserMobile, "");
        strUserEmail = AppConstant.sharedpreferences.getString(AppConstant.UserEmail, "");
        strUserImage = AppConstant.sharedpreferences.getString(AppConstant.UserImage, "");
        strCountry = AppConstant.sharedpreferences.getString(AppConstant.UserCountry, "");
        strState = AppConstant.sharedpreferences.getString(AppConstant.UserState, "");
        strPostal = AppConstant.sharedpreferences.getString(AppConstant.UserPin, "");
        strSelectedCountryCode = AppConstant.sharedpreferences.getString(AppConstant.SelectedCountryCode, "");

        Log.e("dsfl", strUserId);
        Log.e("dsfl", strUserName);
        Log.e("dsfl", strUserMobile);
        Log.e("dsfl", strUserEmail);
        Log.e("dsfl", strPostal);
        Log.e("dsfl", strState);
        Log.e("hfghfghff", strCountry);
        Log.e("dsfl", strUserImage);



      /*  txt_email.setText(strUserEmail);
        edt_name.setText(strUserName);
        edt_contact.setText(strUserMobile);
        edt_country.setText(strCountry);
        edt_state.setText(strState);
        edt_pin.setText(strPostal);*/

        show_profile();


        try {
            Picasso.with(EditProfileActivity.this).load(strUserImage).into(profile_Img);
        } catch (Exception ignored) {

        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strUserName = edt_name.getText().toString().trim();
                String countrycode = "+"+txt_countrycode.getText().toString().trim();
                if (strSelectedCountryCode.equals("")) {
                    strUserMobile = "+60-" + edt_contact.getText().toString().trim();
                } else {
                    strUserMobile = countrycode + "-" + edt_contact.getText().toString().trim();
                }
               /* strUserMobile ="";
                strUserMobile = "+"+txt_countrycode.getText().toString().trim().concat(edt_contact.getText().toString().trim());*/
                Log.i("ffdmkhfdkl", "onClick: "+strUserMobile);
                strCountry = edt_country.getText().toString().trim();
                strState = edt_state.getText().toString().trim();
                strPostal = edt_pin.getText().toString().trim();
                update_profile(strUserName, strUserMobile, strCountry, strState, strPostal);


            }
        });

        profile_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

    }

    public void showPictureDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select ACtion");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture image from camera"};

        builder.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0:
                        choosePhotoFromGallery();
                        break;

                    case 1:
                        captureFromCamera();
                        break;
                }

            }
        });

        builder.show();
    }


    public void choosePhotoFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, GALLERY);
    }


    public void captureFromCamera() {

        Intent intent_2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent_2, CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    profile_Img.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profile_Img.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("fvbcbv", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public void update_profile(String strUserName, String strcontact, String strCountry, String strState, String strPostal) {

        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        Log.e("sdhjhdj", strUserName);
        Log.e("sdhjhdj", strUserId);
        Log.e("sdhjhdj", strcontact);
        Log.e("sdhjhdj", strState);
        Log.e("sdhjhdj", strCountry);

        //   AndroidNetworking.upload("https://3511535117.co/Tokayo/api/process.php?action=update_profile")
        AndroidNetworking.upload(API.BASEURL + API.update_profile)
                .addMultipartParameter("id", strUserId)
                .addMultipartParameter("username", strUserName)
                .addMultipartParameter("contact", strcontact)
                .addMultipartParameter("country", strCountry)
                .addMultipartParameter("state", strState)
                .addMultipartParameter("postal_code", strPostal)
                .addMultipartFile("image", f)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("fjdhfdj", response.toString());
                        try {
                            if (response.getString("result").equals("successfully")) {
                                String strId = response.getString("id");
                                String username = response.getString("username");
                                String email = response.getString("email");
                                String contact = response.getString("contact");
                                String country = response.getString("country");
                                String postal_code = response.getString("postal_code");
                                String state = response.getString("state");
                                String image = response.getString("image");
                                String reward_point = response.getString("reward_point");

                                Log.e("sfhcsdkj", reward_point);
                                Log.e("sdgbvdsfb", image);


                                edt_name.setText(username);
                                String[] code = contact.split("-");
                                if (code.length==1){

                                }

                                txt_countrycode.setText(code[0]);
                                edt_contact.setText(code[1]);


                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.UserMobile, contact);
                                editor.putString(AppConstant.UserName, username);
                                editor.putString(AppConstant.UserCountry, country);
                                editor.putString(AppConstant.UserPin, postal_code);
                                editor.putString(AppConstant.UserState, state);
                                editor.putString(AppConstant.UserImage, image);
                                // editor.putString(AppConstant.UserRewardPoints,reward_point);
                                editor.commit();

                                try {
                                    Picasso.with(EditProfileActivity.this).load(image).into(profile_Img);
                                } catch (Exception ignored) {

                                }
                                //  Toast.makeText( EditProfileActivity.this,response.getString( "result" ),Toast.LENGTH_LONG ).show();


                                Toast.makeText(EditProfileActivity.this, "Successfully updated your profile", Toast.LENGTH_SHORT).show();

                                spin_kit.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            Log.e("fdvgfdfbl", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("gfgdfbl", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }


    public void show_profile() {

        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);


        //   AndroidNetworking.upload("https://3511535117.co/Tokayo/api/process.php?action=update_profile")
        AndroidNetworking.upload(API.BASEURL + API.update_profile)
                .addMultipartParameter("id", strUserId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("fjdhfdj", response.toString());
                        try {
                            if (response.getString("result").equals("successfully")) {
                                String strId = response.getString("id");
                                String username = response.getString("username");
                                String email = response.getString("email");
                                String contact = response.getString("contact");
                                String country = response.getString("country");
                                String postal_code = response.getString("postal_code");
                                String state = response.getString("state");
                                String image = response.getString("image");
                                String reward_point = response.getString("reward_point");

                                Log.e("sfhcsdkj", reward_point);
                                Log.e("sdgbvdsfb", image);

                                String[] code = contact.split("-");

                                Log.i("hghghghgh", "onResponse: "+code.length);
                                if (code.length == 1) {
                                    txt_countrycode.setText(code[0]);
                                } else if (code.length == 2) {
                                    txt_countrycode.setText(code[0]);
                                    edt_contact.setText(code[1]);
                                }


                                edt_name.setText(username);

                                txt_email.setText(email);
                                edt_name.setText(username);

                                edt_country.setText(country);
                                edt_state.setText(state);
                                edt_pin.setText(postal_code);

                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.UserMobile, contact);
                                editor.putString(AppConstant.UserName, username);
                                editor.putString(AppConstant.UserCountry, country);
                                editor.putString(AppConstant.UserPin, postal_code);
                                editor.putString(AppConstant.UserState, state);
                                editor.putString(AppConstant.UserImage, image);
                                // editor.putString(AppConstant.UserRewardPoints,reward_point);
                                editor.commit();

                                try {
                                    Picasso.with(EditProfileActivity.this).load(image).into(profile_Img);
                                } catch (Exception ignored) {

                                }
                                //  Toast.makeText( EditProfileActivity.this,response.getString( "result" ),Toast.LENGTH_LONG ).show();

                                spin_kit.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            Log.e("fdvgfdfbl", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("gfgdfbl", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });


    }


}



