package com.zy.common.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;

/**
 * @Author zhaoya
 * @Date 2025/1/6 14:32
 * @describe
 */
public enum ApkUploadUtil {
    o;
    private final String TAG = this.getClass().getSimpleName();
    private AppDownLoader downLoader;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String apkPath = null;
    private String downloadUrl;
    private String md5;
    private ProgressDialog mUploadDialog;

    public boolean download(Context context, String url, String md5, int versionCode) {
        this.downloadUrl = url;
        this.md5 = md5;
        if (TextUtils.isEmpty(downloadUrl) || TextUtils.isEmpty(md5)) {
            LogUtils.e(TAG, "downloadUrl=" + downloadUrl + ", md5=" + md5);
            return false;
        }
        if (versionCode <= AppUtils.getAppVersionCode()) {
            LogUtils.d(TAG, "versionCode=" + versionCode + ", currentVersionCode=" + AppUtils.getAppVersionCode());
            return false;
        }
        if (downLoader != null) {
            LogUtils.e(TAG, "downLoader != null");
            return false;
        }
        showDialog(context);
        this.apkPath = new File(context.getExternalCacheDir(),
                "/tempApk/" + md5 + ".apk").getPath();
        File root = new File(apkPath).getParentFile();
        if (root.exists()) {
            File[] list = root.listFiles();
            if (list != null) {
                for (File f : list) {
                    f.delete();
                }
            }
        }
        LogUtils.d(TAG, "save apk:" + this.apkPath);
        LogUtils.d(TAG, "downloadUrl:" + this.downloadUrl);
        downLoader = new AppDownLoader(this.downloadUrl, this.apkPath, getDownLoaderListener(context));
        downLoader.start();
        return true;
    }

    private void showDialog(Context context) {
        mUploadDialog = new ProgressDialog(context);
        mUploadDialog.setCanceledOnTouchOutside(false);
        mUploadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mUploadDialog.setMax(100);
        mUploadDialog.show();

        mUploadDialog.setOnCancelListener(dialog -> {
            downLoader.stop();
            downLoader = null;
        });
    }

    private AppDownLoader.OnResultCallBack getDownLoaderListener(Context context) {
        return new AppDownLoader.OnResultCallBack() {
            private int progress = 0;

            @Override
            public boolean checkDisk(long contentLength) {
                if (contentLength <= 0) {//有些下载服务器不会传递contentLength过来
                    contentLength = 20 * 1024 * 1024;//最少留够20M
                }

                long length = getAvailableSpace(getExternalStorageDirectory());
                LogUtils.d(TAG, "当前剩余磁盘空间:" + length + ",下载文件大小:" + contentLength);
                return length > contentLength;
            }

            @Override
            public void progress(final int progress) {
                if (this.progress != progress) {
                    Log.d(TAG, "progress:" + progress);
                    this.progress = progress;

                    handler.post(() -> {
                        mUploadDialog.setProgress(progress);
                    });
                }
            }

            @Override
            public void result(int state, final String savePath) {
                if (state == AppDownLoader.STATE_SUCCEED && mUploadDialog.isShowing()) {
                    if (!TextUtils.isEmpty(md5)) {
                        String fileMd5 = EncryptUtils.encryptMD5File2String(savePath);//本地文件md5
                        LogUtils.d(TAG, "md5=" + md5 + ",fileMd5=" + fileMd5);
                        if (!md5.equalsIgnoreCase(fileMd5)) {
                            ToastUtils.showLong("文件MD5不一致,请稍后重试~");
                            mUploadDialog.dismiss();
                            return;
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mUploadDialog.dismiss();
                            installApk(savePath);
                        }
                    });
                } else {
                    switch (state) {
                        case AppDownLoader.STATE_ERROR_NETWORK:
                            ToastUtils.showLong("网络异常,请稍后重试!");
                            break;
                        case AppDownLoader.STATE_ERROR_DISK_LESS:
                            ToastUtils.showLong("磁盘空间不足!");
                            break;
                        case AppDownLoader.STATE_ERROR_NOT_DATA:
                            ToastUtils.showLong("下载文件为空!");
                            break;
                    }
                }
            }
        };
    }

    private String getExternalStorageDirectory() {
        try {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } catch (Throwable var1) {
            return (new File("/sdcard/")).getAbsolutePath();
        }
    }

    private void installApk(String path) {
        LogUtils.d(TAG, "install = " + path);
        AppUtils.installApp(path);
    }

    private long getAvailableSpace(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0L;
        } else {
            StatFs stat = new StatFs(path);
            int blockSize = stat.getBlockSize();
            int availableBlocks = stat.getAvailableBlocks();
            return (long) blockSize * ((long) availableBlocks - 4L);
        }
    }
}
