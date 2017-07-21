package com.coolweather.android.util;

import android.widget.Toast;

import org.xutils.x;

/**
 * Created by zhaoya on 2016/12/23.
 */
public class ToastUtil {
    private static Toast toast;

    public static final void show(String msg) {
        if (toast == null) {
            toast = Toast.makeText(x.app(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static final void showLong(String msg) {
        if (toast == null) {
            toast = Toast.makeText(x.app(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }
}
