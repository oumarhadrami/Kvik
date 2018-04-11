package com.example.abdelkerim.kvik;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


public class VisionActivity extends AppCompatActivity {
    VideoView vvideo;
    MediaController mediaController;
    DisplayMetrics dm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
            // create an object of media controller class

        // set the media controller for video view

        // initiate a video view
        vvideo=(VideoView)findViewById(R.id.kvikvideo);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.kvikvideo;
        vvideo.setVideoURI(Uri.parse(path));
        mediaController = new MediaController(this);
        dm = new DisplayMetrics();
        //this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        vvideo.setMinimumWidth(width);
        vvideo.setMinimumHeight(height);
        mediaController.setAnchorView(vvideo);
        vvideo.setMediaController(mediaController);
        vvideo.start();
        // set media controller object for a video view
        // implement on completion listener on video view
        vvideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
            }
        });
        vvideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getApplicationContext(), "Oops", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                return false;
            }
        });
    }

}
