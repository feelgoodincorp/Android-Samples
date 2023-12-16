package com.example.user.in_visiblealgorithm;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by User on 22.12.2017.
 */

public class ShadowInterface2 extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Button button = new Button(this);
        RelativeLayout.LayoutParams bLayoutParams = new RelativeLayout.LayoutParams(180,80);
        button.setLayoutParams(bLayoutParams);
        button.setId(R.id.button);
      /*  button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "lalalala", Toast.LENGTH_LONG).show();
            }
        });*/

        /*final RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);

        relativeLayout.setAlpha(0.3f);
        relativeLayout.setBackgroundColor(Color.BLACK);
        relativeLayout.addView(button);
        relativeLayout.setId(R.id.relativelayoutd);*/

        //нужен контейнер,в котором скроллвью и видимо невидимо
        final float len= getResources().getDisplayMetrics().densityDpi/6;
        View.OnTouchListener controlButtonTouchListener = new View.OnTouchListener() {
            float initX;
            float initY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        initX=event.getX();
                        initY=event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        initX-=event.getX(); //now has difference value
                        initY-=event.getY(); //now has difference value
                        if (initY > len) {
                            Toast.makeText(getApplication(),"Swipe up",Toast.LENGTH_SHORT).show();

                        }
                        else if(initY<-len){
                            Toast.makeText(getApplication(),"Swipe Down",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(initY < 0)initY=-initY;
                            if(initX < 0) initX=-initX;
                            if(initX <=len/4 && initY <=len/4){
                                Toast.makeText(getApplication(),"Click",Toast.LENGTH_SHORT).show();
                            }

                        }
                        break;

                }
                return true;
            }
        };

        final RelativeLayout relativeLayout2 = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout2.setLayoutParams(layoutParams2);
        relativeLayout2.setAlpha(0.3f);
        relativeLayout2.setBackgroundColor(Color.BLACK);
        relativeLayout2.setOnTouchListener(controlButtonTouchListener);
        relativeLayout2.addView(button);


       /* relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(relativeLayout.getAlpha()==0.3f){
                    relativeLayout.setAlpha(0.0f);
                }else if(relativeLayout.getAlpha()==0.0f){
                    relativeLayout.setAlpha(0.3f);
                }
            }
        });*/
        setContentView(relativeLayout2);
    }
}
