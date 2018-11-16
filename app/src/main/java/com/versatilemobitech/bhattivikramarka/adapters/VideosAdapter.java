package com.versatilemobitech.bhattivikramarka.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.fragments.VideosFragment;
import com.versatilemobitech.bhattivikramarka.fragments.VideosInnerActivity;
import com.versatilemobitech.bhattivikramarka.models.Videos;

import java.util.ArrayList;


public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoHolder> implements RecyclerView.OnItemTouchListener {
    private Context mContext;
    private VideosAdapter.OnItemClickListener mListener;
    private GestureDetector mGestureDetector;
    private ArrayList<Videos> mVideosList;
    private VideosFragment mVideosFragment;

    public VideosAdapter(Context context, ArrayList<Videos> mVideosList, VideosFragment mVideosFragment) {
        mContext = context;
        this.mVideosList = mVideosList;
        this.mVideosFragment = mVideosFragment;
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

    public VideosAdapter(Context context, VideosAdapter.OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public VideosAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videos, parent, false);
        return new VideosAdapter.VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideosAdapter.VideoHolder holder, final int position) {
        final Videos mVideos = mVideosList.get(position);
        Picasso.with(mContext).load(mVideos.image).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(holder.mImgVideos);
        holder.mTxtVideoname.setText(mVideos.videoname);
        holder.mImgVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent youtube = new Intent(mContext, VideosInnerActivity.class);
                youtube.putExtra("youtube_id", mVideosList.get(position).youtubeid);
                mContext.startActivity(youtube);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVideosList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        LinearLayout mLlLike;
        ImageView mImgVideos;
        TextView mTxtVideoname;

        public VideoHolder(View view) {
            super(view);
//            mLlLike = (LinearLayout) view.findViewById(R.id.llLike);
            mImgVideos = (ImageView) view.findViewById(R.id.imgVideos);
            mTxtVideoname = (TextView) view.findViewById(R.id.txtVideoname);
        }
    }
}




