package com.tokayoapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.tokayoapp.Modal.ProductDetailModal;
import com.tokayoapp.Modal.RewardDetailModal;
import com.tokayoapp.R;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerAdapterRewardDetail extends PagerAdapter {
private Context context;
private LayoutInflater layoutInflater;
List<RewardDetailModal> productListReward;

    public ViewpagerAdapterRewardDetail(Context context, List<RewardDetailModal> productListReward) {
        this.context=context;
        this.productListReward=productListReward;


    }

    @Override
    public int getCount() {
        return productListReward.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_enlarge_image_layout,null);

        RewardDetailModal data = productListReward.get(position);
        ImageView imageView=(ImageView)view.findViewById(R.id.my_image);

        Log.e("dsfsfv",data.getPath());

        Picasso.with(context).load(data.getPath()+data.getImage()).into(imageView);

        ViewPager viewPager=(ViewPager)container;
        viewPager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager=(ViewPager)container;
        View view=(View)object;
        viewPager.removeView(view);
    }
}
