package com.versatilemobitech.bhattivikramarka.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.versatilemobitech.bhattivikramarka.fragments.DailyActivitiesFragment;
import com.versatilemobitech.bhattivikramarka.fragments.DailyActivitiesInnerFragment;
import com.versatilemobitech.bhattivikramarka.models.DailyActivities;

import java.util.ArrayList;


public class DailyActivitiesAdapter extends RecyclerView.Adapter<DailyActivitiesAdapter.VideoHolder> implements RecyclerView.OnItemTouchListener {
    private Context mContext;
    private DailyActivitiesAdapter.OnItemClickListener mListener;
    private GestureDetector mGestureDetector;
    private ArrayList<DailyActivities> mDailyActivitiesList;
    private DailyActivitiesFragment mDailyActivitiesFragment;

    public DailyActivitiesAdapter(Context context, ArrayList<DailyActivities> mDailyActivitiesList, DailyActivitiesFragment mDailyActivitiesFragment) {
        mContext = context;
        this.mDailyActivitiesList = mDailyActivitiesList;
        this.mDailyActivitiesFragment = mDailyActivitiesFragment;
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

    public DailyActivitiesAdapter(Context context, DailyActivitiesAdapter.OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public DailyActivitiesAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_activities, parent, false);
        return new DailyActivitiesAdapter.VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(final DailyActivitiesAdapter.VideoHolder holder, final int position) {
        final DailyActivities mDailyActivities = mDailyActivitiesList.get(position);
        holder.mRlDailyActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle mBundle = new Bundle();
                mBundle.putString("title", mDailyActivities.title);
                mBundle.putString("image", mDailyActivities.image);
                mBundle.putString("service_done_on", mDailyActivities.posted_on);
                mBundle.putString("description", mDailyActivities.description);

                navigateFragment(DailyActivitiesInnerFragment.newInstance(), "SOCIALSERVICEDETAILSFRAGMENT", mBundle, (FragmentActivity) mContext);
            }
        });

        Picasso.with(mContext).load(mDailyActivities.image).placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(holder.mImgView);
        holder.mTitle.setText(mDailyActivities.title);
        holder.mTxtContent.setText((mDailyActivities.description));
        holder.mTxtDate.setText(mDailyActivities.posted_on);
    }

    @Override
    public int getItemCount() {
        return mDailyActivitiesList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        ImageView mImgView;
        TextView mTitle, mTxtContent, mTxtDate, mTxtReadMore;
       RelativeLayout mRlDailyActivities;

        public VideoHolder(View view) {
            super(view);
            mImgView = view.findViewById(R.id.imgViewDailyInner);
            mTitle =  view.findViewById(R.id.txtTitle);
            mTxtContent = view.findViewById(R.id.txtContent);
            mTxtDate =  view.findViewById(R.id.txtDate);
            mTxtReadMore =  view.findViewById(R.id.txtReadMore);
            mRlDailyActivities =  view.findViewById(R.id.rlDailyactivities);
        }
    }

    public static void navigateFragment(Fragment fragment, String tag, Bundle bundle, FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        if (tag != null) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }
}




