package com.example.user.mvptrying;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;




/*public class MyModel {
    private int result=0;

    MyModel(Context context){
        result=doSomeBusyJob(context);
    }
    int doSomeBusyJob(Context context){
        return 0;
    }

    public int getResult() {
        return result;
    }

}*/

public class MyModel implements Contract.ContractModel {


    private static MyModel modelInstance;




    //с приватным конструктором сервис не будет работать,(отдельный класс под сервис нужен)
    private MyModel() {
        // можно заменить на super(modelInstance.getClass().getName());
    }

    public static void initModelInstance() {
        Log.i("MyModel", "MyModel::InitInstance()");
        if (modelInstance == null) {
            modelInstance = new MyModel();
        }
    }

    public static MyModel getModelInstance() {
        Log.i("MyModel", "MyModel::getInstance()");
        return modelInstance;
    }

    @Override
    public void downloadFile(Context context) {
        // на основе count создается url
        // проверяется карта и строится path

        //старт сервиса
        Intent startServiceIntent = new Intent(context, MyIntentService.class);
        //TODO здесь будет передача параметров загрузки
        startServiceIntent.putExtra("counter", MyPresenter.counter);
        context.startService(startServiceIntent);
        Log.i("Starting", " IntentService from HomeButton");

    }

}
