package com.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        mTextView = (TextView) findViewById(R.id.test_textvew);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                EventBus.getDefault().post("refresh time");
//                EventBus.getDefault().post(new AnyEvent("refresh time"));
            }
        }, 0, 1000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1)
    public void onMessageEventMain(String msg) {
        Log.d(TAG, "onEventMainThread(), msg=" + msg);
        mTextView.setText(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()))); // yyyy-MM-dd HH:mm:ss
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
