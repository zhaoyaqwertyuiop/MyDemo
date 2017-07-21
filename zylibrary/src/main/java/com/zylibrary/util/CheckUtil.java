package com.zylibrary.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.zylibrary.BaseApplication;

/**
 * Created by zhaoya on 2016/10/25.
 * 检测文本
 */
public class CheckUtil {

    private boolean result = true;

    public static final String REGEX_NUMBER = "^[0-9]+$";
    public static final String REGEX_MOBILE = "^1[0-9]{10}$"; // 手机号
    public static final String REGEX_MONEY = "^[0-9]+(\\.[0-9]{1,2})?$";
    public static final String REGEX_ID_CARD = "^[1-9]\\d{5}((19)|(20))\\d{2}((0[1-9])|(1[0-2]))(0[1-9]|[1-2]\\d|3[01])\\d{3}[0-9xX]$";
    public static final String REGEX_EMAIL = "([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    /** 检测空 */
    public CheckUtil checkEmpty(String str, String msg) {
        if (!result) {
            return this;
        }
        if (TextUtils.isEmpty(str)) {
            showMessage(msg);
            result = false;
        }
        return this;
    }

    /** 检测正则 */
    public CheckUtil checkRegex(String str, String regex, String msg) {
        if (!result) {
            return this;
        }
        if (!str.matches(regex)) {
            showMessage(msg);
            result = false;
        }
        return this;
    }

    /** 检测boolean */
    public CheckUtil checkBoolean(boolean bool, String msg) {
        if (!result) {
            return this;
        }
        if (!bool) {
            showMessage(msg);
            result = false;
        }
        return this;
    }

    public boolean getResult(){
        return result;
    }

    private void showMessage(String msg) {
        Toast.makeText(BaseApplication.getAppInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
