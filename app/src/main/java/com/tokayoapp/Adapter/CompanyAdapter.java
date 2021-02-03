package com.tokayoapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kodmap.library.kmrecyclerviewstickyheader.KmStickyListener;
import com.tokayoapp.Activities.MainActivity;
import com.tokayoapp.Activities.ShowCompanies;
import com.tokayoapp.Fragments.CartFragment;
import com.tokayoapp.Fragments.HomeFragment;
import com.tokayoapp.Modal.CompanyModal;
import com.tokayoapp.Modal.ItemType;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

public class CompanyAdapter extends ListAdapter<CompanyModal, RecyclerView.ViewHolder> implements KmStickyListener {

    public CompanyAdapter() {
        super(ModelDiffUtilCallback);
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == ItemType.Header) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_company_header, viewGroup, false);
            return new HeaderViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_company_post, viewGroup, false);
            return new PostViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == ItemType.Header) {
            ((HeaderViewHolder) viewHolder).bind(getItem(i));
        } else {
            ((PostViewHolder) viewHolder).bind(getItem(i));
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CheckBox checkbox;


        public HeaderViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_name_header);
            checkbox = itemView.findViewById(R.id.checkbox);


        }

        public void bind(final CompanyModal model) {
            title.setText(model.getCom_title());

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b==true){

                        Log.e("dfsfsdfsf",model.getCom_id());

                        AppConstant.sharedpreferences = ShowCompanies.context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                        editor.putString(AppConstant.customStatus, "1");
                        editor.putString(AppConstant.CompanyName, model.getCom_title());
                        editor.putString(AppConstant.CompanyID, model.getCom_id());
                        editor.commit();
                        ShowCompanies.context.startActivity(new Intent(ShowCompanies.context, MainActivity.class));

                    }
                }
            });

        }
    }


    class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_weight;
        public TextView txt_price;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_weight = (TextView) itemView.findViewById(R.id.txt_weight);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
        }

        public void bind(CompanyModal model) {
            txt_weight.setText("Weight"+" "+model.getWeight()+" "+"kg");
            txt_price.setText("Price"+" "+model.getPrice()+" "+"RM");



        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }


    @Override
    public Integer getHeaderPositionForItem(Integer itemPosition) {
        Integer headerPosition = 0;
        for (Integer i = itemPosition;i > 0 ;i--){
            if (isHeader(i)){
                headerPosition = i;
                return headerPosition;
            }
        }
        return headerPosition;
    }

    @Override
    public Integer getHeaderLayout(Integer headerPosition) {
        return R.layout.item_company_header;
    }

    @Override
    public void bindHeaderData(View header, Integer headerPosition) {
        TextView tv = header.findViewById(R.id.txt_name_header);
        tv.setText(getItem(headerPosition).getCom_title());
    }

    @Override
    public Boolean isHeader(Integer itemPosition) {
        return getItem(itemPosition).getType().equals(ItemType.Header);
    }

    public static final DiffUtil.ItemCallback<CompanyModal> ModelDiffUtilCallback =
            new DiffUtil.ItemCallback<CompanyModal>() {
                @Override
                public boolean areItemsTheSame(@NonNull CompanyModal model, @NonNull CompanyModal t1) {
                    return model.getCom_title().equals(t1.getCom_title());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull CompanyModal model, @NonNull CompanyModal t1) {
                    return model.equals(t1);
                }
            };


}