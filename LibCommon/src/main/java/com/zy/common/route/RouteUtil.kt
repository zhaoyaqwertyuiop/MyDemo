package com.zy.common.route

import android.content.ComponentName
import android.content.Intent
import com.blankj.utilcode.util.ToastUtils
import com.zy.common.CommonApp

object RouteUtil {
    fun startActivity(pkg: String, cls: String) {
        try {
            CommonApp.getApp().startActivity(Intent().apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                component = ComponentName(pkg, cls)
            })
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showLong(e.message)
        }
    }
}