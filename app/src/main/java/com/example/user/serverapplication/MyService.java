package com.example.user.serverapplication;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 29.03.2018.
 */

public class MyService extends IntentService {
    public static volatile boolean shouldContinue = true;
    public static int result = Activity.RESULT_CANCELED;
    public static String data = "this is loaded data";
    public static int count = 0;
    public static final String NOTIFICATION = "notification";
    private byte[] a;


    public MyService() {
        super("MyService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        /*Bundle bundle = intent.getExtras();
        count =  bundle.getInt("counter");*/

        while (shouldContinue) {
            /*switch (count){
                case 0:a = getResources().getDrawable(R.drawable.diods); break;
                case 1:TemporaryAnimation.drawable = getResources().getDrawable(R.drawable.forest); break;
                case 2:TemporaryAnimation.drawable = getResources().getDrawable(R.drawable.window); break;
            }*/
            Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.window);
            Log.i(TAG, "servicelog | bitmap != null " + Boolean.valueOf(bitmap!=null));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Log.i(TAG, "servicelog | stream != null " + Boolean.valueOf(stream!=null));
            Log.i(TAG, "servicelog | streamsize = " + String.valueOf(stream.size()));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();
            int a = bytes.length;
            Log.i(TAG, "servicelog | bytes[] = " + String.valueOf(a));


            Intent my_intent = new Intent(NOTIFICATION);
            my_intent.putExtra("bitmapbytes",bytes);
            my_intent.putExtra("result",result );
            my_intent.putExtra("count",count);

            sendBroadcast(my_intent);

            if(count<2){count++;}else{count=0;}
            try {
                TimeUnit.SECONDS.sleep(1);
                Log.i(TAG, "servicelog - counter(); ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // check the condition
            if (!shouldContinue) {
                Log.i(TAG, "servicelog - it stoped ");
                stopSelf();
                return;
            }
        }
    }
}






/*extends IntentService {

    public static int result = Activity.RESULT_CANCELED;
    public static String data = "this is loaded data";
    public static int count = 0;
    public static final String NOTIFICATION = "notification";


    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle bundle = intent.getExtras();
        count =  bundle.getInt("counter");
        if(count>3) {stopSelf();onDestroy(); return;}else{
        if(data!=null){result = Activity.RESULT_OK;}
        while(true){publishResults(data,result); count++;
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();

            if (isConnected) {
                count=count+100;
            }
        try {
            TimeUnit.SECONDS.sleep(1);
            Log.i(TAG, "servicelog - counter(); " );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        }   }                                  //this method to the cycle

    }

*//*    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "servicelog - startCommand " );
        return START_NOT_STICKY;
    }*//*

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("servicelog", "onDestroy");
    }


    public void onCreate() {
        super.onCreate();
        Log.d("servicelog", "onCreate");
    }

    private void publishResults(String fileData, int loadingResult) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra("result", loadingResult);
        intent.putExtra("dataName",fileData);
        intent.putExtra("count",count);

        sendBroadcast(intent);

    }

}*/