package com.zy.demo.mydemo.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 上拉下拉ListView
 */
public class PullToRefreshListView extends ListView implements Pullable{

	public PullToRefreshListView(Context context) {
		super(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean canPullDown() {
		if (getCount() == 0) {
			return true; // 没有item的时候也可以下拉刷新
		} else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
			return true; // 滑到ListView的顶部了
		}
		return false;
	}

	@Override
	public boolean canPullUp() {
		if (getCount() == 0) {
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1)
				&& getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
				&& getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight()) {
			return true; // 滑倒底部了
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				break;
			default:
				break;
		}
		return super.onTouchEvent(ev);
	}
}
