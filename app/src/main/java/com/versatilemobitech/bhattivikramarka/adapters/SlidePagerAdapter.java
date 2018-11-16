package com.versatilemobitech.bhattivikramarka.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.bhattivikramarka.R;

import java.util.ArrayList;


public class SlidePagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> mResources;
    ImageView mImgClose;
    onCloseImageclick colseListener;


    public SlidePagerAdapter(Context context, onCloseImageclick listener, ArrayList<String> mResources) {
        colseListener = listener;
        this.mResources = mResources;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        //mImgClose = (ImageView) itemView.findViewById(R.id.item_closeimg);
      /*  mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colseListener.onClose();

            }
        });*/
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
         //imageView.setImageResource(mResources.get(position).getImag);
       Picasso.with(mContext).load(mResources.get(position)).placeholder(R.drawable.placeholder).resize(300, 300).error(R.drawable.placeholder).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectedPos = position;
     /*   ((FloatingActionMenu) mCurrentView.findViewById(R.id.menu_floating)).close(true);*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    int selectedPos;
    public interface onCloseImageclick {
        void onClose();
    }


}
