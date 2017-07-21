package com.coolweather.android;

import android.app.Application;

import org.litepal.LitePalApplication;
import org.xutils.x;

/**
 * Created by zhaoya on 2017/2/21.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(this);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
