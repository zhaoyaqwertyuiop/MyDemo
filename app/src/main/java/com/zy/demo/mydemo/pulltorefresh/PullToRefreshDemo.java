package com.zy.demo.mydemo.pulltorefresh;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.zy.demo.mydemo.R;
import com.zy.demo.mydemo.util.DensityUtil;
import com.zy.demo.mydemo.viewpager.CyclicRollView;
import com.zy.demo.mydemo.viewpager.CyclicViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PullToRefreshDemo extends AppCompatActivity implements View.OnClickListener{

	private Context context;
	private PullToRefreshLayout mPullToRefreshLayout;
	private ListView listView;

	// ViewPager
	private View headView; // viewpager加在listView的头部
	private CyclicRollView mViewPager;
	private List<View> mViewList; // 把需要滑动的页卡放进该list中
	private int bannerCount = 3; // 滚动条数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pulltorefresh);
		this.context = this;

		mPullToRefreshLayout = ((PullToRefreshLayout) this.findViewById(R.id.pulltorefresh_layout));
		mPullToRefreshLayout.setOnRefreshListener(new MyListener());
		this.listView = (ListView) this.findViewById(R.id.pulltorefresh_listview);
		
		initViewPager();
		initListView();

		this.listView.addHeaderView(this.headView);

		this.findViewById(R.id.back_iv).setOnClickListener(this);
	}

	/** 初始化viewpager */
	private void initViewPager() {
		this.mViewList = new ArrayList<>();

		LayoutInflater inflater = LayoutInflater.from(this);
		this.headView = inflater.inflate(R.layout.viewpager, null);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 150));
		headView.setLayoutParams(params);
//		this.mViewPager = (ViewPager) headView.findViewById(R.id.pager_viewpager);
		this.mViewPager = (CyclicRollView) headView.findViewById(R.id.pager_viewpager);
//		ViewGroup group = (ViewGroup) headView.findViewById(R.id.pager_viewgroup);

		for (int i = 0; i < bannerCount; i++) {
			View newView = inflater.inflate(R.layout.item_pager_banner, null);
			ImageView imageView = (ImageView) newView.findViewById(R.id.viewpager_image);
			switch (i) {
				case 0:
					imageView.setBackgroundColor(Color.GREEN);
					break;
				case 1:
					imageView.setBackgroundColor(Color.RED);
					break;
				case 2:
					imageView.setBackgroundColor(Color.BLUE);
					break;
				default:
					break;
			}
			mViewList.add(newView);
		}
		CyclicViewAdapter adapter = new CyclicViewAdapter(context) {
			@Override
			public int getViewCount() {
				return bannerCount;
			}

			@Override
			public View getView(View convertView, int position) {
				ViewHolder holder = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(context).inflate(R.layout.item_pager_banner, null);
					holder = new ViewHolder();
					holder.image = (ImageView) convertView.findViewById(R.id.viewpager_image);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				int i = Math.abs(position % bannerCount);
				switch (i) {
					case 0:
						holder.image.setBackgroundColor(Color.GREEN);
						break;
					case 1:
						holder.image.setBackgroundColor(Color.RED);
						break;
					case 2:
						holder.image.setBackgroundColor(Color.BLUE);
						break;
					default:
						break;
				}
				return convertView;
			}
		};

		mViewPager.setSwitchInterval(3000); // 设置切换时间间隔
		mViewPager.setSwitchSpeed(500); // 设置切换速度，也就是动画时长
		mViewPager.setCurrentItem(0);

		mViewPager.getIndicatorView().setIndicatorSpaceSize(5); // 指示器间隔
		mViewPager.getIndicatorView().setIndicatorSize(5); // 指示器大小
//		mViewPager.getIndicatorView().setIndicatorRes(R.drawable.pager_gray_point, R.drawable.pager_lock); // 指示器资源
		mViewPager.getIndicatorView().setIndicatorRes(R.drawable.bg_circle_white, R.drawable.bg_circle_blue); // 指示器资源

		mViewPager.setAdapter(adapter);
	}

	private class ViewHolder {
		ImageView image;
	}

	private void initListView() {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			list.add("这里是item " + i);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

		this.listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_iv:
				finish();
				break;
			default:
				break;
		}
	}

	private class MyListener implements PullToRefreshLayout.OnRefreshListener {

		// 下拉刷新
		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}.sendEmptyMessageDelayed(0, 2000);
		}

		// 上拉加载
		@Override
		public void onLoad(final PullToRefreshLayout pullToRefreshLayout) {
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					pullToRefreshLayout.loadFinish(PullToRefreshLayout.SUCCEED);
				}
			}.sendEmptyMessageDelayed(0, 2000);
		}
	}
}
