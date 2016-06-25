package com.ingentive.presidentsinfo.common;

import android.app.Application;
import android.content.res.Configuration;

import com.activeandroid.ActiveAndroid;

/**
 * Created by PC on 07-06-2016.
 */
public class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}