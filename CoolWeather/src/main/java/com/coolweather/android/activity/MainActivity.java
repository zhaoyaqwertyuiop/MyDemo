package com.coolweather.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.coolweather.android.R;
import com.zylibrary.util.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String weather = SharedPreferenceUtil.getInstence().getValue("weather", "");
        if (!TextUtils.isEmpty(weather)) { // 有缓存时直接进入到天气页面
            startActivity(new Intent(this, WeartherActivity.class));
            finish();
        }
    }
}
