package com.zy.demo.mydemo.treeview;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zy.demo.mydemo.R;
import com.zy.demo.mydemo.util.ScreenUtils;

/**
 * 树
 */
public class OldTreeView extends LinearLayout {

	private Context context;
	private ImageView icon;
	/** 标题 */
	private TextView title;
	/** 右侧显示数量 */
	private TextView count;
	private int numCount;
	/** 果实布局 */
	private RelativeLayout fruitRL;
	/** 分支布局 */
	private LinearLayout branchRL;
	/** 子项宽度 */
	private int width;
	/** 果实数据 */
	private List<TextView> childList;
	/** 分支数据 */
	private List<OldTreeView> branchList;
	/** 是否展开,默认不展开 */
	private boolean isExpand = false;
	/** 每一行子view的最大个数 */
	private static final int LINE_COUNT = 3;
	/** 子项高度 */
	private static final int CHILD_HIGHT = 60;
	/** margin值 */
	private static final int MARGINS = 5;

	public OldTreeView(Context context) {
		super(context);
		initView(context);
	}

	public OldTreeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public OldTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
		View view = View.inflate(context, R.layout.view_item_oldtreeview, this); // 初始化布局文件
		this.icon = (ImageView) view.findViewById(R.id.child_view_icon);
		this.title = (TextView) view.findViewById(R.id.child_view_title_tv);
		this.count = (TextView) view.findViewById(R.id.child_view_count_tv);
		this.fruitRL = (RelativeLayout) view.findViewById(R.id.child_view_fruit_rl);
		this.branchRL = (LinearLayout) view.findViewById(R.id.child_view_Branch_rl);

		if (this.isExpand == false) {// 默认没有展开
			fruitRL.setVisibility(View.GONE);
			branchRL.setVisibility(View.GONE);
		} else { // 默认展开
			fruitRL.setVisibility(View.VISIBLE);
			branchRL.setVisibility(View.VISIBLE);
		}

		this.width = ScreenUtils.getScreenWidth(context) / LINE_COUNT - 10 * MARGINS; // 计算果实宽度

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
		int childCount = this.childList.size(); // 添加前子项数量
		int x = childCount / LINE_COUNT; // 行数(0~)
		int y = childCount % LINE_COUNT; // 列数(0~5)

		int left = MARGINS + y * (width + MARGINS);
		int top = MARGINS + x * (CHILD_HIGHT + MARGINS);
		int right = 0;
		int bottom = 0;
		TextView but = new TextView(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, CHILD_HIGHT);
		params.setMargins(left, top, right, bottom);
		but.setLayoutParams(params);
		but.setText(content);
//		but.setBackgroundResource(R.drawable.button_bg); // 设置背景
		but.setGravity(Gravity.CENTER);
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
	public void addBranch(OldTreeView branch) {
		if (this.branchList == null) {
			this.branchList = new ArrayList<OldTreeView>();
		}
		branchRL.setOrientation(LinearLayout.VERTICAL);
		this.branchRL.addView(branch, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
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
