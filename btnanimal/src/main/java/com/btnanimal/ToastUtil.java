package com.btnanimal;

import android.widget.Toast;

import org.xutils.x;

/**
 * Created by zhaoya on 2016/12/22.
 */
public class ToastUtil {
    private static Toast toast;

    public static final void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(x.app(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

}
