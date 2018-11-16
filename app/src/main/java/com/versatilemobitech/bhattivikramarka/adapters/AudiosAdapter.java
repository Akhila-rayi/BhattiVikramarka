package com.versatilemobitech.bhattivikramarka.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.fragments.AudioSongsFragment;
import com.versatilemobitech.bhattivikramarka.models.Audios;

import java.util.ArrayList;

/**
 * Created by user on 27-12-17.
 */

public class AudiosAdapter extends RecyclerView.Adapter<AudiosAdapter.VideoHolder> implements RecyclerView.OnItemTouchListener {
    private Context mContext;
    private AudiosAdapter.OnItemClickListener mListener;
    private GestureDetector mGestureDetector;
    private ArrayList<Audios> mAudiosList;
    private AudioSongsFragment mAudiosFragment;

    public AudiosAdapter(Context context, ArrayList<Audios> mAudiosList, AudioSongsFragment mAudiosFragment) {
        mContext = context;
        this.mAudiosList = mAudiosList;
        this.mAudiosFragment = mAudiosFragment;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public AudiosAdapter(Context context, AudiosAdapter.OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public AudiosAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new AudiosAdapter.VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(final AudiosAdapter.VideoHolder holder, final int position) {
        final Audios mAudios = mAudiosList.get(position);
        Picasso.with(mContext).load(mAudios.image).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(holder.mImgAudios);
        holder.mTitle.setText(mAudios.title);
        holder.mTxtContent.setText((mAudios.description));
    }

    @Override
    public int getItemCount() {
        return mAudiosList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        ImageView mImgAudios;
        TextView mTitle, mTxtContent;
        RelativeLayout mRlAudiosongs;

        public VideoHolder(View view) {
            super(view);

            mImgAudios = (ImageView) view.findViewById(R.id.imgAudio);
            mTitle = (TextView) view.findViewById(R.id.txtTitle);
            mTxtContent = (TextView) view.findViewById(R.id.txtContent);
            mRlAudiosongs = (RelativeLayout) view.findViewById(R.id.rlAudiosongs);
        }
    }
}
