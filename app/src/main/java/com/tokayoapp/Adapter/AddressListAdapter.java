package com.tokayoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.tokayoapp.Activities.AddNewActivity;
import com.tokayoapp.Activities.AddressListActivity;
import com.tokayoapp.Activities.EditAddressActivity;
import com.tokayoapp.Activities.MainActivity;
import com.tokayoapp.Activities.ReedeemPopUpActivity;
import com.tokayoapp.Fragments.CartFragment;
import com.tokayoapp.Modal.AddressListModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {

    Context context;
    List<AddressListModal> addressListModals;

    String strSeletAddressStatus = "",strSelectedCountryCode="";

    public AddressListAdapter(Context context, List<AddressListModal> getDataAdpter) {

        this.context = context;
        this.addressListModals = getDataAdpter;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_addresslist_layout, parent, false);
        AddressListAdapter.ViewHolder viewHolder = new AddressListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strSeletAddressStatus = AppConstant.sharedpreferences.getString(AppConstant.SeletAddressStatus, "");
        strSelectedCountryCode = AppConstant.sharedpreferences.getString(AppConstant.SelectedCountryCode, "");
        Log.e("yhghg", strSeletAddressStatus);
        Log.e("drhndtrjntgn", strSelectedCountryCode);

        final AddressListModal addressListModal = addressListModals.get(position);

        final String id = addressListModal.getId();
        Log.e("dfdfvcx", addressListModal.getId() + "");
        holder.txt_name.setText(addressListModal.getName());
        holder.tx_contact.setText(addressListModal.getContact());
        holder.txtAddress.setText(addressListModal.getAddress());
        holder.txt_country.setText(strSelectedCountryCode);
        String default_Status = addressListModal.getStatus();
        Log.e("jhkljvc", addressListModal.getStatus() + "");/*1 means check default selected 0 means not selected*/




        if (default_Status.equals("1")) {

            holder.tx_details.setVisibility(View.VISIBLE);
            holder.txt_name.setText(addressListModal.getName());
            holder.tx_contact.setText(addressListModal.getContact());
            holder.txtAddress.setText(addressListModal.getAddress());

            AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
            editor.putString(AppConstant.defaultIdAddress, addressListModal.getId());
            editor.commit();


        } else {
            holder.tx_details.setVisibility(View.GONE);
        }

        if (strSeletAddressStatus.equals("select")){

            holder.tx_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                    editor.putString(AppConstant.defaultIdAddress, addressListModal.getId());
                    editor.commit();

                    context.startActivity(new Intent(context, ReedeemPopUpActivity.class));

                }
            });
        }else{

            holder.tx_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                    editor.putString(AppConstant.defaultIdAddress, addressListModal.getId());
                    editor.putString(AppConstant.customStatus, "1");
                    editor.commit();

                    context.startActivity(new Intent(context, MainActivity.class));

                }
            });
        }




        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_Address(id,position);
            }
        });

  /*  if (position == 0) {
            holder.txt_name.setText(addressListModal.getName());
            holder.tx_contact.setText(addressListModal.getContact());
            holder.txtAddress.setText(addressListModal.getAddress());
            holder.tx_details.setVisibility(View.VISIBLE);
            holder.tx_details.setTextColor(context.getResources().getColor(R.color.pink));


            holder.tx_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 
                  AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                    editor.putString(AppConstant.AddressId, addressListModal.getId());
                    editor.commit();

                    context.startActivity(new Intent(context, MainActivity.class));

                       ((FragmentActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new CartFragment())
                                .commit();
                }
            });

        } else {

    } */

        holder.rl_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.AddressId, addressListModal.getId());
                editor.putString(AppConstant.UserName, addressListModal.getName());
                editor.putString(AppConstant.UserAddress, addressListModal.getAddress());
                editor.putString(AppConstant.UserMobile, addressListModal.getContact());
                editor.putString(AppConstant.defaultStatus, addressListModal.getStatus());
                editor.commit();

                context.startActivity(new Intent(context, EditAddressActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressListModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, tx_contact, txtAddress, tx_details,txt_country;
        ImageView img_delete;
        RelativeLayout rl_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            tx_contact = itemView.findViewById(R.id.tx_contact);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            tx_details = itemView.findViewById(R.id.tx_details);
            img_delete = itemView.findViewById(R.id.img_delete);
            rl_address = itemView.findViewById(R.id.rl_address);
            txt_country = itemView.findViewById(R.id.txt_country);
        }
    }

    public void delete_Address(String id, final int position) {

        Log.e("fkjvvv", id);
        //  AndroidNetworking.post("https://3511535117.co/Tokayo/api/process.php?action=delete_address")
        AndroidNetworking.post(API.BASEURL + API.delete_address)
                .addBodyParameter("id", id)
                .setTag("Delete Addresslist product")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ifujvdivu", response.toString());


                        try {
                            if (response.getString("result").equals("Delete Address Successfully")) {

                                       addressListModals.remove(position);
                                       notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            Log.e("djfcdkjv", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("reftgdg", anError.getMessage());
                    }
                });

    }
}
