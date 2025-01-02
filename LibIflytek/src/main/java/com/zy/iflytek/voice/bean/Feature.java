package com.zy.iflytek.voice.bean;

import androidx.annotation.Keep;

/**
 * @Author zhaoya
 * @Date 2024/12/28 16:42
 * @describe
 */
@Keep
public class Feature {
    public float score; // 正常相似度得分0~1，精确到小数点后两位。（相似度范围-1到1）
    public String featureInfo; // 目标特征的描述信息
    public String featureId; // 目标特征的唯一标识
}
