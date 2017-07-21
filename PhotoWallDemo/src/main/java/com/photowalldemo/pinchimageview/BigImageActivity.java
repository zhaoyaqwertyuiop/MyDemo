//package com.photowalldemo.pinchimageview;
//
//import android.graphics.Bitmap;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
//import com.photowalldemo.R;
//
//public class BigImageActivity extends AppCompatActivity implements View.OnClickListener{
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_big_image);
//
//        PinchImageView pinchImageView = (PinchImageView) super.findViewById(R.id.pic);
//        String imageUrl = getIntent().getStringExtra("url");
//        String memoryCacheKey = getIntent().getStringExtra("cache_key");
//
//
//        ImageViewAware thumbAware = new ImageViewAware(pinchImageView);
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        if (!imageLoader.isInited()) {
//            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
//        }
//        Bitmap bitmap = imageLoader.getMemoryCache().get(memoryCacheKey);
//        if (bitmap != null && !bitmap.isRecycled()) {
//            pinchImageView.setImageBitmap(bitmap);
//        }
//        imageLoader.displayImage(imageUrl, thumbAware, new DisplayImageOptions.Builder().cacheInMemory(true).build());
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.pic:
//                finishAfterTransition(); break;
//            default: break;
//        }
//    }
//}
