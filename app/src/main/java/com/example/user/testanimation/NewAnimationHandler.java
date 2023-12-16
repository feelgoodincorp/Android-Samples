package com.example.user.testanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

/**
 * Created by User on 26.06.2018.
 */

public class NewAnimationHandler {

    Float animatedValueFrom = 0f;
    Float animatedValueTo   = 200f;

    Float firstLevelRight = 200f;
    Float firstLevelLeft = 0f;

    long curTime;


    long duration = 1500l;

    ValueAnimator valueAnimatorView1Right = ValueAnimator.ofFloat(firstLevelLeft,firstLevelRight);
    ValueAnimator valueAnimatorView1Left = ValueAnimator.ofFloat(firstLevelRight,firstLevelLeft);

    ValueAnimator valueAnimatorView2Right = ValueAnimator.ofFloat(firstLevelLeft,firstLevelRight);
    ValueAnimator valueAnimatorView2Left = ValueAnimator.ofFloat(firstLevelRight,firstLevelLeft);


    public void initAnim(final View view, final View view2){
        valueAnimatorView1Right.setDuration(duration);
        valueAnimatorView1Left.setDuration(duration);
        valueAnimatorView2Right.setDuration(duration);
        valueAnimatorView2Left.setDuration(duration);

        valueAnimatorView1Right.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                curTime =  valueAnimatorView1Left.getCurrentPlayTime();
                if (valueAnimatorView1Left != null && valueAnimatorView1Left.isStarted()) {
                    valueAnimatorView1Right.setCurrentPlayTime(duration - curTime);

                    valueAnimatorView1Left.cancel();
                    Log.i("Right View1 animation " , "STARTED WITH CANCEL");
                }else {
                    Log.i("Right View1 animation " , "STARTED WITHOUT CANCEL");
                }

            }


            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Right View1 animation " , "CANCELED");
            }
        });

        valueAnimatorView1Left.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {

                curTime =  valueAnimatorView1Right.getCurrentPlayTime();
                if (valueAnimatorView1Right != null && valueAnimatorView1Right.isStarted()) {
                    valueAnimatorView1Left.setCurrentPlayTime(duration - curTime);

                    valueAnimatorView1Right.cancel();
                    Log.i("Left View1 animation " , "STARTED WITH CANCEL");
                }else {
                    Log.i("Left View1 animation " , "STARTED WITHOUT CANCEL");
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Left View1 animation " , "CANCELED");
            }
        });


        valueAnimatorView1Right.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
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

        //________________View 2 Anim___________________________________________________________________

        valueAnimatorView2Right.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {


                if (valueAnimatorView2Left.isStarted()||valueAnimatorView2Left.isRunning()) {
                    valueAnimatorView2Left.cancel();
                    Log.i("Right View2 animation " , "STARTED WITH CANCEL");
                }else {
                    Log.i("Right View2 animation " , "STARTED WITHOUT CANCEL");
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Right View2 animation " , "CANCELED");
                valueAnimatorView2Left.setCurrentPlayTime(duration - valueAnimatorView2Right.getCurrentPlayTime());
            }
        });


        valueAnimatorView2Left.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {

                if (valueAnimatorView2Right.isStarted()||valueAnimatorView2Right.isRunning()) {
                    valueAnimatorView2Right.cancel();
                    Log.i("Left View2 animation " , "STARTED WITH CANCEL");
                }else {
                    Log.i("Left View2 animation " , "STARTED WITHOUT CANCEL");
                }


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Left View2 animation " , "CANCELED");
                valueAnimatorView2Right.setCurrentPlayTime(duration - valueAnimatorView2Left.getCurrentPlayTime());
            }
        });

        valueAnimatorView2Right.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(valueAnimatorView2Right!=null&&valueAnimatorView2Right.isStarted()) {
                    view2.setTranslationX((Float) valueAnimatorView2Right.getAnimatedValue());
                }else {
                    Log.i("OH MY","GOSHHHHHHHHHHHHHHHH3");
                }
            }
        });


        valueAnimatorView2Left.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //view2.setTranslationX((Float) valueAnimatorView2Left.getAnimatedValue());
                if(valueAnimatorView2Left!=null&&valueAnimatorView2Left.isStarted()) {
                    view2.setTranslationX((Float) valueAnimatorView2Left.getAnimatedValue());
                }else {
                    Log.i("OH MY","GOSHHHHHHHHHHHHHHHH4");
                    //срабатывало во всей длительности противоположной анимации
                }
            }
        });

    }

    public void startViewAnimation(boolean side) {

        if (side) {
            Log.i("Right animations ", "start()");
            //valueAnimatorView1Right.setStartDelay(2000);
            valueAnimatorView1Right.start();
            valueAnimatorView2Right.setStartDelay(800);
            valueAnimatorView2Right.start();



        } else {
            Log.i("_____l2 started?",String.valueOf(valueAnimatorView2Left.isStarted()));
            Log.i("_____r2 started?",String.valueOf(valueAnimatorView2Right.isStarted()));
            Log.i("left animations ", "start()");
            //valueAnimatorView1Left.setStartDelay(2000);
            valueAnimatorView1Left.start();
            valueAnimatorView2Left.setStartDelay(800);
            valueAnimatorView2Left.start();

            //______________________________________________________________________________________________________
            //вот что вызывает ошибку!!!!два старта одной вьюхи!!!!!!!!!!!! (вызввают скорее всего конфликт updatelistener-ов)

            // если вовремя вызвать cancel,все работает
            /*valueAnimatorView2Left.cancel();

            valueAnimatorView2Right.start();*/

        }
    }

    //не проходит на проверку isStarted, поэтому и  не срабатывает cancel, поэтому стартуют две анимации
    // isStarted и StartListener  - разные вещи??
    // сначала старт вправо, затем старт влево без отмены вправо(хотя  в методе ж это отменяется)

}
