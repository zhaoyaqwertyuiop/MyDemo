package com.zy.iflytek.voice.req;

import androidx.annotation.Keep;

/**
 * @Author zhaoya
 * @Date 2024/12/27 17:40
 * @describe 创建声纹特征库
 */
@Keep
public class CreatGroupReq {
    public Header header = new Header();
    public Parameter parameter = new Parameter();

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
            public String func = "createGroup"; // 用于指定声纹的具体能力（创建声纹特征库值为createGroup）
            public String groupId = ""; // 创建分组的标识，支持字母数字下划线，长度最大为32
            public String groupName = ""; // 创建分组的名称，长度最小为0，最大为256， 否
            public String groupInfo = ""; // 创建分组的描述信息，长度最小为0，最大为256 , 否
            public CreateGroupRes createGroupRes = new CreateGroupRes();

            @Keep
            public class CreateGroupRes {
                public String encoding = "utf8"; // 编码格式（固定utf-8）
                public String format = "json"; // 文本格式（固定json）
                public String compress = "raw"; // 压缩格式（固定raw）
            }
        }
    }
}
