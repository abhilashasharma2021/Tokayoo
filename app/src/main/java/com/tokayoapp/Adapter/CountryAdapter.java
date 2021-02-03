package com.tokayoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokayoapp.Activities.ShowCompanies;
import com.tokayoapp.Modal.CountryModal;
import com.tokayoapp.R;
import com.tokayoapp.Utils.AppConstant;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    Context context;
    List<CountryModal> dataAdapters;

    public CountryAdapter(List<CountryModal> getDataAdapter, Context context) {

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_adapter, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, final int position) {

        final CountryModal dataAdapterOBJ = dataAdapters.get(position);
        Viewholder.txt_name.setText(dataAdapterOBJ.getName());
        Viewholder.linCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.CountryId, dataAdapterOBJ.getId());
                editor.putString(AppConstant.CountryName, dataAdapterOBJ.getName());
                editor.putString(AppConstant.ZoneId, dataAdapterOBJ.getZone_id());
                editor.putString(AppConstant.ZoneName, dataAdapterOBJ.getZone_name());
                editor.commit();
                context.startActivity(new Intent(context, ShowCompanies.class));
            }
        });

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        LinearLayout linCountry;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_name=itemView.findViewById(R.id.txt_name);
            linCountry=itemView.findViewById(R.id.linCountry);
        }
    }

}