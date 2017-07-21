package com.zylibrary.util;

import android.util.Log;

/**
 * Created by zhaoya on 2017/3/27.
 */

public class LogUtil {
    private static Boolean isLog = true;
    
    public static void init(Boolean isLog) {
        LogUtil.isLog = isLog;
    }
    
    public static final void d(String tag, String msg) {
        if (isLog) {
            Log.d(tag, msg);
        }
    }

    public static final void i(String tag, String msg) {
        if (isLog) {
            Log.i(tag, msg);
        }
    }

    public static final void e(String tag, String msg) {
        if (isLog) {
            Log.e(tag, msg);
        }
    }

    public static final void v(String tag, String msg) {
        if (isLog) {
            Log.v(tag, msg);
        }
    }

    public static final void w(String tag, String msg) {
        if (isLog) {
            Log.w(tag, msg);
        }
    }
}
