package com.zy.demo.mydemo.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zy.demo.mydemo.R;
import com.zy.demo.mydemo.treeview.NewTreeView;

import org.xutils.common.util.DensityUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhaoya on 2016/11/18.
 * 类似于popupWindow的Activity
 *
 */
public class PopupWindowActivity extends Activity {

    private View mContentView;
    private View mDownView; // 下方弹出的view
    private View mBgView;
    private Context context;

    private boolean canShow = true; // 动画过程中不能执行show,不然会崩掉
    private boolean isDismissing = false; // 标记是否正在执行关闭动画,防止重复执行

    private float alphaRatio = 0.8f; // 透明度比例 (1.0f 到 0.0f 之间, 值越大,弹出时背景透明度越大)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_popwindow_down_domains);
        this.context = this;

        this.mContentView = super.findViewById(R.id.contentView);
        this.mDownView = super.findViewById(R.id.pop_layout);

        initView();

        ColorDrawable dw = new ColorDrawable(0x000000);
        this.mContentView.setBackgroundDrawable(dw);

        mContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mDownView.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (y < height) {
                        finish();
                    }
                }
                return true;
            }
        });

        showAtLocation();
    }

    private void initView() {
        WrapViewGroup wrapViewGroup = (WrapViewGroup) super.findViewById(R.id.checkedVG); // 已选
        LinearLayout contentLL = (LinearLayout) super.findViewById(R.id.contentLL); // 用来添加树的布局

        wrapViewGroup.addView(getTextView("测试1"));
//                wrapViewGroup.addView(getTextView("测试1阿大声道AEFADFADWF"));
//                wrapViewGroup.addView(getTextView("测试阿斯达所大所大去哇发 爱的无法df"));
//                wrapViewGroup.addView(getTextView("测试阿斯达所大所大去哇发 爱的无法df"));
//                wrapViewGroup.addView(getTextView("测试阿斯达所大所大去哇发 爱的无法df"));
//                wrapViewGroup.addView(getTextView("测试阿斯达所大所大去哇发 爱的无法df"));
//                wrapViewGroup.addView(getTextView("测试1"));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        NewTreeView treeView1 = new NewTreeView(context);
        treeView1.setLayoutParams(params);
        treeView1.setIsExpand(true);
        treeView1.setTitle("刑事辩护");

        NewTreeView treeView2 = new NewTreeView(context);
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
//        contentLL.addView(treeView2, params);
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

    public void showAtLocation() {
        if (!canShow) {
            return;
        }
//        super.showAtLocation(parent, gravity, x, y);
        final AnimationSet set = new AnimationSet(true);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.set_in_down);
        set.addAnimation(animation);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                if (timer != null) { // 开始动画还没完
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                final long period = 1;
                final TimerTask timerTask = new TimerTask() {
                    long time = animation.getDuration();
                    long currentTime = 0;
                    @Override
                    public void run() {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                currentTime += period;
                                if (currentTime >= time) {
                                    if(timer != null) {
                                        timer.cancel();
                                        timer = null;
                                    }
                                }
                                float alpha = 1.0f - currentTime * alphaRatio / time ; // 区间从1.0f到0.3f
                                Log.d("TAG", "alpha=" + alpha + ",time=" + time + ",currentTime=" + currentTime);
                                backgroundAlpha(alpha);
                            }
                        });
                    }
                };
                timer.schedule(timerTask, 0, 1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mDownView.startAnimation(set);
    }

    private Timer timer;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public void finish() {
        if (isDismissing) {
            return;
        }
        isDismissing = true; // 如果正在执行直接返回,否则设置正在执行标记
        canShow = false;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.set_out_down);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                if (timer != null) { // 开始动画还没完
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                final long period = 1;
                final TimerTask timerTask = new TimerTask() {
                    long time = animation.getDuration();
                    long currentTime = 0;
                    WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
                    float startAlpha = lp.alpha;
                    @Override
                    public void run() {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                currentTime += period;
                                if (currentTime >= time) {
                                    if (timer != null) {
                                        timer.cancel();
                                        timer = null;
                                    }
                                }
//								float alpha = startAlpha + currentTime * 0.7f / time; // 区间从1.0f到0.3f
                                float alpha = startAlpha + currentTime * alphaRatio / time; // 区间从1.0f到0.3f
                                Log.d("TAG", "alpha=" + alpha + ",time=" + time + ",currentTime=" + currentTime);
                                if (alpha > 1) {
                                    alpha = 1;
                                }
                                backgroundAlpha(alpha);
                            }
                        });
                    }
                };
                timer.schedule(timerTask, 0, 1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                PopupWindowActivity.super.finish();
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                canShow = true;
                isDismissing = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mDownView.startAnimation(animation);
    }

    // 设置屏幕透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        ((Activity)context).getWindow().setAttributes(lp);
    }
}
