package com.zy.demo.mydemo.pulltorefresh;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zy.demo.mydemo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View）
 * 还有一个上拉头，参考自博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 */
public class PullToRefreshLayout extends RelativeLayout
{
	private static final String TAG = "PullToRefreshLayout";

	private static final int STATE_INIT = 0; // 初始状态
	private static final int STATE_RELEASE_TO_REFRESH = 1; // 释放刷新
	private static final int STATE_REFRESHING = 2; // 正在刷新
	private static final int STATE_RELEASE_TO_LOAD = 3; // 释放加载
	private static final int STATE_LOADING = 4; // 正在加载
	private static final int STATE_DONE = 5; // 操作完毕

	public static final int SUCCEED = 0; // 刷新成功
	public static final int FAILED = 1; // 刷新失败

	private int MOVE_SPEED = 8; // 回滚速度

	private int state = STATE_INIT; // 当前状态
	private OnRefreshListener mListener; // 刷新加载的回调接口
	private boolean isLayout = false; // 是否是第一次执行布局
	private boolean isTouch = false; // 在刷新过程中滑动操作

	private float downX, downY, lastX, lastY; // 按下的点的Y坐标, 上一个事件点Y坐标
	private float pullDownY, pullUpY; // 上拉下拉的距离, >0 表示下拉, <0 表示上拉
	private float refreshDist = 200, loadDist = 200; // 释放刷新.加载的距离
	private float radio = 2; // 手指滑动距离与下拉头的滑动距离比,中间会随正切函数变化,越拉越费力

	private RotateAnimation rotateAnimation; // 下拉箭头转180°动画
	private RotateAnimation refreshingAnimation; // 均匀旋转动画

	private View refreshView; // 下拉头
	private View pullView; // 下拉的箭头
	private View refreshingView; // 正在刷新的图标
	private View refreshStateImageView; // 刷新结果的图标
	private TextView refreshStateTextView; // 刷新结果:成功或失败

	private View loadView; // 上拉头
	private View pullUpView; // 上拉的箭头
	private View loadingView; // 正在加载的图标
	private View loadStateImageView; // 加载结果的图标
	private TextView loadStateTextView; // 加载结果

	private View pullableView; // 实现了Pullable接口的View;
	private int mEvents; // 过滤多点触碰

	private MyTimer timer;

	// 这两个变量用来控制pull的方向,如果不加控制,当情况满足可上拉又可下拉时没法下拉
	private boolean canPullDown = true;
	private boolean canPullUp = true;

	public PullToRefreshLayout(Context context) {
		super(context);
		initView(context);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		timer = new MyTimer(updateHandler);
		rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
		// 添加均匀转动动画
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}

