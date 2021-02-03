package com.tokayoapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Activities.ChatActivity;
import com.tokayoapp.Activities.MainActivity;
import com.tokayoapp.Adapter.RewardsAdapter;
import com.tokayoapp.Modal.RewardModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RewardFragment extends Fragment {

    RecyclerView rec_rewards;
    RecyclerView.LayoutManager layoutManagers;
    RewardsAdapter rewardsAdapter;
    ArrayList<RewardModal> arrayListRewards= new ArrayList<>();
    ImageView imgBack,imgchat;
    ProgressBar spin_kit;
    String strUserId="",strFavStatus="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reward, container, false);
        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        Log.e("dkfvld",strUserId);


        imgBack = view.findViewById(R.id.imgBack);
        spin_kit = view.findViewById(R.id.spin_kit);

        imgchat=view.findViewById(R.id.imgchat);
        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });


        rec_rewards = view.findViewById(R.id.rec_rewards);
        layoutManagers = new GridLayoutManager(getActivity(),2,RecyclerView.VERTICAL, false);
        rec_rewards.setLayoutManager(layoutManagers);




       show_Rewards();
        return view;
    }

    public void show_Rewards(){
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
      //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_reward")
        AndroidNetworking.post(API.BASEURL+API.show_reward)
                .addBodyParameter("user_id",strUserId)
                 .setTag("Show Rewards")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("dsfkd",response.toString());

                        arrayListRewards=new ArrayList<>();

                            try {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String id =jsonObject.getString("id");
                                    String brand_id =jsonObject.getString("brand_id");
                                    String reward_name =jsonObject.getString("reward_name");
                                    String point =jsonObject.getString("point");
                                    String weight =jsonObject.getString("weight");
                                    String stock =jsonObject.getString("stock");
                                    String url =jsonObject.getString("url");
                                    String description =jsonObject.getString("description");
                                    String image =jsonObject.getString("image");
                                    String path =jsonObject.getString("path");
                                    String fav_status =jsonObject.getString("fav_status");

                                    RewardModal rewardModal=new RewardModal();

                                    rewardModal.setRewardId(jsonObject.getString("id"));
                                    rewardModal.setImage(jsonObject.getString("image"));
                                    rewardModal.setPath(jsonObject.getString("path"));
                                    rewardModal.setFav_status(jsonObject.getString("fav_status"));
                                    rewardModal.setPoint(jsonObject.getString("point"));
                                    rewardModal.setName(jsonObject.getString("reward_name"));
                                    arrayListRewards.add(rewardModal);




                                }


                                rewardsAdapter = new RewardsAdapter(getActivity(),arrayListRewards);
                                rec_rewards.setAdapter(rewardsAdapter);
                                spin_kit.setVisibility(View.GONE);
                            }catch (JSONException e) {
                                Log.e("trhtgh",e.getMessage());
                                spin_kit.setVisibility(View.GONE);
                            }

                        }

                        @Override
                    public void onError(ANError anError) {
                      Log.e("fjdkjg",anError.getMessage());
                            spin_kit.setVisibility(View.GONE);
                    }
                });
    }
}