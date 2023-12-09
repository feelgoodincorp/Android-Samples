package com.example.user.asyncokhttphandle;


import android.animation.ValueAnimator;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class OneViewAnimation {

    private static OneViewAnimation oneViewAnimation = new OneViewAnimation();

    private OneViewAnimation(){}

    public static OneViewAnimation getViewAnimator(){
        return oneViewAnimation;
    }

    private float animateFrom = 0f;
    private float animateTo = -135f;

    ValueAnimator toCross = ValueAnimator.ofFloat(animateFrom, animateTo);
    ValueAnimator toHamb = ValueAnimator.ofFloat(animateTo, animateFrom);

    void initHamburgerAnimator(ImageView upperLine,
                               ImageView centerLine,
                               ImageView lowerLine,
                               long duration,
                               float outsideLinesPosDifferenceInPxByY,
                               float viewWidthInPx,
                               float viewHeightInPx,
                               int hamburgerColor,
                               int crossColor){

        toCross.setDuration(duration);
        toCross.setInterpolator(null);

        toHamb.setDuration(duration);
        toHamb.setInterpolator(null);

        float outsideLinesHalf = outsideLinesPosDifferenceInPxByY/2;
        float viewWidthHalf = viewWidthInPx/2;
        float viewHeightHalf = viewHeightInPx/2;
        float viewSinFromPivot = outsideLinesHalf/(float)Math.sin(Math.toRadians(45))-outsideLinesHalf;
        float lineScaleByX = (viewSinFromPivot*2+viewWidthInPx)/ viewWidthInPx;

        float firstTactUpperLinePivotX = viewWidthInPx/2+outsideLinesPosDifferenceInPxByY/2;
        float secondTactUpperLinePivotX = viewWidthHalf-(outsideLinesHalf/(float)Math.sin(Math.toRadians(45))-outsideLinesHalf);
        float firstTactLowerLinePivotX = viewWidthInPx/2-outsideLinesPosDifferenceInPxByY/2;
        float secondTactLowerLinePivotX = viewWidthHalf+(outsideLinesHalf/(float)Math.sin(Math.toRadians(45))-outsideLinesHalf);

        upperLine.setPivotY(viewHeightHalf);
        lowerLine.setPivotY(viewHeightHalf);


        toCross.addUpdateListener(animation -> {

            upperLine.setPivotX((Float) toCross.getAnimatedValue()>= animateTo /3f ? firstTactUpperLinePivotX : secondTactUpperLinePivotX);
            upperLine.setTranslationX((Float) toCross.getAnimatedValue() >= animateTo /3 ? 0 : viewSinFromPivot);
            upperLine.setTranslationY((Float) toCross.getAnimatedValue() >= animateTo /3 ? 0 : outsideLinesHalf);
            upperLine.setRotation((Float) toCross.getAnimatedValue());

            centerLine.setScaleX((Float) toCross.getAnimatedValue()>= (animateTo /3) ? 1 : (1f + (((toCross.getAnimatedFraction()-0.3f)/0.7f) * (lineScaleByX - 1f))));
            centerLine.setRotation((Float) toCross.getAnimatedValue()>= animateTo /3f ? (Float) toCross.getAnimatedValue() : ((Float) toCross.getAnimatedValue() * (toCross.getAnimatedFraction() + 0.6666667f)));


            lowerLine.setPivotX((Float) toCross.getAnimatedValue() >= animateTo /3f ? firstTactLowerLinePivotX : secondTactLowerLinePivotX);
            lowerLine.setTranslationX((Float) toCross.getAnimatedValue() >= animateTo /3 ? 0 : -viewSinFromPivot);
            lowerLine.setTranslationY((Float) toCross.getAnimatedValue() >= animateTo /3 ? 0 : -outsideLinesHalf);
            lowerLine.setRotation((Float) toCross.getAnimatedValue() * 1);

            upperLine.setColorFilter(ColorUtils.blendARGB(hamburgerColor, crossColor, toCross.getAnimatedFraction()));
            centerLine.setColorFilter(ColorUtils.blendARGB(hamburgerColor, crossColor, toCross.getAnimatedFraction()));
            lowerLine.setColorFilter(ColorUtils.blendARGB(hamburgerColor, crossColor, toCross.getAnimatedFraction()));
        });

        toHamb.addUpdateListener(animation -> {

            upperLine.setPivotX((Float) toHamb.getAnimatedValue()>= animateTo /3f ? firstTactUpperLinePivotX : secondTactUpperLinePivotX);
            upperLine.setTranslationX((Float) toHamb.getAnimatedValue()>= animateTo /3 ? 0 : viewSinFromPivot);
            upperLine.setTranslationY((Float) toHamb.getAnimatedValue()>= animateTo /3 ? 0 : outsideLinesHalf);
            upperLine.setRotation((Float) toHamb.getAnimatedValue());

            centerLine.setScaleX((Float) toHamb.getAnimatedValue() <= (animateTo /3) ? (1f+((0.7f - toHamb.getAnimatedFraction()) / 0.7f) *(lineScaleByX-1f)) : 1f);
            centerLine.setRotation((Float) toHamb.getAnimatedValue()<= animateTo /3f ? ((Float) toHamb.getAnimatedValue() * ((1-toHamb.getAnimatedFraction()) + 0.6666667f)) : (Float) toHamb.getAnimatedValue());

            lowerLine.setPivotX((Float) toHamb.getAnimatedValue() >= animateTo /3f ? firstTactLowerLinePivotX : secondTactLowerLinePivotX);
            lowerLine.setTranslationX((Float) toHamb.getAnimatedValue() >= animateTo /3 ? 0 : -viewSinFromPivot);
            lowerLine.setTranslationY((Float) toHamb.getAnimatedValue() >= animateTo /3 ? 0 : -outsideLinesHalf);
            lowerLine.setRotation((Float) toHamb.getAnimatedValue() * 1);

            upperLine.setColorFilter(ColorUtils.blendARGB(crossColor, hamburgerColor, toHamb.getAnimatedFraction()));
            centerLine.setColorFilter(ColorUtils.blendARGB(crossColor, hamburgerColor, toHamb.getAnimatedFraction()));
            lowerLine.setColorFilter(ColorUtils.blendARGB(crossColor, hamburgerColor, toHamb.getAnimatedFraction()));
        });

        toHamb.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (toCross.isStarted()) {
                    toHamb.setCurrentPlayTime(duration - toCross.getCurrentPlayTime());
                    toCross.cancel();
                }
            }
        });

        toCross.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (toHamb.isStarted()) {
                    toCross.setCurrentPlayTime(duration - toHamb.getCurrentPlayTime());
                    toHamb.cancel();
                }
            }
        });


    }



    public void startHamburgerAnimation(boolean state){

        //два VA без применения реверса,потому что реверс реверса сделать нельзя

        if(state){
            toCross.start();
        }else {
            toHamb.start();
        }

    }

    //______________________________________________
    //  hamburger можно сделать цельным методом startHamburger(flag){
    //          case 0: hamburgerAnim(params)
    //          params - прописаны в dimens,перенесены в static поля этого класса
    //          flag - прописаны в этом классе
    //______________________________________________
    //статические переменные позиций каждого аниматора
    //setPosition(Animator animator1, class.animator1position1)
    //вложенные классы? метод для каждого VA?
    //   |
    //   |
    // вложенные
    //классы дадут возможность выбирать,
    //какая view может анимироватся после ротации
    //______________________________________________
    //активность хранит значения позиций и использует их в initAnimator и onRestoreActivity и onClickListener
    //и вызывает методы м
    //______________________________________________
    //использовать реверс вместо обратных значений анимации? ограничивает minAPI(>19)
    //______________________________________________
    //можно ли использовать одну VA для горизонтальных действий?


}