	/** 执行自动回滚的handler */
	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 回弹速度随拉动距离moveDeltaY增大而增大
			MOVE_SPEED = (int) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch) {
				// 正在刷新,且没有往上推的话,则悬停,显示"正在刷新..."
				if (state == STATE_REFRESHING && pullDownY <= refreshDist){
					pullDownY = refreshDist;
					timer.cancel();
				} else if(state == STATE_LOADING && -pullUpY <= loadDist) {
					pullUpY = -loadDist;
					timer.cancel();
				}
			}
			if (pullDownY > 0) {
				pullDownY -= MOVE_SPEED;
			} else if (pullUpY < 0) {
				pullUpY += MOVE_SPEED;
			}
			if (pullDownY < 0) { // 已完成回弹
				pullDownY = 0;
				pullView.clearAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != STATE_REFRESHING && state != STATE_LOADING) {
					changeState(STATE_INIT);
				}
				timer.cancel();
			}
			if (pullUpY > 0) { // 已完成回弹
				pullUpY = 0;
				pullUpView.clearAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != STATE_REFRESHING && state != STATE_LOADING) {
					changeState(STATE_INIT);
				}
				timer.cancel();
			}
			// 刷新布局,会自动调用onLayout
			requestLayout();
		}
	};

	/** 设置上拉下拉监听器 */
	public void setOnRefreshListener(OnRefreshListener listener) {
		this.mListener = listener;
	}

	/** 隐藏上拉下拉头 */
	private void hide() {
		timer.schedule(5);
	}

	/**
	 * 完成刷新操作,显示刷新结果,刷新完成后一定要调用这个方法
	 * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void refreshFinish(int refreshResult) {
		refreshingView.clearAnimation();
		refreshingView.setVisibility(View.GONE);
		switch (refreshResult) {
			case SUCCEED: // 刷新成功
				refreshStateTextView.setText("刷新成功");
				refreshStateImageView.setVisibility(View.VISIBLE);
				refreshStateImageView.setBackgroundResource(R.drawable.refresh_succeed);
				break;
			case FAILED: // 刷新失败
			default:
				refreshStateTextView.setText("刷新失败");
				refreshStateImageView.setVisibility(View.VISIBLE);
				refreshStateImageView.setBackgroundResource(R.drawable.refresh_failed);
				break;
		}
		// 刷新结果停留1秒
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				changeState(STATE_DONE);
				hide();
			}
		}.sendEmptyMessageDelayed(0, 1000);
	}

	/**
	 * 加载完毕,显示加载结果,加载完成后一定要调用这个方法
	 * @param refreshResult
	 */
	public void loadFinish(int refreshResult) {
		loadingView.clearAnimation();
		loadingView.setVisibility(View.GONE);
		switch (refreshResult) {
			case SUCCEED: // 加载成功
				loadStateTextView.setText("加载成功");
				loadStateImageView.setVisibility(View.VISIBLE);
				loadStateImageView.setBackgroundResource(R.drawable.load_succeed);
				break;
			case FAILED: // 刷新失败
			default:
				loadStateTextView.setText("加载失败");
				loadStateImageView.setVisibility(View.VISIBLE);
				loadStateImageView.setBackgroundResource(R.drawable.load_failed);
				break;
		}
		// 刷新结果停留1秒
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				changeState(STATE_DONE);
				hide();
			}
		}.sendEmptyMessageDelayed(0, 1000);
	}

	/** 修改状态 */
	private void changeState(int to) {
		state = to;
		switch (state) {
			case STATE_INIT:
				// 下拉布局初始状态
				refreshStateImageView.setVisibility(View.GONE);
				refreshStateTextView.setText("下拉刷新");
				pullView.clearAnimation();
				pullView.setVisibility(View.VISIBLE);
				// 上拉布局初始状态
				loadStateImageView.setVisibility(View.GONE);
				loadStateTextView.setText("上拉加载");
				pullUpView.clearAnimation();
				pullUpView.setVisibility(View.VISIBLE);
				break;
			case STATE_RELEASE_TO_REFRESH: // 释放刷新状态
				refreshStateTextView.setText("释放立即刷新");
				pullView.startAnimation(rotateAnimation);
				break;
			case STATE_REFRESHING: // 正在刷新
				pullView.clearAnimation();
				pullView.setVisibility(View.GONE);
				refreshingView.setVisibility(View.VISIBLE);
				refreshingView.startAnimation(refreshingAnimation);
				refreshStateTextView.setText("正在刷新");
				break;
			case STATE_RELEASE_TO_LOAD:
				loadStateTextView.setText("释放立即加载");
				pullUpView.startAnimation(rotateAnimation);
				break;
			case STATE_LOADING:
				pullUpView.clearAnimation();
				pullUpView.setVisibility(View.GONE);
				loadingView.setVisibility(View.VISIBLE);
				loadingView.startAnimation(refreshingAnimation);
				loadStateTextView.setText("正在加载");
				break;
			case STATE_DONE:
				// 刷新或加载完毕，啥都不做
				break;
		}
	}

	/** 不限制上拉或下拉 */
	private void releasePull() {
		canPullDown = true;
		canPullUp = true;
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		return super.dispatchTouchEvent(ev);
//	}

	/** 在适当的时候拦截触摸事件，这里指的适当的时候是当mContentView滑动到顶部，
	 * 并且是下拉时拦截触摸事件，否则不拦截，交给其child */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		boolean isIntercept = false;
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				downY = event.getY();
				lastX = downX;
				lastY = downY;
				mEvents = 0;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_POINTER_UP:
				// 过滤多点触碰
				mEvents = -1;
				break;
			case MotionEvent.ACTION_MOVE:
				int distanceX = (int) Math.abs(event.getX() - downX);
				int distanceY = (int) Math.abs(event.getY() - downY);

				if (((Pullable) pullableView).canPullDown()) {
					// 可以下拉,正在加载时不能下拉, 拦截
					if (event.getY() - downY > 0 || state == STATE_REFRESHING) { // 下拉拦截
						isIntercept = true;
						Log.d("onInterceptTouchEvent", "下拉拦截");
					}
				} else if (((Pullable) pullableView).canPullUp()) {
					// 可以上拉,正在刷新时不能上拉 拦截
					if (event.getY() - downY < 0 || state == STATE_LOADING) {
						isIntercept = true;
						Log.d("onInterceptTouchEvent", "上拉拦截");
					}
				} else { // 不拦截
					isIntercept = false;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				break;
			default:
				break;
		}
		return isIntercept;
	}

	/**  在这里处理触摸事件以达到下拉刷新或者上拉自动加载的问题 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				downY = event.getY();
				lastX = downX;
				lastY = downY;
				mEvents = 0;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_POINTER_UP:
				// 过滤多点触碰
				mEvents = -1;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mEvents == 0) {
					int distanceX = (int) Math.abs(event.getX() - downX);
					int distanceY = (int) Math.abs(event.getY() - downY);
//					Log.d(TAG, "distanceX=" + distanceX + ", distanceY=" + distanceY + ",isFirstScrool=" + isFirstScrool + ", isHorizontal=" + isHorizontal);

					Log.d("onInterceptTouchEvent", "pullDownY=" + pullDownY + ",pullUpY=" + pullUpY + ",canPullDown=" + canPullDown + ",canPullUp=" + canPullUp);
					if (((Pullable) pullableView).canPullDown()  && state != STATE_LOADING) {
						// 可以下拉,正在加载时不能下拉, 拦截
						pullDownY = pullDownY + (event.getY() - lastY) / radio;
						if (pullDownY < 0) { // 往上拉回
							pullDownY = 0;
							canPullDown = false;
							canPullUp = true;
						}
						if (pullDownY > getMeasuredHeight()) {
							pullDownY = getMeasuredHeight(); // 不能拉过满屏
						}
						if (state == STATE_REFRESHING) {
							// 正在刷新的时候触摸移动
							isTouch = true;
						}
					} else if (((Pullable) pullableView).canPullUp() && canPullUp && state != STATE_REFRESHING) {
						// 可以上拉,正在刷新时不能上拉, 横向滑动时不能上拉, 拦截
						pullUpY = pullUpY + (event.getY() - lastY) / radio;
						if (pullUpY > 0) { // 往下拉回
							pullUpY = 0;
							canPullDown = true;
							canPullUp = false;
						}
						if (pullUpY < -getMeasuredHeight()) {
							pullUpY = -getMeasuredHeight();
						}
						if (state == STATE_LOADING) {
							// 正在加载时触摸移动
							isTouch = true;
						}
						Log.d("onInterceptTouchEvent", "pullDownY=" + pullDownY + ",pullUpY=" + pullUpY);
					} else {
						releasePull();
					}
				} else {
					mEvents = 0;
				}
				lastY = event.getY();
//				// 根据下拉距离改变比例
				radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
				requestLayout();
				if (pullDownY <= refreshDist && state == STATE_RELEASE_TO_REFRESH) {
					changeState(STATE_INIT); // 如果下拉距离没达到刷新的距离且当前状态时释放刷新, 改变状态为下拉刷新
				}
				if (pullDownY >= refreshDist && state == STATE_INIT) {
					changeState(STATE_RELEASE_TO_REFRESH); // 如果下拉距离达到刷新的距离且当前状态是初始状态, 改变状态为释放刷新
				}
				// 下面是判断上拉加载的，同上，注意pullUpY是负值
				if (-pullUpY <= loadDist && state == STATE_RELEASE_TO_LOAD) {
					changeState(STATE_INIT);
				}
				if (-pullUpY >= loadDist && state == STATE_INIT) {
					changeState(STATE_RELEASE_TO_LOAD);
				}

				// 因为刷新和加载操作不能同时进行, 所以pullDownY和pullUpY不会同时为0,因此这里用(pullDownY + Math.abs(pullUpY))就可以不对当前状态作区分了
				if ((pullDownY + Math.abs(pullUpY)) > 8) {
					// 防止下拉过程中误触发长按事件和点击事件
					event.setAction(MotionEvent.ACTION_CANCEL);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (pullDownY > refreshDist || -pullUpY > loadDist) {
					isTouch = false; // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
				}
				if (state == STATE_RELEASE_TO_REFRESH) { // 刷新
					changeState(STATE_REFRESHING);
					if (mListener != null) {
						mListener.onRefresh(this);
					}
				} else if (state == STATE_RELEASE_TO_LOAD) { // 加载
					changeState(STATE_LOADING);
					if (mListener != null) {
						mListener.onLoad(this);
					}
				}
				hide();
				break;
			default:
				break;
		}
		return super.onTouchEvent(event);
	}

//	private boolean isFirstScrool = true; // 记录是否是第一次滑动,通过第一次的滑动趋向判断是否是横向滑动
//	private boolean isHorizontal = false; // 是否是水平滑动
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		switch (event.getActionMasked()) {
//			case MotionEvent.ACTION_DOWN:
//				downX = event.getX();
//				downY = event.getY();
//				lastX = downX;
//				lastY = downY;
//				timer.cancel();
//				mEvents = 0;
//				releasePull();
//				break;
//			case MotionEvent.ACTION_POINTER_DOWN:
//			case MotionEvent.ACTION_POINTER_UP:
//				// 过滤多点触碰
//				mEvents = -1;
//				break;
//			case MotionEvent.ACTION_MOVE:
//				if (mEvents == 0) {
//					int distanceX =(int) Math.abs( event.getX() - downX);
//					int distanceY = (int) Math.abs(event.getY() - downY);
//					Log.d(TAG,"distanceX=" + distanceX + ", distanceY=" + distanceY + ",isFirstScrool=" + isFirstScrool + ", isHorizontal=" + isHorizontal);
//
//					if (isFirstScrool) { // 根据第一次滑动趋向判断是横滑还是竖滑
//						if (Math.abs(distanceX) > 20 || Math.abs(distanceY) > 20) { // 通过最早滑动趋向判断是否是横滑
//							isFirstScrool = false;
//							if (distanceY <= distanceX) { // 根据屏幕宽高比判断是否是横向滑动
//								isHorizontal = true; // 横向滑动
//							} else {
//								isHorizontal = false; // 竖向滑动
//							}
//						}
//					}
//
//					// 可以下拉,正在加载时不能下拉,横滑时不能下拉
//					if (((Pullable) pullableView).canPullDown() && canPullDown && state != STATE_LOADING && !isHorizontal) {
//						// 对实际滑动距离做缩小,造成用力拉的感觉
//						pullDownY = pullDownY + (event.getY() - lastY) / radio;
//						if (pullDownY < 0) { // 往上拉回
//							pullDownY = 0;
//							canPullDown = false;
//							canPullUp = true;
//						}
//						if (pullDownY > getMeasuredHeight()) {
//							pullDownY = getMeasuredHeight(); // 不能拉过满屏
//						}
//						if (state == STATE_REFRESHING) {
//							// 正在刷新的时候触摸移动
//							isTouch = true;
//						}
//					} else if (((Pullable) pullableView).canPullUp() && canPullUp && state != STATE_REFRESHING && !isHorizontal) {
//						// 可以上拉,正在刷新时不能上拉, 横向滑动时不能上拉
//						pullUpY = pullUpY + (event.getY() - lastY) / radio;
//						if (pullUpY > 0) { // 往下拉回
//							pullUpY = 0;
//							canPullDown = true;
//							canPullUp = false;
//						}
//						if (pullUpY < -getMeasuredHeight()) {
//							pullUpY = -getMeasuredHeight();
//						}
//						if (state == STATE_LOADING) {
//							// 正在加载时触摸移动
//							isTouch = true;
//						}
//					} else {
//						releasePull();
//					}
//				} else {
//					mEvents = 0;
//				}
//				lastY = event.getY();
//				// 根据下拉距离改变比例
//				radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
//				requestLayout();
//				if (pullDownY <= refreshDist && state == STATE_RELEASE_TO_REFRESH) {
//					changeState(STATE_INIT); // 如果下拉距离没达到刷新的距离且当前状态时释放刷新, 改变状态为下拉刷新
//				}
//				if (pullDownY >= refreshDist && state == STATE_INIT) {
//					changeState(STATE_RELEASE_TO_REFRESH); // 如果下拉距离达到刷新的距离且当前状态是初始状态, 改变状态为释放刷新
//				}
//				// 下面是判断上拉加载的，同上，注意pullUpY是负值
//				if (-pullUpY <= loadDist && state == STATE_RELEASE_TO_LOAD) {
//					changeState(STATE_INIT);
//				}
//				if (-pullUpY >= loadDist && state == STATE_INIT) {
//					changeState(STATE_RELEASE_TO_LOAD);
//				}
//
//				// 因为刷新和加载操作不能同时进行, 所以pullDownY和pullUpY不会同时为0,因此这里用(pullDownY + Math.abs(pullUpY))就可以不对当前状态作区分了
//				if ((pullDownY + Math.abs(pullUpY)) > 8) {
//					// 防止下拉过程中误触发长按事件和点击事件
//					event.setAction(MotionEvent.ACTION_CANCEL);
//				}
//				break;
//			case MotionEvent.ACTION_UP:
//				if (pullDownY > refreshDist || -pullUpY > loadDist) {
//					isTouch = false; // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
//				}
//				if (state == STATE_RELEASE_TO_REFRESH) { // 刷新
//					changeState(STATE_REFRESHING);
//					if (mListener != null) {
//						mListener.onRefresh(this);
//					}
//				} else if (state == STATE_RELEASE_TO_LOAD) { // 加载
//					changeState(STATE_LOADING);
//					if (mListener != null) {
//						mListener.onLoad(this);
//					}
//				}
//				hide();
//				isFirstScrool = true; // 抬起后再次记录第一滑动趋向
//				break;
//			default:
//				break;
//		}
//		return super.dispatchTouchEvent(event);
//	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (!isLayout) { // 第一次进来的时候初始化
			refreshView = getChildAt(0);
			pullableView = getChildAt(1);
			loadView = getChildAt(2);
			isLayout = true;

			// 初始化下拉布局
			pullView = refreshView.findViewById(R.id.pull_icon);
			refreshStateTextView = (TextView) refreshView.findViewById(R.id.state_tv);
			refreshingView = refreshView.findViewById(R.id.refreshing_icon);
			refreshStateImageView = refreshView.findViewById(R.id.state_iv);
			// 初始化上拉布局
			pullUpView = loadView.findViewById(R.id.pullup_icon);
			loadStateTextView = (TextView) loadView.findViewById(R.id.loadstate_tv);
			loadingView = loadView.findViewById(R.id.loading_icon);
			loadStateImageView = loadView.findViewById(R.id.loadstate_iv);

			refreshDist = ((ViewGroup) refreshView).getChildAt(0).getMeasuredHeight();
			loadDist = ((ViewGroup) loadView).getChildAt(0).getMeasuredHeight();
		}
		// 改变子控件的布局,这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
		refreshView.layout(0, (int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(), refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
		pullableView.layout(0, (int) (pullDownY + pullUpY), pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight());
		loadView.layout(0, (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(), loadView.getMeasuredWidth(), (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight() + loadView.getMeasuredHeight());
	}

	/** 刷新回调接口 */
	public interface OnRefreshListener {
		/** 下拉刷新 */
		public void onRefresh(PullToRefreshLayout pullToRefreshLayout);
		/** 上拉加载 */
		public void onLoad(PullToRefreshLayout pullToRefreshLayout);
	}

	private class MyTimer {
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer (Handler handler) {
			this.handler = handler;
			this.timer = new Timer();
		}

		/** 计时器开始,period 循环间隔 */
		public void schedule(long period) {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		/** 停止任务 */
		public void cancel() {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}

		private class MyTask extends TimerTask{
			private Handler handler;

			public MyTask(Handler handler) {
				this.handler = handler;
			}
			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}
		}
	}
}
