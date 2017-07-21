package com.zy.demo.mydemo.doublescrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 第二个子ScrollView,只实现了
 * Created by ysnow on 2015/4/20.
 */
public class YsnowScrollView extends ScrollView {
	public float oldY;
	private int top; // 距离顶端的距离,top==0表示一定滑动到了顶部

	public YsnowScrollView(Context context) {
		super(context);
	}

	public YsnowScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public YsnowScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//同样是获得滑动事件->记录位置
				getParent().getParent().requestDisallowInterceptTouchEvent(true);
				oldY = ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				Log.d("pageTwo", "move");
				float Y = ev.getY();
				float Ys = Y - oldY;
				//Ys>0表示正在向下滑动->top==0表示一定滑动到了顶部
				if (Ys > 0 && top == 0) {
					//然后让顶级那个scrolLview滑动滑动事件
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
		this.top = t;
		super.onScrollChanged(l, t, oldl, oldt);
	}

}