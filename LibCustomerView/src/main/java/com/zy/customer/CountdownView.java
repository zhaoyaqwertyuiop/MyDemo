package com.zy.customer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Author zhaoya
 * @Date 2024/8/20 15:26
 * @describe
 */
public class CountdownView extends View {

    private Paint mPaint;
    private int mCountdown = 60;
    private RectF rectF = new RectF();

    public CountdownView(Context context) {
        super(context);
        init();
    }

    public CountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = (int) (Math.min(width, height) / 2 - mPaint.getStrokeWidth() / 2);
        canvas.drawCircle(width / 2, height / 2, radius, mPaint);
        float sweepAngle = (float) mCountdown / 60 * 359;
//        canvas.drawArc(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius, 270, -sweepAngle, false, mPaint);
        rectF.left = width / 2 - radius;
        rectF.top = height / 2 - radius;
        rectF.right = width / 2 + radius;
        rectF.bottom = height / 2 + radius;
        canvas.drawArc(rectF, 270, -sweepAngle, false, mPaint);
        canvas.drawText(String.valueOf(mCountdown), width / 2 - mPaint.measureText(String.valueOf(mCountdown)) / 2, height / 2 + mPaint.getTextSize() / 2, mPaint);
    }

    public void startCountdown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mCountdown > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCountdown--;
                    postInvalidate();
                }
            }
        }).start();
    }
}
