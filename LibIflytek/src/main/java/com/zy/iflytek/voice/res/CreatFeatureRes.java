package com.zy.iflytek.voice.res;

import androidx.annotation.Keep;

/**
 * @Author zhaoya
 * @Date 2024/12/27 17:40
 * @describe 创建声纹特征库
 */
@Keep
public class CreatFeatureRes {
    public Header header;
    public Payload payload;

    @Keep
    public static class Header {
        public int code; // 0表示会话调用成功（并不一定表示服务调用成功，服务是否调用成功以text字段为准）
        public String message; // 描述信息
        public String sid; // 本次会话唯一标识id
    }

    // 数据段，用于携带响应的数据
    @Keep
    public class Payload {
        public ParameterInner createFeatureRes = new ParameterInner(); // 响应数据块

        @Keep
        public class ParameterInner {
            public String text = ""; // 响应数据base64编码
        }
    }

//    {"groupId":"voice_groupId","groupName":"voice_groupName","groupInfo":"voice_groupInfo"}
    @Keep
    public class GroupText {
        public String groupId;
        public String groupName;
        public String groupInfo;
    }
}
