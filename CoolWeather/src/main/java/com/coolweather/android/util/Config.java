package com.coolweather.android.util;

/**
 * Created by zhaoya on 2017/2/28.
 */

public class Config {
    public static final String ROOT = "http://guolin.tech/api";

    public static final String WEATHER_KEY = "6e1de0dd41e9470dbf8a0916f4c29a4a";

    public static final String WEATHER = ROOT + "/weather?cityid=$&key=" + WEATHER_KEY;
    public static final String BING_PIC = ROOT + "/bing_pic";
}
