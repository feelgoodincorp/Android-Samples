package com.example.user.testanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

/**
 * Created by User on 26.06.2018.
 */

public class OneViewAnimation {

    Float firstLevelRight = 200f;
    Float firstLevelLeft = 0f;

    long curTime;
    long duration = 1500l;

    ValueAnimator valueAnimatorView1Right = ValueAnimator.ofFloat(firstLevelLeft,firstLevelRight);
    ValueAnimator valueAnimatorView1Left = ValueAnimator.ofFloat(firstLevelRight,firstLevelLeft);


    public void initAnim(final View view, final View view2){
        valueAnimatorView1Right.setDuration(duration);
        valueAnimatorView1Left.setDuration(duration);

        valueAnimatorView1Right.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {

                if (valueAnimatorView1Left.isStarted()) {
                    valueAnimatorView1Left.cancel();
                    valueAnimatorView1Right.setCurrentPlayTime(duration - valueAnimatorView1Left.getCurrentPlayTime());
                    Log.i("Right View1 animation " , "STARTED WITH CANCEL");
                }else {
                    Log.i("Right View1 animation " , "STARTED WITHOUT CANCEL");
                }

            }


            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Right View1 animation " , "CANCELED");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i("Right View1 animation " , "ENDED");
            }


        });

        valueAnimatorView1Left.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {


                if (valueAnimatorView1Right.isStarted()) {
                    valueAnimatorView1Right.cancel();
                    valueAnimatorView1Left.setCurrentPlayTime(duration - valueAnimatorView1Right.getCurrentPlayTime());
                    Log.i("Left View1 animation " , "STARTED WITH CANCEL");
                }else {
                    Log.i("Left View1 animation " , "STARTED WITHOUT CANCEL");
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Left View1 animation " , "CANCELED");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i("Left View1 animation " , "ENDED");
            }

        });


        valueAnimatorView1Right.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //слушатель реагирует на анимации вправо, но else,если влево еще running
                if(!valueAnimatorView1Left.isRunning()) {
                    view.setTranslationX((Float) valueAnimatorView1Right.getAnimatedValue());
                }else {
                    Log.i("OH MY","GOSHHHHHHHHHHHHHHHH1");
                }
            }
        });


        valueAnimatorView1Left.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(!valueAnimatorView1Right.isRunning()) {
                    view.setTranslationX((Float) valueAnimatorView1Left.getAnimatedValue());
                }else {
                    Log.i("OH MY","GOSHHHHHHHHHHHHHHHH2");
                }
            }
        });



    }

    public void startViewAnimation(boolean side) {

        if (side) {
            Log.i("Right animations ", "start()");
            valueAnimatorView1Right.setStartDelay(800);
            valueAnimatorView1Right.start();


        } else {
            Log.i("Left animations ", "start()");
            valueAnimatorView1Left.setStartDelay(800);
            valueAnimatorView1Left.start();
        }
    }





}
