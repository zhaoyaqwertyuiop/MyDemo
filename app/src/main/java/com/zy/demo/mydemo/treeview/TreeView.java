package com.zy.demo.mydemo.treeview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zy.demo.mydemo.R;
import com.zy.demo.mydemo.popupwindow.WrapViewGroup;
import com.zy.demo.mydemo.util.ScreenUtils;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 树
 */
public class TreeView extends LinearLayout {

	private Context context;
	private ImageView icon;
	/** 标题 */
	private TextView title;
	/** 右侧显示数量 */
	private TextView count;
	private int numCount;
	/** 果实布局 */
	private WrapViewGroup fruitRL;
	/** 分支布局 */
	private LinearLayout branchRL;
	/** 果实数据 */
	private List<TextView> childList;
	/** 分支数据 */
	private List<TreeView> branchList;
	/** 是否展开,默认不展开 */
	private boolean isExpand = false;

	public TreeView(Context context) {
		super(context);
		initView(context);
	}

	public TreeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public TreeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
		View view = View.inflate(context, R.layout.view_item_treeview, this); // 初始化布局文件
		this.icon = (ImageView) view.findViewById(R.id.child_view_icon);
		this.title = (TextView) view.findViewById(R.id.child_view_title_tv);
		this.count = (TextView) view.findViewById(R.id.child_view_count_tv);
		this.fruitRL = (WrapViewGroup) view.findViewById(R.id.child_view_fruit_rl);
		this.branchRL = (LinearLayout) view.findViewById(R.id.child_view_Branch_rl);

		if (this.isExpand == false) {// 默认没有展开
			fruitRL.setVisibility(View.GONE);
			branchRL.setVisibility(View.GONE);
		} else { // 默认展开
			fruitRL.setVisibility(View.VISIBLE);
			branchRL.setVisibility(View.VISIBLE);
		}

		ChangeExpandListener listener = new ChangeExpandListener();
		view.findViewById(R.id.child_view_title_rl).setOnClickListener(listener); // title部分设置单击事件
	}

	private class ChangeExpandListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// if (isExpand) { // 之前为展开状态,点击闭合
			// icon.setImageResource(R.drawable.unexpand);
			// fruitRL.setVisibility(View.GONE);
			// branchRL.setVisibility(View.GONE);
			// } else { // 之前为闭合状态,点击展开
			// icon.setImageResource(R.drawable.expand);
			// fruitRL.setVisibility(View.VISIBLE);
			// branchRL.setVisibility(View.VISIBLE);
			// }
			isExpand = !isExpand;
			setIsExpand(isExpand);
		}
	}

	/** 设置是否展开 */
	public void setIsExpand(boolean isExpand) {
		this.isExpand = isExpand;
		if (isExpand) { // 展开状态
			icon.setImageResource(R.drawable.expand);
			fruitRL.setVisibility(View.VISIBLE);
			branchRL.setVisibility(View.VISIBLE);
		} else { // 闭合状态
			icon.setImageResource(R.drawable.unexpand);
			fruitRL.setVisibility(View.GONE);
			branchRL.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置标题
	 */
	public void setTitle(String title) {
		this.title.setText(title);
	}

	/**
	 * 添加果实
	 *
	 * @param content 果实显示的字符串
	 * @param onClick 果实点击事件监听
	 */
	@SuppressLint("NewApi")
	public void addFruit(final String content, final FruitOnClickListener onClick, boolean isClickable) {
		if (this.childList == null) {
			this.childList = new ArrayList<TextView>();
		}
		TextView but = new TextView(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		but.setLayoutParams(params);
		but.setText(content);
		int padding = DensityUtil.dip2px(15);
		but.setPadding(padding, padding, padding, padding);
		but.setBackgroundResource(R.drawable.bg_btn); // 设置背景
		this.fruitRL.addView(but);
		this.childList.add(but);

		if (isClickable) {
			but.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onClick.onClick(v, content);
				}
			});
		} else {
			but.setClickable(false);
			but.setBackground(null);
		}

		this.count.setText(++numCount + "");
	}

	/**
	 * 添加分支
	 */
	public void addBranch(TreeView branch) {
		if (this.branchList == null) {
			this.branchList = new ArrayList<TreeView>();
		}
		branchRL.setOrientation(LinearLayout.VERTICAL);
		this.branchRL.addView(branch, new LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		this.branchList.add(branch);

		numCount += branch.numCount;
		this.count.setText(numCount + "");
	}

	public interface FruitOnClickListener {
		/**
		 * 果实点击事件接口
		 *
		 * @param view    果实的view
		 * @param content 果实的string内容
		 */
		void onClick(View view, String content);
	}
}
