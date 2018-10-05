package com.example.jazzi.coolweather.gson;

/*使用@SerializedName注解的方式让JSON字段和java字段建立映射关系
 * 一般JSON字段中与java字段中 标识符含义差距过大的带注解*/

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    /*这里是一个数组，所以用列表*/
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
