package com.eventbus;

/**
 * Created by zhaoya on 2016/12/28.
 */
public class AnyEvent {
    private String discribe;

    //构造函数
    public AnyEvent(String discribe) {
        this.discribe = discribe;
    }

    //set/get方法
    public void setDiscribe(String discribe) {
        this.discribe = discribe;
    }

    public String getDiscribe() {
        return discribe;
    }
}
