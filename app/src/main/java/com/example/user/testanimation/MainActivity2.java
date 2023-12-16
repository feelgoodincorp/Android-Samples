package com.example.user.testanimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 24.06.2018.
 */

public class MainActivity2 extends AppCompatActivity {

    TextView textView1;
    TextView textView2;

    boolean side  = true;

    final OneViewAnimation handler = new OneViewAnimation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);

        handler.initAnim(textView1,textView2);

        chechAnimatedValue();

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(side){
                    handler.startViewAnimation(textView,side);
                }else {
                    handler.startViewAnimation(textView,side);
                }*/

                if(side){
                    handler.startViewAnimation(side);
                    side=false;
                }else {
                    handler.startViewAnimation(side);
                    side=true;
                }

            }
        });



    }


    public  void chechAnimatedValue(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /*if(animator1_1 !=null){f1 =(Float) animator1_1.getAnimatedValue();}
                    if(animator2_1!=null){f2 = (Float) animator2_1.getAnimatedValue();}
                    if(metadata!=null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                metadata.setText("1_animatedValue = "+ String.valueOf(f1) + "\n"
                                        + "2_animatedValue = "+ String.valueOf(f2));
                            }
                        });
                    }*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });



                }
            }
        }).start();
    }

}
