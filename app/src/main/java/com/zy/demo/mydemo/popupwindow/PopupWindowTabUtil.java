package com.zy.demo.mydemo.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.zy.demo.mydemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 配合PopupWindowTab弹出popupwindow
 * Created by zhaoya on 2016/6/20.
 */
public class PopupWindowTabUtil {

	private PopupWindowTab mPopupWindowTab;

	private Context mContext;

	private List<String> dataList = new ArrayList<>();
	private List<String> tab1List, tab2List, tab3List, tab4List;

	public void initView(Context context, final PopupWindowTab mPopupWindowTab) {
		this.mContext = context;
		this.mPopupWindowTab = mPopupWindowTab;
		View view = LayoutInflater.from(context).inflate(R.layout.view_popupwindow, null);
		final GridView gridView = (GridView) view.findViewById(R.id.pop_gridview);

		final BaseAdapter adapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return dataList.size();
			}

			@Override
			public Object getItem(int position) {
				return dataList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview_text, null);
					holder.text = (TextView) convertView.findViewById(R.id.item_pop_gridview_tv);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				final String str = dataList.get(position);
				holder.text.setText(str);

				// 给选中的项赋背景
				switch (mPopupWindowTab.getCurrentTab()) {
					case 1:
						if (mPopupWindowTab.getContent1Id() == position) {
							holder.text.setBackgroundResource(R.drawable.shape_gridview_item_chose_bg);
						} else {
							holder.text.setBackgroundResource(R.drawable.shape_gridview_item_bg);
						}
						break;
					case 2:
						if (mPopupWindowTab.getContent2Id() == position) {
							holder.text.setBackgroundResource(R.drawable.shape_gridview_item_chose_bg);
						} else {
							holder.text.setBackgroundResource(R.drawable.shape_gridview_item_bg);
						}
						break;
					case 3:
						if (mPopupWindowTab.getContent3Id() == position) {
							holder.text.setBackgroundResource(R.drawable.shape_gridview_item_chose_bg);
						} else {
							holder.text.setBackgroundResource(R.drawable.shape_gridview_item_bg);
						}
						break;
					case 4:
						if (mPopupWindowTab.getContent4Id() == position) {
							holder.text.setBackgroundResource(R.drawable.shape_gridview_item_chose_bg);
						} else {
							holder.text.setBackgroundResource(R.drawable.shape_gridview_item_bg);
						}
						break;
					default:
						break;
				}

				// 设置点击事件
				holder.text.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (mPopupWindowTab.getCurrentTab()) {
							case 1:
								mPopupWindowTab.getmContent1TextView().setText(str);
								if (mPopupWindowTab.getContent1Id() != position) {
									((ViewGroup) gridView.getChildAt(mPopupWindowTab.getContent1Id())).getChildAt(0).setBackgroundResource(R.drawable.shape_gridview_item_bg);
									((ViewGroup) gridView.getChildAt(position)).getChildAt(0).setBackgroundResource(R.drawable.shape_gridview_item_chose_bg);
									mPopupWindowTab.setContent1Id(position);
								}
								break;
							case 2:
								mPopupWindowTab.getmContent2TextView().setText(str);
								if (mPopupWindowTab.getContent2Id() != position) {
									((ViewGroup) gridView.getChildAt(mPopupWindowTab.getContent2Id())).getChildAt(0).setBackgroundResource(R.drawable.shape_gridview_item_bg);
									((ViewGroup) gridView.getChildAt(position)).getChildAt(0).setBackgroundResource(R.drawable.shape_gridview_item_chose_bg);
									mPopupWindowTab.setContent2Id(position);
								}
								break;
							case 3:
								mPopupWindowTab.getmContent3TextView().setText(str);
								if (mPopupWindowTab.getContent3Id() != position) {
									((ViewGroup) gridView.getChildAt(mPopupWindowTab.getContent3Id())).getChildAt(0).setBackgroundResource(R.drawable.shape_gridview_item_bg);
									((ViewGroup) gridView.getChildAt(position)).getChildAt(0).setBackgroundResource(R.drawable.shape_gridview_item_chose_bg);
									mPopupWindowTab.setContent3Id(position);
								}
								break;
							case 4:
								mPopupWindowTab.getmContent4TextView().setText(str);

								if (mPopupWindowTab.getContent4Id() != position) {
									((ViewGroup) gridView.getChildAt(mPopupWindowTab.getContent4Id())).getChildAt(0).setBackgroundResource(R.drawable.shape_gridview_item_bg);
									((ViewGroup) gridView.getChildAt(position)).getChildAt(0).setBackgroundResource(R.drawable.shape_gridview_item_chose_bg);
									mPopupWindowTab.setContent4Id(position);
								}
								break;
							default:
								break;
						}
						mPopupWindowTab.changeTab(0);
					}
				});
				return convertView;
			}

			class ViewHolder {
				TextView text;
			}
		};
		gridView.setAdapter(adapter);

		this.mPopupWindowTab.init(context, view, gridView, new PopupWindowTab.OnChangePopupShowListener() {

			@Override
			public void initShowPopWindow(int currentTab) {
				int currentContentId = 0;
				switch (currentTab) {
					case 1:
						setData(tab1List);
						currentContentId = mPopupWindowTab.getContent1Id();
						break;
					case 2:
						setData(tab2List);
						currentContentId = mPopupWindowTab.getContent2Id();
						break;
					case 3:
						setData(tab3List);
						currentContentId = mPopupWindowTab.getContent3Id();
						break;
					case 4:
						setData(tab4List);
						currentContentId = mPopupWindowTab.getContent4Id();
						break;
					default:
						break;
				}
				adapter.notifyDataSetChanged();
				for(int i = 0; i < dataList.size(); i++) {
					if (gridView.getChildAt(i) == null || ((ViewGroup)gridView.getChildAt(i)).getChildAt(0) == null) {
						continue;
					}
					if (currentContentId == i) {
						((ViewGroup)gridView.getChildAt(i)).getChildAt(0).setBackgroundResource(R.drawable.shape_gridview_item_chose_bg);
					} else {
						((ViewGroup)gridView.getChildAt(i)).getChildAt(0).setBackgroundResource(R.drawable.shape_gridview_item_bg);
					}
				}
			}
		});

		// 空白处触摸就关闭popupWindow
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = gridView.getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (y > height) {
						mPopupWindowTab.changeTab(0);
					}
				}
				return true;
			}
		});
	}

	/** 调用init后要设置数据 */
	public void initData(List<String> tab1List, List<String> tab2List, List<String> tab3List, List<String> tab4List) {
		this.tab1List = (tab1List == null) ? new ArrayList<String>() : tab1List;
		this.tab2List = (tab2List == null) ? new ArrayList<String>() : tab2List;
		this.tab3List = (tab3List == null) ? new ArrayList<String>() : tab3List;
		this.tab4List = (tab4List == null) ? new ArrayList<String>() : tab4List;
		if (this.tab1List.size() == 0) {
			this.mPopupWindowTab.getmTab1().setVisibility(View.GONE);
		}
		if (this.tab2List.size() == 0) {
			this.mPopupWindowTab.getmTab2().setVisibility(View.GONE);
		}
		if (this.tab3List.size() == 0) {
			this.mPopupWindowTab.getmTab3().setVisibility(View.GONE);
		}
		if (this.tab4List.size() == 0) {
			this.mPopupWindowTab.getmTab4().setVisibility(View.GONE);
		}
	}

	private void setData(List<String> list) {
		if (dataList == null) {
			dataList = new ArrayList<>();
		}
		dataList.clear();
		for (String str : list) {
			dataList.add(str);
		}
	}
}
