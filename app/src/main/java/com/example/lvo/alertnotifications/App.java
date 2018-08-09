package com.example.lvo.alertnotifications;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.lvo.alertnotifications.ServerApi;

public class App extends Application {

    private static ServerApi userByToken;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.1.100.33:3000") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        userByToken = retrofit.create(ServerApi.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static ServerApi getApi() {
        return userByToken;
    }
}