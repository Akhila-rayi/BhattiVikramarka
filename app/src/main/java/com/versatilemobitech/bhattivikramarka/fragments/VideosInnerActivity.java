package com.versatilemobitech.bhattivikramarka.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.versatilemobitech.bhattivikramarka.R;


public class VideosInnerActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    String youtube_id;
    YouTubePlayerView youtubePlayr;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    View parentLayout;
    private Handler mHandler = null;
   // private YouTubePlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_videos_inner);

        Intent intent = getIntent();
        youtube_id = intent.getStringExtra("youtube_id");
        Log.e("youtube_id", "" + youtube_id);
        youtubePlayr = (YouTubePlayerView) findViewById(R.id.youtubePlayr);
        //parentLayout = findViewById(android.R.id.content);


        youtubePlayr.initialize("AIzaSyCWHbcd2CD8TRm6QUJnZhpq3Bua68BwOw", this);


    }

   /* private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v="+youtube_id);
        startActivity(Intent.createChooser(sharingIntent, "share using"));
    }*/

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        Log.e("youtube_id", "started");
        if (!b) {
            Log.e("youtube_id", "started Val");
            youTubePlayer.loadVideo(youtube_id);

        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("Network error", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
//            StaticUtils.showSnackbar(this,parentLayout,errorMessage);
        }
    }

}
