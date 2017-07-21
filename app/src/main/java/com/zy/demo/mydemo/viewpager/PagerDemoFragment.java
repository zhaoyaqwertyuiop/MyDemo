package com.zy.demo.mydemo.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zy.demo.mydemo.R;

import java.util.ArrayList;
import java.util.List;

public class PagerDemoFragment extends Fragment {
	private SyncHorizontalScrollView mSyncHorizontalScrollView;
	private ViewPager mViewpager;
	private Context context;
//	private String[] titles = new String[]{"title0", "title1", "title2", "title3"};
	private String[] titles = new String[]{"title0", "title1", "title2", "title3", "title4", "title5", "title6", "title7", "title8"};
	private int count = 5; // 显示5个标签
	private List<Fragment> fragments;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		this.context = getActivity();
		View view = View.inflate(getActivity(), R.layout.fragment_pager, null);
		this.mSyncHorizontalScrollView = (SyncHorizontalScrollView) view.findViewById(R.id.SyncHorizontalScrollView);
		this.mViewpager = (ViewPager) view.findViewById(R.id.ViewPager);
		initViewPager();
		mSyncHorizontalScrollView.initView(mViewpager, titles, count, context);
		return view;
	}

	private void initViewPager() {
		fragments = new ArrayList<>();
		for (int i = 0; i < titles.length; i++) {
			GameFragment fragment = new GameFragment();
			fragments.add(fragment);
			fragment.setText("game" + i);
		}
		mViewpager.setAdapter(new FragmentStatePagerAdapter(((FragmentActivity) context).getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}
		});
	}
}
