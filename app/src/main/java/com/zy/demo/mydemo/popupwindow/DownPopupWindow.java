package com.zy.demo.mydemo.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.zy.demo.mydemo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 从底部弹出popWin
 */
public class DownPopupWindow extends PopupWindow {

	private View mContentView;
	private View mDownView; // 下方弹出的view
	private Context context;

	private boolean canShow = true; // 动画过程中不能执行show,不然会崩掉
	private boolean isDismissing = false; // 标记是否正在执行关闭动画,防止重复执行

	private float alphaRatio = 0.8f; // 透明度比例 (1.0f 到 0.0f 之间, 值越大,弹出时背景透明度越大)

	public DownPopupWindow(Context context) {
		super(context);
		this.context = context;
	}

	public void initView(IInitView callBack){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentView = callBack.initView(inflater); // 内部调用，外部实现
		mDownView = callBack.initDownView(mContentView);
		this.setContentView(mContentView);
		this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);

		ColorDrawable dw = new ColorDrawable(0x000000);
		this.setBackgroundDrawable(dw);

		//mContentView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mContentView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = mDownView.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	public interface IInitView {
		/** 外部实现布局的初始化 */
		public View initView(LayoutInflater inflater);

		/**
		 * 配置下方弹出的view
		 * @param contentView 弹出的整个layout的view
		 * @return
		 */
		public View initDownView(View contentView);
	}

	@Override
	public void showAtLocation(final View parent, final int gravity, final int x, final int y) {
		if (!canShow) {
			return;
		}
		super.showAtLocation(parent, gravity, x, y);
		final AnimationSet set = new AnimationSet(true);
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.set_in_down);
		set.addAnimation(animation);
		set.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(final Animation animation) {
				if (timer != null) { // 开始动画还没完
					timer.cancel();
					timer = null;
				}
				timer = new Timer();
				final long period = 1;
				final TimerTask timerTask = new TimerTask() {
					long time = animation.getDuration();
					long currentTime = 0;
					@Override
					public void run() {

						handler.post(new Runnable() {
							@Override
							public void run() {
								currentTime += period;
								if (currentTime >= time) {
									if(timer != null) {
										timer.cancel();
										timer = null;
									}
								}
								float alpha = 1.0f - currentTime * alphaRatio / time ; // 区间从1.0f到0.3f
//								Log.d("TAG", "alpha=" + alpha + ",time=" + time + ",currentTime=" + currentTime);
								backgroundAlpha(alpha);
							}
						});
					}
				};
				timer.schedule(timerTask, 0, 1);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		mDownView.startAnimation(set);
	}

	private Timer timer;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	@Override
	public void dismiss() {
		if (isDismissing) {
			return;
		}
		isDismissing = true; // 如果正在执行直接返回,否则设置正在执行标记
		canShow = false;
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.set_out_down);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(final Animation animation) {
				if (timer != null) { // 开始动画还没完
					timer.cancel();
					timer = null;
				}
				timer = new Timer();
				final long period = 1;
				final TimerTask timerTask = new TimerTask() {
					long time = animation.getDuration();
					long currentTime = 0;
					WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
					float startAlpha = lp.alpha;
					@Override
					public void run() {

						handler.post(new Runnable() {
							@Override
							public void run() {
								currentTime += period;
								if (currentTime >= time) {
									if (timer != null) {
										timer.cancel();
										timer = null;
									}
								}
//								float alpha = startAlpha + currentTime * 0.7f / time; // 区间从1.0f到0.3f
								float alpha = startAlpha + currentTime * alphaRatio / time; // 区间从1.0f到0.2f
//								Log.d("TAG", "alpha=" + alpha + ",time=" + time + ",currentTime=" + currentTime);
								if (alpha > 1) {
									alpha = 1;
								}
								backgroundAlpha(alpha);
							}
						});
					}
				};
				timer.schedule(timerTask, 0, 1);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						DownPopupWindow.super.dismiss();
					}
				});

				if (timer != null) {
					timer.cancel();
					timer = null;
				}
				canShow = true;
				isDismissing = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		mDownView.startAnimation(animation);
	}

	// 设置屏幕透明度
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0~1.0
		((Activity)context).getWindow().setAttributes(lp);
	}

	/** 从屏幕下方弹出 */
	public void showPop() {
		this.showAtLocation(((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
	}
}
