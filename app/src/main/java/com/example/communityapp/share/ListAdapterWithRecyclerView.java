package com.example.communityapp.share;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.communityapp.R;

import java.util.List;

public class ListAdapterWithRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "yoooooooooooooo";

    private List<Uri> uriList;
    private Context context;
    private image_listener image_listener;
    private video_listener video_listener;

    public ListAdapterWithRecyclerView(List<Uri> uriList,image_listener image_listener,video_listener video_listener,Context context) {
        this.uriList = uriList;
        this.image_listener = image_listener;
        this.video_listener = video_listener;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(uriList.get(position).toString().contains("image"))
        {
            return R.layout.activity_user_image;
        }

        else
        {
            Log.e(TAG,"yo1");
            return R.layout.activity_user_video;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final RecyclerView.ViewHolder holder;
        View view;
        switch (viewType)
        {
            case R.layout.activity_user_image:
                view = LayoutInflater.from(context).inflate(R.layout.activity_user_image,parent,false);
                holder = new UriHolder(view,image_listener);
                break;

            case R.layout.activity_user_video:
                view = LayoutInflater.from(context).inflate(R.layout.activity_user_video,parent,false);
                holder = new UriHolder1(view,video_listener);
                Log.e(TAG,"yo2");
                break;

            default:
                view = LayoutInflater.from(context).inflate(R.layout.activity_user_image,parent,false);
                holder = new UriHolder(view,image_listener);
                break;

        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Uri uri = uriList.get(position);
        if(holder instanceof UriHolder)
        {
            ((UriHolder) holder).user_imageview.setImageURI(uri);
        }

        else
        {
            ((UriHolder1) holder).user_videoview.setVideoURI(uri);
            Log.e(TAG,"yo3 "+position);
        }

    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class UriHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView user_imageview;
        image_listener image_listener;

        public UriHolder(@NonNull View itemView,image_listener image_listener) {
            super(itemView);
            user_imageview = itemView.findViewById(R.id.user_imageview);
            this.image_listener = image_listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            image_listener.image_click(getAdapterPosition());
        }

    }

    public class UriHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener{

        public VideoView user_videoview;
        video_listener video_listener;

        public UriHolder1(@NonNull View itemView, video_listener video_listener) {
            super(itemView);
            user_videoview = itemView.findViewById(R.id.user_videoview);
            this.video_listener = video_listener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            video_listener.video_click(getAdapterPosition());
        }
    }


    public interface image_listener
    {
        void image_click(int position);
    }

    public interface video_listener
    {
        void video_click(int position);
    }

}
