package com.versatilemobitech.bhattivikramarka.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.versatilemobitech.bhattivikramarka.activities.HomeActivity;
import com.versatilemobitech.bhattivikramarka.R;


/**
 * Created by Excentd11 on 4/3/2017.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private View view;
   // private ImageView mImgProfile, mImgGallery, mImgVideos, mImgDaily, mImgComplaint, mImgContactus;
    private TextView mTxtProfile, mTxtGallery, mTxtVideos, mTxtDaily, mTxtComplaint, mTxtContactus;
    private Bundle mBundle;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        ((HomeActivity) getActivity()).mTxtTitle.setText(R.string.home);
        ((HomeActivity) getActivity()).mImgMenu.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).mImgBack.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).mImgSettings.setVisibility(View.VISIBLE);

        initComponents();
        return view;
    }

    private void initComponents() {
        setReferences();
        setClickListeners();
        getBundleData();
    }

    private void setReferences() {
        mTxtProfile = (TextView) view.findViewById(R.id.txtProfile);
        mTxtGallery = (TextView) view.findViewById(R.id.txtGallery);
        mTxtVideos = (TextView) view.findViewById(R.id.txtVideos);
        mTxtDaily = (TextView) view.findViewById(R.id.txtDailyActivities);
        mTxtComplaint = (TextView) view.findViewById(R.id.txtComplaint);
        mTxtContactus = (TextView) view.findViewById(R.id.txtContactus);
    }

    private void setClickListeners() {
        mTxtProfile.setOnClickListener(this);
        mTxtGallery.setOnClickListener(this);
        mTxtVideos.setOnClickListener(this);
        mTxtDaily.setOnClickListener(this);
        mTxtComplaint.setOnClickListener(this);
        mTxtContactus.setOnClickListener(this);
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtProfile:
                mBundle = new Bundle();
                navigateFragment(ProfileFragment.newInstance(), "PROFILEFRAGMENT", mBundle, getActivity());
                break;
            case R.id.txtGallery:
                mBundle = new Bundle();
                navigateFragment(GalleryFragment.newInstance(), "GALLERYFRAGMENT", mBundle, getActivity());
                break;
            case R.id.txtVideos:
                mBundle = new Bundle();
                navigateFragment(VideosFragment.newInstance(), "VIDEOSFRAGMENT", mBundle, getActivity());
                break;
            case R.id.txtDailyActivities:
                mBundle = new Bundle();
                mBundle.putString("from", "0");
                navigateFragment(DailyActivitiesFragment.newInstance(), "SOCIALSERVICEFRAGMENT", mBundle, getActivity());
                break;
            case R.id.txtComplaint:
                mBundle = new Bundle();
                navigateFragment(ComplaintBoxFragment.newInstance(), "NEEDHELPFRAGMENT", mBundle, getActivity());
                break;
            case R.id.txtContactus:
                mBundle = new Bundle();
               // navigateFragment(AudioSongsFragment.newInstance(), "AUDIOSONGSFRAGMENT", mBundle, getActivity());
                navigateFragment(ContactUsFragment.newInstance(), "CONTACTUSFRAGMENT", mBundle, getActivity());
                break;
            default:
                break;
        }
    }
}
