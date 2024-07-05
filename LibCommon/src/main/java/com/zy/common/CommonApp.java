package com.zy.common;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhaoya
 * @Date 2024/1/22 10:13
 * @describe
 */
public class CommonApp {
    private final static String TAG = "CommonApp";
    private static Application application;
    private static boolean isInit = false;
    private static final List<InitCallback> initCallbackList = new ArrayList<>();

    public static void init(Application application) {
        LogUtils.d(TAG, "init()");
        long start = System.currentTimeMillis();
        CommonApp.application = application;
        initOthers();
    }

    public static Context getApp() {
        return application;
    }

    private static void initOthers(){
        isInit = true;
        for (InitCallback initCallback: initCallbackList) {
            LogUtils.d(TAG, "initOthers():" + initCallback);
            initCallback.init();
        }
    }

    public static interface InitCallback {
        void init();
    }

    public static void afterInit(InitCallback initCallback) {
        if (isInit) {
            LogUtils.d(TAG, "afterInit():" + initCallback);
            initCallback.init();
        } else {
            initCallbackList.add(initCallback);
        }
    }
}
