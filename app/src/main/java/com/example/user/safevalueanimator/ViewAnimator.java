package com.example.user.safevalueanimator;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.widget.ImageView;
import com.example.user.safevalueanimator.R;

import java.util.ArrayList;

public class ViewAnimator {

    private static ViewAnimator mainActivityAnimator = new ViewAnimator();
    private ViewAnimator(){}
    public static ViewAnimator getAnimator(){
        return mainActivityAnimator;
    }

    ArrayList <View> primaryMenuRow;
    ArrayList <View> secondaryMenuRow;

    ValueAnimator animateBackgroundTo = ValueAnimator.ofFloat(.0f, 1.0f);
    ValueAnimator animateBackgroundFrom = ValueAnimator.ofFloat(.0f, 1.0f);

    long backgroundAnimationDuration;
    long hamburgerAnimationDuration;

    private final float animateFrom = 0f;
    private final float animateTo = -135f;

    ValueAnimator toCross = ValueAnimator.ofFloat(animateFrom, animateTo);
    ValueAnimator toHamb = ValueAnimator.ofFloat(animateTo, animateFrom);



    public void animateHamburger(boolean direction, long duration){
        //два VA без применения реверса,потому что реверс реверса сделать нельзя
        if(direction){
            //сеттинг длительности оказал влияние на работу при стресс тесте
            toCross.start();
        }else {
            toHamb.start();
        }
    }



