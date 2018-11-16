package com.versatilemobitech.bhattivikramarka.adapters;

import android.content.Context;
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

import com.squareup.picasso.Picasso;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.fragments.GalleryInnerFragment;
import com.versatilemobitech.bhattivikramarka.fragments.SliderFragment;
import com.versatilemobitech.bhattivikramarka.models.Images;

import java.util.ArrayList;


public class ImgaesAdapter extends RecyclerView.Adapter<ImgaesAdapter.VideoHolder> implements RecyclerView.OnItemTouchListener {
    private Context mContext;
    private ImgaesAdapter.OnItemClickListener mListener;
    private GestureDetector mGestureDetector;
    private ArrayList<Images> mImagesList;
    private GalleryInnerFragment mGalleryInnerFragment;
    private String folderName;

    public ImgaesAdapter(String folderName, Context context, ArrayList<Images> mImagesList, GalleryInnerFragment mGalleryInnerFragment) {
        mContext = context;
        this.folderName = folderName;
        this.mImagesList = mImagesList;
        this.mGalleryInnerFragment = mGalleryInnerFragment;
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

    public ImgaesAdapter(Context context, ImgaesAdapter.OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public ImgaesAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images, parent, false);
        return new ImgaesAdapter.VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImgaesAdapter.VideoHolder holder, final int position) {
        final Images mImages = mImagesList.get(position);
        final int mPosition = position;
        holder.mImgGalleryInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateFragment(SliderFragment.newInstance(folderName, mPosition, mImagesList), "SLIDERFRAGMENT", (FragmentActivity) mContext);
            }
        });

        Picasso.with(mContext).load(mImages.imageUrl).placeholder(R.drawable.placeholder).resize(300, 300)
                .error(R.drawable.placeholder).into(holder.mImgGalleryInner);
    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        ImageView mImgGalleryInner;

        public VideoHolder(View view) {
            super(view);
            mImgGalleryInner = (ImageView) view.findViewById(R.id.imgGalleryInner);
        }
    }

    public static void navigateFragment(Fragment fragment, String tag, FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);

        if (tag != null) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }
}




