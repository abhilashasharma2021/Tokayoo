package com.tokayoapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.ChatActivity;
import com.tokayoapp.Modal.ChatModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.List;

import static com.tokayoapp.Activities.ChatActivity.edt_typeMsg;
import static com.tokayoapp.Activities.ChatActivity.img_send;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

   Context context;
   List<ChatModal> chatAdapterList;
   String strUserID="";

   public ChatAdapter(Context context, List<ChatModal> getDataAdpter){

       this.context=context;
       this.chatAdapterList=getDataAdpter;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_chat_layout,parent,false);
       ChatAdapter.ViewHolder viewHolder=new ChatAdapter.ViewHolder(view);

        AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserID = AppConstant.sharedpreferences.getString(AppConstant.UserId, "");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChatModal chatModal=chatAdapterList.get(position);

        if(chatModal.getSender_id().equals(strUserID)){
            holder.relAdmin.setVisibility(View.GONE);
            holder.relCurrent_user.setVisibility(View.VISIBLE);

            holder.tx_user_message.setText(chatModal.getMessage());
            holder.txt_c_time.setText(chatModal.getTime());
            holder.txt_c_day.setText(chatModal.getDay());

            try {
                Picasso.with(context).load(chatModal.getPath() + chatModal.getImage()).into(holder.imgCurrentUser);
            } catch (Exception ignored) {

            }

        }

        else {
            holder.relAdmin.setVisibility(View.VISIBLE);
            holder.relCurrent_user.setVisibility(View.GONE);
            holder.tx_title.setText(chatModal.getMessage());
            holder.txt_time.setText(chatModal.getTime());
            holder.txt_day.setText(chatModal.getDay());
            try {
                Picasso.with(context).load(chatModal.getPath() + chatModal.getImage()).into(holder.imgUser);
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public int getItemCount() {
        return chatAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView imgUser,imgCurrentUser;
       TextView tx_title,txt_day,txt_time;

       RelativeLayout relCurrent_user,relAdmin;
       TextView tx_user_message;
       TextView txt_c_day;
       TextView txt_c_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relAdmin = itemView.findViewById(R.id.relAdmin);
            imgUser = itemView.findViewById(R.id.imgUser);
            imgCurrentUser = itemView.findViewById(R.id.imgCurrentUser);
            txt_day = itemView.findViewById(R.id.txt_day);
            txt_time = itemView.findViewById(R.id.txt_time);
            tx_title = itemView.findViewById(R.id.tx_title);
            relCurrent_user = itemView.findViewById(R.id.relCurrent_user);
            tx_user_message = itemView.findViewById(R.id.tx_user_message);
            txt_c_day = itemView.findViewById(R.id.txt_c_day);
            txt_c_time = itemView.findViewById(R.id.txt_c_time);
        }
    }
}
