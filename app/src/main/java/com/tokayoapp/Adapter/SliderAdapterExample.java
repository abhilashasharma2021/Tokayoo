package com.tokayoapp.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Modal.SliderModel;
import com.tokayoapp.R;

import java.util.List;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    Typeface Fsaloon;
    List<SliderModel> dataAdapters;

    public SliderAdapterExample(List<SliderModel> getDataAdapter, Context context) {
        this.context = context;
        this.dataAdapters = getDataAdapter;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        final SliderModel dataAdapterOBJ = dataAdapters.get(position);


        try {
            Picasso.with(context).load(dataAdapterOBJ.getPath() + dataAdapterOBJ.getImage()).into(viewHolder.imageViewBackground);
        } catch (Exception e) {


        }


    }

    // viewHolder.textViewDescription.setText(dataAdapterOBJ.getName());
    // Picasso.get().load(R.drawable.fishing).into(viewHolder.imageViewBackground);
       /* if(position==0){
            Picasso.get().load(R.drawable.slider_two).into(viewHolder.imageViewBackground);
        }
        if(position==1){
            Picasso.get().load(R.drawable.fishing).into(viewHolder.imageViewBackground);
        }

        if(position==2){

            Picasso.get().load(R.drawable.slider_3).into(viewHolder.imageViewBackground);
        }
*/
        /*Glide.with(viewHolder.itemView)
                .load(dataAdapterOBJ.getPath()+dataAdapterOBJ.getIcon())
                .into(viewHolder.imageViewBackground);*/


    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return dataAdapters.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            // textViewDescription = itemView.findViewById(R.id.txt_details);
            this.itemView = itemView;
        }
    }


}