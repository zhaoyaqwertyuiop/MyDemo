package com.zy.demo.mydemo.doublescrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ScrollView;

import com.zy.demo.mydemo.util.ScreenUtils;

/**
 * Created by ysnow on 2015/4/20..
 */
public class YsnowScrollViewPageOne extends ScrollView{

	private int mScreenHeight; // 屏幕高度
	public float oldY;
	private int t;

	public YsnowScrollViewPageOne(Context context) {
		super(context);
		mScreenHeight = ScreenUtils.getScreenHeight(context);
	}

	public YsnowScrollViewPageOne(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScreenHeight = ScreenUtils.getScreenHeight(context);
	}

	public YsnowScrollViewPageOne(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

//		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		DisplayMetrics metrics = new DisplayMetrics();
//		windowManager.getDefaultDisplay().getMetrics(metrics);
//		mScreenHeight = metrics.heightPixels; // 获得屏幕高度的像素值
		mScreenHeight = ScreenUtils.getScreenHeight(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 手指按下的时候,获得滑动事件,也就是让顶级的scrollView失去滑动事件
				getParent().getParent().requestDisallowInterceptTouchEvent(true);
				oldY = ev.getY(); // 记录上一个点
				break;
			case MotionEvent.ACTION_MOVE:
				Log.d("pageOne", "move");
				float Y = ev.getY();
				float Ys = Y - oldY; // 获得Y轴滑动的距离
				int childHeight = this.getChildAt(0).getMeasuredHeight(); // 得到scrollView里面空间的高度
				int padding = childHeight - t; // 子控件高度减去scrollview向上滑动的距离
				// Ys<0表示手指正在向上滑动, padding==mScreenHeight表示本scrollview已经滑动到了底部
				if (Ys < 0 && padding == mScreenHeight) {
					// 让顶级的scrollview重新获得滑动事件
					getParent().getParent().requestDisallowInterceptTouchEvent(false);
				}
				break;
			case MotionEvent.ACTION_UP:
				getParent().getParent().requestDisallowInterceptTouchEvent(true);
				break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		this.t = t; // t表示本scrollview向上滑动的距离
		super.onScrollChanged(l, t, oldl, oldt);
	}
}