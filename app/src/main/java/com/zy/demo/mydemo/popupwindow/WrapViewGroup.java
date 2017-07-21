package com.zy.demo.mydemo.popupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.common.util.DensityUtil;

/**
 * 当一行显示不够时,自动换行
 */
public class WrapViewGroup extends ViewGroup{

	private static final int VIEW_MARGI = DensityUtil.dip2px(15); // 控件间的间距

	public WrapViewGroup(Context context) {
		super(context);
	}

	public WrapViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int stages = 1; // 行号
		int stageHeight = 0;
		int stageWidth = 0; // 记录当前行所占宽度

		int wholeWidth = MeasureSpec.getSize(widthMeasureSpec); // 父布局宽度

		for (int i = 0; i < getChildCount(); i++) {
			final View child = getChildAt(i);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			stageWidth += child.getMeasuredWidth() + VIEW_MARGI;
			stageHeight = child.getMeasuredHeight();
			if (stageWidth >= wholeWidth) { // 换行
				stages++;
				stageWidth = child.getMeasuredWidth();
			}
		}

		int wholeHeight = (stageHeight + VIEW_MARGI) * stages + VIEW_MARGI; // 上下都加了 VIEW_MARGI 的间隔;
		if (getChildCount() == 0) { // 如果没有子项,高度为0
			wholeHeight = 0;
		}

		setMeasuredDimension(resolveSize(wholeWidth, widthMeasureSpec), resolveSize(wholeHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		int row = 0; // which row lay you view relative to parent
		int lengthX = l; // right position of child relative to parent
		int lengthY = t; // bottom position of child relative to parent
		for (int i = 0; i < count; i++) {
			final View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			lengthX += width + VIEW_MARGI;//第一个的时候不需要加
			if (lengthX > r) {
				lengthX = width + VIEW_MARGI + l;
				row++;
			}
//			lengthY = row * (height + VIEW_MARGI) + VIEW_MARGI + height + t;
			int top = row * (height + VIEW_MARGI) + VIEW_MARGI;
			int bottom = top + height;
			child.layout(lengthX - width, top, lengthX, bottom);
		}
	}
}
