package com.tokayoapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.tokayoapp.Adapter.BrandListAdapter;
import com.tokayoapp.Adapter.ProductListAdapter;
import com.tokayoapp.Modal.BrandNameModal;
import com.tokayoapp.Modal.ProductListModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubCategoryActivity extends AppCompatActivity {


    RecyclerView rec_product_list, rec_brands_hori;
    RecyclerView.LayoutManager layoutManagers, layoutManagerBrand;
    ProductListAdapter productListAdapter;
    BrandListAdapter brandListAdapter;
    ArrayList<ProductListModal> arrayProductList = new ArrayList<>();
    ArrayList<BrandNameModal> arrayBrandList = new ArrayList<>();
    ProgressBar spin_kit;
    TextView txt_catName;
    EditText edit_Search;
    String strBrandId = "", strCatName = "",strUserId="",strFavStatus="",strStrSearch;
    ImageView imgDown;
    ImageView imgUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);


        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strBrandId = AppConstant.sharedpreferences.getString(AppConstant.BrandId, "");
        strCatName = AppConstant.sharedpreferences.getString(AppConstant.BrandName, "");
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strStrSearch = AppConstant.sharedpreferences.getString(AppConstant.SearchQuery, "");

        Log.e("dsfl", strBrandId);
        Log.e("dsfl", strCatName);
        Log.e("cdvcx", strUserId);

        imgDown = findViewById(R.id.imgDown);
        imgUP = findViewById(R.id.imgUP);
        spin_kit = findViewById(R.id.spin_kit);
        txt_catName = findViewById(R.id.txt_catName);
        edit_Search = findViewById(R.id.edit_Search);
        txt_catName.setText(strCatName);
        rec_product_list = findViewById(R.id.rec_product_list);
        rec_brands_hori = findViewById(R.id.rec_brands_hori);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        layoutManagers = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        rec_product_list.setLayoutManager(layoutManagers);
        layoutManagerBrand = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rec_brands_hori.setLayoutManager(layoutManagerBrand);


        LinearSnapHelper snapHelperServices = new LinearSnapHelper();
        snapHelperServices.attachToRecyclerView(rec_brands_hori);

            show_subCategory(strStrSearch);
            show_Brands();



        edit_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {




            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = String.valueOf(s);
                if(text.length()>3){

                    show_subCategory(text);
                }

            }
        });


        rec_product_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && imgDown.getVisibility() == View.VISIBLE) {
                    imgDown.setVisibility(View.GONE);
                    imgUP.setVisibility(View.VISIBLE);
                } else if (dy < 0 && imgDown.getVisibility() != View.VISIBLE) {
                    imgDown.setVisibility(View.VISIBLE);
                    imgUP.setVisibility(View.GONE);
                }
            }
        });

        imgUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layoutManagers.smoothScrollToPosition(rec_product_list, null, 0);

            }
        });

        imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rec_product_list.smoothScrollToPosition(productListAdapter.getItemCount() - 1);
            }
        });


    }

    public void show_subCategory(String text) {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite cubeGrid = new CubeGrid();
        spin_kit.setIndeterminateDrawable(cubeGrid);

        Log.e("tgghff", strBrandId);
        Log.e("tgghff", strUserId);

        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_bramdproduct")
        AndroidNetworking.post(API.BASEURL + API.show_bramdproduct)
                .addBodyParameter("brand_id", strBrandId)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("word", text)
                .setTag("show All Brand Product")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("fgdgf", response.toString());
                        arrayProductList = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String brand_id = jsonObject.getString("brand_id");
                                String subbrand_id = jsonObject.getString("subbrand_id");
                                String description = jsonObject.getString("description");
                                String brnad = jsonObject.getString("brnad");
                                String price = jsonObject.getString("price");
                                String images = jsonObject.getString("images");
                                String weight = jsonObject.getString("weight");
                                String path = jsonObject.getString("path");
                                String stock = jsonObject.getString("stock");
                                String fav_status = jsonObject.getString("fav_status");
                                ProductListModal productListModal = new ProductListModal();

                                productListModal.setId(jsonObject.getString("id"));
                                productListModal.setBrandId(jsonObject.getString("brand_id"));
                                productListModal.setName(jsonObject.getString("name"));
                                productListModal.setBrandPrice(jsonObject.getString("price"));
                                productListModal.setImage(jsonObject.getString("images"));
                                productListModal.setDis(jsonObject.getString("description"));
                                productListModal.setPath(jsonObject.getString("path"));
                                productListModal.setFav_status(jsonObject.getString("fav_status"));
                                arrayProductList.add(productListModal);
                            }

                            productListAdapter = new ProductListAdapter(SubCategoryActivity.this, arrayProductList);
                            rec_product_list.setAdapter(productListAdapter);
                            spin_kit.setVisibility(View.GONE);




                        } catch (JSONException e) {
                            Log.e("fghfh", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("uyiuy", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }


    public void show_Brands() {

        spin_kit.setVisibility(View.VISIBLE);
        Sprite cubeGrid = new CubeGrid();
        spin_kit.setIndeterminateDrawable(cubeGrid);

        Log.e("dxklv", strBrandId);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_sub_brand")
        AndroidNetworking.post(API.BASEURL + API.show_sub_brand)
                .addBodyParameter("brand_id", strBrandId)
                .setTag("show Brand")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("fgdgf", response.toString());
                        arrayProductList = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String brand_id = jsonObject.getString("brand_id");
                                String name = jsonObject.getString("name");
                                String image = jsonObject.getString("image");
                                String path = jsonObject.getString("path");
                                Log.e("clxkv", id);

                                BrandNameModal brandNameModal = new BrandNameModal();
                                brandNameModal.setId(jsonObject.getString("id"));
                                brandNameModal.setBrandId(jsonObject.getString("brand_id"));
                                brandNameModal.setName(jsonObject.getString("name"));
                                brandNameModal.setImage(jsonObject.getString("image"));
                                brandNameModal.setPath(jsonObject.getString("path"));

                                arrayBrandList.add(brandNameModal);


                            }


                            brandListAdapter = new BrandListAdapter(SubCategoryActivity.this, arrayBrandList);
                            rec_brands_hori.setAdapter(brandListAdapter);
                            spin_kit.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("fghfh", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("uyiuy", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }


   public void show_filter_brands(String subbrand_id) {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite cubeGrid = new CubeGrid();
        spin_kit.setIndeterminateDrawable(cubeGrid);

        Log.e("dsfjhdsj",subbrand_id);
    //   AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_filter_brands")
                AndroidNetworking.post(API.BASEURL+API.show_filter_brands)
                .addBodyParameter("subbrand_id", subbrand_id)
                .setTag("show All Brand Product")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("fgdgf", response.toString());
                        arrayProductList = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);


                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String brand_id = jsonObject.getString("brand_id");
                                String subbrand_id = jsonObject.getString("subbrand_id");
                                String description = jsonObject.getString("description");
                                String brnad = jsonObject.getString("brnad");
                                String price = jsonObject.getString("price");
                                String image = jsonObject.getString("images");
                                String arrival_status = jsonObject.getString("arrival_status");
                                String path = jsonObject.getString("path");

                                Log.e("fhcdkjh",id);
                                Log.e("fhcdkjh",name);
                                Log.e("fhcdkjh",price);


                               ProductListModal productListModal = new ProductListModal();

                                productListModal.setId(jsonObject.getString("id"));
                                productListModal.setBrandId(jsonObject.getString("brand_id"));
                                productListModal.setName(jsonObject.getString("name"));
                                productListModal.setBrandPrice(jsonObject.getString("price"));
                                productListModal.setImage(jsonObject.getString("images"));
                                productListModal.setDis(jsonObject.getString("description"));
                                productListModal.setPath(jsonObject.getString("path"));

                                arrayProductList.add(productListModal);


                            }


                            productListAdapter = new ProductListAdapter(SubCategoryActivity.this, arrayProductList);
                            rec_product_list.setAdapter(productListAdapter);
                            spin_kit.setVisibility(View.GONE);
                            if (response.length()==0){
                                Toast.makeText(SubCategoryActivity.this, "There is no product available", Toast.LENGTH_SHORT).show();

                            }

                        }
                       catch (JSONException e) {
                            Log.e("fghfh", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("uyiuy", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });
                }



}
