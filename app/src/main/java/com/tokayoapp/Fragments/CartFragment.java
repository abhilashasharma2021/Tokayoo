package com.tokayoapp.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.tokayoapp.Activities.AddressListActivity;
import com.tokayoapp.Activities.ChatActivity;
import com.tokayoapp.Activities.MainActivity;
import com.tokayoapp.Activities.PaymentMode;
import com.tokayoapp.Activities.ShowCountry;
import com.tokayoapp.Adapter.CartAdapter;
import com.tokayoapp.Adapter.ChangeAddPopUpAdapter;
import com.tokayoapp.Modal.AddressListModal;
import com.tokayoapp.Modal.CartModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CartFragment extends Fragment {

    public static TextView txt_subTotal, txt_weight, txt_delivery, txt_Totalprice;
    ImageView img_back, imgchat;
    RecyclerView rec_cart;
    RecyclerView.LayoutManager layoutManager;
    CartAdapter cartAdapter;
    Button btn_checkout;
    ArrayList<CartModal> cartModalArrayList = new ArrayList<>();
    ProgressBar spin_kit;
    RelativeLayout rl_empty;
    TextView txt_Name, txt_contact, txtAddress, txt_country;
    String strUserId = "", stdefaultIdAddress = "", strUserName = "", strProdutId = "", strItems = "", strUserAddress = "", strAddressId = "";
    LinearLayout ll_all;
    TextView btn_add, btnChangeAddress;
    public static CheckBox check_all;
    RecyclerView rec_changedadd;
    RecyclerView.LayoutManager layoutManager1;
    ChangeAddPopUpAdapter addressListAdapter;
    ArrayList<AddressListModal> chnageAddArrayList = new ArrayList<>();
    public static Button btn_delAll;
    public static Dialog dialog;
    String strCompanyID = "";
    String strCompanyName = "", strSelectedCountryCode = "";
    String strCountry = "";
    TextView txtShowComPrice;
    TextView txtCountry, txtMalaysia;
    public static ArrayList<String> arrayListItems;
    public static ArrayList<String> arrayListItemsQuantity;
    public static String strItemsStatus = "", strSelectedItems = "", LastAddressId = "";
    String size_id = "";
    public static ArrayList<String> Arr_cartId = new ArrayList<>();
    public static ArrayList<String> Arr_ProQty = new ArrayList<>();
    public static int CountAll = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        // strAddressId = AppConstant.sharedpreferences.getString(AppConstant.AddressId, "");
        strProdutId = AppConstant.sharedpreferences.getString(AppConstant.ProdutId, "");
        stdefaultIdAddress = AppConstant.sharedpreferences.getString(AppConstant.defaultIdAddress, "");
        strCompanyID = AppConstant.sharedpreferences.getString(AppConstant.CompanyID, "");
        strCompanyName = AppConstant.sharedpreferences.getString(AppConstant.CompanyName, "");
        strSelectedCountryCode = AppConstant.sharedpreferences.getString(AppConstant.SelectedCountryCode, "");
        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
        editor.putString(AppConstant.customStatus, "");
        editor.putString(AppConstant.CompanyID, "");
        editor.commit();

        Log.e("asafafasfasf", stdefaultIdAddress);


        arrayListItems = new ArrayList<>();
        Arr_ProQty = new ArrayList<>();
        Arr_cartId = new ArrayList<>();

        txtShowComPrice = view.findViewById(R.id.txtShowComPrice);
        txtMalaysia = view.findViewById(R.id.txtMalaysia);
        txtCountry = view.findViewById(R.id.txtCountry);
        img_back = view.findViewById(R.id.img_back);
        btn_delAll = view.findViewById(R.id.btn_delAll);
        ll_all = view.findViewById(R.id.ll_all);
        check_all = view.findViewById(R.id.check_all);
        btn_add = view.findViewById(R.id.btn_add);
        txtAddress = view.findViewById(R.id.txtAddress);
        txt_contact = view.findViewById(R.id.txt_contact);
        txt_Name = view.findViewById(R.id.txt_Name);
        btnChangeAddress = view.findViewById(R.id.btnChangeAddress);
        rec_cart = view.findViewById(R.id.rec_cart);
        spin_kit = view.findViewById(R.id.spin_kit);
        rl_empty = view.findViewById(R.id.rl_empty);
        txt_subTotal = view.findViewById(R.id.txt_subTotal);
        txt_weight = view.findViewById(R.id.txt_weight);
        btn_checkout = view.findViewById(R.id.btn_checkout);
        txt_delivery = view.findViewById(R.id.txt_delivery);
        txt_country = view.findViewById(R.id.txt_country);
        txt_Totalprice = view.findViewById(R.id.txt_Totalprice);
        // txt_country.setText(strSelectedCountryCode);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.AddAddressType, "0");
                editor.putString(AppConstant.SeletAddressStatus, "Add");
                editor.commit();
                Intent myIntent = new Intent(getActivity(), AddressListActivity.class);
                getActivity().startActivity(myIntent);
            }
        });


        TextView btnCompany = view.findViewById(R.id.btnCompany);
        // btnCompany.setText(strCompanyName);
        btnCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ShowCountry.class));
            }
        });
        imgchat = view.findViewById(R.id.imgchat);
        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });


        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < Arr_cartId.size(); i++) {
                    if (strItemsStatus.equals("")) {
                        strItemsStatus = Arr_cartId.get(i);
                    } else {
                        strItemsStatus = strItemsStatus + "," + Arr_cartId.get(i);
                    }
                }

                Log.e("xcsffbfbbfbf", strItemsStatus);


                if (strItemsStatus.equals("")) {

                    Toast.makeText(getActivity(), "Select order items", Toast.LENGTH_SHORT).show();
                } else {

                    Arr_cartId = new ArrayList<>();
                    AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                    editor.putString(AppConstant.CheckoutStatus, "OrderProduct");
                    editor.putString(AppConstant.OrderItems, strItemsStatus);
                    editor.putString(AppConstant.defaultIdAddress, stdefaultIdAddress);
                    editor.putString(AppConstant.FinalSizeId, size_id);
                    editor.commit();
                    Log.e("CartFragment", "dsnhjdfgsyhdfgsuit: " + stdefaultIdAddress);


                    if (txtAddress.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please select address", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(getActivity(), PaymentMode.class));
                        Animatoo.animateZoom(getActivity());
                    }


                }


            }

        });


        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.activity_change_address_popup);

                rec_changedadd = dialog.findViewById(R.id.rec_changedadd);
                layoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                rec_changedadd.setLayoutManager(layoutManager1);
                change_Address();
                RelativeLayout rl_change = (RelativeLayout) dialog.findViewById(R.id.rl_change);
                rl_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });


        /*On back of history to cart */
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    return true;
                }

                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayListItems = new ArrayList<>();
        arrayListItemsQuantity = new ArrayList<>();
        Arr_cartId = new ArrayList<>();
        strItemsStatus = "";
        strSelectedItems = "";
        show_AddAddress();
        show_Cart();
        ShowIds();

    }


    public void ShowIds() {/*Product Id*/
        Log.e("dxvsssdfsdf", "CALL");

        strSelectedItems = "";
        for (int i = 0; i < Arr_cartId.size(); i++) {
            if (strSelectedItems.equals("")) {
                strSelectedItems = Arr_cartId.get(i);
            } else {
                strSelectedItems = strSelectedItems + "," + Arr_cartId.get(i);
            }
        }
        Log.e("dfssgsgfgfggffg", strSelectedItems + "");

        Log.e("CartFragment", "strUserId: " + strUserId);
        Log.e("CartFragment", "cart_id: " + strSelectedItems);
        AndroidNetworking.post(API.BASEURL + API.showCartTotalPrice)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("cart_id", strSelectedItems)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("afgasasasf", response.toString());
                        try {
                            if (response.has("sub_total")) {

                                String sub_total = response.getString("sub_total");
                                String delivery_charge = response.getString("delivery_charge");
                                String grand_total = response.getString("grand_total");
                                String net_weight = response.getString("net_weight");


                                Log.e("CartFragment", "grand_total1: " + grand_total);
                                txt_subTotal.setText(sub_total);
                                txt_Totalprice.setText(grand_total);
                                txt_delivery.setText(delivery_charge);
                                txt_weight.setText(net_weight+"Kg");

                                AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, 0);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.CartGrandTotal, grand_total);
                                editor.putString(AppConstant.NetDeliveryCharge, delivery_charge);
                                editor.commit();


                            }
                        } catch (JSONException e) {
                            Log.e("tyhth", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ukuihj", anError.getMessage());
                    }
                });


    }


    public void show_Cart() {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite chasingDots = new ChasingDots();
        spin_kit.setIndeterminateDrawable(chasingDots);
        Log.e("fidseufk", strUserId);

        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_cart")
        AndroidNetworking.post(API.BASEURL + API.show_cart)
                .addBodyParameter("user_id", strUserId)
                // .addBodyParameter("company_id", strCompanyID)
                .setTag("Show Cart")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ewflkjsdl", response.toString());
                        cartModalArrayList = new ArrayList<>();
                        arrayListItems = new ArrayList<>();
                        Arr_cartId = new ArrayList<>();
                        try {

                            String status_all = response.getString("status_all");
                            String sub_total = response.getString("sub_total");
                            String delivery_charge = response.getString("delivery_charge");
                            String grand_total = response.getString("grand_total");
                            String net_weight = response.getString("net_weight");
                            String product = response.getString("product");
                            Log.e("dfkjdj", status_all);
                            Log.e("dfkjdj", delivery_charge);
                            Log.e("dfkjdj", sub_total);

                            txtShowComPrice.setText(response.getString("company_delivery_charge"));
                            Log.e("CartFragment", "show: " + grand_total);
                            Log.e("dfkjdj", sub_total);
                            JSONArray jsonArray = new JSONArray(product);

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                String id_new = jsonObject1.getString("id");
                                size_id = jsonObject1.getString("size_id");

                                Log.e("CartFragment", "size_id: " + size_id);
                                Log.e("sgvdsgb", id_new);

                               /* AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.CartGrandTotal, grand_total);
                                editor.putString(AppConstant.FinalSizeId, jsonObject1.getString("size_id"));

                                editor.commit();*/


                                CartModal myCartModal = new CartModal();
                                myCartModal.setId(jsonObject1.getString("id"));
                                myCartModal.setProductId(jsonObject1.getString("product_id"));
                                myCartModal.setProductPrice(jsonObject1.getString("price"));
                                myCartModal.setColor_id(jsonObject1.getString("color_id"));
                                myCartModal.setModel_id(jsonObject1.getString("model_id"));
                                myCartModal.setStatus_single(jsonObject1.getString("status_single"));
                                myCartModal.setStatus_all(jsonObject1.getString("status_all"));
                                myCartModal.setGrandTotal(grand_total);
                                myCartModal.setName(jsonObject1.getString("name"));
                                myCartModal.setImage(jsonObject1.getString("images"));
                                myCartModal.setPath(jsonObject1.getString("path"));
                                myCartModal.setStock(jsonObject1.getString("stock"));
                                myCartModal.setColor(jsonObject1.getString("colorss"));
                                myCartModal.setBrand(jsonObject1.getString("modelss"));
                                myCartModal.setQuantity(jsonObject1.getString("quantity"));
                                cartModalArrayList.add(myCartModal);

                               /* if (jsonObject1.getString("status_all").equals("1")){
                                    check_all.setChecked(true);
                                }else {
                                    check_all.setChecked(false);
                                }*/
                            }


                            if (jsonArray.length() == 0) {
                                rl_empty.setVisibility(View.VISIBLE);
                                ll_all.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "There is no product in  cart!!!!", Toast.LENGTH_SHORT).show();
                                spin_kit.setVisibility(View.GONE);
                            } else {

                                layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                rec_cart.setLayoutManager(layoutManager);
                                rec_cart.setHasFixedSize(true);
                                cartAdapter = new CartAdapter(getActivity(), cartModalArrayList, CartFragment.this);
                                rec_cart.setAdapter(cartAdapter);
                                spin_kit.setVisibility(View.GONE);

                            }


                            check_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    if (isChecked) {
                                        CountAll = 1;
                                        show_Cart();
                                        Arr_cartId = new ArrayList<>();
                                    } else {
                                        CountAll = 0;
                                        show_Cart();
                                        Arr_cartId = new ArrayList<>();
                                    }
                                }
                            });



                   /*     check_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                             *//*   if (b) {

                                    update_status(strUserId,"1");

                                } else {
                                    update_status(strUserId,"0");
                                }
*//*



                                if (check_all.isChecked()) {

                                        arrayListItems=new ArrayList<>();
                                        arrayListItems.add("All");
                                        *//*cartAdapter.selectAll();
                                        cartAdapter.notifyDataSetChanged();*//*

                             *//*status:1 = single product deleted, 0= delete all*//*


                                        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                        editor.putString(AppConstant.CheckStatus, "0");
                                        editor.putString(AppConstant.AllStatus, "1");
                                        editor.commit();
                                        show_Cart();
                                    } else {
                                        arrayListItems.remove("All");
                                       *//* cartAdapter.unselectall();
                                        cartAdapter.notifyDataSetChanged();*//*
                                        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                        editor.putString(AppConstant.AllStatus, "0");
                                        editor.commit();
                                    }

                            }
                        });
*/


                        } catch (JSONException e) {
                            Log.e("dsfjkj", e.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ewtff", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }


    public void change_Address() {

        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_address")
        AndroidNetworking.post(API.BASEURL + API.show_address)
                .addBodyParameter("user_id", strUserId)
                .setTag("Show Address")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("ytuijy", response.toString());

                        chnageAddArrayList = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String user_id = jsonObject.getString("user_id");
                                String name = jsonObject.getString("name");
                                String contact = jsonObject.getString("contact");
                                String address = jsonObject.getString("address");

                                AddressListModal addressListModal = new AddressListModal();
                                addressListModal.setId(jsonObject.getString("id"));
                                addressListModal.setAddress(jsonObject.getString("address"));
                                addressListModal.setName(jsonObject.getString("name"));
                                addressListModal.setContact(jsonObject.getString("contact"));
                                chnageAddArrayList.add(addressListModal);

                            }

                            addressListAdapter = new ChangeAddPopUpAdapter(getActivity(), chnageAddArrayList, CartFragment.this);
                            rec_changedadd.setAdapter(addressListAdapter);
                        } catch (JSONException e) {
                            Log.e("tyhuhj", e.getMessage());

                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("fdgfhh", anError.getMessage());

                    }
                });


    }

    public void show_AddAddress() {

        Log.e("fsgsgssgs", "show_AddAddress: ");
        Log.e("fsgsgssgs", stdefaultIdAddress);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_address")
        AndroidNetworking.post(API.BASEURL + API.show_address)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("id", stdefaultIdAddress)
                .setTag("Show Address")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("rgdfrg", response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                /*if (i==0){}*/
                                String id = jsonObject.getString("id");
                                String user_id = jsonObject.getString("user_id");
                                String name = jsonObject.getString("name");
                                String contact = jsonObject.getString("contact");
                                String address = jsonObject.getString("address");
                                strCountry = jsonObject.getString("country_name");

                                Log.e("ghfuhb", id);
                                Log.e("ghfuhb", name);
                                Log.e("ghfuhb", contact);
                                Log.e("ghfuhb", address);
                                stdefaultIdAddress=id;

                                AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.defaultIdAddress, stdefaultIdAddress);
                                editor.commit();
                                //   txtCountry.setText(strCountry);
                                txt_Name.setText(name);
                                String[] strings = contact.split("-");
                                txt_country.setText(strings[0]);
                                txt_contact.setText(strings[1]);
                                txtAddress.setText(address);
                                //  txtMalaysia.setText("Malaysia");
                            }


                        } catch (JSONException e) {
                            Log.e("efrdegf", e.getMessage());

                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("etfgr", anError.getMessage());

                    }
                });


    }


    public void show_changeAddress(String id, String name, String contact, String address) {

        Log.e("fsgsgssgs", "show_changeAddress: ");
        Log.e("fsgsgssgs", id);
        //AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=show_address")
        AndroidNetworking.post(API.BASEURL + API.show_address)
                .addBodyParameter("id", id)
                .addBodyParameter("user_id", strUserId)
                .setTag("Set Address")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("hgmjhm", response.toString());


                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String user_id = jsonObject.getString("user_id");
                                String name = jsonObject.getString("name");
                                String contact = jsonObject.getString("contact");
                                String address = jsonObject.getString("address");


                                stdefaultIdAddress = id;
                                Log.e("dfdfdfdfdf", stdefaultIdAddress);

                                AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.defaultIdAddress, stdefaultIdAddress);
                                editor.commit();

                                txt_Name.setText(name);
                                String[] strings = contact.split("-");
                                txt_country.setText(strings[0]);
                                txt_contact.setText(strings[1]);
                                txtAddress.setText(address);

                            }


                        } catch (JSONException e) {
                            Log.e("ghjhk", e.getMessage());

                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfgfbgh", anError.getMessage());

                    }
                });
    }


}