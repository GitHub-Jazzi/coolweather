package com.example.jazzi.coolweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jazzi.coolweather.gson.Forecast;
import com.example.jazzi.coolweather.gson.Weather;
import com.example.jazzi.coolweather.util.HttpUtil;
import com.example.jazzi.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;


    private ImageView bingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*将系统状态栏与背景图片融合为一体
        * 仅支持安卓5.0以上版本
        * 即判断如果大于版本5.0，则运行该代码
        * 首先获得当前活动的DecorView
        * 然后改UI显示，表示活动的布局会显示在状态栏上面
        * 然后将状态栏设置为透明色*/
        if(Build.VERSION.SDK_INT>=21){
            View decorView =getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        /*初始化各个控件*/
        weatherLayout=(ScrollView) findViewById(R.id.weather_layout);
        titleCity=(TextView) findViewById(R.id.title_city);
        titleUpdateTime=(TextView) findViewById(R.id.title_update_time);
        degreeText=(TextView) findViewById(R.id.degree_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        forecastLayout=(LinearLayout) findViewById(R.id.forecast_layout);
        aqiText=(TextView)findViewById(R.id.aqi_text);
        pm25Text=(TextView)findViewById(R.id.pm25_text);
        comfortText=(TextView)findViewById(R.id.comfort_text);
        carWashText=(TextView)findViewById(R.id.car_wash_text);
        sportText=(TextView)findViewById(R.id.sport_text);
        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);


        /*这里尝试从本地缓存中读取天气数据*/
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString =prefs.getString("weather",null);
        String bingPic=prefs.getString("bing_pic",null);


        /*如果缓存中有图片，使用Glide将其图片数据bingPic载入bingPicImg控件中*/
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }

        if(weatherString!=null){
//        if(false){
            /*有缓存时直接解析天气数据*/
            Weather weather= Utility.handleWeatherResponse(weatherString);

            showWeatherInfo(weather);

        }else{
            /*无缓存时取服务器查询天气*/
            String weatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }


    /*加载必应的每日一图*/
    private void loadBingPic(){
        String requesBingPic="http://guolin.tech/api/bing_pic";

        //这里处理返回的数据
        HttpUtil.sendOkHttpRequest(requesBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                /*将返回的图片以字符串的形式存入String，也是厉害*/
                final String bingPic=response.body().string();

                /*创建一个缓存对象
                * 将图片数据以bing_pic的标签存入缓存
                * 保存*/
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();

                /*开启主线程
                * 利用Glide将bingPic图片数据载入到WeatherActivity活动下bingPicImg控件中*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });

            }
        });
    }
    public void requestWeather(final String weatherId){

        loadBingPic();

        String weatherUrl="http://guolin.tech/api/weather?cityid="+
                weatherId+"&key=f1988ad8d4d84a94b36c5dcc5a9add6c";
        //这里处理返回的数据
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            //获取失败的做法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }


            //获取成功的做法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseText);

                /*切换到主线程，才能修改UI*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status)){
                            /*获取缓存对象
                            * 将weather的数据以"weather"标签存入缓存
                            * 保存缓存
                            * 显示信息*/
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);

                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    private void showWeatherInfo(Weather weather){
        /*获取各种信息*/
        String cityName=weather.basic.cityName;
        String updateTime=weather.basic.update.updateTime.split(" ")[1];
        String degree =weather.now.temperature+"℃";
        String weatherInfo=weather.now.more.info;

        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);


        /*先清空forecastLayout布局
        * 然后遍历JSON中传来的数组数据
        * 获得当前布局的view，参数1：子布局，参数2：父布局，参数3：false固定
        * 初始化各个子布局控件
        * 将传进来的数据依次载入
        * 最后添加至forecastLayout*/
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);

            TextView dateText=(TextView)view.findViewById(R.id.date_text);
            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView maxText=(TextView)view.findViewById(R.id.max_text);
            TextView minText=(TextView)view.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);

            forecastLayout.addView(view);
        }

        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort="舒适度"+weather.suggestion.comfort.info;
        String carWash="洗车指数"+weather.suggestion.carWash.info;
        String sport="运动建议"+weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        /*别忘记让其重新可见等价于刷新的意思了*/
        weatherLayout.setVisibility(View.VISIBLE);

    }
}
