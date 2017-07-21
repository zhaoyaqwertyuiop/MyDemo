package com.photowalldemo;

import android.os.Bundle;
import android.widget.GridView;

import com.photowalldemo.adapter.PhotoWallAdapter;
import com.zylibrary.BaseActivity;

public class MainActivity extends BaseActivity {

    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mPhotoWall = (GridView) super.findViewById(R.id.photoWallGV);
        this.adapter = new PhotoWallAdapter(this, 0, Images.imageThumbUrls, mPhotoWall);
        mPhotoWall.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务
        adapter.cancleAllTasks();
    }
}
