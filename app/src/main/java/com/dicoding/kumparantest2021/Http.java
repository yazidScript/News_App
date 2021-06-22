package com.dicoding.kumparantest2021;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.BuildConfig;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Http extends Application {

    private OkHttpClient okHttpClient = null;
    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        if(BuildConfig.DEBUG)
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        AndroidNetworking.initialize(this, okHttpClient);
    }

    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }
}
