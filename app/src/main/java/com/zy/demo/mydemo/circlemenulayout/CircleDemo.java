package com.zy.demo.mydemo.circlemenulayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.zy.demo.mydemo.R;

/** 仿建行圆形MenuItem */
public class CircleDemo extends Activity implements View.OnClickListener{

	private CircleMenuLayout mCircleMenuLayout;
	private String[] mItemTexts = new String[] {"安全中心", "特色服务", "投资理财", "转账汇款", "我的账户", "信用卡"};
	private int[] mItemImgs = new int[] {R.drawable.home_mbank_1_normal, R.drawable.home_mbank_2_normal,
			R.drawable.home_mbank_3_normal, R.drawable.home_mbank_4_normal,
			R.drawable.home_mbank_5_normal, R.drawable.home_mbank_6_normal};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_circle);

		this.mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
		mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);

		mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
			@Override
			public void itemClick(View view, int position) {
				Toast.makeText(CircleDemo.this, mItemTexts[position], Toast.LENGTH_SHORT).show();
			}

			@Override
			public void itemCenterClick(View view) {
				Toast.makeText(CircleDemo.this, "center", Toast.LENGTH_SHORT).show();
			}
		});
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
}
