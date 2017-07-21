package com.zy.demo.mydemo.treeview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.demo.mydemo.R;
import com.zy.demo.mydemo.popupwindow.DownPopupWindow;
import com.zy.demo.mydemo.popupwindow.PopupWindowActivity;
import com.zy.demo.mydemo.popupwindow.WrapViewGroup;

import org.xutils.common.util.DensityUtil;

/**
 * Created by zhaoya on 2016/5/31.
 */
public class TreeViewDemo extends Activity implements View.OnClickListener{
	private Context context;
	private TreeView mTreeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_treeview);
		this.context = this;
		initTreeView();
		this.findViewById(R.id.back_iv).setOnClickListener(this);
	}

	private void initTreeView() {
		this.mTreeView = (TreeView) this.findViewById(R.id.treeview);

		FruitOnClickListenerImpl listener = new FruitOnClickListenerImpl();

		TreeView branch1 = new TreeView(context);
		branch1.setTitle("branch1");
		branch1.addFruit("fruit1-1asdfafadfgasdgfasdf", listener, true);
		branch1.addFruit("fruit1-2asdfadfasfd", listener, true);
		branch1.addFruit("fruit1-3", listener, true);
		branch1.addFruit("fruit1-4asdfasdfasf", listener, true);
		branch1.addFruit("fruit1-5", listener, true);

		TreeView branch2 = new TreeView(context);
		branch2.setTitle("branch2");
		branch2.addFruit("fruit2-1", listener, true);
		branch2.addFruit("fruit2-2adfasdfasdfasdf", listener, true);

		this.mTreeView.addBranch(branch1);
		this.mTreeView.addBranch(branch2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_iv:
				finish();
				break;
			case R.id.showPopWindowBut:
				showPopupWindowTree(v);
//				startActivity(new Intent(context, PopupWindowActivity.class));
				break;
			default:
				break;
		}
	}

	private class FruitOnClickListenerImpl implements TreeView.FruitOnClickListener {

		@Override
		public void onClick(View view, String content) {
			Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
		}
	}

	private void showPopupWindowTree(View view) {
		DownPopupWindow popupWindow = new DownPopupWindow(context);
		popupWindow.initView(new DownPopupWindow.IInitView() {
			@Override
			public View initView(LayoutInflater inflater) {
				View view = inflater.inflate(R.layout.view_popwindow_down_domains, null);
				WrapViewGroup wrapViewGroup = (WrapViewGroup) view.findViewById(R.id.checkedVG); // 已选
				LinearLayout contentLL = (LinearLayout) view.findViewById(R.id.contentLL); // 用来添加树的布局

				wrapViewGroup.addView(getTextView("测试1"));
//                wrapViewGroup.addView(getTextView("测试1阿大声道AEFADFADWF"));
//                wrapViewGroup.addView(getTextView("测试阿斯达所大所大去哇发 爱的无法df"));
//                wrapViewGroup.addView(getTextView("测试阿斯达所大所大去哇发 爱的无法df"));
//                wrapViewGroup.addView(getTextView("测试阿斯达所大所大去哇发 爱的无法df"));
//                wrapViewGroup.addView(getTextView("测试阿斯达所大所大去哇发 爱的无法df"));
//                wrapViewGroup.addView(getTextView("测试1"));

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				NewTreeView treeView1 = new NewTreeView(context);
				treeView1.initView();
				treeView1.setLayoutParams(params);
				treeView1.setIsExpand(true);
				treeView1.setTitle("刑事辩护");

				NewTreeView treeView2 = new NewTreeView(context);
				treeView2.initView();
				treeView2.setIsExpand(true);
				treeView2.setTitle("企业顾问");
				treeView2.addFruit("合同事务", null, true);
				treeView2.addFruit("公司融资", null, true);
				treeView2.addFruit("知识产权", null, true);
				treeView2.addFruit("税务", null, true);
				treeView2.addFruit("劳动与人事", null, true);
				treeView2.addFruit("犯罪预防与政府合规", null, true);
				treeView2.addFruit("重大投资与战略事务", null, true);
				treeView2.addFruit("重大投资与战略事务", null, true);
				treeView2.addFruit("重大投资与战略事务", null, true);
				treeView2.addFruit("重大投资与战略事务", null, true);
				treeView2.addFruit("重大投资与战略事务", null, true);
				treeView2.addFruit("重大投资与战略事务", null, true);
				treeView2.addFruit("重大投资与战略事务", null, true);
				treeView2.addFruit("重大投资与战略事务", null, true);

				NewTreeView branch = new NewTreeView(context);
				branch.setTitle("名师类");
				branch.addFruit("婚姻家庭", null, true);
				branch.addFruit("交通事故", null, true);
				branch.addFruit("婚姻家庭", null, true);
				branch.addFruit("婚姻家庭", null, true);
				treeView2.addBranch(branch);

				contentLL.addView(treeView1, params);
				contentLL.addView(treeView2, params);
				return view;
			}

			@Override
			public View initDownView(View contentView) {
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				int windowHeight = wm.getDefaultDisplay().getHeight(); // 屏幕高度
				int maxPopHeight = windowHeight - DensityUtil.dip2px(100); // 弹出布局最大高度,这里是离屏幕顶部100dp

				View popView = contentView.findViewById(R.id.pop_layout);

				ViewGroup.LayoutParams params = popView.getLayoutParams();
				params.height = maxPopHeight;
				popView.setLayoutParams(params);
				return popView;
			}
		});
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	private TextView getTextView(String str) {
		int padding = DensityUtil.dip2px(10);
		TextView textView = new TextView(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textView.setPadding(padding, padding, padding, padding);
		textView.setLayoutParams(params);
		textView.setText(str);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			textView.setTextColor(getResources().getColor(R.color.fruitCheckedColor, null));
//		} else {
//			textView.setTextColor(getResources().getColor(R.color.fruitCheckedColor));
//		}
//		textView.setBackgroundResource(R.drawable.bg_fruit_checked);
		return textView;
	}
}
