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
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.fragments.GalleryFragment;
import com.versatilemobitech.bhattivikramarka.fragments.GalleryInnerFragment;
import com.versatilemobitech.bhattivikramarka.models.Gallery;

import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.VideoHolder> implements RecyclerView.OnItemTouchListener {
    private Context mContext;
    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;
    private ArrayList<Gallery> mGalleryList;
    private GalleryFragment mGalleryFragment;

    public GalleryAdapter(Context context, ArrayList<Gallery> mGalleryList, GalleryFragment mGalleryFragment) {
        mContext = context;
        this.mGalleryList = mGalleryList;
        this.mGalleryFragment = mGalleryFragment;
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

    public GalleryAdapter(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public GalleryAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new GalleryAdapter.VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.VideoHolder holder, final int position) {
        final Gallery mGallery = mGalleryList.get(position);
        try {
            Picasso.with(mContext).load(mGallery.image).placeholder(R.drawable.placeholder).resize(300, 300)
                    .error(R.drawable.placeholder).into(holder.mImgGallery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.mTxtFoldername.setText(mGallery.foldername);
        holder.mImgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle mBundle = new Bundle();
                mBundle.putString("folder_name", mGallery.foldername);
                mBundle.putString("folder_id", mGallery.folderid);
                navigateFragment(GalleryInnerFragment.newInstance(), "GALLERYINNERFRAGMENT", mBundle, (FragmentActivity) mContext);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGalleryList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        ImageView mImgGallery;
        TextView mTxtFoldername;

        public VideoHolder(View view) {
            super(view);
            mImgGallery = (ImageView) view.findViewById(R.id.imgGallery);
            mTxtFoldername = (TextView) view.findViewById(R.id.txtFoldername);
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




