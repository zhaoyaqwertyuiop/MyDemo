package com.zy.demo.mydemo.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zy.demo.mydemo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 从底部弹出popWin
 */
public class DownPopupWindowCamara extends DownPopupWindow {

	private CallBack callBack;

	public DownPopupWindowCamara(Context context) {
		super(context);
		super.initView(new IInitView() {
			@Override
			public View initView(LayoutInflater inflater) {
				View view = inflater.inflate(R.layout.view_popwindow_down, null);
				view.findViewById(R.id.btn_take_photo).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 拍照
						if (callBack != null) {
							callBack.onCamera();
						}
					}
				});
				view.findViewById(R.id.btn_pick_photo).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 从相册选择
						if (callBack != null) {
							callBack.onPick();
						}
					}
				});
				view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (callBack != null) {
							callBack.onCancel();
						}
					}
				});
				return view;
			}

			@Override
			public View initDownView(View contentView) {
				return contentView.findViewById(R.id.pop_layout);
			}
		});
	}

	public CallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	public interface CallBack {
		/** 拍照 */
		void onCamera();
		/** 从相册选择 */
		void onPick();
		/** 取消 */
		void onCancel();
	}
}
