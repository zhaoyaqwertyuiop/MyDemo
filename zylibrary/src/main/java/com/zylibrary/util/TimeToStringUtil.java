package com.zylibrary.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhaoya on 2016/10/8.
 */
public class TimeToStringUtil {

    /**
     * 毫秒值转string
     * @param time 毫秒值
     * @param formatStr yyyyMMddHHmmss
     * @return
     */
    public static final String format(String time, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = new Date(Long.parseLong(time) + 0);
        return sdf.format(date);
    }

    /**
     * 毫秒值转距离现在的时间
     * @param time 毫秒值
     * @return
     */
    public static final String formatCurrentTime(String time) {
        long difference = System.currentTimeMillis() - Long.parseLong(time);
        long s = difference / 1000; // 秒
        long m = s / 60; // 分钟
        long H = m / 60; // 小时
        long d = H / 24; // 天
        String result = "";
        if (d > 5) { // 大于5天,显示年月日
            result = TimeToStringUtil.format(time, "yyyy.MM.dd");
        } else if (d > 2 && d <= 5) {
            result = d + "天前";
        } else if (d == 2) {
            result = d + "前天";
        } else if (d == 1) {
            result = d + "昨天";
        } else if (H > 0 ) {
            result = H + "小时前";
        } else if (m > 0) {
            result = m + "分钟前";
        } else {
            result = "刚刚";
        }
        return result;
    }
}
