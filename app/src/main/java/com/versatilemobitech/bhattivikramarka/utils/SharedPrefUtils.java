package com.versatilemobitech.bhattivikramarka.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Excentd12 on 7/01/2017.
 */

public class SharedPrefUtils {

    public static void setSharedPrefStringData(Context context, String key, String value) {
        try {
            if (context != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("Kavithak_Pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
                sharedPreferenceEditor.putString(key, value);
                sharedPreferenceEditor.apply();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSharedPreference(Context context, String key) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Kavithak_Pref", Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isValueNullOrEmpty(String value) {
        boolean isValue = false;
        if (value == null || value.equals(null) || value.equals("")
                || value.equals("null") || value.trim().length() == 0) {
            isValue = true;
        }
        return isValue;
    }
}
