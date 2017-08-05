package com.example.divyanshujain.qrcodereaderlib.Utilities;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Lenovo on 08-09-2015.
 */
public class MySharedPereference {

    public static MySharedPereference instance;
    public static final String APP_PREFERENCE = "QR_Lib_App";
    private MySharedPereference() {
    }

    public static MySharedPereference getInstance() {
        if (instance == null) {
            instance = new MySharedPereference();
        }
        return instance;
    }

    public void setString(Context context, String Key, String Value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, Value);
        editor.commit();
    }


    public String getString(Context context, String Key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        String requestToken = sharedPreferences.getString(Key, "");
        return requestToken;
    }

    public void setBoolean(Context context, String Key, boolean Value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Key, Value);
        editor.commit();
    }


    public boolean getBoolean(Context context, String Key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        boolean requestToken = sharedPreferences.getBoolean(Key, false);
        return requestToken;
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}