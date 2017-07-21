package com.zy.demo.mydemo.doublescrollview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.zy.demo.mydemo.R;

import java.util.ArrayList;
import java.util.List;

/** 仿淘宝商品详情页的上下翻页 */
public class DoubleScrollViewDemo2 extends AppCompatActivity {

	private Context context;
//	private CyclicRollView mViewPager;
	private ViewPager mViewPager;
	private int bannerCount = 3;
	private List<View> mViewList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_double_scroll_view_demo2);
		this.context = this;

//		this.mViewPager = (CyclicRollView) super.findViewById(R.id.CyclicRollView);
//		CyclicViewAdapter adapter = new CyclicViewAdapter(context) {
//			@Override
//			public int getViewCount() {
//				return bannerCount;
//			}
//
//			class ViewHolder {
//				ImageView image;
//			}
//
//			@Override
//			public View getView(View convertView, int position) {
//				ViewHolder holder = null;
//				if (convertView == null) {
//					convertView = LayoutInflater.from(context).inflate(R.layout.item_pager_banner, null);
//					holder = new ViewHolder();
//					holder.image = (ImageView) convertView.findViewById(R.id.viewpager_image);
//					convertView.setTag(holder);
//				} else {
//					holder = (ViewHolder) convertView.getTag();
//				}
//				final int i = Math.abs(position % bannerCount);
//				switch (i) {
//					case 0:
//						holder.image.setBackgroundColor(Color.GREEN);
//						break;
//					case 1:
//						holder.image.setBackgroundColor(Color.RED);
//						break;
//					case 2:
//						holder.image.setBackgroundColor(Color.BLUE);
//						break;
//					default:
//						break;
//				}
//				convertView.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Toast.makeText(context, "" + i, Toast.LENGTH_SHORT).show();
//						switch(i) {
//							case 0:
//								break;
//							case 1:break;
//							case 2:break;
//							default:break;
//						}
//					}
//				});
//				return convertView;
//			}
//		};
//
//		mViewPager.setSwitchInterval(3000); // 设置切换时间间隔
//		mViewPager.setSwitchSpeed(500); // 设置切换速度，也就是动画时长
//		mViewPager.setCurrentItem(0);
//
//		mViewPager.getIndicatorView().setIndicatorSpaceSize(5); // 指示器间隔
//		mViewPager.getIndicatorView().setIndicatorSize(7); // 指示器大小
//		mViewPager.getIndicatorView().setIndicatorRes(R.drawable.pager_gray_point, R.drawable.pager_lock); // 指示器资源
//		mViewPager.setAdapter(adapter);


		this.mViewPager = (ViewPager) super.findViewById(R.id.ViewPager);

		LayoutInflater inflater = LayoutInflater.from(context);
		mViewList = new ArrayList<>();
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
		this.mViewPager.setAdapter(new PagerAdapter() {
			@Override
			public int getCount() {
				return bannerCount;
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
//				super.destroyItem(container, position, object);
				container.removeView(mViewList.get(position));//删除页卡
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
//				return super.instantiateItem(container, position);
				container.addView(mViewList.get(position), 0);//添加页卡
				return mViewList.get(position);
			}
		});

	}
}
