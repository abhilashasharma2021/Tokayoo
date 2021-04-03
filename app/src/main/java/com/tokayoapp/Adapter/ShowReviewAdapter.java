package com.tokayoapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.SubCategoryActivity;
import com.tokayoapp.Modal.DataShowReview;
import com.tokayoapp.Modal.DataShowReview.Data;
import com.tokayoapp.Modal.DataShowReview.Data;
import com.tokayoapp.Modal.ProductListModal;
import com.tokayoapp.R;

import java.util.ArrayList;
import java.util.List;


public class ShowReviewAdapter extends RecyclerView.Adapter<ShowReviewAdapter.ViewHolder> {

    private Context context;
    List<DataShowReview.Data> data;
    ProductListAdapter productListAdapter;
    RecyclerView rec_product_list;
    ArrayList<ProductListModal> arrayProductList = new ArrayList<>();
    String subBrandId="";
    public ShowReviewAdapter(Context context, List<DataShowReview.Data> getDataAdapter) {
        this.context = context;
        this.data = getDataAdapter;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);

        ShowReviewAdapter.ViewHolder viewHolder = new ShowReviewAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final DataShowReview.Data DataShowReview=data.get(position);



        holder.textViewUserName.setText(DataShowReview.getName());
        holder.textViewComment.setText(DataShowReview.getComment());
        holder.textViewDateAndTime.setText(DataShowReview.getCreated_date());
        holder.rating_Star.setRating(Float.parseFloat(DataShowReview.getScore()));
      //  holder.tx_name.setText(DataShowReview.Data.getName());

     /*   try{
            Picasso.with(context).load(DataShowReview.Data.getUserPath()+DataShowReview.Data.getUserImage()).into(holder.imgUserProfile);
        }
        catch (Exception e){
        }*/


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUserProfile;
        TextView textViewUserName,textViewComment,textViewDateAndTime;
        RatingBar rating_Star;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewDateAndTime = itemView.findViewById(R.id.textViewDateAndTime);
            rating_Star = itemView.findViewById(R.id.rating_Star);
            imgUserProfile = itemView.findViewById(R.id.imgUserProfile);

        }
    }



}
