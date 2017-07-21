package com.photowalldemo.utils;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;

import com.photowalldemo.MyApplication;
import com.zylibrary.BaseApplication;

/**
 * 后台加载图片
 */
public class ImageLoader extends AsyncTask<Integer, Void, Bitmap>{

    private LruCache<String, Bitmap> mMemoryCache; // 图片缓存技术的核心类,用于缓存所有下载好的图片,在程序内存达到设定值时会将最少最近使用的图片移除掉

    private static ImageLoader mImageLoader;
    private ImageLoader() {
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 使用最大可用内存值1/8作为缓存的大小
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小,默认返回图片数量
                return  bitmap.getByteCount();
//                return super.sizeOf(key, value);
            }
        };
    }

    /** 单例模式获取ImageLoader的实例 */
    public static ImageLoader getInstance() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader();
        }
        return mImageLoader;
    }

    // 在后台加载图片
    @Override
    protected Bitmap doInBackground(Integer... params) {
        final Bitmap bitmap = decodeSampledBitMapFromResource(MyApplication.getAppInstance().getResources(), params[0], 100, 100);
        addBitmapToMemoryCache(String.valueOf(params[0]), bitmap); // 把新加载的图片的键值对放到缓存中
        return bitmap;
    }

    /** 根据传入的宽和高,计算出合适的inSampleSize值 */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float)height / (float) reqHeight);
            final int widthRatio = Math.round((float)width / (float) reqWidth);
            // 选择宽和高中最小比率作为inSampleSize的值, 这样可以保证最终图片的宽和高一定都会大于等于目标宽和高
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return  inSampleSize;
    }

    /** 根据传入的宽和高,计算出合适的inSampleSize值 */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
        // 源图片宽度
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = widthRatio;
        }
        return  inSampleSize;
    }

    /** 得到合适的压缩后的图片 */
    public static Bitmap decodeSampledBitMapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true,来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false; // 关闭inJustDecodeBounds
        return BitmapFactory.decodeResource(res, resId, options); // 使用获取到的inSampleSize值再次解析图片并返回结果
    }

    /** 得到合适的压缩后的图片 */
    public static Bitmap decodeSampledBitMapFromFile(String pathName, int reqWidth) {
        // 第一次解析将inJustDecodeBounds设置为true,来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        options.inJustDecodeBounds = false; // 关闭inJustDecodeBounds
        return BitmapFactory.decodeFile(pathName, options); // 使用获取到的inSampleSize值再次解析图片并返回结果
    }

    /**
     *  将一张图片存储到LruCache中。
     * @param key   LruCache的键，这里传入图片的URL地址。
     * @param bitmap    LruCache的值，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     * @param key LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return  mMemoryCache.get(key);
    }

}
