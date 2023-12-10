package com.example.user.dynamicalviewpager;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    public boolean cycleShouldContinue;
    public boolean cycleIsRuns;
    public int counter;

    static TextView currentCountTextView;
    TextView lastShowedCountTextView;
    Button startButton;
    Button stopButton;

    Example runnable = new Example();

    static MainActivity activity;

    Thread thread = new Thread(runnable);


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentCountTextView = findViewById(R.id.currentCount);
        lastShowedCountTextView = findViewById(R.id.lastShowedCount);
        startButton = findViewById(R.id.start);
        stopButton = findViewById(R.id.stop);

        activity = this;

        //пауза перед thread.start, потому что он запустит runnable.run()
        //(возможно перенести в on)

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("STOP BUTTON","PRESSED");
                runnable.stop();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("START BUTTON","PRESSED");
                Log.i("Thread state = ", String.valueOf(thread.getState()));
                runnable.resume();
                //нельзя вызвать wait на неработающем потоке
                if((thread.getState()==Thread.State.NEW)){
                    Log.i("thread state",String.valueOf(thread.isAlive()));
                    thread = new Thread(runnable);
                    thread.start();
                }else if(thread.getState()==Thread.State.TERMINATED){
                    thread = new Thread(runnable);
                    Log.i("thread are ","created");
                    thread.start();
                    //runnable resume
                }
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();
        runnable.pause();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!runnable.isStarted()){
            runnable.pause();
        }

    }

    public static void setCounter(final String s){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentCountTextView.setText(s);
            }
        });

    }

}