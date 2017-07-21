package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoya on 2017/2/21.
 * 省
 */
public class Province extends DataSupport {
    private String provinceName;
    private int provinceCode;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
