package com.photowalldemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.photowalldemo.utils.ImageLoader;
import com.pinchimageview.BigImageUtil;
import com.pinchimageview.PagerActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhaoya on 2016/11/9.
 */
public class MyScrollView extends ScrollView implements View.OnTouchListener{
    private static final String TAG = MyScrollView.class.getSimpleName();

    public static final int PAGE_SIZE = 20; // 每页要加载的图片数量
    private int page; // 记录当前加载到几页
    private int columnWidth; // 每一列的宽度
    private int firstColumnHeight, secondColumnHeight, thirdColumnHeight; // 当前第1,2,3列的高度
    private boolean loadOnce; // 是否加载过一次layout, 这里onLayout中初始化只需要加载一次
    private ImageLoader imageLoader;
    private LinearLayout firstColumn, secondColumn, thirdColumn; // 第1,2,3列的布局
    private static Set<LoadImageTask> taskCollection; // 记录所有正在下载或等待下载的任务

    private static View scrollLayout; // MyScrollView下的直接子布局
    private static int scrollViewHeight; // MyScrollView布局的高度
    private static int lastScrollY = -1; // 记录垂直方向上的滚动距离

    private List<ImageView> imageViewList = new ArrayList<>(); // 记录所有界面上的图片, 可用以随时控制对图片的释放

