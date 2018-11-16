package com.versatilemobitech.bhattivikramarka.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.activities.HomeActivity;
import com.versatilemobitech.bhattivikramarka.models.Audios;
import com.versatilemobitech.bhattivikramarka.utils.StaticUtils;
import com.versatilemobitech.bhattivikramarka.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;

public class AudioSongsInnerFragment extends BaseFragment implements View.OnClickListener,
        MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private static String TAG = AudioSongsInnerFragment.class.getSimpleName();
    private View view;
    TextView mTxtSongCurrentDuration, mTxtTotalsongDuration;
    ImageView mImgPlay, mImgPrevious, mImgNext, mImgAudioPlay;
    private MediaPlayer mp;

    private Utilities utils;
    private SeekBar songProgressBar;
    private Handler mHandler = new Handler();
    private int duration;
    private String id = "", audio = "", image = "";
    private Handler seekHandler = new Handler();
    private int position;
    private static ArrayList<Audios> mAudiosList;

    public static AudioSongsInnerFragment newInstance(ArrayList<Audios> mAudiosList1) {
        mAudiosList = mAudiosList1;
        AudioSongsInnerFragment fragment = new AudioSongsInnerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_audio_inner, container, false);

        ((HomeActivity) getActivity()).mImgMenu.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).mImgBack.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).mImgSettings.setVisibility(View.GONE);
        mp = new MediaPlayer();

        initComponents();
        return view;
    }

    private void initComponents() {
        setReferences();
        setClickListeners();
        getBundleData();
    }

    private void setReferences() {
        mTxtSongCurrentDuration = view.findViewById(R.id.txtsongCurrentDurationLabel);
        mTxtTotalsongDuration = view.findViewById(R.id.txtsongTotalDurationLabel);
        mImgPlay = view.findViewById(R.id.imgPlay);
        mImgPrevious = view.findViewById(R.id.imgPrevious);
        mImgNext = view.findViewById(R.id.imgNext);
        songProgressBar = view.findViewById(R.id.seebar);
        mImgAudioPlay = view.findViewById(R.id.imgAudioPlay);
    }

    private void setClickListeners() {
        mImgPlay.setOnClickListener(this);
        mImgPrevious.setOnClickListener(this);
        mImgNext.setOnClickListener(this);
        songProgressBar.setOnSeekBarChangeListener(this);
        mp.setOnCompletionListener(this);
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            //audio = bundle.getString("audio");
            ((HomeActivity) getActivity()).mTxtTitle.setText(mAudiosList.get(position).title);
            position = bundle.getInt("position");
            audio = "http://versatilemobitech.in/aumaujaya/adminportal/bhajanas/AVMS_Song.mp3";
            Picasso.with(getActivity()).load(bundle.getString("image")).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(mImgAudioPlay);
            utils = new Utilities();
        }
        setMediaPlayer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgPlay:
                if (mp.isPlaying()) {
                    if (mp != null) {
                        mp.pause();
                        // Changing button image to play button
                        mImgPlay.setImageResource(R.drawable.playbuttongreenicon);
                    }
                } else {
                    // Resume song
                    if (mp != null) {
                        mp.start();
                        // Changing button image to pause button
                        mImgPlay.setImageResource(R.drawable.pause);
                    }
                }
                break;
            case R.id.imgPrevious:
                if (position == 0 && position != -1) {
                    Toast.makeText(getContext(), "this is the first Song..", Toast.LENGTH_SHORT).show();
                } else {
                    playSong(position - 1);
                    Toast.makeText(getContext(), "You are Playing First song", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgNext:
//                if (position < songUrl.size() && position != 0) {
//                    Log.e(TAG, "getIntentData:>>>>>>>>>> " + currentSongIndex);
//               /*     currentSongIndex = currentSongIndex + 1;
//                    playSong(currentSongIndex);*/
//                    Toast.makeText(getContext(), "this is the Last Song..", Toast.LENGTH_SHORT).show();
//                } else {
//                    playSong(0);
//                    //playSong(position + 1);
//                }
                break;
        }
    }

    private void setMediaPlayer() {
        mp.reset();
        try {
            mp.setDataSource(audio);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mp.start();
        duration = StaticUtils.getAudioDuration(audio);
        songProgressBar.setMax(duration);
        mp.start();
        Log.e("Duration", "" + duration);
        mImgPlay.setImageResource(R.drawable.pause);
        seekUpdation();
       /* String url = "http://www.gaana.mp3"; // your URL here
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.setDataSource(url);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.prepareAsync();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mp.start();
            }
        });
        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int i, int i1) {
                return false;
            }
        });*/
        }

    public void playSong(final int songIndex) {
        mp.reset();
        try {
            mp.setDataSource(mAudiosList.get(position).audiourl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mp.start();
        duration = StaticUtils.getAudioDuration(mAudiosList.get(position).audiourl);
        songProgressBar.setMax(duration);
        mp.start();
        Log.e("Duration", "" + duration);
        seekUpdation();

    }

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    public void seekUpdation() {
        if (mp != null) {
            int mCurrentPosition = mp.getCurrentPosition() / 1000;
            songProgressBar.setProgress(mCurrentPosition);
            long currentDuration = mp.getCurrentPosition();
            long minute = duration / (60);
            long second = duration - (minute * 60);
            mTxtTotalsongDuration.setText(minute + ":" + second);
            mTxtSongCurrentDuration.setText("" + utils.milliSecondsToTimer(currentDuration));
        }
        seekHandler.postDelayed(run, 1000);
        Log.e("Current pos", "" + mp.getCurrentPosition() / 1000);
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
//            playSong(currentSongIndex);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mp != null && fromUser) {
//                    seekBar.setProgress(progress);
            mp.seekTo(progress * 1000);
            Log.e("onProgressChanged:", "" + progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        seekHandler.removeCallbacks(run);
        mp.release();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
