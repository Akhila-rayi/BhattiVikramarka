package com.versatilemobitech.bhattivikramarka.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.activities.HomeActivity;


public class KhammamOfficeAddressFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    TextView mTxtPhoneno,mTxtWebsite1,mTxtWebsite2,mTxtMail,mTxtMap;
    LinearLayout mLlMaps,mLlPhone,mLlMail,mLlWebsite;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_khammam_office_address, container, false);

        ((HomeActivity) getActivity()).mTxtTitle.setText(R.string.contactus);
        ((HomeActivity) getActivity()).mImgMenu.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).mImgBack.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).mImgSettings.setVisibility(View.INVISIBLE);

        initComponents();
        return view;
    }
    private void initComponents() {
        setReferences();
        setClickListeners();
        getBundleData();
    }

    private void setReferences() {
/*
        mTxtMap=(TextView) view.findViewById(R.id.txtAddress);
        mTxtMail=(TextView) view.findViewById(R.id.txtMail);
        mTxtPhoneno=(TextView)view.findViewById(R.id.txtPhone);
*/
        //mTxtWebsite1=(TextView)view.findViewById(R.id.txtWebsite1);


     mLlMaps=view.findViewById(R.id.llMaps);

     mLlPhone=view.findViewById(R.id.llPhone);
     mLlWebsite=view.findViewById(R.id.llWebsite);


    }
    private void setClickListeners() {/*
        mTxtMap.setOnClickListener(this);
        mTxtMail.setOnClickListener(this);
        mTxtPhoneno.setOnClickListener(this);*/
        //mTxtWebsite1.setOnClickListener(this);

    mLlMaps.setOnClickListener(this);
    mLlPhone.setOnClickListener(this);

    mLlWebsite.setOnClickListener(this);

    }
    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.llPhone:
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "08749272239"));
                    startActivity(callIntent);
                } catch (Exception e) {
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{android.Manifest.permission.CALL_PHONE}, 8);
                    }
                }
                break;
            case R.id.llMaps:
               /* Uri maps = Uri.parse("geo:17.410142,78.48612100000003");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, maps);

                startActivity(mapIntent);*/
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<17.152065>,<80.339990>?q=<17.152065>,<80.339990>(Madhira)"));
                startActivity(intent);

                break;


            case R.id.llWebsite:
                Intent website = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bhattivikramarkamallu.in"));
                startActivity(website);
                break;
            default:
                break;
        }
    }
}
