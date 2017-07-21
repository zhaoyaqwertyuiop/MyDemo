package com.zy.demo.mydemo.autotextview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.zy.demo.mydemo.R;

import java.util.zip.Inflater;

/** 自动滚动的TextView */
public class TextViewDemo extends AppCompatActivity implements View.OnClickListener{

	private Context context;
	private AutoTextView mAutoTextView;

	private String str1 = "";
	private String[] str = new String[] {"这是滚动TextView的第1条数据.", "这是滚动TextView的第2条数据.",
			"这是滚动TextView的第3条数据.", "这是滚动TextView的第4条数据.",
			"这是滚动TextView的第5条数据.", "这是滚动TextView的第6条数据."};

	private ViewSwitcher mViewSwitcher; // 只能是两个view切换

	private ViewFlipper mViewFlipper; // 多个view切换

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_text_demo);
		context = this;

		// 跑马灯
		this.mAutoTextView = (AutoTextView) super.findViewById(R.id.autoTextView);
		for (int i = 0; i < str.length; i++) {
			if (i == 0) {
				str1 += str[i];
			} else {
				str1 += "               " + str[i];
			}
		}
		mAutoTextView.setText(str1);

		// ViewSwitcher
		this.mViewSwitcher = (ViewSwitcher) super.findViewById(R.id.text_switcher);
		mViewSwitcher.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchNext(v);
			}
		});

		// ViewFlipper
		this.mViewFlipper = (ViewFlipper) super.findViewById(R.id.viewFlipper);
		flipperAutoStart();
		mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
			float downY = 0;
			float lastY = 0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						downY = event.getY();
						Log.d("TAG", "DOWN: event.get(Y)=" + event.getY() + ", downY=" + downY + ", lastY=" + lastY);
//						Toast.makeText(TextViewDemo.this, "down", Toast.LENGTH_SHORT).show();
						mViewFlipper.setAutoStart(false);
						return true;
					case MotionEvent.ACTION_MOVE:
						lastY = event.getY();
						Log.d("TAG", "MOVE: event.get(Y)=" + event.getY() + ", downY=" + downY + ", lastY=" + lastY);
//						Toast.makeText(TextViewDemo.this, "move", Toast.LENGTH_SHORT).show();
						break;
					case MotionEvent.ACTION_UP:
						Log.d("TAG", "UP: event.get(Y)=" + event.getY() + ", downY=" + downY + ", lastY=" + lastY);
						if (lastY - downY > 120) { // 向下滑动
							Toast.makeText(TextViewDemo.this, "lastY - downY=" + (lastY - downY) + "向下滑动", Toast.LENGTH_SHORT).show();
							mViewFlipper.setInAnimation(context, R.anim.set_in_up);
							mViewFlipper.setOutAnimation(context, R.anim.set_out_down);
							mViewFlipper.showPrevious();
						} else if (lastY - downY < -120) { // 向上滑动
							Toast.makeText(TextViewDemo.this, "lastY - downY=" + (lastY - downY) + "向上滑动", Toast.LENGTH_SHORT).show();
							mViewFlipper.setInAnimation(context, R.anim.set_in_down);
							mViewFlipper.setOutAnimation(context, R.anim.set_out_up);
							mViewFlipper.showNext();
						}
						flipperAutoStart();
//						Toast.makeText(TextViewDemo.this, "up", Toast.LENGTH_SHORT).show();
						break;
					default:break;
				}
				return false;
			}
		});
	}

	private void switchNext(View v) {
		mViewSwitcher.setInAnimation(this, R.anim.set_in_down);
		mViewSwitcher.setOutAnimation(this, R.anim.set_out_up);
		mViewSwitcher.showNext();
	}

	private void flipperNext() {
		mViewFlipper.setInAnimation(this, R.anim.set_in_down);
		mViewFlipper.setOutAnimation(this, R.anim.set_out_up);
		mViewFlipper.showNext();
	}

	private void flipperAutoStart() {
		mViewFlipper.setInAnimation(this, R.anim.set_in_down);
		mViewFlipper.setOutAnimation(this, R.anim.set_out_up);
		mViewFlipper.setAutoStart(true);
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
















































