//package com.photowalldemo.pinchimageview;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.view.PagerAdapter;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.photowalldemo.Images;
//import com.photowalldemo.R;
//
//import java.util.LinkedList;
//
//
//public class PagerActivity extends Activity {
//
//    private TextView countTV;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pager);
//
//        countTV = (TextView) super.findViewById(R.id.countTV);
//
//        final LinkedList<PinchImageView> viewCache = new LinkedList<PinchImageView>();
//        final DisplayImageOptions thumbOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheInMemory(true).build();
//        final DisplayImageOptions originOptions = new DisplayImageOptions.Builder().build();
//
//        final PinchImageViewPager pager = (PinchImageViewPager) findViewById(R.id.pager);
//        pager.setAdapter(new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return Images.imageThumbUrls.length;
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object o) {
//                return view == o;
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                PinchImageView piv;
//                if (viewCache.size() > 0) {
//                    piv = viewCache.remove();
//                    piv.reset();
//                } else {
//                    piv = new PinchImageView(PagerActivity.this);
//                }
//
//                // ImageView的宽和高
//                getImageLoader(getApplicationContext()).displayImage(Images.imageThumbUrls[position], piv, thumbOptions);
//                container.addView(piv);
//                return piv;
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                PinchImageView piv = (PinchImageView) object;
//                container.removeView(piv);
//                viewCache.add(piv);
//            }
//
//            @Override
//            public void setPrimaryItem(ViewGroup container, int position, Object object) {
//                PinchImageView piv = (PinchImageView) object;
////                getImageLoader(getApplicationContext()).displayImage(image.getUrl(image.getOriginWidth(), image.getOriginHeight()), piv, originOptions);
//                getImageLoader(getApplicationContext()).displayImage(Images.imageThumbUrls[position], piv, originOptions);
//                pager.setMainPinchImageView(piv);
//            }
//        });
//
//        pager.setOnPageChangeListener(new PinchImageViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                countTV.setText(position + "/" + Images.imageThumbUrls.length);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//        int currentPosition = getIntent().getIntExtra("position", 0);
//        pager.setCurrentItem(currentPosition);
//    }
//
//    public static ImageLoader getImageLoader(Context context) {
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        if (!imageLoader.isInited()) {
//            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//        }
//        return imageLoader;
//    }
//}