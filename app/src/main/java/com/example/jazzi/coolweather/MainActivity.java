package com.example.jazzi.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*获取缓存对象
        * 如果缓存对象中有叫"weather"的
        * 就直接显示天气信息，无需再选择城市了
        * 开始WeatherActivity活动
        * 关闭当前活动*/
//        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
//        if(prefs.getString("weather",null)!=null){
//            Intent intent=new Intent(this,WeatherActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }
}
