package com.tokayoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tokayoapp.Activities.AddressListActivity;
import com.tokayoapp.Activities.EditAddressActivity;
import com.tokayoapp.Activities.MainActivity;
import com.tokayoapp.Activities.ReedeemPopUpActivity;
import com.tokayoapp.Modal.AddressListModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.API;
import com.tokayoapp.Utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class RewardAddressListAdapter extends RecyclerView.Adapter<RewardAddressListAdapter.ViewHolder> {

    Context context;
    List<AddressListModal> addressListModals;

    String strSeletAddressStatus = "";

    public RewardAddressListAdapter(Context context, List<AddressListModal> getDataAdpter) {

        this.context = context;
        this.addressListModals = getDataAdpter;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_reward_addresslist_layout, parent, false);
        RewardAddressListAdapter.ViewHolder viewHolder = new RewardAddressListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AddressListModal addressListModal = addressListModals.get(position);

        final String id = addressListModal.getId();
        Log.e("dfdfvcx", addressListModal.getId() + "");
        holder.txt_name.setText(addressListModal.getName());
        holder.tx_contact.setText(addressListModal.getContact());
        holder.txtAddress.setText(addressListModal.getAddress());


        holder.rl_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.defaultIdAddress, addressListModal.getId());
                editor.commit();

                context.startActivity(new Intent(context, ReedeemPopUpActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressListModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, tx_contact, txtAddress;
        RelativeLayout rl_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            tx_contact = itemView.findViewById(R.id.tx_contact);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            rl_address = itemView.findViewById(R.id.rl_address);
        }
    }


}
