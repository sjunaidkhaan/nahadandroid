package com.ingentive.presidentsinfo.common;

import android.app.Application;
import android.content.res.Configuration;

import com.activeandroid.ActiveAndroid;
import com.ingentive.presidentsinfo.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by PC on 07-06-2016.
 */
//@ReportsCrashes(formKey = "",
//        formUri = "http://yourbrand.pk/yourbrand/junaid_khan/nahadcrashes/crashscript.php",
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.crash_toast_text)
public class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        //ACRA.init(this);
    }
}