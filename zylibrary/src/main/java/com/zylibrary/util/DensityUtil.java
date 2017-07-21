package com.zylibrary.util;

import com.zylibrary.ZYLibrary;
/**
 * dp和px转换工具类
 * @author zhaoya
 *
 */
public class DensityUtil {
	/** 根据手机的分辨率从 dp 的单位 转成为 px(像素) */  
    public static int dip2px(float dpValue) {
        final float scale = ZYLibrary.app().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /** 根据手机的分辨率从 px(像素) 的单位 转成为 dp */  
    public static int px2dip(float pxValue) {
        final float scale = ZYLibrary.app().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);  
    }
}
