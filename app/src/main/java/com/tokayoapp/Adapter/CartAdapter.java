package com.tokayoapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Fragments.CartFragment;
import com.tokayoapp.Modal.CartModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.tokayoapp.Fragments.CartFragment.LastAddressId;
import static com.tokayoapp.Fragments.CartFragment.btn_delAll;
import static com.tokayoapp.Fragments.CartFragment.check_all;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<CartModal> cartAdapterList;
    String strProdQ;
    String strUserId = "", strCheckStatus = "",   Quantity = "";
    String strUpdateQty = "",strAllStatus="";
    boolean isSelectedAll = false;
    int count = 0;
    String stProductId="";
    CartFragment cartFragment;
    String new_str="" , Newqty="",Status="",strPro_Ids="",strQtys="";
    ArrayList<String>arrayList=new ArrayList<>();
    String StrFinalStatus = "";
    StringBuilder stringBuilder1,stringBuilder2;

    public CartAdapter(Context context, List<CartModal> getDataAdpter,CartFragment fragment) {

        this.context = context;
        this.cartFragment = fragment;
        this.cartAdapterList = getDataAdpter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_cart_layout, parent, false);
        CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        strUpdateQty = AppConstant.sharedpreferences.getString(AppConstant.UpdateQty, "");
        strAllStatus = AppConstant.sharedpreferences.getString(AppConstant.AllStatus, "");

        Log.e("rgfdfg", strUserId);
        Log.e("rgfdfg", strUpdateQty);
        Log.e("sdgvdsv", strAllStatus);


        final CartModal cartModal = cartAdapterList.get(position);


        holder.txt_name.setText(cartModal.getName());
        holder.txt_color.setText(cartModal.getColor());
        holder.txt_model.setText(cartModal.getBrand());
        holder.txt_price.setText(cartModal.getProductPrice());
        holder.txt_itemCount.setText(cartModal.getQuantity());
        Log.e("dfsfsdfsfs", cartModal.getQuantity());
        // holder.img_product.setImageResource(cartModal.getImage());

        final String color_id = cartModal.getColor_id();
        Log.e("ujdhfu", cartModal.getColor_id() + "");
        final String model_id = cartModal.getModel_id();
        Log.e("ujdhfu", cartModal.getModel_id() + "");



        CheckboxCheck(cartModal,holder,CartFragment.CountAll);
     /*   if (cartModal.getStatus_all().equals("1")){
            holder.check_item.setChecked(true);
            CartFragment.check_all.setChecked(true);
            StrFinalStatus="all";

        }else {
            CartFragment.check_all.setChecked(false);
          if (cartModal.getStatus_single().equals("1")) {
              holder.check_item.setChecked(true);
              StrFinalStatus="single";
              Log.e("dsdfdds", "if");
          }else {
              holder.check_item.setChecked(false);
              Log.e("dsdfdds", "else");
          }
        }*/
        Log.e("sfsfsfsss", CartFragment.CountAll+"" );




        holder.check_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                           String qty=holder.txt_itemCount.getText().toString().trim();
                                                           cartModal.setIsselected(!cartModal.isIsselected());

                                                           if (cartModal.isIsselected()) {
                                                               Log.e("scadvsvcvsv", qty );
                                                               CartFragment.CountAll=0;
                                                               CheckboxCheck(cartModal,holder, CartFragment.CountAll);
                                                               holder.check_item.setChecked(true);
                                                               check_all.setChecked(false);

                                                               if(CartFragment.Arr_cartId.size()==0){
                                                                   Log.e("scadvsvcvsv", "ifff" );
                                                                   CartFragment.Arr_cartId.add(cartModal.getId());
                                                                   cartFragment.ShowIds();
                                                               }else{
                                                                   if(!CartFragment.Arr_cartId.contains(cartModal.getId())){
                                                                       CartFragment.Arr_cartId.add(cartModal.getId());
                                                                       cartFragment.ShowIds();
                                                                       Log.e("scadvsvcvsv", "else iff" );

                                                                   }else{
                                                                       Log.e("scadvsvcvsv", "else" );

                                                                       for (int i = 0; i < CartFragment.Arr_cartId.size(); i++) {
                                                                           if(CartFragment.Arr_cartId.get(i).equals(cartModal.getId())){
                                                                               String s=  CartFragment.Arr_cartId.get(i).replace(CartFragment.Arr_cartId.get(i),cartModal.getId());
                                                                               CartFragment.Arr_cartId.set(i,s);
                                                                           }
                                                                           cartFragment.ShowIds();

                                                                       }

                                                                   }

                                                               }


                                                               stringBuilder2 = new StringBuilder();
                                                               for (int i = 0; i <CartFragment.Arr_cartId.size(); i++) {
                                                                   CartFragment.Arr_cartId.get(i);
                                                                   stringBuilder2.append(CartFragment.Arr_cartId.get(i));
                                                                   stringBuilder2.append(",");
                                                               }

                                                               holder.check_item.setChecked(true);


                                                           } else {

                                                               holder.check_item.setChecked(false);
                                                               CartFragment.Arr_cartId.remove(cartModal.getId());
                                                               cartFragment.ShowIds();
                                                           }


                                                       }

                                                   }

        );



