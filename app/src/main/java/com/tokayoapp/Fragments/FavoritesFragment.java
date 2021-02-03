package com.tokayoapp.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Activities.ChatActivity;
import com.tokayoapp.Activities.MainActivity;
import com.tokayoapp.Adapter.FavouriteAdapter;
import com.tokayoapp.Modal.FavouriteModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FavoritesFragment extends Fragment {

    RecyclerView rec_favourite;
    RecyclerView.LayoutManager layoutManager;
    FavouriteAdapter favouriteAdapter;
    ArrayList<FavouriteModal> arrayListFavourite = new ArrayList<>();
    ImageView imgBack,imgchat;
    RelativeLayout rl_wishEmpty;
    String strUserId="";
    ProgressBar spin_kit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        AppConstant.sharedpreferences =getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId =AppConstant.sharedpreferences.getString(AppConstant.UserId, "");

        Log.e("rtgrfg", strUserId);

        rec_favourite = view.findViewById(R.id.rec_favourite);
        rl_wishEmpty = view.findViewById(R.id.rl_wishEmpty);
        layoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        rec_favourite.setLayoutManager(layoutManager);


        imgchat=view.findViewById(R.id.imgchat);
        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });
        spin_kit = view.findViewById(R.id.spin_kit);
        imgBack = view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });




        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        show_Favourite();
    }

    public void show_Favourite() {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
            //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_fav_products")
            AndroidNetworking.post(API.BASEURL+API.show_fav_products)
                    .addBodyParameter("user_id",strUserId)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.e("eftretg",response.toString());
                            arrayListFavourite = new ArrayList<>();
                            try {

                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject jsonObject = response.getJSONObject(i);

                                    String id=jsonObject.getString("id");
                                    String user_id=jsonObject.getString("user_id");
                                    String product_id=jsonObject.getString("product_id");
                                    String status=jsonObject.getString("status");/*status 0=no fav, 1=fav product*/
                                    String type=jsonObject.getString("type");/*type 0=product, 1=reward*/
                                    String name=jsonObject.getString("name");
                                    String description=jsonObject.getString("description");
                                    String path=jsonObject.getString("path");
                                    String price=jsonObject.getString("price");
                                    String image=jsonObject.getString("product_images");
                                    Log.e("gfhng",path+image);
                                    Log.e("tfhg",type);


                                    FavouriteModal favouriteModal = new FavouriteModal();
                                    favouriteModal.setProductId(jsonObject.getString("product_id"));
                                    favouriteModal.setName(jsonObject.getString("name"));
                                    favouriteModal.setPrice(jsonObject.getString("price"));
                                    favouriteModal.setType(jsonObject.getString("type"));
                                    favouriteModal.setStatus(jsonObject.getString("status"));
                                    favouriteModal.setDiscription(jsonObject.getString("description"));
                                    favouriteModal.setImage(jsonObject.getString("product_images"));
                                    arrayListFavourite.add(favouriteModal);


                                }


                                if (response.length() == 0){
                                    rl_wishEmpty.setVisibility(View.VISIBLE);
                                    layoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
                                    rec_favourite.setLayoutManager(layoutManager);
                                    favouriteAdapter = new FavouriteAdapter(getActivity(), arrayListFavourite);
                                    rec_favourite.setAdapter(favouriteAdapter);
                                    spin_kit.setVisibility(View.GONE);
                                }
                                else {
                                    layoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
                                    rec_favourite.setLayoutManager(layoutManager);
                                    favouriteAdapter = new FavouriteAdapter(getActivity(), arrayListFavourite);
                                    rec_favourite.setAdapter(favouriteAdapter);

                                    spin_kit.setVisibility(View.GONE);

                                }



                            } catch (Exception ex) {
                                spin_kit.setVisibility(View.GONE);
                                Log.e("dfgdfg", ex.getMessage());
                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("uujkj", anError.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }
                    });


    }



   /* public void show_Favourite(){

        FavouriteModal favouriteModal=new FavouriteModal("Machine",R.drawable.rod,"AM XXXX")   ;

        for (int i=0;i<3;i++){
            arrayListFavourite.add(favouriteModal);

        }
    }*/

}
