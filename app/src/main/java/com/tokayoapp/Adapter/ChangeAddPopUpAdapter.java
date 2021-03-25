package com.tokayoapp.Adapter;

import android.app.Dialog;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.tokayoapp.Fragments.CartFragment;
import com.tokayoapp.Modal.AddressListModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.List;


public class ChangeAddPopUpAdapter extends RecyclerView.Adapter<ChangeAddPopUpAdapter.ViewHolder> {

   Context context;
   List<AddressListModal> addressListModals;
   CartFragment mycartFragment;
   String strSelectedCountryCode="";
   public ChangeAddPopUpAdapter(Context context, List<AddressListModal> getDataAdpter,CartFragment cartFragment){

       this.context=context;
       this.addressListModals=getDataAdpter;
       this.mycartFragment = cartFragment;
   }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_change_address_popup_layout,parent,false);
       ChangeAddPopUpAdapter.ViewHolder viewHolder=new ChangeAddPopUpAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);

        strSelectedCountryCode = AppConstant.sharedpreferences.getString(AppConstant.SelectedCountryCode, "");

        Log.e("drhndtrjntgn", strSelectedCountryCode);
        final AddressListModal addressListModal=addressListModals.get(position);



       // holder.txt_country.setText(strSelectedCountryCode);

        String [] strings = addressListModal.getContact().split("-");
        holder.txt_country.setText(strings[0]);
        holder.txtnumber.setText(strings[1]);
        holder.txt_name.setText(addressListModal.getName());
      //  holder.txtnumber.setText(addressListModal.getContact());
        holder.txtLocalAddress.setText(addressListModal.getAddress());

        holder.rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((CartFragment)mycartFragment).show_changeAddress(addressListModal.getId(),addressListModal.getName(),addressListModal.getContact(),addressListModal.getAddress());
                  mycartFragment.dialog.dismiss();


            }


        });


        }



    @Override
    public int getItemCount() {
        return addressListModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      RelativeLayout rl2;
       TextView txt_name,txtnumber,txtLocalAddress,txt_country;
        Dialog dialog;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            rl2 = itemView.findViewById(R.id.rl2);
            txtnumber = itemView.findViewById(R.id.txtnumber);
            txt_country = itemView.findViewById(R.id.txt_country);
            txtLocalAddress = itemView.findViewById(R.id.txtLocalAddress);
        }
    }
}
