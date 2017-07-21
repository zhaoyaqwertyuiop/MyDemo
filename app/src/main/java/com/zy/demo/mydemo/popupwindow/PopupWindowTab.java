package com.zy.demo.mydemo.popupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zy.demo.mydemo.R;

/**
 * 有动画效果的Tab,配合弹出popupwindow
 * Created by zhaoya on 2016/6/17.
 */
public class PopupWindowTab extends LinearLayout implements View.OnClickListener{

	private Context context;
	private PopupWindow mPopupWindow;
	private View mPopView;
	private View mAnimView;

	private OnChangePopupShowListener mOnChangePopupShowListener; // 改变popupWindow内容的回调

	private TextView mTitle1TextView;
	private TextView mTitle2TextView;
	private TextView mTitle3TextView;
	private TextView mTitle4TextView;

	private TextView mContent1TextView;
	private TextView mContent2TextView;
	private TextView mContent3TextView;
	private TextView mContent4TextView;

	private int content1Id, content2Id, content3Id, content4Id; // 用来记内容的顺序

	// tab
	private LinearLayout mTab1, mTab2, mTab3, mTab4;

	// 箭头
	private ImageView mImageView1;
	private ImageView mImageView2;
	private ImageView mImageView3;
	private ImageView mImageView4;

	// 下划线
	private View mView1;
	private View mView2;
	private View mView3;
	private View mView4;

	private RotateAnimation mShowRotateAnimation; // 展开时箭头动画
	private RotateAnimation mHindRotateAnimation; // 隐藏时箭头动画
	private Animation mContentInAnimation; // 弹出框显示动画
	private Animation mContentOutAnimation; // 弹出框消失动画
	private Animation mContentChangeHindAnimation; // 弹出框内容改变动画
	private Animation mContentChangeShowAnimation; // 弹出框内容改变动画

	private int currentTab = 0; // 当前选中的tab, 0表示没选中

	private OnTabClickListener mOnTabClickListener;

	public PopupWindowTab(Context context) {
		super(context);
	}

