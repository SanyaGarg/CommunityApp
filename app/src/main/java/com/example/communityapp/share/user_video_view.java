package com.example.communityapp.share;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.communityapp.R;

public class user_video_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_video_view);

        VideoView vid = findViewById(R.id.uri_vid);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vid);
        vid.setMediaController(mediaController);
        vid.setVideoURI(UriHelper.getInstance().getUri());
        vid.start();
    }
}