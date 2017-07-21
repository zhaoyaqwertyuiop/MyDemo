package com.zy.demo.mydemo.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wheelview.CurrencyWheelView;
import com.zy.demo.mydemo.R;
import com.zy.demo.mydemo.util.PermissionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class PopupWindowDemo extends AppCompatActivity implements View.OnClickListener, GalleryFinal.OnHanlderResultCallback{

	private Context context;

	private PopupWindowTab mPopupWindowTab;
	private List<String> tab1List, tab2List, tab3List, tab4List;

	private DownPopupWindowCamara mDownPopupWindowCamara;
	private DownPopupWindowListView downPopupWindowListView;
	private DownPopupWindow downPopupWindowWheelView;

	public static final int REQUEST_CODE_GALLERYFINAL = 1; // GalleryFinal 请求码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_popup_window_demo);
		this.context = this;

		initView();
	}

	/** PopupWindowTab */
	private void initView() {
		this.mPopupWindowTab = (PopupWindowTab) super.findViewById(R.id.popupWindowTab);

		initData();

		PopupWindowTabUtil tabWindow = new PopupWindowTabUtil();
		tabWindow.initView(context, this.mPopupWindowTab);
		tabWindow.initData(tab1List, tab2List, tab3List, tab4List);
	}


	/**
	 * 模拟数据
	 */
	private void initData() {
		tab1List = new ArrayList<>();
		tab1List.add("全部");
		tab1List.add("一居");
		tab1List.add("二居");
		tab1List.add("三居");
		tab1List.add("四居");
		tab1List.add("小户型");
		tab1List.add("公寓");
		tab1List.add("复式");
		tab1List.add("别墅");

		tab2List = new ArrayList<>();
		tab2List.add("全部");
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());
		tab2List.add("tab2" + tab2List.size());

		tab3List = new ArrayList<>();
		tab3List.add("全部");
		tab3List.add("tab3" + tab3List.size());
		tab3List.add("tab3" + tab3List.size());
		tab3List.add("tab3" + tab3List.size());
		tab3List.add("tab3" + tab3List.size());
		tab3List.add("tab3" + tab3List.size());
		tab3List.add("tab3" + tab3List.size());
		tab3List.add("tab3" + tab3List.size());
		tab3List.add("tab3" + tab3List.size());
		tab3List.add("tab3" + tab3List.size());

		tab4List = new ArrayList<>();
		tab4List.add("全部");
		tab4List.add("tab4" + tab4List.size());
		tab4List.add("tab4" + tab4List.size());
		tab4List.add("tab4" + tab4List.size());
		tab4List.add("tab4" + tab4List.size());
		tab4List.add("tab4" + tab4List.size());
		tab4List.add("tab4" + tab4List.size());
		tab4List.add("tab4" + tab4List.size());
		tab4List.add("tab4" + tab4List.size());
		tab4List.add("tab4" + tab4List.size());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_iv:
				finish();
				break;
			case R.id.popup_demo_but: // 从下方弹出popupWindow
				showDownPopupWindowCamera();
				break;
			case R.id.popup_demo_but2:
				Toast.makeText(PopupWindowDemo.this, "点击生效", Toast.LENGTH_SHORT).show();
				break;
			case R.id.downPopupWindowListView:
				showDownPopupWindowListView();
				break;
			case R.id.downPopupWindowWheelView:
				showDownPopupWindowWheelView();
				break;
			default:
				mPopupWindowTab.onClick(v);
				break;
		}
	}

	/** 从下方弹出popUpWindow */
	private void showDownPopupWindowCamera() {
		if (mDownPopupWindowCamara == null) {
			mDownPopupWindowCamara = new DownPopupWindowCamara(context);
			// 拍照
			mDownPopupWindowCamara.setCallBack(new DownPopupWindowCamara.CallBack() {
				@Override
				public void onCamera() {
					if (PermissionUtil.requestPermission((Activity) context, android.Manifest.permission.CAMERA)) {
						GalleryFinal.openCamera(REQUEST_CODE_GALLERYFINAL, (GalleryFinal.OnHanlderResultCallback) context);
					}
				}

				@Override
				public void onPick() {
				}

				@Override
				public void onCancel() {
					mDownPopupWindowCamara.dismiss();
				}
			});
		}
		// 设置layout在PopupWindow中显示的位置
//		mDownPopupWindow.showAtLocation(mPopupWindowTab, Gravity.BOTTOM, 0, 0);
		mDownPopupWindowCamara.showPop();
	}

	private void showDownPopupWindowListView() {
		if (downPopupWindowListView == null) {
			final List<String> list = new ArrayList<>();
			for (int i = 0; i < 20; i++) {
				list.add("" + i);
			}
			downPopupWindowListView = new DownPopupWindowListView(this, list, new DownPopupWindowListView.CallBack() {
				@Override
				public void initListView(final DownPopupWindowListView popupWindow, ListView listView) {
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Toast.makeText(context, list.get(position), Toast.LENGTH_SHORT).show();
							popupWindow.dismiss();
						}
					});
				}
			});
		}
		downPopupWindowListView.showPop();
	}

	private void showDownPopupWindowWheelView() {
		if (downPopupWindowWheelView == null) {
			final List<CurrencyWheelView.IData> list1 = new ArrayList<>();
			list1.add(new WheelData("0", "0", "0"));
			list1.add(new WheelData("1", "0", "0"));
			list1.add(new WheelData("1", "0", "0"));
			list1.add(new WheelData("3", "0", "0"));
			list1.add(new WheelData("4", "0", "0"));
			list1.add(new WheelData("5", "0", "0"));
			list1.add(new WheelData("6", "0", "0"));
			list1.add(new WheelData("7", "0", "0"));
			list1.add(new WheelData("8", "0", "0"));
			list1.add(new WheelData("9", "0", "0"));

			final List<CurrencyWheelView.IData> list2 = new ArrayList<>();
			list2.add(new WheelData("0", "0", "0"));
			list2.add(new WheelData("1", "1", "0"));
			list2.add(new WheelData("1", "2", "0"));
			list2.add(new WheelData("3", "3", "0"));
			list2.add(new WheelData("4", "4", "0"));
			list2.add(new WheelData("5", "5", "0"));
			list2.add(new WheelData("6", "6", "0"));
			list2.add(new WheelData("7", "7", "0"));
			list2.add(new WheelData("8", "8", "0"));
			list2.add(new WheelData("9", "9", "0"));

			final List<CurrencyWheelView.IData> list3 = new ArrayList<>();
			list3.add(new WheelData("0", "0", "0"));
			list3.add(new WheelData("1", "1", "1"));
			list3.add(new WheelData("1", "2", "2"));
			list3.add(new WheelData("3", "3", "3"));
			list3.add(new WheelData("4", "4", "4"));
			list3.add(new WheelData("5", "5", "5"));
			list3.add(new WheelData("6", "6", "6"));
			list3.add(new WheelData("7", "7", "7"));
			list3.add(new WheelData("8", "8", "8"));
			list3.add(new WheelData("9", "9", "9"));

			downPopupWindowWheelView = new DownPopupWindow(context);
			downPopupWindowWheelView.initView(new DownPopupWindow.IInitView() {
				@Override
				public View initView(LayoutInflater inflater) {
					CurrencyWheelView currencyWheelView =  new CurrencyWheelView(context, 2, new CurrencyWheelView.IInitDataCallBack() {
						@Override
						public List<CurrencyWheelView.IData> initOneList() {
							return list1;
						}

						@Override
						public List<CurrencyWheelView.IData> initTwoList(CurrencyWheelView.IData data, int position) {
							return list2;
						}

						@Override
						public List<CurrencyWheelView.IData> initThreeList(CurrencyWheelView.IData data, int position) {
							return list3;
						}
					});
					currencyWheelView.getOkBtn().setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							downPopupWindowWheelView.dismiss();
						}
					});
					currencyWheelView.getOneWheelTV().setText("省");
					currencyWheelView.getTwoWheelTV().setText("市");
					currencyWheelView.getThreeWheelTV().setText("区(县)");
					currencyWheelView.setOneWheelItem(0);
					currencyWheelView.setTwoWheelItem(0);
					currencyWheelView.setThreeWheelItem(0);
					return currencyWheelView;
				}

				@Override
				public View initDownView(View contentView) {
					return contentView.findViewById(R.id.pop_layout);
				}
			});
		}
		downPopupWindowWheelView.showPop();
	}

	private class WheelData implements CurrencyWheelView.IData {

		String one;
		String two;
		String three;

		WheelData(String one, String two, String three) {
			this.one = one;
			this.two = two;
			this.three = three;
		}

		@Override
		public String getOneWheelName() {
			return one;
		}

		@Override
		public String getTwoWheelName() {
			return two;
		}

		@Override
		public String getThreeWheelName() {
			return three;
		}

		@Override
		public Object getOneWheelId() {
			return one;
		}

		@Override
		public Object getTwoWheelId() {
			return two;
		}

		@Override
		public Object getThreeWheelId() {
			return three;
		}

		public String getOne() {
			return one;
		}

		public void setOne(String one) {
			this.one = one;
		}

		public String getTwo() {
			return two;
		}

		public void setTwo(String two) {
			this.two = two;
		}

		public String getThree() {
			return three;
		}

		public void setThree(String three) {
			this.three = three;
		}
	}

	@Override
	public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

	}

	@Override
	public void onHanlderFailure(int requestCode, String errorMsg) {

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case PermissionUtil.REQUESTCODE_PREM:
				List<String> permissionList = new ArrayList<>();
				for (int i = 0; i < permissions.length; i++) {
					if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
						permissionList.add(permissions[i]);
					}
				}
				if (permissionList.size() > 0) {
					String msg = "需要权限: ";
					for(String permission : permissionList) {
						msg =  msg + permission + "  ";
					}
					Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
				break;
		}
	}
}
