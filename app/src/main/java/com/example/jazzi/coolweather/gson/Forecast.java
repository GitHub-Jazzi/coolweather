package com.example.jazzi.coolweather.gson;


/*使用@SerializedName注解的方式让JSON字段和java字段建立映射关系
 * 一般JSON字段中与java字段中 标识符含义差距过大的带注解*/

import com.google.gson.annotations.SerializedName;

public class Forecast {

    public String date;
    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;


    public class Temperature{
        public String max;
        public String min;
    }
    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
