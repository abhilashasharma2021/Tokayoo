package com.tokayoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
;

import com.tokayoapp.Modal.NotificationModal;
import com.tokayoapp.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

   Context context;
   List<NotificationModal> notiAdapterList;

   public NotificationAdapter(Context context, List<NotificationModal> getDataAdpter){

       this.context=context;
       this.notiAdapterList=getDataAdpter;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_notifi_layout,parent,false);
       NotificationAdapter.ViewHolder viewHolder=new NotificationAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NotificationModal notificationModal=notiAdapterList.get(position);

        holder.txt_dis.setText(notificationModal.getDisc());
        holder.txt_date.setText(notificationModal.getDate());
        holder.txt_time.setText(notificationModal.getTime());
        holder.tx_title.setText(notificationModal.getTitle());


    }

    @Override
    public int getItemCount() {
        return notiAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       TextView tx_title,txt_dis,txt_date,txt_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tx_title = itemView.findViewById(R.id.tx_title);
            txt_dis = itemView.findViewById(R.id.txt_dis);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_time = itemView.findViewById(R.id.txt_time);
        }
    }
}
