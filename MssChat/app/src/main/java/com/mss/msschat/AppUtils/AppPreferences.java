package com.mss.msschat.AppUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class AppPreferences {
    SharedPreferences pref;
    Editor editor;
    public static Context mContext;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "stepcounts_prefers";
    private static final AppPreferences ourInstance = new AppPreferences();


    public AppPreferences() {

    }

    @SuppressLint("CommitPrefEdits")
    public AppPreferences(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public static void init(Context context) {
        AppPreferences.mContext = context;

    }

    public static AppPreferences getInstance() {
        return ourInstance;
    }


    public String getPrefrenceString(String keyName) {
        return pref.getString(keyName, "");
    }

    public void setPrefrenceString(String keyName, String stringValue) {
        editor.putString(keyName, stringValue);
        editor.commit();
    }

    public void setPrefrenceBoolean(String keyName, Boolean booleanValue) {
        editor.putBoolean(keyName, booleanValue);
        editor.commit();
    }


    public void setPreferenceBoolean(String key, Boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getPreferenceBoolean(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getBoolean(key, true);
    }

    public static String getTokenString(String keyName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AppPreferences.mContext);
        return preferences.getString(keyName, "");
    }

    public static void setTokenString(String keyName, String stringValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AppPreferences.mContext);
        Editor editor = preferences.edit();
        editor.putString(keyName, stringValue);
        editor.commit();
    }

    public static void setPreferenceRelaod(String key, String value) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AppPreferences.mContext);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPreferenceRelaod(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AppPreferences.mContext);
        return preferences.getString(key, "");
    }


    public boolean getPrefrenceBoolean(String keyName) {
        return pref.getBoolean(keyName, false);
    }


    public int getPrefrenceInt(String keyName) {
        return pref.getInt(keyName, 0);
    }

    public void setPrefrenceInt(String keyName, int intValue) {
        editor.putInt(keyName, intValue);
        editor.commit();
    }

}
