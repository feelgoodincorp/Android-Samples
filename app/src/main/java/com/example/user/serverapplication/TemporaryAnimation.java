package com.example.user.serverapplication;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * Created by User on 11.03.2018.
 */

public class TemporaryAnimation extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mCLayout;
    private Button mButton;
    private TextView mTextView;
    private  ImageView imageView;

    int counter;
    boolean active = false;
    int a=0;

    static boolean checkstate = false;
    public static Drawable drawable;

    static Intent intent;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int resultCode = bundle.getInt("result");
                String string = bundle.getString("dataName");
                counter = bundle.getInt("count");
                byte[] bytes = intent.getByteArrayExtra("bitmapbytes");
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                drawable = new BitmapDrawable(bmp);
                imageView.setImageDrawable(drawable);
                if (string!= null) {
                    mTextView.setText("Download done"+ string+" "+counter);
                } else {
                    mTextView.setText("Download failed");
                }
            }
        }
    };



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.templayout);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = TemporaryAnimation.this;

        // Get the widget reference from XML layout
        mCLayout = (RelativeLayout) findViewById(R.id.coordinator_layout);
        mButton = (Button) findViewById(R.id.btn);
        mTextView = (TextView) findViewById(R.id.tv);



        if(drawable!=null){imageView.setImageDrawable(drawable);}

        // Initialize a new click listener for button widget
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                MyService.NOTIFICATION));
        MyService.shouldContinue=true;
        intent = new Intent(getApplicationContext(), MyService.class);
        intent.putExtra("counter",counter);
        startService(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //влияет ли на целосность передачи при ротации?
        MyService.shouldContinue=false;
        unregisterReceiver(receiver);
        Log.i("activity ", "onPause");
    }

}

/* extends AppCompatActivity {
    RelativeLayout container;
    AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.templayout);


        container = (RelativeLayout) findViewById(R.id.container);

        anim = (AnimationDrawable) container.getBackground();
        anim.setDither(true);
        anim.setEnterFadeDuration(6000);
        anim.setExitFadeDuration(2000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning())
            anim.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && anim.isRunning())
            anim.stop();
    }
}

    Animation animation;
    TextView view;
    TextView view2;
    TextView view3;
    ImageView imageView;
    ObjectAnimator animator;
    int screenWidth1;
    boolean toDoClick = false;
    long curPlTm=0;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        long animationCurrentPlayTime = 0;
        if(animator!=null){
        animationCurrentPlayTime = animator.getCurrentPlayTime();
        }
        savedInstanceState.putLong("animCurPlayTime",animationCurrentPlayTime);
        savedInstanceState.putBoolean("todoclick",toDoClick);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.templayout);
        view = findViewById(R.id.temptext1);
        view2 = findViewById(R.id.temptext2);
        view3 = findViewById(R.id.temptext3);
        imageView = findViewById(R.id.tempImageView);


        if (savedInstanceState != null) {curPlTm = savedInstanceState.getLong("animCurPlayTime");
                                         toDoClick = savedInstanceState.getBoolean("todoclick");
                                            view3.setText(String.valueOf(curPlTm));
                                         }
        screenWidth1 = -Math.round(getResources().getDisplayMetrics().widthPixels);
        view.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int viewWidth = view.getMeasuredWidth();

        *//*RelativeLayout.LayoutParams rlParams =
                (RelativeLayout.LayoutParams)view3.getLayoutParams();
        rlParams.setMargins(0, 0, -(screenWidth1), 0);

        view3.setLayoutParams(rlParams);*//*


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //что насчет imageDrawable?
                imageView.setImageResource(R.drawable.m_vector);
                toDoClick=true;
                int pushValue=-600;
                int duration = 5000;
                animator = ObjectAnimator.ofFloat(view3, View.TRANSLATION_X,pushValue);
                animator.setDuration(duration);

                animator.start();animator.setCurrentPlayTime(curPlTm);

                view2.setText(String.valueOf(curPlTm));

                *//*
                int[] locationOnScreent = new int[2];
                view2.getLocationOnScreen(locationOnScreent);
                long playTime = 0;
                int pushValue = 0;
                pushValue=-600;
                int duration = 5000;


                //get/set current time!!!

                pushValue=pushValue+locationOnScreent[0];
                if(animator!=null){ playTime = animator.getCurrentPlayTime();}
                animator = ObjectAnimator.ofFloat(view3, View.TRANSLATION_X,pushValue);
                time = time - (int) playTime;
                animator.setCurrentPlayTime(time);
                animator.setDuration(duration-time);
                animator.start();
                view3.setText(String.valueOf(playTime));*//*
            }
        });
        if(animator != null){}
        if(toDoClick){view.performClick();}

    }
}
*/