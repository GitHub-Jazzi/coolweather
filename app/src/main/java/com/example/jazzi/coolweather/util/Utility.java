package com.example.jazzi.coolweather.util;

import android.text.TextUtils;

import com.example.jazzi.coolweather.db.City;
import com.example.jazzi.coolweather.db.County;
import com.example.jazzi.coolweather.db.Province;
import com.example.jazzi.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


public class Utility {
    /*解析和处理服务器返回的省级数据
    * 将返回的数据存入JSONArray数组中
    * 利用循环依次将JSON元素取出
    * 然后组装成实体类对象
    * 最后再调用save()方法将数据存储到数据库当中
    *
    * 这个保存的是省的名字与id*/
    public static boolean handleProvinceResponse(String responce){
        if(!TextUtils.isEmpty(responce)){
            try{
                JSONArray allProvinces =new JSONArray(responce);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    Province province =new Province();
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.setProvinceName(provinceObject.getString("name"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*保存的是市的名字与id,与所属省的id*/
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities =new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject provinceObject=allCities.getJSONObject(i);
                    City city =new City();
                    city.setCityCode(provinceObject.getInt("id"));
                    city.setCityName(provinceObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*保存的是县的名字与天气情况id，与所属市的id*/
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties =new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setCityId(cityId);
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response){
        try {
            /*根据返回的数据创建一个JSON对象
            * 取出标题是HeWeather那一部分主题内容
            * 将那一部分JSON对象内容转化为字符串
            * 根据字符串与Weather类，返回JSON数据解析成Weather实体类*/
            JSONObject jsonObject =new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