    private int position = -1; // 用于记录position;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MyScrollView myScrollView = (MyScrollView) msg.obj;
            int scrollY = myScrollView.getScrollY();
            Log.d(TAG, "lastScrollY=" + lastScrollY + ", scrollY=" + scrollY);
            if (scrollY == lastScrollY) { // 如果当前的滚动位置和上次相同,表示已停止滚动
                // 当滚动到最底部,并且当前没有正在下载的任务时, 开始加载下一页的图片
                if (scrollViewHeight + scrollY >= scrollLayout.getHeight() && taskCollection.isEmpty()) {
                    myScrollView.loadMoreImages();
                }
                myScrollView.checkVisibility();
            } else {
                lastScrollY = scrollY;
                Message message = new Message();
                message.obj = myScrollView;
                handler.sendMessageDelayed(message, 5); // 5毫秒后再次对滚动位置进行判断
            }
        }
    };

    public MyScrollView(Context context) {
        super(context);
        init();
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /** 初始化操作 */
    private void init() {
        imageLoader = ImageLoader.getInstance();
        taskCollection = new HashSet<>();
        setOnTouchListener(this);
    }

    /** 进行一些关键性的初始化操作,获取MyScrollView的高度,以及得到第一列的宽度值.并在这里开始加载第一页的图片 */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            scrollViewHeight = getHeight();
            scrollLayout = getChildAt(0);
            firstColumn = (LinearLayout) findViewById(R.id.first_column);
            secondColumn = (LinearLayout) findViewById(R.id.second_column);
            thirdColumn = (LinearLayout) findViewById(R.id.third_column);
            columnWidth = firstColumn.getWidth();
            loadOnce = true;
            loadMoreImages();
        }
    }

    /** 监听用户的触屏事件,如果用户手指离开屏幕则开始进行滚动检测 */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Message message = new Message();
            message.obj = this;
            handler.sendMessageDelayed(message, 5);
        }
        return false;
    }

    /** 开始加载下一页的图片,每张图片都会开启一个异步线程去下载 */
    public void loadMoreImages() {
        if (hasSDCard()) {
            int startIndex = page * PAGE_SIZE;
            int endIndex = (page + 1) * PAGE_SIZE;
            if (startIndex  < Images.imageThumbUrls.length) {
                Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT).show();
                if (endIndex > Images.imageThumbUrls.length) {
                    endIndex = Images.imageThumbUrls.length;
                }
                for (int i = startIndex; i < endIndex; i++) {
                    LoadImageTask task = new LoadImageTask();
                    taskCollection.add(task);
                    task.execute(Images.imageThumbUrls[i]);
                }
                page++;
            } else {
                Toast.makeText(getContext(), "已没有更多图片", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    /** 遍历imageViewList中的每张图片,对图片的可见性进行检测,如果图片已经离开屏幕可见范围,则见图片换成一张空图 */
    public void checkVisibility() {
        for (int i = 0; i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);
            int borderTop = (int) imageView.getTag(R.string.border_top); // 获取图片所在列添加这个imageView之前的高度
            int borderBottom = (int) imageView.getTag(R.string.border_bottom); // 获取图片所在列添加这个imageView之后的高度
            if (borderBottom > getScrollY() && borderTop < getScrollY() + scrollViewHeight) {
                // 可见, 添加ImageView之后这个imageView底部的高度在可见范围, 加载图片
                String imageUrl = (String) imageView.getTag(R.string.image_url);
                Bitmap bitmap = imageLoader.getBitmapFromMemCache(imageUrl);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    File imageFile = new File(getImagePath(imageUrl));
                    bitmap = ImageLoader.decodeSampledBitMapFromFile(imageFile.getPath(), columnWidth);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        LoadImageTask task = new LoadImageTask(imageView);
                        task.execute(imageUrl);
                    }
                }
            } else { // 不可见
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }

    /** 判断手机是否有SD卡 */
    private boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取图片的本地存储路径
     * @param imageUrl 图片的URL地址
     * @return 图片的本地存储路径
     */
    private String getImagePath(String imageUrl) {
        int lastSlashIndex = imageUrl.lastIndexOf("/");
        String imageName = imageUrl.substring(lastSlashIndex + 1);
        String imageDir = Environment.getExternalStorageDirectory().getPath() + "/PhotoWallFalls/";
        File file = new File(imageDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String imagePath = imageDir + imageName;
        return imagePath;
    }

    /** 异步下载图片的任务 */
    class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        private String mImageUrl; // 图片的URL地址
        private ImageView mImageView; // 可重复使用的ImageView

        public LoadImageTask() {}

        /**
         * 将可重复使用的ImageView传入
         * @param imageView
         */
        public LoadImageTask(ImageView imageView) {
            this.mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            mImageUrl = params[0];
            Bitmap imageBitmap = imageLoader.getBitmapFromMemCache(mImageUrl);
            if (imageBitmap == null) {
                imageBitmap = loadImage(mImageUrl);
            }
            return imageBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                double ratio = bitmap.getWidth() / (columnWidth * 1.0);
                int scaledHeight = (int) (bitmap.getHeight() / ratio);
                addImage(bitmap, columnWidth, scaledHeight);
            }
            taskCollection.remove(this);
        }

        /**
         * 根据传入的URL,对图片进行加载.如果这张图片已经存在于SD卡中,则直接从SD卡里读取,否则就从网络上下载
         * @param imageUrl 图片的URL地址
         * @return 加载到内存的图片
         */
        private Bitmap loadImage(String imageUrl) {
            File imageFile = new File(getImagePath(imageUrl));
            if (!imageFile.exists()) {
                downloadImage(imageUrl);
            }
            if (imageUrl != null) {
                Bitmap bitmap = ImageLoader.decodeSampledBitMapFromFile(imageFile.getPath(), columnWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                    return bitmap;
                }
            }
            return null;
        }

        /**
         * 将图片下载到SD卡缓存起来
         * @param imageUrl 图片的URL地址
         */
        private void downloadImage(String imageUrl) {
            HttpURLConnection con = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File imageFile = null;
            try {
                URL url = new URL(imageUrl);
                try {
                    con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(5 * 1000);
                    con.setReadTimeout(15 * 1000);
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    bis = new BufferedInputStream(con.getInputStream());
                    imageFile = new File(getImagePath(imageUrl));
                    fos = new FileOutputStream(imageFile);
                    bos = new BufferedOutputStream(fos);
                    byte[] b = new byte[1024];
                    int length;
                    while((length = bis.read(b)) != -1) {
                        bos.write(b, 0, length);
                        bos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bis != null) {
                            bis.close();
                        }
                        if (bos != null) {
                            bos.close();
                        }
                        if (con != null) {
                            con.disconnect();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (imageFile != null) {
                Bitmap bitmap = ImageLoader.decodeSampledBitMapFromFile(imageFile.getPath(), columnWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                }
            }
        }

        /**
         * 向ImageView 中添加一张图片
         * @param bitmap 待添加的图片
         * @param imageWidth 图片的宽度
         * @param imageHeight 图片的高度
         */
        private void addImage(Bitmap bitmap, int imageWidth, int imageHeight) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            if (mImageView != null) {
                mImageView.setImageBitmap(bitmap);
            } else {
                final ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(5, 5, 5, 5);
                imageView.setTag(R.string.image_url, mImageUrl);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setTransitionName("image");
                }
                findColumnToAdd(imageView, imageHeight).addView(imageView);
                position++; // 记录position

//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    imageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(((Activity) getContext()), imageView, "image");
//                            Intent intent = new Intent(getContext(), BigImageActivity.class);
//                            String imageUrl = imageView.getTag(R.string.image_url).toString();
//                            intent.putExtra("url", imageUrl);
//                            ImageViewAware thumbAware = new ImageViewAware(imageView);
//                            ImageSize targetSize = new ImageSize(thumbAware.getWidth(), thumbAware.getHeight());
//                            String memoryCacheKey = MemoryCacheUtils.generateKey(imageUrl, targetSize);
//                            intent.putExtra("cache_key", memoryCacheKey);
//                            getContext().startActivity(intent, options.toBundle());
//                        }
//                    });
//                } else {
//                // TODO 这里添加点击事件,MainAcitivity的GridView的OnItemClick无效
//                    BigImageUtil.showBigImage(imageView, (String) imageView.getTag(R.string.image_url)); // 单页
//                }
                // pager页
                imageViewList.add(imageView);
                final int finalPosition = position;
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getContext(), PagerActivity.class);
//                        intent.putExtra("position", finalPosition);
//                        getContext().startActivity(intent);

                        ArrayList imageList = new ArrayList(Arrays.asList(Images.imageThumbUrls));
                        PagerActivity.startActivity((Activity)getContext(), imageList, finalPosition);
                    }
                });
            }
        }

        /**
         * 找到此时应该添加图片的一列. 原则就是对三列的高度进行判断,当前高度最小的一列就是应该添加的一列
         * @param imageView
         * @param imageHeight
         * @return 应该添加图片的一列
         */
        private LinearLayout findColumnToAdd(ImageView imageView, int imageHeight) {
            if (firstColumnHeight <= secondColumnHeight) {
                if (firstColumnHeight <= thirdColumnHeight) {
                    imageView.setTag(R.string.border_top, firstColumnHeight); // 设置第一列添加图片前的高度
                    firstColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, firstColumnHeight); // 设置第一列添加图片后的高度
                    return firstColumn;
                } else {
                    imageView.setTag(R.string.border_top, thirdColumnHeight);
                    thirdColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, thirdColumnHeight);
                    return thirdColumn;
                }
            } else {
                if (secondColumnHeight <= thirdColumnHeight) {
                    imageView.setTag(R.string.border_top, secondColumnHeight);
                    secondColumnHeight += imageHeight;
                    imageView
                            .setTag(R.string.border_bottom, secondColumnHeight);
                    return secondColumn;
                } else {
                    imageView.setTag(R.string.border_top, thirdColumnHeight);
                    thirdColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, thirdColumnHeight);
                    return thirdColumn;
                }
            }
        }
    }
}
