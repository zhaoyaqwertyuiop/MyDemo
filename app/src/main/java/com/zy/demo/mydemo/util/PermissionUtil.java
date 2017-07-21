package com.zy.demo.mydemo.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoya on 2016/10/17.
 * 权限
 */
public class PermissionUtil {
    public static final int REQUESTCODE_PREM = 1001;

    protected Callback callback;

    public static final boolean requestPermission(final Activity context, String... permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        final List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有权限
                permissionList.add(permission);
            }
        }
        if (permissionList.size() > 0) {
            ActivityCompat.requestPermissions(context, permissionList.toArray(new String[permissionList.size()]), REQUESTCODE_PREM);
            return false;
        }
        return true;
    }

    public interface Callback {
        public void onPermissionRequestFinish();
    }
}
