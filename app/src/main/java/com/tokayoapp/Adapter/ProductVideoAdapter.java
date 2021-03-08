package com.tokayoapp.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.tokayoapp.Activities.SubCategoryActivity;
import com.tokayoapp.Modal.BrandNameModal;
import com.tokayoapp.Modal.ProductListModal;
import com.tokayoapp.Modal.ProductVideoModal;
import com.tokayoapp.R;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class ProductVideoAdapter extends RecyclerView.Adapter<ProductVideoAdapter.ViewHolder> {

    private Context context;
    List<ProductVideoModal> videoAdapters ;


    public ProductVideoAdapter(Context context, List<ProductVideoModal> getDataAdapter) {
        this.context = context;
        this.videoAdapters = getDataAdapter;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_video_layout, parent, false);

        ProductVideoAdapter.ViewHolder viewHolder = new ProductVideoAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final ProductVideoModal videoModal=videoAdapters.get(position);

        String vi = videoModal.getVideo();
       String status = videoModal.getStatus();/*if Status =0 means simple video and 1= youtube video*/
       Log.e("kszdjvjxzikjv",status+"");
        Log.e("kszdjvjxzikjv",vi);
       if (status.equals("0")){
            holder.rel.setVisibility(View.VISIBLE);
            holder.rl_youtube.setVisibility(View.GONE);

            Uri uri = Uri.parse(vi);
            holder.videoplayer.setUp(vi
                    , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, uri);

            holder.clickPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.clickPlay.setVisibility(View.GONE);
                    holder.img_replay.setVisibility(View.GONE);
                    holder.videoplayer.startVideo();


                }
            });

        }else {
            holder.rel.setVisibility(View.GONE);
            holder.rl_youtube.setVisibility(View.VISIBLE);


              //https://www.youtube.com/watch?v=9xwazD5SyVg
           //  https://www.youtube.com/watch?v=xcJtL7QggTI

     // String vi_video = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

            String[] str = vi.split("=");

            String str1 = str[0];  //hello

            String str2 = str[1];

            Log.e("sdjvkjxc",  str1);
            Log.e("sdjvkjxc",  str2);

            holder.youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoId = str2;
                    youTubePlayer.loadVideo(videoId, 0);
                    youTubePlayer.pause();


                }
            });

/*
        if (videoModal.getVideo().contains("https://www.youtube.com/")){


        }else{


        }
*/








        }







    }

    @Override
    public int getItemCount() {
        return videoAdapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBrand;
        RelativeLayout rel,rl_youtube;
        public ImageView img_replay, clickPlay;
        public JCVideoPlayerStandard videoplayer;
        YouTubePlayerView youtube_player_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBrand = itemView.findViewById(R.id.imgBrand);
            youtube_player_view = itemView.findViewById(R.id.youtube_player_view);
            videoplayer = itemView.findViewById(R.id.videoplayer);
            img_replay = itemView.findViewById(R.id.img_replay);
            clickPlay = itemView.findViewById(R.id.clickPlay);
            rel = itemView.findViewById(R.id.rel);
            rl_youtube = itemView.findViewById(R.id.rl_youtube);


        }
    }



}
