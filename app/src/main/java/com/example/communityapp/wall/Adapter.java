package com.example.communityapp.wall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.communityapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Post> posts;
    private Context context;
    private OnItemClickListener onItemClickListener;

    LinearLayout gallery;

    public Adapter(List<Post> posts,Context context){
        this.posts = posts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.activity_post,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holders, int position) {

        final MyViewHolder holder = holders;
        Post model = posts.get(position);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.background_shadow)
                .error(R.drawable.ic_launcher_background)
                .priority(Priority.HIGH);

        LayoutInflater inflater = LayoutInflater.from(context);

        for (int i=0;i<model.getPostMedia().size();i++) {

            //View v = inflater.inflate(R.layout.horizontal,gallery,false);
            List<String> media = model.getPostMedia();

            RequestOptions myOptions = new RequestOptions()
                    .encodeFormat(Bitmap.CompressFormat.JPEG)
                    .override(1280, 1024) // this is what you need to take care of WxH
                    .fitCenter();

            Glide.with(context)
                    .load(media.get(i))
                    .apply(myOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageView);

            LoadImage loadImage = new LoadImage(holder.imageView);
            loadImage.execute(media.get(i));
        }

        String s = "" +model.getLikes().size();
        holder.author.setText(model.getUserId());
        holder.likes.setText(s);
        holder.text.setText(model.getText());
        holder.date.setText(model.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        //   this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // implements View.OnClickListener{

        TextView author, likes, comments, text, date;
        ProgressBar progressBar;
        ImageView imageView;
        //  OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // itemView.setOnClickListener(this);
            author = itemView.findViewById(R.id.user);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            text = itemView.findViewById(R.id.text);
            gallery = itemView.findViewById(R.id.gallery);
            date = itemView.findViewById(R.id.date);
            progressBar = itemView.findViewById(R.id.progress_circular);
            imageView = itemView.findViewById(R.id.horizontal_img);

            //   this.onItemClickListener = onItemClickListener;
        }
    }
}
class LoadImage extends AsyncTask<String,Void,Bitmap> {

     ImageView imageView;
    public LoadImage(ImageView ivResult){
        this.imageView = ivResult;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlLink = strings[0];
        Bitmap bitmap=null;

        try {
            InputStream inputStream = new java.net.URL(urlLink).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
}
