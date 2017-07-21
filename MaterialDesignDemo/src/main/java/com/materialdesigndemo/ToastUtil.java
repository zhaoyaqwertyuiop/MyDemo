package com.materialdesigndemo;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by zhaoya on 2016/12/23.
 */
public class ToastUtil {
    private static Toast toast;
    private static Snackbar snackbar;

    public static final void show(String msg) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getAppInstance(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static final void showSanckbar(View view, String msg, String btn, View.OnClickListener clickListener) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                    .setAction(btn, clickListener);
        } else {
            snackbar.setText(msg).setAction(btn, clickListener);
        }
        snackbar.show();
    }
}
