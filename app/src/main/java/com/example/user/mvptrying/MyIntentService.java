package com.example.user.mvptrying;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;


public class MyIntentService extends IntentService {

    public static boolean isIntentServiceRunning = false;

    int counterValue;
    int responseStatus;
    boolean isLastDataItem;
    boolean downloadState  = false;//по дефолту false //обязательно к отправке ресиверу //может быть int для обозначения разных ситуаций загрузки(хотя вообще зачем эта переменная!?)
    String contentType;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        /*intent!=null?
        extra = intent.get_extra
        downloadstate = donwload(extra)
        dataIntent.putExtra("downloadState", downloadState);
        */

        Log.i("MyModel", "DownloadData():: response");
        Intent dataIntent = new Intent(MyPresenter.intentActionReceiverIntent);

        downloadState = false;
        isLastDataItem = false;
        responseStatus = 200;
        contentType = ".jpg";

        dataIntent.putExtra("downloadState", downloadState);
        dataIntent.putExtra("isLastDataItem",isLastDataItem);
        dataIntent.putExtra("responseStatus",responseStatus);
        dataIntent.putExtra("contentType", contentType);
        sendBroadcast(dataIntent);

    }

    //boolean можно  заменить на более широкий спектр ответов
    private boolean donwload(String fromURL, String toPath){

        return false;
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        Log.i(getClass().getName(), "IntentService_removed from Recent");
        super.onTaskRemoved(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(getClass().getName(), "IntentService_onDestroy()");
        isIntentServiceRunning = false;
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        Log.i(getClass().getName(), "IntentService_onCreate()");
        isIntentServiceRunning = true;
        super.onCreate();
    }

}
