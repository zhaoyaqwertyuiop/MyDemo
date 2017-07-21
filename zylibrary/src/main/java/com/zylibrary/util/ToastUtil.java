package com.zylibrary.util;

import android.widget.Toast;

import com.zylibrary.ZYLibrary;

/**
 * Created by zhaoya on 2016/12/23.
 */
public class ToastUtil {
    private static Toast toast;

    public static final void show(String msg) {
        if (toast == null) {
            toast = Toast.makeText(ZYLibrary.app(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static final void showLong(String msg) {
        if (toast == null) {
            toast = Toast.makeText(ZYLibrary.app(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
