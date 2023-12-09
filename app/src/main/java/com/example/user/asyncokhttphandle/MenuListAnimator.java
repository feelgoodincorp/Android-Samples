package com.example.user.asyncokhttphandle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MenuListAnimator {

    private static MenuListAnimator menuListAnimator = new MenuListAnimator();
    private MenuListAnimator(){}
    public static MenuListAnimator getViewAnimator(){
        return menuListAnimator;
    }

    private ValueAnimator animateBackgroundTo = ValueAnimator.ofFloat(.0f, 1.0f);
    private ValueAnimator animateBackgroundFrom = ValueAnimator.ofFloat(.0f, 1.0f);

    private float lastPosition;

    public void animateItems(ArrayList<View> viewArrayList, long duration, long startOffset, float translationX, int pivot){
        float startOffsetArray[] = generateStartOffsetQueue(startOffset,pivot,viewArrayList.size());
        //last position будет один для всех?
        //анимация фона
        //позиция в пейзажной ориентации
        //state machine покрывает проблему ориентации, позиции меню при ротации
        //(скормить методы анимации состоянию,а состояние вызывать из активности,передав дефолтные значения и значения для кождого из состояний)
        for(int a = 0; a<viewArrayList.size(); a++){
            viewArrayList.get(a).animate().translationX(translationX).setDuration(duration).setStartDelay((long) startOffsetArray[a]).start();
        }
        lastPosition = translationX;
    }


    public void initAnimateBackground(View view, int duration, String from, String to){
        animateBackgroundFrom.setDuration(duration);
        animateBackgroundTo.setDuration(duration);


        animateBackgroundTo.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (animateBackgroundFrom.isStarted()) {
                    animateBackgroundTo.setCurrentPlayTime(duration - animateBackgroundFrom.getCurrentPlayTime());
                    animateBackgroundFrom.cancel();
                }
            }
        });

        animateBackgroundFrom.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (animateBackgroundTo.isStarted()) {
                    animateBackgroundFrom.setCurrentPlayTime(duration - animateBackgroundTo.getCurrentPlayTime());
                    animateBackgroundTo.cancel();
                }
            }
        });

        animateBackgroundTo.addUpdateListener(animation -> {
            view.setBackgroundColor(ColorUtils.blendARGB(Color.parseColor(from)
                    ,Color.parseColor(to)
                    ,(Float) animateBackgroundTo.getAnimatedValue()));
        });

        animateBackgroundFrom.addUpdateListener(animation -> {
            view.setBackgroundColor(ColorUtils.blendARGB(Color.parseColor(to)
                    ,Color.parseColor(from)
                    ,(Float) animateBackgroundFrom.getAnimatedValue()));
        });

    }
    public void animateBackgroundColor(boolean direction){
        if(direction){
            animateBackgroundFrom.start();
        }else {
            animateBackgroundTo.start();
        }
    }

    public void notifyViewsPosition(ArrayList<View> viewArrayList){
        if(viewArrayList.size()>0){
            for (View view: viewArrayList) {
                view.setTranslationX(lastPosition);
                //инвалидация по одному значения, потому что разположение изменяется относительно позиции в xml
                Log.i("invalidate = ",String.valueOf(lastPosition));
            }
        }
    }

    private float [] generateStartOffsetQueue(long startOffset, int pivot,int arraySize){
        float sortedArray [] = new float [arraySize];
        for(int a = 0; a<arraySize; a++){
            sortedArray[a] = a>pivot ? (a-pivot)*startOffset : (pivot-a)*startOffset;
        }
        return sortedArray;
    }


    public void animateMenuItemsColor(View animatableView,String propertyName,int startColor,int endColor,int duration){
        final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(animatableView,
                propertyName,
                new android.support.graphics.drawable.ArgbEvaluator(), // try/catch
                startColor,
                endColor);
        backgroundColorAnimator.setDuration(duration);
        backgroundColorAnimator.start();
    }


/*
    private static MenuListAnimator menuListAnimator = new MenuListAnimator();
    private MenuListAnimator(){}

    public static MenuListAnimator getViewAnimator(){
        return menuListAnimator;
    }

    ValueAnimator valueAnimator;
    long duration;

    void initMenuListAnimator(float start,float end,View view){
        duration = 3000l;
        valueAnimator = ValueAnimator.ofFloat(start,end);
        valueAnimator.setDuration(duration);
        valueAnimator.setStartDelay(1500l);
        valueAnimator.setInterpolator(null);

        valueAnimator.addUpdateListener(animation -> {
            view.setTranslationX((Float) valueAnimator.getAnimatedValue());
        });
    }


    void startMenuListAnimator(){
        valueAnimator.start();
    }
}

*/


    void startMenuListAnimator(boolean side){
            if(side){
                //toRightAnimator.start();
            }else{
                //toLeftAnimator.start();
            }

        }

    }


