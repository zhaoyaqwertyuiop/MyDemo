package com.zy.demo.mydemo.viewpager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zy.demo.mydemo.R;

/**
 * ViewPager头部联动的水平滚动View
 */
public class SyncHorizontalScrollView extends HorizontalScrollView {
	private View view;
//	private ImageView leftImage; // 左箭头
//	private ImageView rightImage; // 右箭头
	private int windowWidth = 0; // 屏幕宽度
	private Context context;
	private RadioGroup rg_nav_content; // 标签栏
	private ImageView iv_nav_indictor; // 滑动下标
	private LayoutInflater mInflater;
	private int count; // 屏幕显示的标签个数
	private int allCount;
	private int indicatorWidth; // 每个标签所占的宽度
	private int currentIndicatorLeft = 0; // 当前所在标签页面的位移
	private ViewPager mViewPager;
	private int scrollX;

	public SyncHorizontalScrollView(Context context) {
		super(context);
	}

	public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SyncHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void initView(ViewPager mViewPager, String[] tabTitle, int count, Context context) {
		allCount = tabTitle.length;
		this.setHorizontalScrollBarEnabled(false); // 隐藏滚动条
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mViewPager = mViewPager;
		this.view = mInflater.inflate(R.layout.view_sync_hsv, null);
		this.addView(view);
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		windowWidth = dm.widthPixels;
		this.count = tabTitle.length >= count ? count : tabTitle.length;
		this.indicatorWidth = windowWidth / this.count;
		init(tabTitle);
	}


	private void init(String[] tabTitle) {
		rg_nav_content = (RadioGroup) view.findViewById(R.id.rg_nav_content);
		iv_nav_indictor = (ImageView) view.findViewById(R.id.iv_nav_indicator);
		initIndicatorWidth();
		initNavigationHSV(tabTitle);
		setListener();
	}

	/** 初始化滑动下标的宽 */
	private void initIndicatorWidth() {
		ViewGroup.LayoutParams cursor_Params = iv_nav_indictor.getLayoutParams();
		cursor_Params.width = indicatorWidth;
		iv_nav_indictor.setLayoutParams(cursor_Params);
	}

	private void initNavigationHSV(String[] tabTitle) {
		rg_nav_content.removeAllViews();
		for (int i = 0; i < tabTitle.length; i++) {
			RadioButton rb = (RadioButton) mInflater.inflate(R.layout.item_sync_nav_radiogroup, null);
			rb.setId(i);
			rb.setText(tabTitle[i]);
			rb.setLayoutParams(new ViewGroup.LayoutParams(indicatorWidth, ViewGroup.LayoutParams.MATCH_PARENT));
			rg_nav_content.addView(rb);
		}
	}

	private void setListener() {
		rg_nav_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (rg_nav_content.getChildAt(checkedId) != null) {
					moveAnimation(checkedId, 0);
					mViewPager.setCurrentItem(checkedId); // viewPager跟随一起切换
				}
			}
		});

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				Log.d("onPageScrolled", "position=" + position + ",positionOffset=" + positionOffset);
				moveAnimation(position, positionOffset);
//				float checkedId = position + positionOffset;
//				float tagerX = (position + positionOffset) * indicatorWidth;
//				// title数量过长 且为奇数,滑动到中间的时候整个view同步滑动,看起来就像是title在滑动
//				if ((allCount > count && count % 2 != 0) && ((checkedId >= count/2 && checkedId <= allCount-1 - count/2))) {
//					smoothScrollTo((int) (positionOffset * indicatorWidth), 0);
////					scrollTo((int) tagerX, 0);
//				}
//				ViewPropertyAnimator.animate(iv_nav_indictor).translationX(tagerX).setDuration(0);
			}

			@Override
			public void onPageSelected(int position) {
//				performLabelClick(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	// 模拟点击事件
	public void performLabelClick(int position) {
		if (rg_nav_content != null && rg_nav_content.getChildCount() > position) {
			((RadioButton) rg_nav_content.getChildAt(position)).performClick();
		}
	}

	private void moveAnimation(int position, float positionOffset) {
		float checkedId = position + positionOffset;
		float tagerX = (position + positionOffset) * indicatorWidth;
		// title数量过长 ,滑动到中间的时候整个view同步滑动,看起来就像是title在滑动
		if (allCount > count && ((checkedId >= count/2 && checkedId <= allCount-1 - (count - 1)/2))) {
			currentIndicatorLeft = (int) (indicatorWidth * checkedId); // 记录当前下标距最左侧的距离
			scrollX = currentIndicatorLeft - indicatorWidth * (count/2); // 计算需要横向滚动的距离
//			this.post(runnable);
			smoothScrollTo(scrollX, 0);
		}
		ViewPropertyAnimator.animate(iv_nav_indictor).translationX(tagerX).setDuration(0);
	}

	// 动画移动效果
	private void moveAnimation(int checkedId) {
		TranslateAnimation animation = new TranslateAnimation(currentIndicatorLeft, indicatorWidth * checkedId, 0, 0);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(100);
		animation.setFillAfter(true);
//		iv_nav_indictor.startAnimation(animation); // 执行位移动画
		currentIndicatorLeft = indicatorWidth * checkedId; // 记录当前下标距最左侧的距离
		scrollX = (checkedId > 1 ? currentIndicatorLeft : 0) - indicatorWidth * 2; // 计算需要横向滚动的距离
		this.post(runnable);
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			smoothScrollTo(scrollX, 0);
		}
	};

	public int getIndictorWidth() {
		return indicatorWidth;
	}

}