package com.example.jazzi.coolweather.gson;

import com.google.gson.annotations.SerializedName;


/*使用@SerializedName注解的方式让JSON字段和java字段建立映射关系
 * 一般JSON字段中与java字段中 标识符含义差距过大的带注解*/
public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
