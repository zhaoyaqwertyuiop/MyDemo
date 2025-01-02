package com.zy.iflytek.voice.req;

import androidx.annotation.Keep;

/**
 * @Author zhaoya
 * @Date 2024/12/27 17:40
 * @describe 添加音频特征
 */
@Keep
public class CreatFeatureReq {
    public Header header = new Header();
    public Parameter parameter = new Parameter();
    public Payload payload = new Payload();

    @Keep
    public static class Header {
        public String app_id = ""; // 必填
        public int status = 3; // 请求状态，取值为：3（一次传完）
    }

    // 用于上传服务特性参数
    @Keep
    public class Parameter {
        public ParameterInner s782b4996 = new ParameterInner(); // 用于上传功能参数

        @Keep
        public class ParameterInner {
            public String func = "createFeature"; // 用于指定声纹的具体能力（创建声纹特征库值为createGroup）
            public String groupId = ""; // 特征的标识，长度最小为0，最大为32
            public String featureId = ""; // 创建分组的名称，长度最小为0，最大为256， 否
            public String featureInfo = ""; // 特征描述信息，长度最小为0，最大为256（建议在特征信息里加入时间戳） , 否
            public CreateGroupRes createFeatureRes = new CreateGroupRes();

            @Keep
            public class CreateGroupRes {
                public String encoding = "utf8"; // 编码格式（固定utf-8）
                public String format = "json"; // 文本格式（固定json）
                public String compress = "raw"; // 压缩格式（固定raw）
            }
        }
    }

    // 用于上传请求数据
    @Keep
    public class Payload {
        public Resource resource = new Resource(); // 用于相关音频相关参数

        @Keep
        public class Resource {
            public String encoding = "lame"; // 音频编码（固定lame）
            public int sample_rate = 16000; // 音频采样率（16000）
            public int channels = 1; // 音频声道数（1单声道）
            public int bit_depth = 16; // 音频位深（16）
            public int status = 3; // 音频数据状态（3一次性传完）
            public String audio = ""; // 音频数据base64编码（编码后最小长度:1B 最大长度:4M）
        }
    }
}
