package com.versatilemobitech.bhattivikramarka.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.versatilemobitech.bhattivikramarka.fragments.HyderabadOfficeAddressFragment;
import com.versatilemobitech.bhattivikramarka.fragments.KhammamOfficeAddressFragment;



public class TabsAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    //Constructor to the class
    public TabsAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                KhammamOfficeAddressFragment khammamOfficeAddressFragment = new KhammamOfficeAddressFragment();
                return khammamOfficeAddressFragment;
            case 1:
                HyderabadOfficeAddressFragment hyderabadOfficeAddressFragment = new HyderabadOfficeAddressFragment();
                return hyderabadOfficeAddressFragment;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}