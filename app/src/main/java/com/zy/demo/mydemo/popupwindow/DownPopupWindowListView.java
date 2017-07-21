package com.zy.demo.mydemo.popupwindow;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zy.demo.mydemo.R;

import java.util.List;

/**
 * 从底部弹出popWin
 */
public class DownPopupWindowListView extends DownPopupWindow {

	private List<String> dataList;
	private Context context;
	public DownPopupWindowListView(Context context, final List<String> dataList, final CallBack callBack) {
		super(context);
		this.context = context;
		this.dataList = dataList;
		super.initView(new IInitView() {
			@Override
			public View initView(LayoutInflater inflater) {
				View view = inflater.inflate(R.layout.view_popwindow_down_list, null);
				ListView listView = (ListView) view.findViewById(R.id.downPopLV);
				BaseAdapterImpl adapter = new BaseAdapterImpl();
				if (dataList.size() > 5) { // 当大于5条数据时,控制listview的高度为5.5条数据的高度
					ViewGroup.LayoutParams params = listView.getLayoutParams();
					View listItem = adapter.getView(0, null, listView);
					// 计算子项View 的宽高
					listItem.measure(0, 0);
					int itemHeight = listItem.getMeasuredHeight();
					params.height = (int) (itemHeight * 5.5);
				}
				listView.setAdapter(new BaseAdapterImpl());
				callBack.initListView(DownPopupWindowListView.this, listView);
				return view;
			}

			@Override
			public View initDownView(View contentView) {
				View downView = contentView.findViewById(R.id.pop_layout);
				return downView;
			}
		});
	}

	public interface CallBack{
		/**  */
		public void initListView(DownPopupWindowListView popupWindow, ListView listView);
	}

	private class BaseAdapterImpl extends BaseAdapter {

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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.view_popwindow_down_list_item, null);
				holder.text = (TextView) convertView.findViewById(R.id.itemTV);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(TextUtils.isEmpty(dataList.get(position))? "" : dataList.get(position));
			return convertView;
		}

		class ViewHolder {
			TextView text;
		}
	}
}
