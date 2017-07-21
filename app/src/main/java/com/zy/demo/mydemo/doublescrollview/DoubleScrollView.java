package com.zy.demo.mydemo.doublescrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zy.demo.mydemo.util.ScreenUtils;

/**
 * 仿淘宝商品详情页的上下翻页
 * Created by zhaoya on 2016/6/30.
 */
public class DoubleScrollView extends ScrollView {

	private int mScreenHeight;
	private YsnowScrollViewPageOne mPageOne;
	private YsnowScrollView mPageTwo;

	private boolean isSetted = false;
	private boolean isPageOne = true; // 默认在第一页

	public DoubleScrollView(Context context) {
		super(context);
	}

	public DoubleScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DoubleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!isSetted) {
			// 初始话布局
			final LinearLayout wrapper = (LinearLayout) getChildAt(0);
			this.mPageOne = (YsnowScrollViewPageOne) wrapper.getChildAt(0);
			this.mPageTwo = (YsnowScrollView) wrapper.getChildAt(1);

			// 设置两个子view的高度为手机的高度
			mScreenHeight = ScreenUtils.getScreenHeight(getContext());
			this.mPageOne.getLayoutParams().height = mScreenHeight;
			this.mPageTwo.getLayoutParams().height = mScreenHeight;
			isSetted = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(0, 0);
		}
	}

	/**
	 * 获取第一页
	 */
	public YsnowScrollViewPageOne getPageOne() {
		return this.mPageOne;
	}

	/**
	 * 获取第二页
	 */
	public YsnowScrollView getPageTwo() {
		return this.mPageTwo;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				int scrollY = getScrollY();
				int creteria = mScreenHeight / 5; // 滑动5分只一距离翻页
				Log.d("DoubleScrollView", "creteria=" + creteria + ",scrollY=" + scrollY +
						",mPageOneHeight=" + this.mPageOne.getLayoutParams().height +
						",mPageTwoHeight=" + this.mPageTwo.getLayoutParams().height);
				if (isPageOne) { // 当前是在第一页
					if (scrollY <= creteria) { // 不翻页
						this.smoothScrollTo(0, 0);
					} else { // 翻页
//						this.smoothScrollTo(0, this.mPageOne.getLayoutParams().height);
						this.smoothScrollTo(0, mScreenHeight);
						this.setFocusable(false);
						isPageOne = false;
					}
				} else { // 当前在第二页
					int scrollpadding = mScreenHeight - scrollY;
					if (scrollpadding >= creteria) { // 翻回第一页
						this.smoothScrollTo(0, 0);
						isPageOne = true;
					} else {
						this.smoothScrollTo(0, mScreenHeight);
//						this.smoothScrollTo(0, this.mPageOne.getLayoutParams().height);
					}
				}
				return true;
		}
		return super.onTouchEvent(ev);
	}
}























