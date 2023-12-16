package com.example.user.mvptrying;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;


//TODO можно ли спроектировать так, что данные в metadata меняются как только они есть/появляются новые? вызовом метода смены данных сразу после их получения в http классе


public class MyPresenter implements Contract.ContractPresenter {

    //почему m? - для нестатических непубличных полей

    //по возможности, перенести переменные в неглобальные
    private static MyPresenter presenterInstance;
    private Contract.ContractView mViewInstance;
    private MyModel model = MyModel.getModelInstance();

    private static final String intentActionConnectivityChange = "android.net.conn.CONNECTIVITY_CHANGE";
    static final String intentActionReceiverIntent =  "receiverIntent";

    public boolean connectionState;
    public static int counter = 1;
    boolean isDownloadCycleRuns = false;
    boolean isDownloadCycleShouldContinue = false;
    boolean isDownloadCycleShouldWait = true;

    private Thread downloadCycle;

    public BroadcastReceiver receiver;

    private MyPresenter(){
        //TODO проверить, не работает ли когда не зарегестрирован
        initReceiver();
    }

     static synchronized void initPresenterInstance() {
        Log.i("MyPresenter", "initInstance()");
        if (presenterInstance == null) {
            presenterInstance = new MyPresenter();
        }
    }

    static MyPresenter getPresenterInstance() {
        Log.i("MyPresenter", "getInstance()");
        return presenterInstance;
    }

    @Override
    public void bindView(Contract.ContractView view) {
        this.mViewInstance = view;
    }

    @Override
    public void unbindView() {
        this.mViewInstance = null;
    }

    @Override
    public void initReceiver() {

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent==null) {
                    //Log.i("Receiver"," intent = null");
                } else{

                    //Log.i("Receiver", "action: " + intent.getAction());
                    //Log.i("Receiver", "component: " + intent.getComponent());

                    Bundle bundle = intent.getExtras();
                    boolean lastLoadState = bundle.getBoolean("downloadState");
                    //обнуляется ли интент после ротации - нет
                    //после завершения работы сервиса,интент != null

                    switch (intent.getAction()){
                        case intentActionConnectivityChange :
                            connectionState = checkConnection(context);
                            if(connectionState){
                                isDownloadCycleShouldContinue=true;
                                if(!isDownloadCycleRuns){
                                    Log.i("Receiver", "____________________pre start");
                                    downloadingCycle(context);
                                }
                            }else{
                                isDownloadCycleShouldContinue=false;
                            }
                            MainActivity.updateMetadataValue("connectionState", String.valueOf(connectionState));

                            break;
                        case intentActionReceiverIntent :
                            Log.i("MyPresenter", "intentReceiverAction");

                            if(lastLoadState){
                                mViewInstance.showImageFromPath(bundle.getString("dataPath"));
                                MainActivity.updateMetadataValue("responseStatus", String.valueOf(bundle.getInt("responseStatus")));
                                isDownloadCycleShouldWait = true;
                            }else {
                                MainActivity.updateMetadataValue("responseStatus", String.valueOf(bundle.getInt("responseStatus")));
                                isDownloadCycleShouldWait = false;
                            }

                            /*  По моему нужно обновлять все разом в metadata, чтоб данные от прошлых запросов обнулялись, если нет новых таких же типов данных в новом запросе
                            dataIntent.putExtra("responseStatus",responseStatus);
                            dataIntent.putExtra("contentType", contentType);*/

                            MainActivity.updateMetadataValue("counter", String.valueOf(counter));
                            if(bundle.getBoolean("isLastDataItem")){
                                counter=1;
                            }else{
                                counter++;
                            }
                            break;
                        default: break;
                    }
                }
            }
        };

    }


    public boolean checkConnection(Context context){
        Log.i("MyPresenter", "checkConnection()");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void registerMyReceiver(BroadcastReceiver receiver, Context context){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(intentActionReceiverIntent);
        intentFilter.addAction(intentActionConnectivityChange);
        context.registerReceiver(receiver, intentFilter);
    }

    public void unregisterMyReceiver(BroadcastReceiver receiver, Context context) {
        context.unregisterReceiver(receiver);
    }


    @Override
    public void downloadingCycle(final Context context) {




        /*
        isDownloadCycleShouldWait = true;
        isDownloadCycleRuns = true;
        Log.i("downloadCycle", " isDownloadCycleRuns = true, cycle started");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isDownloadCycleShouldContinue){
                    while (MyIntentService.isIntentServiceRunning){
                        try {
                            Thread.sleep(500); //первый sleep ждет,пока закончится работа сервиса
                            //обезопасить от бесконечного цикла

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //TODO объект будет конечно же перемещен, а вообще нужно проверить потокобезопасность такого подхода
                    model.downloadFile(context);

                    if(isDownloadCycleShouldWait){
                        Log.i("Starting", " waiting 10 seconds");
                        try {
                            Thread.sleep(5000); //второй sleep задает интервал между загрузками
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //TODO тут может не сработать(условие выхода из цикла?),потому что нужно прописать false после start
                isDownloadCycleRuns=false;
                Log.i("downloadCycle", "ends");

                //переменная whait не должна быть false при старте метода

                //Log.i("downloadCycle", " isDownloadCycleRuns = false, cycle ended");
            }
        }).start();*/
    }

}
