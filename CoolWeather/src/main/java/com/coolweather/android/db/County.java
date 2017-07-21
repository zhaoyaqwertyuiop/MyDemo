package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoya on 2017/2/21.
 * 县
 */

public class County extends DataSupport {
    private String countyName;
    private int countyCode;
    private String weatherId; // 天气id
    private int cityId; // 市id

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(int countyCode) {
        this.countyCode = countyCode;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
