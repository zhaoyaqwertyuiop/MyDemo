package com.zy.demo.mydemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.demo.mydemo.amap.LocationActivity;
import com.zy.demo.mydemo.anim.AnimDemo;
import com.zy.demo.mydemo.autotextview.TextViewDemo;
import com.zy.demo.mydemo.circlemenulayout.CircleDemo;
import com.zy.demo.mydemo.doublescrollview.DoubleScrollViewDemo2;
import com.zy.demo.mydemo.galleryfinal.GalleryFinalDemo;
import com.zy.demo.mydemo.popupwindow.PopupWindowDemo;
import com.zy.demo.mydemo.pulltorefresh.PullToRefreshDemo;
import com.zy.demo.mydemo.sharesdk.onekeyshare.ShareSDKDemo;
import com.zy.demo.mydemo.treeview.TreeViewDemo;
import com.zy.demo.mydemo.util.DividerItemDecoration;
import com.zy.demo.mydemo.view.RoundImageView;
import com.zy.demo.mydemo.viewpager.ViewPagerDemo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	private Context context;
	private RecyclerView mRecyclerView;
	private MainAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	public void initView() {
		this.context = this;
		this.mRecyclerView = (RecyclerView) this.findViewById(R.id.main_recyclerview);
		LinearLayoutManager manager = new LinearLayoutManager(context);
		manager.setOrientation(LinearLayoutManager.VERTICAL); // 垂直滑动
		this.mRecyclerView.setLayoutManager(manager);
		mRecyclerView.setHasFixedSize(true); // 保证每个item高度一样的话,这句代码提高性能

		// TODO 在这里添加新的demo
		final List<Class> classlist = new ArrayList<>();
		classlist.add(TreeViewDemo.class);
		classlist.add(PullToRefreshDemo.class);
		classlist.add(CircleDemo.class);
		classlist.add(TextViewDemo.class);
		classlist.add(ViewPagerDemo.class);
		classlist.add(AnimDemo.class);
		classlist.add(PopupWindowDemo.class);
		classlist.add(DoubleScrollViewDemo2.class);
		classlist.add(LocationActivity.class);
		classlist.add(ShareSDKDemo.class);
		classlist.add(GalleryFinalDemo.class);

		final List<String> strList = new ArrayList<>();
		for (Class c: classlist) {
			strList.add(c.getSimpleName());
		}
		this.mAdapter = new MainAdapter(context, strList);

		this.mAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				String str = strList.get(position);
				Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, classlist.get(position));
				startActivity(intent);
			}
		});

		this.mRecyclerView.setAdapter(mAdapter);
		this.mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST)); // 设置分割线

	}

	private static class MainAdapter extends RecyclerView.Adapter {

		private Context context;
		private List<String> mList;
		private OnItemClickListener mOnItemClickListener;
		public MainAdapter(Context context, List<String> list) {
			this.context = context;
			this.mList = list;
		}
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = View.inflate(parent.getContext(), R.layout.item_main, null);
			ViewHolder holder = new ViewHolder(view);
			return holder;
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
			ViewHolder hold = (ViewHolder) holder;
			String str = mList.get(position);
			hold.name.setText(str);
			hold.image.setImageResource(R.drawable.ic_head);

			if(mOnItemClickListener != null) {
				hold.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mOnItemClickListener.onItemClick(v, position);
					}
				});
			}
		}

		@Override
		public int getItemCount() {
			return mList.size();
		}

		private class ViewHolder extends RecyclerView.ViewHolder{
			TextView name;
			RoundImageView image;
			public ViewHolder(View itemView) {
				super(itemView);
				this.name = (TextView) itemView.findViewById(R.id.item_main_name_tv);
				this.image = (RoundImageView) itemView.findViewById(R.id.item_main_image_riv);
			}
		}

		public interface OnItemClickListener {
			void onItemClick(View view, int position);
		}

		public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
			this.mOnItemClickListener = mOnItemClickListener;
		}
	}

	// 两次返回键退出
	private long mExitTime;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
