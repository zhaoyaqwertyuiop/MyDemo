package com.zy.demo.mydemo.anim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.zy.demo.mydemo.R;

public class AnimDemo extends AppCompatActivity implements View.OnClickListener{

	private boolean isHind = false; // 是否隐藏动画

	// TranslateAnimation 移动
	private TranslateAnimation mShowTranslateAnimation; // 显示动画
	private TranslateAnimation mHindTranslateAnimation; // 隐藏动画

	// AlphaAnimation 淡入淡出
	private AlphaAnimation mAlphaAnimation;

	// ScaleAnimation 缩放
	private ScaleAnimation mScaleAnimation;

	// RotateAnimation 旋转
	private RotateAnimation mRotateAnimation;

	private TextView mTranslateTextView;
	private TextView mAlphaTextView;
	private TextView mScaleTextView;
	private TextView mRotateTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_anim_demo);

		initView();
		initAnimation();
	}

	private void initView() {
		mTranslateTextView = (TextView) super.findViewById(R.id.anim_translate_tv);
		mAlphaTextView = (TextView) super.findViewById(R.id.anim_alpha_tv);
		mScaleTextView = (TextView) super.findViewById(R.id.anim_scale_tv);
		mRotateTextView = (TextView) super.findViewById(R.id.anim_rotate_tv);
	}

	private void initAnimation() {
		initShowTranslateAnim();
		initHindTranslateAnim();
	}

	/** 显示 */
	private void initShowTranslateAnim() {
		this.mShowTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -1,
				Animation.RELATIVE_TO_SELF, 0);
		mShowTranslateAnimation.setDuration(500);
	}

	/** 隐藏 */
	private void initHindTranslateAnim() {
		this.mHindTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -1);
		mHindTranslateAnimation.setDuration(500);
	}

	/** 淡入淡出 */
	private void showAlpahAnim() {
		// 创建一个AnimationSet对象,参入为true表示使用Animation的interpolator,false则是使用自己的
		AnimationSet animationSet = new AnimationSet(true);
		// 创建一个AlphaAnimation对象,参数从完全的透明度到完全不透明
		this.mAlphaAnimation = new AlphaAnimation(1, 0);
		mAlphaAnimation.setDuration(500);
		animationSet.addAnimation(mAlphaAnimation);
//		animationSet.addAnimation(mHindTranslateAnimation);
		this.mAlphaTextView.startAnimation(animationSet);
	}

	/** 缩放 */
	private void showScaleAnimation() {
		// 参数1,3: X,Y轴的初始值    参数2,4: X,Y轴收缩后的值
		// 参数5,7: X,Y轴坐标的类型  参数6,8: X,Y轴的值, 0.5f表示以自身控件的一半长度为X,Y轴
		mScaleAnimation = new ScaleAnimation(1, 0.1f, 1, 0.1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mScaleAnimation.setDuration(500);
		mScaleTextView.startAnimation(mScaleAnimation);
	}

	/** 旋转 */
	private void showRotateAnimation() {
		// 参数1:旋转开始角度    参数2: 旋转结束角度   后4个参数用于设置围绕着旋转的中心点
		// 参数3,5: 确定X,Y轴坐标的类型,有 ABSOLUT(绝对坐标), RELATIBE_TO_SELF(相对于自身坐标), RELATIVE_TO_PARENT(相对于父控件的坐标)
		// 参数4,6: 0.5f 表示以自身控件的一半长度为x,y轴
		mRotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setDuration(500);
		mRotateTextView.startAnimation(mRotateAnimation);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.anim_translate_but: // 位移
				if(isHind) { // 隐藏的状态,要显示
					mTranslateTextView.startAnimation(mShowTranslateAnimation);
					mTranslateTextView.setVisibility(View.VISIBLE);
				} else { // 隐藏
					mTranslateTextView.startAnimation(mHindTranslateAnimation);
					mTranslateTextView.setVisibility(View.INVISIBLE);
				}
				isHind = !isHind;
				break;
			case R.id.anim_alpha_but: // 淡入淡出
				showAlpahAnim();
				break;
			case R.id.anim_scale_but: // 缩放
				showScaleAnimation();
				break;
			case R.id.anim_rotate_but: // 旋转
				showRotateAnimation();
				break;
			case R.id.back_iv:
				finish();
				break;
			default:
				break;
		}
	}
}
