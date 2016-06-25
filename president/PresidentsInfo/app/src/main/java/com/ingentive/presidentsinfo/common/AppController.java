package com.ingentive.presidentsinfo.common;

import android.app.Application;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ingentive.presidentsinfo.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by PC on 30-05-2016.
 */
@ReportsCrashes(formKey = "",
        formUri = "url here",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class AppController  extends Application{

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //ACRA.init(this);
        ActiveAndroid.initialize(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
