package com.btnanimal;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.nfc.NfcAdapter;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import static com.btnanimal.R.id.imageview;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn1, btn2, btn3;
    private ImageView imageView;
    private int margin;

    private boolean isShwo = false;

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        x.Ext.init(this.getApplication());
        x.Ext.setDebug(BuildConfig.DEBUG);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btn1 = (Button) super.findViewById(R.id.btn1);
        this.btn2 = (Button) super.findViewById(R.id.btn2);
        this.btn3 = (Button) super.findViewById(R.id.btn3);
        this.imageView = (ImageView) super.findViewById(imageview);

        margin = DensityUtil.dip2px(this, 10);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void show() {
//        btn2.setVisibility(View.VISIBLE);
//        btn3.setVisibility(View.VISIBLE);

        Log.d(TAG, "btn1.getWidth()=" + btn1.getWidth() + ", btn2.getWidth()=" + btn2.getWidth() + ", btn3.getWidth()=" + btn3.getWidth());
        btn2.animate().translationX(btn1.getWidth() + margin).alpha(1).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                btn2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        btn3.animate().translationX(btn1.getWidth() + margin + btn2.getWidth() + margin).alpha(1).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        isShwo = true;
    }

    private void hind() {
        btn2.animate().translationX(0).alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        btn3.animate().translationX(0).alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btn3.setVisibility(View.GONE);
                btn3.clearAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        isShwo = false;
    }

    private void checkNFC() {
        // 获取默认的NFC控制器
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            ToastUtil.showToast("设备不支持NFC！");
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            ToastUtil.showToast("请在系统设置中先启用NFC功能！");
            return;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                if (!isShwo) {
                    Log.d(TAG, "btn1 clicked show");
                    show();
                } else {
                    Log.d(TAG, "btn1 clicked hind");
                    hind();
                }
                break;
            case R.id.btn2:
                Log.d(TAG, "btn2 clicked");
                hind();
                break;
            case R.id.btn3:
                Log.d(TAG, "btn3 clicked");
                hind();
                break;
            case R.id.NFCbtn:
                checkNFC();
                break;
            case imageview:
                anim();
                break;
            default: break;
        }
    }

    float translationX = 0;
    private void anim() {
        // Interpolator 被用来修饰动画效果，定义动画的变化率，可以使存在的动画效果accelerated(加速)，decelerated(减速),repeated(重复),bounced(弹跳)等。
//        imageView.animate().translationXBy(200).setInterpolator(new LinearInterpolator()).start(); // 横向动画设为匀速运动
//        imageView.animate().translationYBy(1000).setInterpolator(new AccelerateInterpolator()); // 竖向动画设为加速运动

//        imageView.animate().translationXBy(200).setInterpolator(new LinearInterpolator()).setDuration(400).start();
//        imageView.animate().translationYBy(100).setDuration(200).setInterpolator(new LinearInterpolator()).withEndAction(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        })start();

        translationX += 200;
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(imageView, "translationX", translationX).setDuration(400);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageView, "translationY", -100).setDuration(200);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(imageView, "translationY", 0).setDuration(200);
//        anim1.setInterpolator(new LinearInterpolator());
//        anim2.setInterpolator(new DecelerateInterpolator());
//        anim3.setInterpolator(new AccelerateInterpolator());
        anim3.setStartDelay(200);

        AnimatorSet animSet = new AnimatorSet();
        //两个动画同时执行
        animSet.playTogether(anim1, anim2, anim3);
        animSet.start();
    }
}
