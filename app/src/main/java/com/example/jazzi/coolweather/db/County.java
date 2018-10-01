package com.example.jazzi.coolweather.db;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    /*记录县的id号
    * 记录县的名字
    * 记录县对应的天气id
    * 记录县所属的id值*/
    private int id;
    private String  countyName;
    private String  weatherId;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
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
