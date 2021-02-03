package com.tokayoapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.AboutUsActivity;
import com.tokayoapp.Activities.AddressListActivity;
import com.tokayoapp.Activities.ChatActivity;
import com.tokayoapp.Activities.ContactUs;
import com.tokayoapp.Activities.EditProfileActivity;
import com.tokayoapp.Activities.FaqActivity;
import com.tokayoapp.Activities.LoginActivity;
import com.tokayoapp.Activities.MainActivity;
import com.tokayoapp.Activities.NotificationActivity;
import com.tokayoapp.Activities.PrivacyPolicy;
import com.tokayoapp.Activities.PurchaseHistoryActivity;
import com.tokayoapp.Activities.RedemptionHistoryActivity;
import com.tokayoapp.Activities.SettingActivity;
import com.tokayoapp.Activities.SplashActivity;
import com.tokayoapp.Activities.TermAndCondActivity;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountFragment extends Fragment {
    ImageView imgchat;
    TextView tx_about, tx_terms, tx_privacy, tx_faq, tx_contact, txt_point, txt_name;
    String strRewardPoints = "", strUserName = "", strUserImage = "",strUserId="";
    ImageView img_ProfileNew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strRewardPoints = AppConstant.sharedpreferences.getString(AppConstant.UserRewardPoints, "");
        strUserName = AppConstant.sharedpreferences.getString(AppConstant.UserName, "");
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
        editor.putString(AppConstant.Statusback, "");
        editor.commit();
        Log.e("fdkflv", strRewardPoints);
        Log.e("fdkflv", strUserName);
        Log.e("fdkflv", strUserId);

        img_ProfileNew = view.findViewById(R.id.img_ProfileNew);
        txt_point = view.findViewById(R.id.txt_point);
        txt_name = view.findViewById(R.id.txt_name);

        txt_name.setText(strUserName);

        RelativeLayout rl_history = view.findViewById(R.id.rl_history);
        rl_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RedemptionHistoryActivity.class));
                Animatoo.animateZoom(getActivity());

            }
        });

        tx_about = view.findViewById(R.id.tx_about);

        tx_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                Animatoo.animateZoom(getActivity());
            }
        });
        tx_contact = view.findViewById(R.id.tx_contact);
        tx_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ContactUs.class));
                Animatoo.animateZoom(getActivity());
            }
        });
        tx_privacy = view.findViewById(R.id.tx_privacy);
        tx_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PrivacyPolicy.class));
                Animatoo.animateZoom(getActivity());
            }
        });
        tx_faq = view.findViewById(R.id.tx_faq);
        tx_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FaqActivity.class));
                Animatoo.animateZoom(getActivity());
            }
        });

        tx_terms = view.findViewById(R.id.tx_terms);
        tx_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TermAndCondActivity.class));
                Animatoo.animateZoom(getActivity());
            }
        });

        imgchat = view.findViewById(R.id.imgchat);
        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });
        RelativeLayout rl_edit = view.findViewById(R.id.rl_edit);
        rl_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
                Animatoo.animateZoom(getActivity());
            }
        });


        RelativeLayout rl_noti = view.findViewById(R.id.rl_noti);
        rl_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), NotificationActivity.class));
                Animatoo.animateZoom(getActivity());
            }
        });

        RelativeLayout rl_purchase = view.findViewById(R.id.rl_purchase);

        rl_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PurchaseHistoryActivity.class));
                Animatoo.animateZoom(getActivity());
                AppConstant.sharedpreferences =getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.StatusPurchase,"Profile");
                editor.commit();
            }
        });

        RelativeLayout rl_add = view.findViewById(R.id.rl_add);
        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.AddAddressType, "1");
                editor.commit();
                startActivity(new Intent(getActivity(), AddressListActivity.class));
                Animatoo.animateZoom(getActivity());
            }
        });

        RelativeLayout rl_setting = view.findViewById(R.id.rl_setting);
        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
                Animatoo.animateZoom(getActivity());
            }
        });

        RelativeLayout rl_logout = view.findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog;
                alertDialog = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
                alertDialog.setTitle("Logout ?");
                alertDialog.setMessage("Are you sure you want to logout ?");
                // Setting Icon to PopDialog
                alertDialog.setIcon(R.drawable.logout);
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                        editor.putString(AppConstant.UserMobile, "");
                        editor.putString(AppConstant.UserId, "");
                        editor.putString(AppConstant.UserName, "");
                        editor.putString(AppConstant.UserCountry, "");
                        editor.putString(AppConstant.UserPin, "");
                        editor.putString(AppConstant.UserState, "");
                        editor.putString(AppConstant.UserImage, "");
                        editor.commit();
                        Intent intent6 = new Intent(getActivity(), SplashActivity.class);
                        startActivity(intent6);
                        getActivity().finish();

                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = alertDialog.create();

                // Finally, display the alert dialog
                dialog.show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setTextColor(Color.parseColor("#000000"));
                negativeButton.setTextColor(Color.parseColor("#000000"));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
     /*   AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
     //  strRewardPoints = AppConstant.sharedpreferences.getString(AppConstant.RewardPoints, "");
        strUserName = AppConstant.sharedpreferences.getString(AppConstant.UserName, "");
        strUserImage = AppConstant.sharedpreferences.getString(AppConstant.UserImage, "");

      *//*  try {
            Picasso.with(getActivity()).load(strUserImage).placeholder(R.drawable.user_100).into(img_ProfileNew);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        update_profile();

    }


    public void update_profile(){



        //   AndroidNetworking.upload("https://3511535117.co/Tokayo/api/process.php?action=update_profile")
        AndroidNetworking.post(API.BASEURL+API.update_profile)
                .addBodyParameter("id",strUserId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("fjdhfdj",response.toString());
                        try {
                            if (response.getString("result").equals("successfully")){
                                String strId=response.getString( "id");
                                String username=response.getString( "username");
                                String email=response.getString( "email");
                                String contact=response.getString( "contact");
                                String country=response.getString( "country");
                                String postal_code=response.getString( "postal_code");
                                String state = response.getString("state");
                                String image = response.getString("image");
                                String reward_point = response.getString("reward_point");

                                Log.e("sfhcsdkj",reward_point);





                                try {
                                    Picasso.with(getActivity()).load(image).into(img_ProfileNew);
                                } catch (Exception ignored) {

                                }

                                txt_point.setText(reward_point);

                            }
                        } catch (JSONException e) {
                            Log.e("fdvgfdfbl", e.getMessage());

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("gfgdfbl", anError.getMessage());

                    }
                });




    }
}