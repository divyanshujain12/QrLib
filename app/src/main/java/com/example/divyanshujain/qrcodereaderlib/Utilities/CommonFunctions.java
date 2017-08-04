package com.example.divyanshujain.qrcodereaderlib.Utilities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by divyanshu.jain on 8/4/2017.
 */
public class CommonFunctions {
    private static CommonFunctions ourInstance = new CommonFunctions();

    public static CommonFunctions getInstance() {
        return ourInstance;
    }

    private CommonFunctions() {
    }

    public void configureToolbarWithOutBackButton(final AppCompatActivity appCompatActivity, Toolbar toolbar, String name) {
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setTitle(name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appCompatActivity.onBackPressed();
            }
        });
    }
}