	public PopupWindowTab(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PopupWindowTab(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void init(Context context, View view, OnChangePopupShowListener mOnChangePopupShowListener) {
		init(context, view, null, mOnChangePopupShowListener);
	}

	public void init(Context context, View view, View animView, OnChangePopupShowListener mOnChangePopupShowListener) {
		mPopView = view;
		this.mAnimView = animView == null ? mPopView : animView;
		this.mOnChangePopupShowListener = mOnChangePopupShowListener;
		this.context = context;
		initView(context);
		initAnim();
	}

	public void setmOnTabClickListener(OnTabClickListener mOnTabClickListener) {
		this.mOnTabClickListener = mOnTabClickListener;
	}

	/** 获取当前tab */
	public int getCurrentTab() {
		return currentTab;
	}

	private void initAnim() {
		mShowRotateAnimation = new RotateAnimation(0, -90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mShowRotateAnimation.setDuration(500);
		mShowRotateAnimation.setFillAfter(true);

		mHindRotateAnimation = new RotateAnimation(-90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mHindRotateAnimation.setDuration(500);
		mShowRotateAnimation.setFillAfter(true);

		//使用AnimationUtils类的静态方法loadAnimation()来加载XML中的动画XML文件
//		mContentInAnimation = AnimationUtils.loadAnimation(context, R.anim.set_in_up);
//		mContentOutAnimation = AnimationUtils.loadAnimation(context, R.anim.set_out_up);
		mContentChangeHindAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_pop_change_hind);
		mContentChangeShowAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_pop_change_show);

		mContentInAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -1,
				Animation.RELATIVE_TO_SELF, 0);
		mContentInAnimation.setFillAfter(true);
		mContentInAnimation.setDuration(500);

		mContentOutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -1);
		mContentOutAnimation.setFillAfter(true);
		mContentOutAnimation.setDuration(500);
	}

	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_popup_window_tab, this);
		this.mTitle1TextView = (TextView) super.findViewById(R.id.pop_title1_tv);
		this.mTitle2TextView = (TextView) super.findViewById(R.id.pop_title2_tv);
		this.mTitle3TextView = (TextView) super.findViewById(R.id.pop_title3_tv);
		this.mTitle4TextView = (TextView) super.findViewById(R.id.pop_title4_tv);
		this.mContent1TextView = (TextView) super.findViewById(R.id.pop_content1_tv);
		this.mContent2TextView = (TextView) super.findViewById(R.id.pop_content2_tv);
		this.mContent3TextView = (TextView) super.findViewById(R.id.pop_content3_tv);
		this.mContent4TextView = (TextView) super.findViewById(R.id.pop_content4_tv);
		this.mImageView1 = (ImageView) super.findViewById(R.id.pop_right1_iv);
		this.mImageView2 = (ImageView) super.findViewById(R.id.pop_right2_iv);
		this.mImageView3 = (ImageView) super.findViewById(R.id.pop_right3_iv);
		this.mImageView4 = (ImageView) super.findViewById(R.id.pop_right4_iv);
		this.mView1 = super.findViewById(R.id.pop_down1_view);
		this.mView2 = super.findViewById(R.id.pop_down2_view);
		this.mView3 = super.findViewById(R.id.pop_down3_view);
		this.mView4 = super.findViewById(R.id.pop_down4_view);
		this.mTab1 = (LinearLayout) super.findViewById(R.id.pop_but1_ll);
		this.mTab2 = (LinearLayout) super.findViewById(R.id.pop_but2_ll);
		this.mTab3 = (LinearLayout) super.findViewById(R.id.pop_but3_ll);
		this.mTab4 = (LinearLayout) super.findViewById(R.id.pop_but4_ll);
	}

	public void changeTab(int num) {
		switch(num) {
			case 1:
				if (currentTab == num) {
					currentTab = 0;
					mImageView1.startAnimation(mHindRotateAnimation);
					mView1.setVisibility(View.INVISIBLE);
					return;
				}
				mImageView1.startAnimation(mShowRotateAnimation);
				if (currentTab == 2) {
					mImageView2.startAnimation(mHindRotateAnimation);
					mImageView2.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 3) {
					mImageView3.startAnimation(mHindRotateAnimation);
					mImageView3.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 4) {
					mImageView4.startAnimation(mHindRotateAnimation);
					mImageView4.setImageResource(R.drawable.arrow_triangle_left);
				}
				mView1.setVisibility(View.VISIBLE);
				mView2.setVisibility(View.INVISIBLE);
				mView3.setVisibility(View.INVISIBLE);
				mView4.setVisibility(View.INVISIBLE);
				currentTab = 1;
				showPopupWindow(1);
				break;
			case 2:
				if (currentTab == num) {
					currentTab = 0;
					mImageView2.startAnimation(mHindRotateAnimation);
					mView2.setVisibility(View.INVISIBLE);
					return;
				}
				mImageView2.startAnimation(mShowRotateAnimation);
				if (currentTab == 1) {
					mImageView1.startAnimation(mHindRotateAnimation);
					mImageView1.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 3) {
					mImageView3.startAnimation(mHindRotateAnimation);
					mImageView3.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 4) {
					mImageView4.startAnimation(mHindRotateAnimation);
					mImageView4.setImageResource(R.drawable.arrow_triangle_left);
				}
				mView1.setVisibility(View.INVISIBLE);
				mView2.setVisibility(View.VISIBLE);
				mView3.setVisibility(View.INVISIBLE);
				mView4.setVisibility(View.INVISIBLE);
				currentTab = 2;
				showPopupWindow(2);
				break;
			case 3:
				if (currentTab == num) {
					currentTab = 0;
					mImageView3.startAnimation(mHindRotateAnimation);
					mView3.setVisibility(View.INVISIBLE);
					return;
				}
				mImageView3.startAnimation(mShowRotateAnimation);
				if (currentTab == 1) {
					mImageView1.startAnimation(mHindRotateAnimation);
					mImageView1.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 2) {
					mImageView2.startAnimation(mHindRotateAnimation);
					mImageView2.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 4) {
					mImageView4.startAnimation(mHindRotateAnimation);
					mImageView4.setImageResource(R.drawable.arrow_triangle_left);
				}
				mView1.setVisibility(View.INVISIBLE);
				mView2.setVisibility(View.INVISIBLE);
				mView3.setVisibility(View.VISIBLE);
				mView4.setVisibility(View.INVISIBLE);
				currentTab = 3;
				showPopupWindow(3);
				break;
			case 4:
				if (currentTab == num) {
					currentTab = 0;
					mImageView4.startAnimation(mHindRotateAnimation);
					mView4.setVisibility(View.INVISIBLE);
					return;
				}
				mImageView4.startAnimation(mShowRotateAnimation);
				if (currentTab == 1) {
					mImageView1.startAnimation(mHindRotateAnimation);
					mImageView1.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 2) {
					mImageView2.startAnimation(mHindRotateAnimation);
					mImageView2.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 3) {
					mImageView3.startAnimation(mHindRotateAnimation);
					mImageView3.setImageResource(R.drawable.arrow_triangle_left);
				}
				mView1.setVisibility(View.INVISIBLE);
				mView2.setVisibility(View.INVISIBLE);
				mView3.setVisibility(View.INVISIBLE);
				mView4.setVisibility(View.VISIBLE);
				currentTab = 4;
				showPopupWindow(4);
				break;
			case 0: // 隐藏popUpWindow
				if (currentTab == 1) {
					mImageView1.startAnimation(mHindRotateAnimation);
					mImageView1.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 2) {
					mImageView2.startAnimation(mHindRotateAnimation);
					mImageView2.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 3) {
					mImageView3.startAnimation(mHindRotateAnimation);
					mImageView3.setImageResource(R.drawable.arrow_triangle_left);
				} else if (currentTab == 4) {
					mImageView4.startAnimation(mHindRotateAnimation);
					mImageView4.setImageResource(R.drawable.arrow_triangle_left);
				}
				showPopupWindow(0);
				mView1.setVisibility(View.INVISIBLE);
				mView2.setVisibility(View.INVISIBLE);
				mView3.setVisibility(View.INVISIBLE);
				mView4.setVisibility(View.INVISIBLE);
				currentTab = 0;
				break;
			default:
				break;
		}
	}

	private boolean isPopupShowing = false; // popupwindow初始化后判断是否显示

	/** 显示popupWindow,根据传入参数不同改变显示内容 */
	private void showPopupWindow(final int position) {
		if (mPopView == null) {
			return;
		}
		if (position == 0) { // 隐藏
			if (mPopupWindow == null) {
				return;
			}
			if (isPopupShowing) {
				isPopupShowing = false;
				mAnimView.startAnimation(mContentOutAnimation);
				mContentOutAnimation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						mPopView.setVisibility(GONE); // 必须GONE,不然会覆盖掉下面的布局
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
			}
			return;
		}
		// 显示
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(mPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			mPopupWindow.setAnimationStyle(R.style.Pop_TAB_anim);
			mPopupWindow.showAsDropDown(this);
			mOnChangePopupShowListener.initShowPopWindow(position);
			isPopupShowing = true;
			return;
		}

		if (!isPopupShowing) { // 没有显示
			mOnChangePopupShowListener.initShowPopWindow(position);
			mPopView.setVisibility(VISIBLE);
			mAnimView.startAnimation(mContentInAnimation);
			isPopupShowing = true;
		} else { // 已经显示了,要更新内容
			mAnimView.startAnimation(mContentChangeHindAnimation);
			mContentChangeHindAnimation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mOnChangePopupShowListener.initShowPopWindow(position);
					mAnimView.startAnimation(mContentChangeShowAnimation);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.pop_but1_ll:
				if (currentTab == 1) {
					changeTab(0);
				} else {
					changeTab(1);
				}
				if (mOnTabClickListener != null) {
					mOnTabClickListener.onTab1Click(v);
				}
				break;
			case R.id.pop_but2_ll:
				if (currentTab == 2) {
					changeTab(0);
				} else {
					changeTab(2);
				}
				if (mOnTabClickListener != null) {
					mOnTabClickListener.onTab2Click(v);
				}
				break;
			case R.id.pop_but3_ll:
				if (currentTab == 3) {
					changeTab(0);
				} else {
					changeTab(3);
				}
				if (mOnTabClickListener != null) {
					mOnTabClickListener.onTab3Click(v);
				}
				break;
			case R.id.pop_but4_ll:
				if (currentTab == 4) {
					changeTab(0);
				} else {
					changeTab(4);
				}
				if (mOnTabClickListener != null) {
					mOnTabClickListener.onTab4Click(v);
				}
				break;
			default:
				changeTab(0);
				break;
		}
	}

	public interface OnChangePopupShowListener {
		/** 显示popupWindow时初始化 */
		void initShowPopWindow(int currentTab);
	}

	public interface OnTabClickListener {
		void onTab1Click(View v);
		void onTab2Click(View v);
		void onTab3Click(View v);
		void onTab4Click(View v);
	}

	public TextView getmTitle1TextView() {
		return mTitle1TextView;
	}

	public TextView getmTitle2TextView() {
		return mTitle2TextView;
	}

	public TextView getmTitle3TextView() {
		return mTitle3TextView;
	}

	public TextView getmTitle4TextView() {
		return mTitle4TextView;
	}

	public TextView getmContent1TextView() {
		return mContent1TextView;
	}

	public TextView getmContent2TextView() {
		return mContent2TextView;
	}

	public TextView getmContent3TextView() {
		return mContent3TextView;
	}

	public TextView getmContent4TextView() {
		return mContent4TextView;
	}

	public int getContent1Id() {
		return content1Id;
	}

	public void setContent1Id(int content1Id) {
		this.content1Id = content1Id;
	}

	public int getContent2Id() {
		return content2Id;
	}

	public void setContent2Id(int content2Id) {
		this.content2Id = content2Id;
	}

	public int getContent3Id() {
		return content3Id;
	}

	public void setContent3Id(int content3Id) {
		this.content3Id = content3Id;
	}

	public int getContent4Id() {
		return content4Id;
	}

	public void setContent4Id(int content4Id) {
		this.content4Id = content4Id;
	}

	public LinearLayout getmTab1() {
		return mTab1;
	}

	public LinearLayout getmTab2() {
		return mTab2;
	}

	public LinearLayout getmTab3() {
		return mTab3;
	}

	public LinearLayout getmTab4() {
		return mTab4;
	}
}
