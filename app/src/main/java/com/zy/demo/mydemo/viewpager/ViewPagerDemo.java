package com.zy.demo.mydemo.viewpager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;
import com.zy.demo.mydemo.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaoya on 2016/6/14.
 */
public class ViewPagerDemo extends FragmentActivity {

	private ArrayList<Fragment> fragments;
	private ArrayList<Fragment> fragments2;
	private ViewPager viewPager;
	private TextView tab_game;
	private TextView tab_app;
	private int line_width;
	private View line;

	// 垂直滑动的ViewPager
	private VerticalViewPager mVerticalViewPager;
	private VerticalPagerAdapter mVerticalPagerAdapter;
	private boolean isContinue = true; // 是否继续滑动
	private int mCurrentNum = 0;
//	private ImageHandler handler = new ImageHandler(new WeakReference<ViewPagerDemo>(this));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		initView(); // 普通ViewPager

		initVerticalViewPager();

		initViewPagerIndicator();
	}

	private void initView() {
		tab_game = (TextView) findViewById(R.id.tab_game);
		tab_app = (TextView) findViewById(R.id.tab_app);
		line = findViewById(R.id.line);


		// 初始化TextView动画
		ViewPropertyAnimator.animate(tab_app).scaleX(1.2f).setDuration(0);
		ViewPropertyAnimator.animate(tab_app).scaleY(1.2f).setDuration(0);

		fragments = new ArrayList<Fragment>();
		fragments.add(new PagerDemoFragment());
		fragments.add(new GameFragment());
		line_width = getWindowManager().getDefaultDisplay().getWidth()
				/ fragments.size();
		line.getLayoutParams().width = line_width;
		line.requestLayout();
		viewPager = (ViewPager) findViewById(R.id.viewPager);
//		viewPager.setAdapter(adapter);
		viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}
		});

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				float tagerX = (position + positionOffset) * line_width;
				ViewPropertyAnimator.animate(line).translationX(tagerX).setDuration(0);
			}

			@Override
			public void onPageSelected(int position) {
				changeState(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		tab_game.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(1);

			}
		});

		tab_app.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(0);
			}
		});
	}

	private List<View> mViewList;

	/** 初始化垂直ViewPager */
	private void initVerticalViewPager() {
		this.mVerticalViewPager = (VerticalViewPager) this.findViewById(R.id.VerticalViewPager);
		mViewList = new ArrayList<>();
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		for (int i = 0; i < 3; i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.item_pager_banner, null);
			ImageView image = (ImageView) view.findViewById(R.id.viewpager_image);
			if (i == 0) {
				image.setImageResource(R.mipmap.image_dashen0);
			}
			if (i == 1) {
				image.setImageResource(R.mipmap.image_dashen1);
			}
			if (i == 2) {
				image.setImageResource(R.mipmap.image_dashen2);
			}
			mViewList.add(view);
		}
		mVerticalPagerAdapter = new VerticalPagerAdapter() {
			@Override
			public int getCount() {
//				return 3;
				//设置成最大，使用户看不到边界
				return Integer.MAX_VALUE;
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
//				container.removeView(mViewList.get(position));
//				不要在这里调用removeView
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
//				container.addView(mViewList.get(position), 0);
//				return mViewList.get(position);
				//对ViewPager页号求模取出View列表中要显示的项
				position %= mViewList.size();
				if (position<0){
					position = mViewList.size()+position;
				}
				View view = mViewList.get(position);
				//如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
				ViewParent vp = view.getParent();
				if (vp!=null){
					ViewGroup parent = (ViewGroup)vp;
					parent.removeView(view);
				}
				container.addView(view);
				//add listeners here if necessary
				return view;
			}
		};

		mVerticalViewPager.setAdapter(mVerticalPagerAdapter);

		mVerticalViewPager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//				Toast.makeText(ViewPagerDemo.this, "CurrentItem=" + mVerticalViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
			}

			//配合Adapter的currentItem字段进行设置。
			@Override
			public void onPageSelected(int position) {
				mCurrentNum = position;
//				handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
			}

			//覆写该方法实现轮播效果的暂停和恢复
			@Override
			public void onPageScrollStateChanged(int state) {
//				switch (state) {
//					case ViewPager.SCROLL_STATE_DRAGGING:
//						handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
//						break;
//					case ViewPager.SCROLL_STATE_IDLE:
//						handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
//						break;
//					default:
//						break;
//				}
			}
		});

		mCurrentNum = mViewList.size() * 100;
		mVerticalViewPager.setCurrentItem(mCurrentNum);//默认在中间，使用户看不到边界

		//开始轮播效果
//		handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);

		mVerticalViewPager.setOnTouchListener(new OnTouchListenerImpl());
		start();
	}

	private void initViewPagerIndicator() {
		ViewPager viewPager2 = (ViewPager) this.findViewById(R.id.viewPager2);
		PageIndicator indicator = (PageIndicator) this.findViewById(R.id.indicator);
		fragments2 = new ArrayList<>();
		fragments2.add(new GameFragment());
		fragments2.add(new GameFragment());
		fragments2.add(new GameFragment());
		fragments2.add(new GameFragment());
		fragments2.add(new GameFragment());
		fragments2.add(new GameFragment());
		fragments2.add(new GameFragment());

		viewPager2.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return fragments2.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragments2.get(arg0);
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return "title" + position;
			}
		});
		indicator.setViewPager(viewPager2);
