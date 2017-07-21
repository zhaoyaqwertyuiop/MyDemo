package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoya on 2017/2/21.
 * 市
 */

public class City extends DataSupport {
    private String cityName;
    private int cityCode;
    private int provincedId; // 省id

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvincedId() {
        return provincedId;
    }

    public void setProvincedId(int provincedId) {
        this.provincedId = provincedId;
    }
}
