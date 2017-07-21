package com.zy.demo.mydemo.autotextview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 水平滚动TextViw
 */
public class AutoTextView extends AppCompatTextView {

	public AutoTextView(Context context) {
		super(context);
	}

	public AutoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean isFocused() {
//		return super.isFocused();
		return true;
	}
}