//		indicator.setFades(false); // 不隐藏
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				setIndicatorViewSelected(position);
			}

			// 在这里设置被选中时候选项卡变化的效果
			private void setIndicatorViewSelected(int pos) {
//				for (int i = 0; i < list.size(); i++) {
//					if (i == pos) {
//						View v = list.getChildAt(i);
//						TextView tv = (TextView) v;
//						// Android Holo 样式的蓝色
//						tv.setTextColor(0xff33B5E5);
//					} else {
//						View v = mLinearLayout.getChildAt(i);
//						TextView tv = (TextView) v;
//						tv.setTextColor(COLOR_NORMAL);
//					}
//				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	/* 根据传入的值来改变状态 */
	private void changeState(int arg0) {
		if (arg0 == 0) {
			tab_app.setTextColor(getResources().getColor(R.color.green));
			tab_game.setTextColor(getResources().getColor(R.color.gray));
			ViewPropertyAnimator.animate(tab_app).scaleX(1.2f).setDuration(200);
			ViewPropertyAnimator.animate(tab_app).scaleY(1.2f).setDuration(200);
			ViewPropertyAnimator.animate(tab_game).scaleX(1.0f)
					.setDuration(200);
			ViewPropertyAnimator.animate(tab_game).scaleY(1.0f)
					.setDuration(200);

		} else {
			tab_game.setTextColor(getResources().getColor(R.color.green));
			tab_app.setTextColor(getResources().getColor(R.color.gray));
			ViewPropertyAnimator.animate(tab_app).scaleX(1.0f).setDuration(200);
			ViewPropertyAnimator.animate(tab_app).scaleY(1.0f).setDuration(200);
			ViewPropertyAnimator.animate(tab_game).scaleX(1.2f)
					.setDuration(200);
			ViewPropertyAnimator.animate(tab_game).scaleY(1.2f)
					.setDuration(200);
		}
	}

//	private static class ImageHandler extends Handler {
//		/**
//		 * 请求更新显示的View。
//		 */
//		protected static final int MSG_UPDATE_IMAGE  = 1;
//		/**
//		 * 请求暂停轮播。
//		 */
//		protected static final int MSG_KEEP_SILENT   = 2;
//		/**
//		 * 请求恢复轮播。
//		 */
//		protected static final int MSG_BREAK_SILENT  = 3;
//		/**
//		 * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
//		 * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
//		 * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
//		 */
//		protected static final int MSG_PAGE_CHANGED  = 4;
//
//		//轮播间隔时间
//		protected static final long MSG_DELAY = 3000;
//
//		//使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
//		private WeakReference<ViewPagerDemo> weakReference;
//		private int currentItem;
//
//		protected ImageHandler(WeakReference<ViewPagerDemo> wk){
//			weakReference = wk;
//		}
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
////			Log.d(LOG_TAG, "receive message " + msg.what);
//			ViewPagerDemo activity = weakReference.get();
//			if (activity==null){
//				//Activity已经回收，无需再处理UI了
//				return ;
//			}
//			switch (msg.what) {
//				case MSG_UPDATE_IMAGE:
//					//检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
//					if (activity.handler.hasMessages(MSG_UPDATE_IMAGE)){
//						activity.handler.removeMessages(MSG_UPDATE_IMAGE);
//					}
//					currentItem++;
//					activity.mVerticalViewPager.setCurrentItem(currentItem);
//					//准备下次播放
//					activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//					break;
//				case MSG_KEEP_SILENT:
//					//只要不发送消息就暂停了
//					break;
//				case MSG_BREAK_SILENT:
//					activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//					break;
//				case MSG_PAGE_CHANGED:
//					//记录当前的页号，避免播放的时候页面显示不正确。
//					currentItem = msg.arg1;
//					break;
//				default:
//					break;
//			}
//		}
//	}

	private ScheduledExecutorService scheduledExecutorService;

	/** 开始轮播 */
	public void start() {
		// 用一个定时器 来完成图片切换
		// Timer 与 ScheduledExecutorService 实现定时器的效果

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 通过定时器 来完成 每2秒钟切换一个图片
		// 经过指定的时间后，执行所指定的任务
		// scheduleAtFixedRate(command, initialDelay, period, unit)
		// command 所要执行的任务
		// initialDelay 第一次启动时 延迟启动时间
		// period 每间隔多次时间来重新启动任务
		// unit 时间单位
		scheduledExecutorService.scheduleAtFixedRate(new ViewPagerTask(), 5, 5,
				TimeUnit.SECONDS);
	}

	/** 停止轮播 */
	public void stop() {
		scheduledExecutorService.shutdown();
	}

	// 用来完成图片切换的任务
	private class ViewPagerTask implements Runnable {
		public void run() {
			// Handler来实现图片切换
			if (isContinue) {
//				what.incrementAndGet(); 	// 相当于++
				mCurrentNum++;
				handler.handleMessage(new Message());
			}
		}
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler.post(new Runnable() {
				@Override
				public void run() {
					mVerticalViewPager.setCurrentItem(mCurrentNum);
				}
			});
		}
	};

	// 触摸ViewPager时不滚动
	private class OnTouchListenerImpl implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					isContinue = false;
					break;
				case MotionEvent.ACTION_UP:
					isContinue = true;
					break;
				default:
					isContinue = true;
					break;
			}
			return false;
		}
	}
}