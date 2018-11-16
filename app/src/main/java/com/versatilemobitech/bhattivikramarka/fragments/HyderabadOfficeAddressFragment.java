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
import com.versatilemobitech.bhattivikramarka.adapters.TabsAdapter;


public class HyderabadOfficeAddressFragment extends BaseFragment implements View.OnClickListener {
    private View view;

    LinearLayout mLlMaps,mLlPhone,mLlMail;
    TabsAdapter mAdapter;
    public HyderabadOfficeAddressFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hyderabadoffice_address, container, false);

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

        mLlMaps=view.findViewById(R.id.llMaps);
        mLlPhone=view.findViewById(R.id.llPhone);
        mLlMail=view.findViewById(R.id.llMail);


    }
    private void setClickListeners() {

       mLlPhone.setOnClickListener(this);
       mLlMaps.setOnClickListener(this);
        mLlMail.setOnClickListener(this);
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
                    callIntent.setData(Uri.parse("tel:" + "9948500123"));
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
              /*  Uri maps = Uri.parse("geo:17.410142,78.48612100000003");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, maps);
                startActivity(mapIntent);*/
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<17.4227055>,<78.4262599>?q=<17.4227055>,<78.4262599>()"));
                startActivity(intent);

                break;
           case R.id.llMail:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "bhattivikramarkamallu@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Chooser Email"));
                break;
            /* case R.id.txtWebsite1:
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kavithakalvakuntla.org"));
                startActivity(websiteIntent);
                break;
            case R.id.txtWebsite2:
                Intent websiteIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telanganajagruthi.org"));
                startActivity(websiteIntent2);
                break;*/
            default:
                break;
        }
    }
}
