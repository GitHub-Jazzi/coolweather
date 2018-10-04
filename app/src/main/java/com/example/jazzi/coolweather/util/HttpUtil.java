package com.example.jazzi.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    /*创建一个OkHttpClient对象
     * 创建一个request对象，可以连缀很多其他方法来丰富这个对象
     * 调用OkHttpClient的newCall方法创建一个Call对象，并调用它的execute方法
     * 发送请求并获取服务器返回的数据
     * */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
