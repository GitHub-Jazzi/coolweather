package com.example.jazzi.coolweather.gson;

/*使用@SerializedName注解的方式让JSON字段和java字段建立映射关系
 * 一般JSON字段中与java字段中 标识符含义差距过大的带注解*/

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{

        @SerializedName("txt")
        public String info;
    }
}
