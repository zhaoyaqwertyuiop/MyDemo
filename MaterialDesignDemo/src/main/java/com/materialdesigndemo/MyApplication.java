package com.materialdesigndemo;

import android.app.Application;

/**
 * Created by zhaoya on 2017/1/11.
 */

public class MyApplication extends Application {

    private static MyApplication appInstance;

    public static final MyApplication getAppInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }
}
