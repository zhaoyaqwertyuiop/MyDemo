package com.zy.customer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.LogUtils;

/**
 * @Author zhaoya
 * @Date 2024/5/31
 * @describe 自定义可改变颜色的textView,跟歌词效果类似
 */
public class ChangeColorTextView extends AppCompatTextView {

    private final String TAG = ChangeColorTextView.class.getSimpleName();
    private float progress;
    private Paint paintChange;
    private Paint paintDef;
    private Paint paintGradient;

    private int changeColor; // 改变后的颜色
    private int defColor; // 默认颜色

    private Rect rect = new Rect();
    private Rect rect2 = new Rect();

    private volatile float autoSpeed = 0.45f; // 初始自动播放的速度(规定时间播放的字数)，后续根据setProgress的频率自动修正,
    private volatile float changeSpeed = 0.1f; // 自动调整的幅度
    private int autoDelay = 100; // 执行自动播放的时间间隔
    public ChangeColorTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public ChangeColorTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChangeColorTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        changeColor = getCurrentTextColor();
        defColor = getCurrentHintTextColor();

        paintChange = new Paint(getPaint());
        paintChange.setColor(changeColor);

        paintDef = new Paint(getPaint());
        paintDef.setColor(defColor);

        paintGradient = new Paint(getPaint());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int currentLength = 0; // 记录字符串数量
        float currentPosition = getText().length()* progress; // 当前位置
        for (int i = 0; i < getLineCount(); i++) {
            String text = getLineText(i);
            currentLength += text.length();

            if (currentLength < currentPosition) { // 当前行之前
                getLayout().getLineBounds(i, rect);
                drawLineText(canvas, text, paintChange);
            } else if (currentLength - text.length() < currentPosition) { // 正在改动的行
                getPaint().getTextBounds(getText().toString(), currentLength - text.length(), currentLength, rect2);
                getLayout().getLineBounds(i, rect);
                float tempProgress = (currentPosition - (currentLength - text.length())) / text.length() * 1f; // 这里的进度要用当前行的进度
                LinearGradient gradient1 = new LinearGradient(rect2.right*tempProgress-1, 0, rect2.right*tempProgress, 0, changeColor, defColor, Shader.TileMode.CLAMP);
                paintGradient.setShader(gradient1);
                drawLineText(canvas, text, paintGradient);
            } else { // 当前行之后
                getLayout().getLineBounds(i, rect);
                drawLineText(canvas, text, paintDef);
            }
        }

//        super.onDraw(canvas);
    }

    // 获取某一行的文本
    private String getLineText(int lineNumber) {
        int start = getLayout().getLineStart(lineNumber);
        int end = getLayout().getLineEnd(lineNumber);
        String lineText = getText().subSequence(start, end).toString();
        return lineText;
    }

    private void drawLineText(Canvas canvas, String text, Paint paint) {
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int baseLine = rect.bottom - fontMetricsInt.bottom + getPaddingTop(); //基线的位置
        if (text.endsWith("\n")) { // 去掉行尾部的\n
            text = text.substring(0, text.length() - 1);
        }
        canvas.drawText(text, rect.left + getPaddingLeft(), baseLine, paint);
    }

    public void setProgress(float progress) {//改变进度
        this.progress = progress;
        invalidate();
    }

    public void setChangeColor(int changeColor) {
        this.changeColor = changeColor;
        paintChange.setColor(changeColor);
    }

    public void setDefColor(int defColor) {
        this.defColor = defColor;
        paintDef.setColor(defColor);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progress += autoSpeed/getText().length();
            invalidate();
            if (progress < 1) {
                postDelayed(this, autoDelay);
            }
        }
    };

    public void start() {
        post(runnable);
        autoProgress(0);
        progress = 0;
    }

    // 改变autoSpeed 让当前的prgogress趋近preProgress
    public void autoProgress(float preProgress) {
        autoSpeed += (preProgress - progress) * changeSpeed;
        if (autoSpeed < 0.1) { // 防止回流
            autoSpeed = 0.1f;
        }
        LogUtils.d(TAG, "autoSpeed=" + autoSpeed + ",progress=" + progress + ",preProgress=" + preProgress);
    }

    public void setAutoSpeed(float autoSpeed) {
        this.autoSpeed = autoSpeed;
    }

    public void setChangeSpeed(float changeSpeed) {
        this.changeSpeed = changeSpeed;
    }
}
