package com.zy.demo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.multidex.MultiDex
import java.lang.reflect.Field


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        closeDetectedProblemApiDialog()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    /**
     * Android 9开始限制开发者调用非官方API方法和接口(即用反射直接调用源码)
     * 弹框提示 Detected problems with API compatibility(visit g.co/dev/appcompat for more info)
     *
     *
     * 隐藏警告弹框
     */
    @SuppressLint("SoonBlockedPrivateApi")
    private fun closeDetectedProblemApiDialog() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return
        }
        try {
            @SuppressLint("PrivateApi") val clsPkgParser =
                Class.forName("android.content.pm.PackageParser\$Package")
            val constructor = clsPkgParser.getDeclaredConstructor(
                String::class.java
            )
            constructor.isAccessible = true

            @SuppressLint("PrivateApi") val clsActivityThread =
                Class.forName("android.app.ActivityThread")
            val method = clsActivityThread.getDeclaredMethod("currentActivityThread")
            method.isAccessible = true
            val activityThread = method.invoke(null)
            val hiddenApiWarning: Field = clsActivityThread.getDeclaredField("mHiddenApiWarningShown")
            hiddenApiWarning.setAccessible(true)
            hiddenApiWarning.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}