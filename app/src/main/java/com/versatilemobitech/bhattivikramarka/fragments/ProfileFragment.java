package com.versatilemobitech.bhattivikramarka.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.versatilemobitech.bhattivikramarka.activities.HomeActivity;
import com.versatilemobitech.bhattivikramarka.R;


/**
 * Created by Excentd11 on 10/17/2017.
 */

public class ProfileFragment extends BaseFragment {
    private View view;
    private TextView mTxtAbout;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        ((HomeActivity) getActivity()).mTxtTitle.setText(R.string.profile);
        ((HomeActivity) getActivity()).mImgMenu.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).mImgBack.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).mImgSettings.setVisibility(View.INVISIBLE);

        initComponents();
        return view;
    }

    private void initComponents() {
        setReferences();
        getBundleData();
    }

    private void setReferences() {
        mTxtAbout = (TextView) view.findViewById(R.id.txtAbout);
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
    }
}
