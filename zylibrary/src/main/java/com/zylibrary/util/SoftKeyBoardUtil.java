package com.zylibrary.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zylibrary.ZYLibrary;

/**
 * Created by zhaoya on 2016/9/27.
 * 软键盘
 */
public class SoftKeyBoardUtil {
    /** 弹出软键盘 */
    public static final void show(EditText editText) {
        if (editText != null) {
            editText.requestFocus();
            ((InputMethodManager) ZYLibrary.app().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
        }
    }

    /** 隐藏软键盘 */
    public static final void hind(View view) {
        if (view != null) {
            ((InputMethodManager) ZYLibrary.app().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
