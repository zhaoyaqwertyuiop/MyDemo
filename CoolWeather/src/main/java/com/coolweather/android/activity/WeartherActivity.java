package com.coolweather.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coolweather.android.R;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.service.AutoUpdateService;
import com.coolweather.android.util.Config;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.ToastUtil;
import com.coolweather.android.util.Utility;
import com.zylibrary.util.SharedPreferenceUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeartherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText, pm25Text, comfortText, carwashText, sportText;

    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefresh;

    public DrawerLayout drawerLayout;
    private ImageButton navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearther);

        weatherLayout = (ScrollView) findViewById(R.id.wearther_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carwashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);
        navButton = (ImageButton) findViewById(R.id.nav_button);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary); // 设置圈圈颜色

        String weatherString = SharedPreferenceUtil.getInstence().getValue("weather", ""); // 缓存的天气
        String bingPic = SharedPreferenceUtil.getInstence().getValue("bingPic", ""); // 缓存的图片

        final String weatherId;

        if (!TextUtils.isEmpty(weatherString)) { // 有缓存时直接解析天气数据
            Weather weather = Utility.handlerWeatherResponse(weatherString);
            weatherId  = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else { // 无缓存时去服务器查询天气
            weatherId = getIntent().getStringExtra("weatherId");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        if (!TextUtils.isEmpty(bingPic)) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }

        // 占用状态栏背景
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    /** 加载必应每日一图 */
    private void loadBingPic() {
        String requestBingPic = Config.BING_PIC;
        HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferenceUtil.getInstence().saveValue("bing_pic", bingPic);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeartherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    public static final void startActivity(Activity activity, String weatherId) {
        Intent intent = new Intent(activity, WeartherActivity.class);
        intent.putExtra("weatherId", weatherId);
        activity.startActivity(intent);
    }

    /** 处理并展示Weather实体类中的数据 */
    private void showWeatherInfo(Weather weather) {
        if (weather != null && "ok".equals(weather.status)) {
            String cityName = weather.basic.cityName;
            String updateTime = weather.basic.update.updateTime.split(" ")[1];
            String degress = weather.now.temperature + "℃";
            String weatherInfo = weather.now.more.info;

            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degress);
            weatherInfoText.setText(weatherInfo);

            forecastLayout.removeAllViews();
            for (Forecast forecast : weather.forecastList) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
                TextView dateText = (TextView) view.findViewById(R.id.date_text);
                TextView infoText = (TextView) view.findViewById(R.id.info_text);
                TextView maxText = (TextView) view.findViewById(R.id.max_text);
                TextView minText = (TextView) view.findViewById(R.id.min_text);

                dateText.setText(forecast.date);
                infoText.setText(forecast.more.info);
                maxText.setText(forecast.temperature.max);
                minText.setText(forecast.temperature.min);

                forecastLayout.addView(view);
            }
            if (weather.aqi != null) {
                aqiText.setText(weather.aqi.city.aqi);
                pm25Text.setText(weather.aqi.city.pm25);
            }
            String comfort = "舒适度:" + weather.suggestion.comfort.info;
            String carWash = "洗车指数:" + weather.suggestion.carWash.info;
            String sport = "运动建议:" + weather.suggestion.sport.info;

            comfortText.setText(comfort);
            carwashText.setText(carWash);
            sportText.setText(sport);

            weatherLayout.setVisibility(View.VISIBLE);

            // 开启服务,每隔8小时刷新天气
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        } else {
            ToastUtil.show("获取天气信息失败");
        }
    }

    /** 根据天气id请求城市天气信息 */
    public void requestWeather(final String weatherId) {
        String weatherUrl = Config.WEATHER.replace("$", weatherId);

        HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showLong("获取天气信息失败:" + e.toString());
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handlerWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferenceUtil.getInstence().saveValue("weather", responseText);
                            showWeatherInfo(weather);
                        } else {
                            ToastUtil.showLong("获取天气信息失败");
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        loadBingPic();
    }

}
