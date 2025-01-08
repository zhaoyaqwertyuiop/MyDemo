package com.zy.common.util;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author zhaoya
 * @Date 2023/11/21 16:20
 * @describe
 */
public class SpanUtil {

    // 通用修改textview文字颜色
    public static void changeColor(@NonNull TextView textView, @ColorInt int color, String... regexs) {
        findRegex(textView, (start, end, spannableString) -> {
            ForegroundColorSpan span = new ForegroundColorSpan(color);
            spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }, regexs);
    }

    /**
     * {@link Typeface}
     */
    // 修改textview文字加粗
    public static void changeTypeface(@NonNull TextView textView, int typeface, String... regexs) {
        findRegex(textView, (start, end, spannableString) -> {
            StyleSpan span = new StyleSpan(typeface);
            spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }, regexs);
    }

    // 修改textview文字大小, 绝对大小， size 单位为px
    public static void changeSizeAbsolute(@NonNull TextView textView, int size, String... regexs) {
        findRegex(textView, (start, end, spannableString) -> {
            AbsoluteSizeSpan span = new AbsoluteSizeSpan(size);
            spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }, regexs);
    }

    // 修改textview文字大小, 相对大小，按比例缩放
    public static void changeSizeRelative(@NonNull TextView textView, @FloatRange(from = 0) float proportion, String... regexs) {
        findRegex(textView, (start, end, spannableString) -> {
            RelativeSizeSpan span = new RelativeSizeSpan(proportion);
            spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }, regexs);
    }

    // android:letterSpacing在api21以后才有用，用这个来替代，设置文字横向间距
    // 如果文本原来就有 "\u00A0" 会有影响，不适用该方法
    // 当 proportion 大于 1 时，文本将放大；当 proportion 小于 1 时，文本将缩小；当 proportion 等于 1 时，文本保持原始大小。
    public static void setLetterSpace(@NonNull TextView textView, float proportion) {
        CharSequence charSequence = textView.getText();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < charSequence.length(); i++) {
            String c = ""+ charSequence.charAt(i);
            builder.append(c);
            if(i+1 < charSequence.length() && !c.equals("\n")) {
                builder.append("\u00A0");
            }
        }
        textView.setText(builder);

        findRegex(textView, (start, end, spannableString) -> {
            ScaleXSpan span = new ScaleXSpan(proportion);
            spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }, "\u00A0");
    }

    public static void findRegex(TextView textView, @NonNull CallBack callback, String... regexs) {
        if (regexs.length == 0) {
            return;
        }
        CharSequence charSequence = textView.getText();
        SpannableString spannableString;
        if (charSequence instanceof SpannableString) {
            spannableString = (SpannableString) charSequence;
        } else {
            spannableString = new SpannableString(charSequence);
        }

        for (String key: regexs) {
            String regex = key;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(charSequence);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                callback.onFind(start, end, spannableString);
            }
        }
        textView.setText(spannableString);
    }

    public static SpannableString findRegex(CharSequence charSequence, @NonNull CallBack callback, String... regexs) {
        SpannableString spannableString;
        if (charSequence instanceof SpannableString) {
            spannableString = (SpannableString) charSequence;
        } else {
            spannableString = new SpannableString(charSequence);
        }
        if (regexs.length == 0) {
            return spannableString;
        }

        for (String key: regexs) {
            String regex = key;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(charSequence);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                callback.onFind(start, end, spannableString);
            }
        }
        return spannableString;
    }

    public interface CallBack{
        void onFind(int start, int end, SpannableString spannableString);
    }
}
