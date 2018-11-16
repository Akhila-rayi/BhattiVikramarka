package com.versatilemobitech.bhattivikramarka.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CHITTURI on 07-Oct-16.
 */

public class UserDetails {
    public static final String PREF_NAME = "GyanaYogi";
    private static UserDetails instance;
    private SharedPreferences sh;
    private SharedPreferences.Editor edit;

    private UserDetails(Context mContext) {
        sh = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        edit = sh.edit();
    }

    public static synchronized UserDetails getInstance(Context mContext) {
        if (instance == null) {
            instance = new UserDetails(mContext);
        }
        return instance;
    }

    public void clear() {
        edit.clear().commit();
    }

    public void setLanguage(String language) {
        edit.putString("language", language).commit();
    }

    public void setDeviceToken(String deviceToken) {
        edit.putString("device_token", deviceToken).commit();
    }

    public void setPushPageNo(String pushPageNo) {
        edit.putString("push_pageNo", pushPageNo).commit();
    }


    public String getLanguage() {
        return sh.getString("language", "");
    }

    public String getDeviceToken() {
        return sh.getString("device_token", "");
    }

    public String getPushPageNo() {
        return sh.getString("push_pageNo", "");
    }
}
