package com.versatilemobitech.bhattivikramarka.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.versatilemobitech.bhattivikramarka.activities.HomeActivity;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.adapters.TabsAdapter;


public class ContactUsFragment extends BaseFragment implements View.OnClickListener,TabLayout.OnTabSelectedListener{
    private View view;

  /*  TextView mTxtPhoneno,mTxtMobile,mTxtMap2;
    LinearLayout mLlMaps,mLlMail,mLlPhone;*/
  private TabLayout tabLayout;
    private ViewPager viewPager;

    public static ContactUsFragment newInstance() {
        ContactUsFragment fragment = new ContactUsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);

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
        setTabs();
        getBundleData();
    }

    private void setReferences() {
      /*  mTxtMap2=(TextView) view.findViewById(R.id.txtAddress2);
        mTxtPhoneno=(TextView) view.findViewById(R.id.txtPhone);
        mTxtMobile=(TextView)view.findViewById(R.id.txtMobile);
        mLlMaps=(LinearLayout)view.findViewById(R.id.llMaps);
        mLlMail=(LinearLayout)view.findViewById(R.id.llMail);
        mLlPhone=(LinearLayout)view.findViewById(R.id.llPhone);*/
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);

    }

    private void setClickListeners() {
       /* mLlMaps.setOnClickListener(this);
        mLlMail.setOnClickListener(this);
        mTxtPhoneno.setOnClickListener(this);
        mTxtMobile.setOnClickListener(this);
        mLlPhone.setOnClickListener(this);
        mTxtMap2.setOnClickListener(this);*/


    }
    private void setTabs() {
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Khammam Office Address"));
        tabLayout.addTab(tabLayout.newTab().setText("Hyderabad Office Address"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Creating our pager adapter
        TabsAdapter adapter = new TabsAdapter(getFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
    }
    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*case R.id.llPhone:
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
            case R.id.txtPhone:
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
            case R.id.txtMobile:
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
               *//* Uri maps = Uri.parse("geo:17.410142,78.48612100000003");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, maps);

                startActivity(mapIntent);*//*
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<17.152065>,<80.339990>?q=<17.152065>,<80.339990>(Madhira)"));
                startActivity(intent);

                break;
            case R.id.txtAddress2:
               *//* Uri maps = Uri.parse("geo:17.410142,78.48612100000003");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, maps);

                startActivity(mapIntent);*//*
                Intent address = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<17.152065>,<80.339990>?q=<17.152065>,<80.339990>(Madhira)"));
                startActivity(address);

                break;
            case R.id.llMail:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "bv_mallu@yahoo.com"));
                startActivity(Intent.createChooser(emailIntent, "Chooser Email"));
                break;*/
            default:
                break;
        }

    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
