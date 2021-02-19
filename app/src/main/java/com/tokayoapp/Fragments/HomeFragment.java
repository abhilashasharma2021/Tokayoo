package com.tokayoapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tokayoapp.Activities.ChatActivity;
import com.tokayoapp.Activities.ShowNewArrival;
import com.tokayoapp.Activities.SubCategoryActivity;
import com.tokayoapp.Adapter.ChatAdapter;
import com.tokayoapp.Adapter.HomeArrivalAdapter;
import com.tokayoapp.Adapter.HomeCategoryAdapter;
import com.tokayoapp.Adapter.SliderAdapterExample;
import com.tokayoapp.Modal.ChatModal;
import com.tokayoapp.Modal.HomeCategoryModal;
import com.tokayoapp.Modal.HomeNewArrivalModal;
import com.tokayoapp.Modal.SliderModel;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {


    SliderAdapterExample sliderAdapter;
    List<SliderModel> listOfSlider = new ArrayList<>();
    SliderView sliderView;

    RecyclerView rec_cat, rec_newArrival;
    RecyclerView.LayoutManager layoutManager, layoutManager1;
    HomeCategoryAdapter categoryAdapter;
    HomeArrivalAdapter arrivalAdapter;
    ArrayList<HomeCategoryModal> categoryArrayList = new ArrayList<>();
    ArrayList<HomeNewArrivalModal> arrivalArrayList = new ArrayList<>();

    ImageView imgUser, imgchat, imgRemove;
    Uri whatsappURI;
    CardView cardRod;
    String strDummy;
    Intent intents;
    TextView txt_more;
    EditText edit_Search;
    String st_serach = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sliderView = view.findViewById(R.id.imageSlider);
        cardRod = view.findViewById(R.id.cardRod);
        imgRemove = view.findViewById(R.id.imgRemove);
        imgUser = view.findViewById(R.id.imgUser);
        rec_cat = view.findViewById(R.id.rec_cat);
        txt_more = view.findViewById(R.id.txt_more);
        edit_Search = view.findViewById(R.id.edit_Search);
        imgchat = view.findViewById(R.id.imgchat);
        rec_newArrival = view.findViewById(R.id.rec_newArrival);

        txt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ShowNewArrival.class));
                Animatoo.animateSlideUp(getActivity());
            }
        });
        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
                Animatoo.animateSlideUp(getActivity());
            }
        });

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();
        show_Category();
        show_Arrival();
        show_Slider();
        layoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        rec_cat.setLayoutManager(layoutManager);

      /*  LinearSnapHelper snapHelperServices  = new LinearSnapHelper();
        snapHelperServices.attachToRecyclerView(rec_newArrival);*/

        layoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rec_newArrival.setLayoutManager(layoutManager1);

        /*ye tumhare kaam ka nhi h 3 lines ko remove bhi mt krna or*/

       /*  intents = getActivity().getIntent();
        whatsappURI = (Uri) intents.getParcelableExtra(Intent.EXTRA_STREAM);
        Log.e("dfgdgdgdfg", "getData" + whatsappURI);*/

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                st_serach = edit_Search.getText().toString().trim();
                show_AllProduct();
            }
        });


   edit_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
       @Override
       public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

           if (actionId == EditorInfo.IME_ACTION_DONE) {

               AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
               editor.putString(AppConstant.SearchQuery, edit_Search.getText().toString());
               editor.commit();
               startActivity(new Intent(getActivity(),SubCategoryActivity.class));
               Log.e("sdfdsfsf",edit_Search.getText().toString());
               return true;
           }
           // Return true if you have consumed the action, else false.
           return false;
       }
   });

        return view;
    }

  /*  public void ShowSliderImages() {
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();

       *//* for (int i = 0; i < 3; i++) {
            SliderModel sliderModel = new SliderModel();
            sliderModel.setId("1");
            sliderModel.setName("Name");
            sliderModel.setIcon("https://trave1blogs.com/wp-content/uploads/2017/07/fly-fishing-equipment-should-fisherman_9a4327f4c8b2ffa5.jpg");
            listOfSlider.add(sliderModel);
        }

        sliderAdapter = new SliderAdapterExample(listOfSlider, getActivity());
        sliderView.setSliderAdapter(sliderAdapter);*//*
     *//*  Ion.with(getActivity())
                .load(Baseurl.Slides)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.e("fkgfkgfg", result.toString());
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            listOfSlider = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SliderModel sliderModel = new SliderModel();
                                sliderModel.setId(jsonObject.getString("id"));
                                sliderModel.setName(jsonObject.getString("name"));
                                sliderModel.setIcon(jsonObject.getString("image"));
                                sliderModel.setPath(jsonObject.getString("path"));
                                sliderModel.setType(jsonObject.getString("type"));
                                sliderModel.setLinks(jsonObject.getString("link"));
                                listOfSlider.add(sliderModel);
                            }

                            sliderAdapter = new SliderAdapterExample(listOfSlider,getActivity());
                            sliderView.setSliderAdapter(sliderAdapter);
                        } catch (JSONException ex) {
                            Log.e("rrterew", ex.getMessage());
                        }
                    }
                });*//*
    }*/


    public void show_Slider() {

        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_slider")
        AndroidNetworking.post(API.BASEURL + API.Show_slider)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("gfhgg", response.toString());
                        listOfSlider = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);


                                String id = jsonObject.getString("id");
                                String image = jsonObject.getString("image");
                                String path = jsonObject.getString("path");
                                String URL = jsonObject.getString("URL");

                                Log.e("dfgkldkg", path + image);


                                SliderModel sliderModel = new SliderModel();

                                sliderModel.setImage(jsonObject.getString("image"));
                                sliderModel.setPath(jsonObject.getString("path"));
                                sliderModel.setURL(jsonObject.getString("URL"));

                                listOfSlider.add(sliderModel);


                            }


                            sliderAdapter = new SliderAdapterExample(listOfSlider, getActivity());
                            sliderView.setSliderAdapter(sliderAdapter);


                        } catch (JSONException e) {
                            Log.e("rtgrgfjl", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tryghtfjl", anError.getMessage());
                    }
                });


    }


    public void show_Category() {


        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_brand")
        AndroidNetworking.post(API.BASEURL + API.show_brand)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("sdklsk", response.toString());
                        categoryArrayList = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String image = jsonObject.getString("image");
                                String path = jsonObject.getString("path");
                                Log.e("tyuiyi", path + image);
                                HomeCategoryModal categoryModal = new HomeCategoryModal();
                                categoryModal.setBrandId(jsonObject.getString("id"));
                                categoryModal.setPath(jsonObject.getString("path"));
                                categoryModal.setImage(jsonObject.getString("image"));
                                categoryModal.setName(jsonObject.getString("name"));
                                categoryArrayList.add(categoryModal);
                            }

                            categoryAdapter = new HomeCategoryAdapter(getActivity(), categoryArrayList);
                            rec_cat.setAdapter(categoryAdapter);
                           /* edit_Search.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                                    String text = String.valueOf(charSequence);
                                    categoryAdapter.filter(text);

                                }

                                @Override
                                public void afterTextChanged(Editable s) {


                                }
                            });*/
                        } catch (JSONException e) {
                            Log.e("rtgrgfjl", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tryghtfjl", anError.getMessage());
                    }
                });


    }

    public void show_Arrival() {


        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=arrival_products")
        AndroidNetworking.post(API.BASEURL + API.arrival_products)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("fdfgf", response.toString());
                        arrivalArrayList = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);


                                String id = jsonObject.getString("id");
                                String title = jsonObject.getString("name");
                                String price = jsonObject.getString("price");
                                String description = jsonObject.getString("description");
                                String image = jsonObject.getString("images");
                                String path = jsonObject.getString("path");


                                HomeNewArrivalModal arrivalModal = new HomeNewArrivalModal();
                                arrivalModal.setImage(jsonObject.getString("images"));
                                arrivalModal.setId(jsonObject.getString("id"));
                                arrivalModal.setName(jsonObject.getString("name"));
                                arrivalModal.setPrice(jsonObject.getString("price"));
                                arrivalModal.setBrand(jsonObject.getString("description"));
                                arrivalModal.setPath(jsonObject.getString("path"));
                                arrivalArrayList.add(arrivalModal);
                            }
                            arrivalAdapter = new HomeArrivalAdapter(getActivity(), arrivalArrayList);
                            rec_newArrival.setAdapter(arrivalAdapter);


                        } catch (JSONException e) {
                            Log.e("dgfdg", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("frftr", anError.getMessage());
                    }
                });


    }


    public void show_AllProduct() {
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=search_product")
        AndroidNetworking.post(API.BASEURL + API.search_product)
                .addBodyParameter("word", st_serach)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("fdfgf", response.toString());
                        arrivalArrayList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                HomeNewArrivalModal arrivalModal = new HomeNewArrivalModal();
                                String id = jsonObject.getString("id");
                                String title = jsonObject.getString("name");
                                String price = jsonObject.getString("price");
                                String description = jsonObject.getString("description");
                                String image = jsonObject.getString("images");
                                String path = jsonObject.getString("path");

                                arrivalModal.setImage(jsonObject.getString("images"));
                                arrivalModal.setId(jsonObject.getString("id"));
                                arrivalModal.setName(jsonObject.getString("name"));
                                arrivalModal.setPrice(jsonObject.getString("price"));
                                arrivalModal.setBrand(jsonObject.getString("description"));
                                arrivalModal.setPath(jsonObject.getString("path"));
                                arrivalArrayList.add(arrivalModal);
                            }

                            arrivalAdapter = new HomeArrivalAdapter(getActivity(), arrivalArrayList);
                            rec_newArrival.setAdapter(arrivalAdapter);
                            edit_Search.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                                    String text = String.valueOf(charSequence);
                                    arrivalAdapter.filter(text);
                                    imgRemove.setVisibility(View.VISIBLE);
                                    imgRemove.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            edit_Search.setText("");
                                            imgRemove.setVisibility(View.GONE);
                                            show_Arrival();
                                        }
                                    });

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });


                        } catch (JSONException e) {
                            Log.e("rgdftrg", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("frftr", anError.getMessage());
                    }
                });
    }


}