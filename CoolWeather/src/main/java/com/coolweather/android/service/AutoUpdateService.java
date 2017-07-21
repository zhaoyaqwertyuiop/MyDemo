package com.coolweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.Config;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;
import com.zylibrary.util.SharedPreferenceUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE); // 定时任务
        int anHour = 8 * 60 * 60 * 1000; // 8小时
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /** 更新天气信息 */
    private void updateWeather() {
        String weatherString = SharedPreferenceUtil.getInstence().getValue("weather", "");
        if (TextUtils.isEmpty(weatherString)) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handlerWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = Config.WEATHER.replace("$", weatherId);
            HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responceText = response.body().string();
                    Weather weather = Utility.handlerWeatherResponse(responceText);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferenceUtil.getInstence().saveValue("weather", responceText);
                    }
                }
            });
        }
    }

    /** 更新必应每日一图 */
    private void updateBingPic() {
        String requestBingPic = Config.BING_PIC;
        HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferenceUtil.getInstence().saveValue("bing_pic", bingPic);
            }
        });
    }
}
