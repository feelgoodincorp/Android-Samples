package com.example.user.betaserveraplication;

import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by User on 10.06.2018.
 */

public class MainModel implements MainContract.Model {
    @Override
    public void loadData() {
        //TODO метод загрузки данных
        //может давать возвращаемое значение в виде ____

        Log.i("LOADING CYCLE", " STARTED");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("LOADING", " 8 SEC.");
                try {
                    TimeUnit.SECONDS.sleep(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("LOADING CYCLE", " SHOW IMAGE METHOD");
                Log.i("LOADING CYCLE", " ENDED");
            }
        }).start();
    }
}
