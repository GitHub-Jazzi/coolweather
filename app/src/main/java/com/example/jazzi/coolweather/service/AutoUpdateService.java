package com.example.jazzi.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.jazzi.coolweather.gson.Weather;
import com.example.jazzi.coolweather.util.HttpUtil;
import com.example.jazzi.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*每次启动后做的第一件事情就是执行这个方法*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateBingPic();
        updateWeather();

        /*创建后台闹钟管理对象*/
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);

        /*8小时的毫秒数*/
        int anHour=8*60*60*1000;

        /*设置下一次启动时间
        * 为当前系统时间+8小时*/
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;

        /*PendingIntent
         * 倾向于在某个合适的时机去执行某个动作
         *
         *  参数1：Context
            参数2：一般是0
            参数3：一个Intent对象，通过这个参数构建出PendingIntent的“意图”
            参数4：确定PendingIntent的行为，一般为0
         *
         *  这里的意图是启动AutoUpdateService*/
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);

        /*取消之前的所有关于PendingIntent的闹钟*/
        manager.cancel(pi);

        /*新增一个关于PendingIntent的闹钟*/
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);


        return super.onStartCommand(intent,flags,startId);
    }

    /*更新天气信息*/
    private void updateWeather(){
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString =prefs.getString("weather",null);

        /*这里是仅仅用于更新缓存的，不存在缓存则不更新*/
        if(weatherString!=null){
            Weather weather= Utility.handleWeatherResponse(weatherString);
            final String weatherId=weather.basic.weatherId;

            String weatherUrl="http://guolin.tech/api/weather?cityid="+
                    weatherId+"&key=f1988ad8d4d84a94b36c5dcc5a9add6c";


            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText=response.body().string();
                    Weather weather=Utility.handleWeatherResponse(responseText);

                    if(weather!=null&&"ok".equals(weather.status)){
                        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }


    /*更新必应每日一图
    * 无论缓存有没有，直接更新载入缓存*/
    private void updateBingPic(){
        final String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic=response.body().string();
                SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }

}
