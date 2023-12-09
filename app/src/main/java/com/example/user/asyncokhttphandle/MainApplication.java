package com.example.user.asyncokhttphandle;

import android.app.Application;
import android.content.Context;


public class MainApplication extends Application {

    // почему используется экземпляр Application, а не MainApplication
    // не имеет значения, этой переменной все равно будет присвоен this
    //не смог получить контекст приложения  при заблокированном экране
    private static Application instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }
}