/*
        check_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cartFragment.GetCartId();


            }
        });
*/

      /*  if (holder.check_item.isChecked()) {

            if (CartFragment.arrayListItems.size() == 0) {
                CartFragment.arrayListItems.add(cartModal.getProductId());
            } else {
                if (!CartFragment.arrayListItems.equals(cartModal.getProductId())) {
                    CartFragment.arrayListItems.add(cartModal.getProductId());
                } else {
                    CartFragment.arrayListItems.remove(cartModal.getProductId());
                }

            }
            cartFragment.ShowIds();
        }else {
        }
*/

           Newqty=holder.txt_itemCount.getText().toString().trim();

         /*   if (!isSelectedAll) {
                Log.e("dddfgdgd","Not");
                count = 1;
                holder.check_item.setChecked(false);
            } else {
                Log.e("dddfgdgd","Yes");
                count = 0;
                holder.check_item.setChecked(true);
            }*/

    /*    CartFragment.check_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    if (arrayList.size() == 0) {
                        arrayList.add(cartModal.getId());
                    } else {
                        if (!arrayList.equals(cartModal.getId())) {
                            arrayList.add(cartModal.getId());
                        } else {
                            arrayList.remove(cartModal.getId());
                        }

                    }
                    update_status_all(strUserId,"1",holder);
                    holder.check_item.setChecked(true);
                    CartFragment.check_all.setChecked(true);
                  //  selectAll();

                } else {

                    if (arrayList.size() == 0) {
                        arrayList.add(cartModal.getId());
                    } else {
                        if (!arrayList.equals(cartModal.getId())) {
                            arrayList.add(cartModal.getId());
                        } else {
                            arrayList.remove(cartModal.getId());
                        }

                    }
                    update_status_all(strUserId,"0",holder);
                    holder.check_item.setChecked(false);
                    CartFragment.check_all.setChecked(false);

                }
            }
        });*/





      /*  holder.check_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("dfsdfsdfsdf", b+"");
                if (b) {
                   // Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                    update_status(cartModal.getId(),strUserId,"1");
                    if (CartFragment.arrayListItems.size() == 0) {
                        CartFragment.arrayListItems.add(cartModal.getProductId());
                    } else {
                        if (!CartFragment.arrayListItems.equals(cartModal.getProductId())) {
                            CartFragment.arrayListItems.add(cartModal.getProductId());
                        } else {
                            CartFragment.arrayListItems.remove(cartModal.getProductId());
                        }

                    }
                    cartFragment.ShowIds();

                }
                else {
                    Log.e("dfsdfsdfsdf", "else" );
                    update_status(cartModal.getId(),strUserId,"0");
                    CartFragment.arrayListItems.remove(cartModal.getProductId());
                    cartFragment.ShowIds();
                }

            }
        });*/

        /*holder.check_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                     Quantity=holder.txt_itemCount.getText().toString().trim();
                     CartFragment.arrayListItems.add(cartModal.getProductId());
                     cartFragment.ShowIds();
                    *//*if(count==1){
                        CartFragment.arrayListItems.add(cartModal.getProductId());
                        cartFragment.ShowIds();
                    }*//*

                }
                else {
                    CartFragment.arrayListItems.remove(cartModal.getProductId());
                    cartFragment.ShowIds();
                    *//*if(count==1){

                        CartFragment.arrayListItems.remove(cartModal.getProductId());
                        cartFragment.ShowIds();
                    }*//*

                }

            }
        });*/



        try {
            Picasso.with(context).load(cartModal.getPath() + cartModal.getImage()).into(holder.img_product);
        } catch (Exception ignored) {

        }


        final int stock_count = Integer.parseInt(cartModal.getStock());
        Log.e("retgryht", cartModal.getStock() + "");

        final String showCartId = cartModal.getId();
        Log.e("dfdvg", cartModal.getId() + "");

        final String productId = cartModal.getProductId();
        Log.e("gfrdegf", cartModal.getProductId() + "");

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*status:1 = single product deleted, 0= delete all*/
                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.CheckStatus, "1");
                editor.commit();

                delete_cart(productId, color_id, model_id);
            }
        });


        btn_delAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll_cart();
            }
        });


        holder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                /*strProdQ = holder.txt_itemCount.getText().toString();

                 new_str = String.valueOf(Integer.parseInt(strProdQ) + 1);

                holder.txt_itemCount.setText(new_str);

                int qty = Integer.parseInt(new_str);

                if (qty > stock_count) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Can't purchase more than stock Please select under the stock ");
                    alertDialogBuilder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                    Fragment fragment = new CartFragment();
                                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null).commit();


                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {

                }*/


                if (!holder.check_item.isChecked()) {
                    Toast.makeText(context, "you have to  select the checkbox", Toast.LENGTH_SHORT).show();
                } else {


                    strProdQ = holder.txt_itemCount.getText().toString();
                    String new_str = String.valueOf(Integer.parseInt(strProdQ) + 1);
                    holder.txt_itemCount.setText(new_str);
                    Log.e("ddfddfd", new_str);
                    Log.e("dfdddd", CartFragment.Arr_cartId.size() + "");
                    Log.e("cnsjchjc", cartAdapterList.get(position).isIsselected() + "");
                    if (cartAdapterList.get(position).isIsselected() == true) {
                        if (CartFragment.Arr_cartId.size() == 0) {
                            CartFragment.Arr_cartId.add(cartModal.getId());

                        } else {
                            if (!CartFragment.Arr_cartId.contains(cartModal.getId())) {
                                CartFragment.Arr_cartId.add(cartModal.getId());

                            } else {
                                for (int i = 0; i < CartFragment.Arr_cartId.size(); i++) {
                                    if (CartFragment.Arr_cartId.get(i).equals(cartModal.getId())) {
                                        String s = CartFragment.Arr_cartId.get(i).replace(CartFragment.Arr_cartId.get(i), cartModal.getId());
                                        CartFragment.Arr_cartId.set(i, s);
                                    }

                                }

                            }

                        }


                        stringBuilder2 = new StringBuilder();
                        for (int i = 0; i < CartFragment.Arr_cartId.size(); i++) {
                            CartFragment.Arr_cartId.get(i);
                            stringBuilder2.append(CartFragment.Arr_cartId.get(i));
                            stringBuilder2.append(",");
                        }
                        Log.e("dcdadaddd", stringBuilder2.toString().trim() + "");
                        update_quantity(productId, new_str, holder, color_id, model_id);

                    } else {
                        update_quantity(productId, new_str, holder, color_id, model_id);
                    }


                    //  notifyDataSetChanged();
                }
            }
        });

        holder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!holder.check_item.isChecked()) {
                    Toast.makeText(context, "you have to  select the checkbox", Toast.LENGTH_SHORT).show();
                }
                else {
                    strProdQ = holder.txt_itemCount.getText().toString();
                    new_str = String.valueOf(Integer.parseInt(strProdQ) - 1);
                    int xyz = Integer.parseInt(strProdQ) - 1;
                    if (xyz < 1) {

                        holder.txt_itemCount.setText("1");

                    } else {

                        holder.txt_itemCount.setText(new_str);
                        Newqty = holder.txt_itemCount.getText().toString().trim();
                        // update_quantity(productId, new_str, holder, color_id, model_id);
                        if (cartAdapterList.get(position).isIsselected() == true) {
                            if (CartFragment.Arr_cartId.size() == 0) {
                                CartFragment.Arr_cartId.add(cartModal.getId());
                                CartFragment.Arr_ProQty.add(new_str);
                            } else {
                                if (!CartFragment.Arr_cartId.contains(cartModal.getId())) {
                                    CartFragment.Arr_cartId.add(cartModal.getId());
                                    CartFragment.Arr_ProQty.add(new_str);

                                } else {
                                    for (int i = 0; i < CartFragment.Arr_cartId.size(); i++) {
                                        if (CartFragment.Arr_cartId.get(i).equals(cartModal.getId())) {
                                            String s = CartFragment.Arr_cartId.get(i).replace(CartFragment.Arr_cartId.get(i), cartModal.getId());
                                            CartFragment.Arr_cartId.set(i, s);
                                        }

                                    }

                                }

                            }


                            stringBuilder2 = new StringBuilder();
                            for (int i = 0; i < CartFragment.Arr_cartId.size(); i++) {
                                CartFragment.Arr_cartId.get(i);
                                stringBuilder2.append(CartFragment.Arr_cartId.get(i));
                                stringBuilder2.append(",");
                            }
                            Log.e("dcdadaddd", stringBuilder2.toString().trim() + "");
                            update_quantity(productId, new_str, holder, color_id, model_id);

                        } else {
                            update_quantity(productId, new_str, holder, color_id, model_id);
                        }


                    }
                }
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return cartAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product, img_minus, img_plus, img_delete;
        TextView txt_name, txt_color, txt_price, txt_itemCount, txt_model;
        public TextView txt_subTotal, txt_delivery, txt_Totalprice;
        CheckBox check_item;
        public Button btn_delAll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_color = itemView.findViewById(R.id.txt_color);
            txt_price = itemView.findViewById(R.id.txt_price);
            btn_delAll = itemView.findViewById(R.id.btn_delAll);
            txt_itemCount = itemView.findViewById(R.id.txt_itemCount);
            img_minus = itemView.findViewById(R.id.img_minus);
            check_item = itemView.findViewById(R.id.check_item);
            img_plus = itemView.findViewById(R.id.img_plus);
            img_delete = itemView.findViewById(R.id.img_delete);
            txt_subTotal = itemView.findViewById(R.id.txt_subTotal);
            txt_delivery = itemView.findViewById(R.id.txt_delivery);
            txt_model = itemView.findViewById(R.id.txt_model);
            txt_Totalprice = itemView.findViewById(R.id.txt_Totalprice);
        }
    }

    public void CheckboxCheck(CartModal model, ViewHolder holder, int countAll){

        if (countAll==1){
            Log.e("dfvsvsvxcvx", countAll+"" );
            CartFragment.Arr_cartId.add(model.getId());
            holder.check_item.setChecked(true);
            check_all.setChecked(true);
        }else  if (countAll==0){
            Log.e("dfvsvsvxcvx", countAll+"" );
            holder.check_item.setChecked(false);
            check_all.setChecked(false);
        }

    }

    public void selectAll() {
        isSelectedAll = true;
        notifyDataSetChanged();
    }

    public void unselectall() {
        isSelectedAll = false;
        notifyDataSetChanged();
    }


    public void update_quantity(String productId, String new_str, final ViewHolder holder, String color_id, String model_id) {
        Log.e("tgfdhg", productId);
        Log.e("tgfdhg", strUserId);
        Log.e("tgfdhg", new_str);
        Log.e("tgfdhg", stringBuilder2+"");

        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?actCarion=update_quantity")
        AndroidNetworking.post(API.BASEURL + API.update_quantity)
                .addBodyParameter("product_id", productId)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("quantity", new_str)
                .addBodyParameter("color_id", color_id)
                .addBodyParameter("model_id", model_id)
                .addBodyParameter("status",StrFinalStatus)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("etrfdeg", response.toString());
                        try {
                            if (response.getString("result").equals("sucessfull")) {
                                update_status(String.valueOf(stringBuilder2),strUserId);
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


    public void delete_cart(String productId, String color_id, String model_id) {
        AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strCheckStatus = AppConstant.sharedpreferences.getString(AppConstant.CheckStatus, "");

        Log.e("efdsf", strCheckStatus);
        Log.e("efdsf", strUserId);
        Log.e("efdsf", productId);
        Log.e("ghjj ", color_id);
        Log.e("ghjhg", model_id);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=delete_cart")
        AndroidNetworking.post(API.BASEURL + API.delete_cart)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("product_id", productId)
                .addBodyParameter("color_id", color_id)
                .addBodyParameter("model_id", model_id)
                .addBodyParameter("status", strCheckStatus)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("utyuyuu", response.toString());
                        try {
                            if (response.getString("result").equals("Product Deleted")) {


                                Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();

                                Fragment fragment = new CartFragment();
                                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null).commit();

                                //    ((FragmentActivity) context).finish();

                            }
                        } catch (JSONException e) {
                            Log.e("flkjdslkjf", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rdegtrh", anError.getMessage());
                    }
                });


    }

    public void deleteAll_cart() {
        AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strCheckStatus = AppConstant.sharedpreferences.getString(AppConstant.CheckStatus, "");


        Log.e("fiouvicv", strCheckStatus);
        Log.e("fiouvicv", strUserId);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=delete_cart")
        AndroidNetworking.post(API.BASEURL + API.delete_cart)
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("status", "0")
                //  .addBodyParameter("product_id", strProdutId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("thbgfb", response.toString());
                        try {
                            if (response.getString("result").equals("Cart Cleared")) {


                                Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();

                                Fragment fragment = new CartFragment();
                                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null).commit();
                                //CartAdapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            Log.e("erfe", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rdegtrh", anError.getMessage());
                    }
                });


    }





    public void update_status(String cart_id,String strUserId) {
        Log.e("dsrgdfh", cart_id);
        Log.e("dsrgdfh", strUserId);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?actCarion=updatestatuscart")
        AndroidNetworking.post(API.BASEURL + API.updatestatuscart)
                .addBodyParameter("cart_id",cart_id)
                .addBodyParameter("user_id",strUserId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("rfrrferer", response.toString());
                        try {
                            if (response.getString("msg").equals("susccess")) {
                                ((CartFragment)cartFragment).ShowIds();
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


  /*  public void update_status_all(String userId, final String strCheckStatus, final ViewHolder holder) {
        Log.e("sdghdfh", userId);
        Log.e("sdghdfh", strCheckStatus);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?actCarion=updatestatuscart")

        String strSingleIds = "";
        for (int i = 0; i < arrayList.size(); i++) {
            if (strSingleIds.equals("")) {
                strSingleIds = arrayList.get(i);
            } else {
                strSingleIds = strSingleIds + "," + arrayList.get(i);
            }
        }
        Log.e("fsdssddf", strSingleIds );
        final String finalStrSingleIds = strSingleIds;
        AndroidNetworking.post(API.BASEURL + API.updatestatuscart)
                .addBodyParameter("user_id",userId)
                .addBodyParameter("status_all",strCheckStatus)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("rfrrferer", response.toString());
                        try {
                            if (response.getString("msg").equals("susccess")) {
                                if (strCheckStatus.equals("0")){
                                    Update_all_single(finalStrSingleIds,strUserId,"0");
                                }else   if (strCheckStatus.equals("1")){
                                    Update_all_single(finalStrSingleIds,strUserId,"1");
                                }


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


    }*/


    /*public void Update_all_single(String cart_id,String strUserId, String strCheckStatus) {
        Log.e("dsrgdfh", cart_id);
        Log.e("dsrgdfh", strCheckStatus);
        Log.e("dsrgdfh", strUserId);
        // AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?actCarion=updatestatuscart")
        AndroidNetworking.post(API.BASEURL + API.updatestatuscart)
                .addBodyParameter("cart_id",cart_id)
                .addBodyParameter("user_id",strUserId)
                .addBodyParameter("status_single",strCheckStatus)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("rfrrferer", response.toString());

                        try {
                            if (response.getString("msg").equals("susccess")) {
                                arrayList=new ArrayList<>();
                                Log.e("dfsfdgsgfgffgf", LastAddressId );

                                AppConstant.sharedpreferences =context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.defaultIdAddress,LastAddressId );
                                editor.commit();

                                Fragment fragment = new CartFragment();
                                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null).commit();

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


    }*/

}
