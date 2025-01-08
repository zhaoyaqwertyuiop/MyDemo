package com.zy.common.route

import android.content.ComponentName
import android.content.Intent
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils

object RouteUtil {
    fun startActivity(cls: String, pkg: String = AppUtils.getAppPackageName(), intent: Intent ?= null) {
        try {
            Utils.getApp().startActivity(Intent().apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                component = ComponentName(pkg, cls)
                intent?.let { putExtras(it) }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showLong(e.message)
        }
    }
}