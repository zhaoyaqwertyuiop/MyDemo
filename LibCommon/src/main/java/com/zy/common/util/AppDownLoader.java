package com.zy.common.util;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppDownLoader implements Runnable {
    public interface OnResultCallBack {
        //当前下载文件大小
        boolean checkDisk(long contentLength);

        void progress(int progress);

        void result(int state, String savePath);
    }

    //成功
    public static final int STATE_SUCCEED = 0;
    //没有下载到数据
    public static final int STATE_ERROR_NOT_DATA = -101;
    public static final int STATE_ERROR_NETWORK = -102;
    public static final int STATE_ERROR_DISK_LESS = -103;
    private String savePath;
    @NonNull
    private OnResultCallBack callBack;

    public AppDownLoader(String downloadUrl, String savePath, @NonNull OnResultCallBack callBack) {
        this.downloadUrl = downloadUrl;
        this.savePath = savePath;
        this.callBack = callBack;
    }

    private boolean isRun = false;

    public synchronized void start() {
        if (isRun)
            return;
        isRun = true;
        new Thread(this).start();
    }

    public synchronized void stop() {
        isRun = false;
    }

    private String downloadUrl;

    @Override
    public void run() {
        FileOutputStream out = null;
        int state = STATE_ERROR_NETWORK;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(this.downloadUrl).openConnection();
            byte buf[] = new byte[1024 * 8];
            connection.connect();
            connection.setConnectTimeout(10*1000);
            if (connection.getResponseCode() == 200) {
                long contentLength = connection.getContentLength();
                if (!callBack.checkDisk(contentLength)) {
                    //磁盘空间不足
                    state = STATE_ERROR_DISK_LESS;
                    return;
                }
                InputStream in = connection.getInputStream();
                File file = new File(savePath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                out = new FileOutputStream(savePath + ".temp");
                int i = 0;
                long count = 0;
                while (isRun && (i = in.read(buf)) != -1) {
                    count += i;
                    out.write(buf, 0, i);
                    if (contentLength > 0) {
                        callBack.progress((int) ((1.0f * count / contentLength) * 100));
                    }

                }
                new File(savePath + ".temp").renameTo(new File(savePath));
                out.close();
                state = count > 0 ? STATE_SUCCEED :
                        STATE_ERROR_NOT_DATA;
            }
        } catch (Exception e) {
            e.printStackTrace();
            state = STATE_ERROR_NETWORK;//网络异常
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isRun) {
                callBack.result(state, savePath);
            }
            isRun = false;
        }
    }
}