    void initHamburgerAnimator(final ImageView upperLine,
                               final ImageView centerLine,
                               final ImageView lowerLine,
                               Resources resources) {




        final long duration = resources.getInteger(R.integer.hamburgerAnimationDuration);
        final float outsideLinesPosDifferenceInPxByY = resources.getDimension(R.dimen.hamburgerLowerLineMarginTop) - resources.getDimension(R.dimen.hamburgerUpperLineMarginTop);
        final float viewWidthInPx = resources.getDimension(R.dimen.hamburgerLinesWidth);
        final float viewHeightInPx = resources.getDimension(R.dimen.hamburgerLinesHeight);
        final int hamburgerColor = resources.getColor(R.color.White);
        final int crossColor = resources.getColor(R.color.Black);

        hamburgerAnimationDuration = duration;

        toCross.setDuration(duration);
        toCross.setInterpolator(null);

        toHamb.setDuration(duration);
        toHamb.setInterpolator(null);

        final float outsideLinesHalf = outsideLinesPosDifferenceInPxByY / 2;
        final float viewWidthHalf = viewWidthInPx / 2;
        final float viewHeightHalf = viewHeightInPx/ 2;
        final float viewSinFromPivot = outsideLinesHalf / (float) Math.sin(Math.toRadians(45)) - outsideLinesHalf;
        final float lineScaleByX = (viewSinFromPivot * 2 + viewWidthInPx) / viewWidthInPx;

        final float firstTactUpperLinePivotX = viewWidthInPx / 2 + outsideLinesPosDifferenceInPxByY / 2;
        final float secondTactUpperLinePivotX = viewWidthHalf - (outsideLinesHalf / (float) Math.sin(Math.toRadians(45)) - outsideLinesHalf);
        final float firstTactLowerLinePivotX = viewWidthInPx / 2 - outsideLinesPosDifferenceInPxByY / 2;
        final float secondTactLowerLinePivotX = viewWidthHalf + (outsideLinesHalf / (float) Math.sin(Math.toRadians(45)) - outsideLinesHalf);

        upperLine.setPivotY(viewHeightHalf);
        upperLine.setPivotX(viewWidthHalf);
        //не bug fix, неизвестно почему, но scale при этом уменьшает высоту view
        centerLine.setPivotY(viewHeightHalf);
        centerLine.setPivotX(viewWidthHalf);

        lowerLine.setPivotY(viewHeightHalf);
        lowerLine.setPivotX(viewWidthHalf);


        System.out.println(outsideLinesHalf);
        System.out.println(viewWidthHalf);
        System.out.println(viewHeightHalf);
        System.out.println(viewSinFromPivot);
        System.out.println(lineScaleByX);
        System.out.println(firstTactUpperLinePivotX);
        System.out.println(secondTactUpperLinePivotX);
        System.out.println(firstTactLowerLinePivotX );
        System.out.println(secondTactLowerLinePivotX);
        System.out.println((float) Math.sin(Math.toRadians(45)));


        toCross.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                upperLine.setPivotX((Float) toCross.getAnimatedValue() >= animateTo / 3f ? firstTactUpperLinePivotX : secondTactUpperLinePivotX);
                upperLine.setTranslationX((Float) toCross.getAnimatedValue() >= animateTo / 3 ? 0 : viewSinFromPivot);
                upperLine.setTranslationY((Float) toCross.getAnimatedValue() >= animateTo / 3 ? 0 : outsideLinesHalf);
                upperLine.setRotation((Float) toCross.getAnimatedValue());

                //centerLine.setRotation(135);
                centerLine.setScaleX((Float) toCross.getAnimatedValue() >= animateTo / 3f ? 1 : lineScaleByX);
                centerLine.setRotation((Float) toCross.getAnimatedValue() >= animateTo / 3f ? (Float) toCross.getAnimatedValue() : ((Float) toCross.getAnimatedValue() * (toCross.getAnimatedFraction() + 0.6666667f)));

                lowerLine.setPivotX((Float) toCross.getAnimatedValue() >= animateTo / 3f ? firstTactLowerLinePivotX : secondTactLowerLinePivotX);
                lowerLine.setTranslationX((Float) toCross.getAnimatedValue() >= animateTo / 3 ? 0 : -viewSinFromPivot);
                lowerLine.setTranslationY((Float) toCross.getAnimatedValue() >= animateTo / 3 ? 0 : -outsideLinesHalf);
                lowerLine.setRotation((Float) toCross.getAnimatedValue() * 1);

                upperLine.setColorFilter(ColorUtils.blendARGB(hamburgerColor, crossColor, toCross.getAnimatedFraction()));
                centerLine.setColorFilter(ColorUtils.blendARGB(hamburgerColor, crossColor, toCross.getAnimatedFraction()));
                lowerLine.setColorFilter(ColorUtils.blendARGB(hamburgerColor, crossColor, toCross.getAnimatedFraction()));

            }
        });

        toHamb.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                upperLine.setPivotX((Float) toHamb.getAnimatedValue() >= animateTo / 3f ? firstTactUpperLinePivotX : secondTactUpperLinePivotX);
                upperLine.setTranslationX((Float) toHamb.getAnimatedValue() >= animateTo / 3 ? 0 : viewSinFromPivot);
                upperLine.setTranslationY((Float) toHamb.getAnimatedValue() >= animateTo / 3 ? 0 : outsideLinesHalf);
                upperLine.setRotation((Float) toHamb.getAnimatedValue());

                centerLine.setScaleX((Float) toHamb.getAnimatedValue() <= (animateTo / 3) ? (1f + ((0.7f - toHamb.getAnimatedFraction()) / 0.7f) * (lineScaleByX - 1f)) : 1f);
                centerLine.setRotation((Float) toHamb.getAnimatedValue() <= animateTo / 3f ? ((Float) toHamb.getAnimatedValue() * ((1 - toHamb.getAnimatedFraction()) + 0.6666667f)) : (Float) toHamb.getAnimatedValue());

                lowerLine.setPivotX((Float) toHamb.getAnimatedValue() >= animateTo / 3f ? firstTactLowerLinePivotX : secondTactLowerLinePivotX);
                lowerLine.setTranslationX((Float) toHamb.getAnimatedValue() >= animateTo / 3 ? 0 : -viewSinFromPivot);
                lowerLine.setTranslationY((Float) toHamb.getAnimatedValue() >= animateTo / 3 ? 0 : -outsideLinesHalf);
                lowerLine.setRotation((Float) toHamb.getAnimatedValue() * 1);

                upperLine.setColorFilter(ColorUtils.blendARGB(crossColor, hamburgerColor, toHamb.getAnimatedFraction()));
                centerLine.setColorFilter(Color.GREEN);
                centerLine.setColorFilter(ColorUtils.blendARGB(crossColor, hamburgerColor, toHamb.getAnimatedFraction()));
                lowerLine.setColorFilter(ColorUtils.blendARGB(crossColor, hamburgerColor, toHamb.getAnimatedFraction()));
            }
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

}
