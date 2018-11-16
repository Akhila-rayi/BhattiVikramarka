package com.versatilemobitech.bhattivikramarka.fragments;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.versatilemobitech.bhattivikramarka.ReaderViewPagerTransformer;
import com.versatilemobitech.bhattivikramarka.activities.HomeActivity;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.adapters.SlidePagerAdapter;
import com.versatilemobitech.bhattivikramarka.models.Images;

import java.util.ArrayList;

public class SliderFragment extends BaseFragment implements SlidePagerAdapter.onCloseImageclick ,View.OnClickListener{
    private View view;
    private int position;
    private String folderName;
    private SlidePagerAdapter mCustomPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<Images> mfolderimageList;
    private ArrayList<String> mImagesList;
    private ImageView mImgClose;
    private boolean isItemClick;
    private int previousState;

    public static SliderFragment newInstance(String folderName, int postion, ArrayList<Images> mImagesList) {
        SliderFragment sliderFragment = new SliderFragment();
        Bundle args = new Bundle();
        args.putInt("position", postion);
        args.putString("folderName", folderName);
        args.putParcelableArrayList("IMAGESLIST", mImagesList);
        sliderFragment.setArguments(args);
        return sliderFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
        mfolderimageList = getArguments().getParcelableArrayList("IMAGESLIST");
        folderName = getArguments().getString("folderName");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_slider, container, false);

        // ((HomeActivity) getActivity()).mTxtTitle.setText(R.string.gallery);
        ((HomeActivity) getActivity()).mImgMenu.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).mImgBack.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).mImgSettings.setVisibility(View.INVISIBLE);

        initComponents();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImagesList = getImageUrls();
        mCustomPagerAdapter = new SlidePagerAdapter(getActivity(), this, mImagesList);

        mViewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.setPageTransformer(true, new ReaderViewPagerTransformer(ReaderViewPagerTransformer.TransformType.DEPTH));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                //((HomeActivity) getActivity()).textView.setText(ApplicationData.getInstance().photoGalleries.get(position).image_title);
                // if (!isItemClick) {
                // fancyCoverFlow.requestFocusFromTouch();
                // fancyCoverFlow.setSelection(position);
                mCustomPagerAdapter.onPageSelected(position);

//
//                        MotionEvent e1 = MotionEvent.obtain(
//                                SystemClock.uptimeMillis(),
//                                SystemClock.uptimeMillis(),
//                                MotionEvent.ACTION_DOWN,  265.33334f,89.333336f, 0);
//                        MotionEvent e2 = MotionEvent.obtain(
//                                SystemClock.uptimeMillis(),
//                                SystemClock.uptimeMillis(),
//                                MotionEvent.ACTION_UP,  238.00003f,300.0f, 0);
//
//                        fancyCoverFlow.onFling(e2, e1, -600, 0);


                //} else {
                isItemClick = false;
                // }

                // Log.e("", "fancyCoverFlow " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (previousState == ViewPager.SCROLL_STATE_DRAGGING
                        && state == ViewPager.SCROLL_STATE_SETTLING) {
//scroll changed here
                } else if (previousState == ViewPager.SCROLL_STATE_SETTLING
                        && state == ViewPager.SCROLL_STATE_IDLE) {

                }

                previousState = state;
            }
        });
    }

    private ArrayList<String> getImageUrls() {
        ArrayList<String> onlyImages = new ArrayList<>();
        for (int i = 0; i < mfolderimageList.size(); i++) {
            onlyImages.add(mfolderimageList.get(i).getImageUrl());

        }
        return onlyImages;
    }

    private void initComponents() {
        setReferences();
        setClickListeners();
        getBundleData();
    }

    private void setReferences() {
       // mImgClose=(ImageView)view.findViewById(R.id.imgClose);


    }

    private void setClickListeners() {
        //mImgClose.setOnClickListener(this);

    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            ((HomeActivity) getActivity()).mTxtTitle.setText(folderName);
        }
    }

    @Override
    public void onClose() {

        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
          /*  case R.id.imgClose:
              onClose();
                break;*/
            default:
                break;

        }
    }
}
