package com.example.user.mvptrying;

import android.app.Application;
import android.content.Context;
import android.util.Log;


public class MyApplication extends Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MyModel.initModelInstance();
        MyPresenter.initPresenterInstance();
        Log.i("MyApplication", "onCreate MyApp");
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

}
