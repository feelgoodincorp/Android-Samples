package com.example.user.demoApp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.widget.ImageView;

//TODO fix hamburger - asymmetry(correct middle line), add independent setting of duration, proceed animation after rotation.
//TODO should animation of buttons have different duration for different values of translation?

class MainActivityAnimator {

    private static MainActivityAnimator mainActivityAnimator = new MainActivityAnimator();
    private MainActivityAnimator(){}
    static MainActivityAnimator getAnimator(){
        return mainActivityAnimator;
    }

    private ValueAnimator animateUiBackgroundTo = ValueAnimator.ofFloat(.0f, 1.0f);
    private ValueAnimator animateUiBackgroundFrom = ValueAnimator.ofFloat(.0f, 1.0f);

    private long uiBackgroundAnimationDuration;
    private long hamburgerAnimationDuration;

    private final float animateFrom = 0f;
    private final float animateTo = -135f;

    private ValueAnimator toCross = ValueAnimator.ofFloat(animateFrom, animateTo);
    private ValueAnimator toHamburger = ValueAnimator.ofFloat(animateTo, animateFrom);

    void translateViewByX(View view, float translation, long duration, long startOffset){
        view.animate().translationX(translation).setDuration(duration).setStartDelay(startOffset).start();
    }

    //safe - means that the animation will occur based on the current value of the animation
    //to ensure smoothness when changing the direction of the animation
    void safeAnimateUiBackgroundColor(boolean direction){
        animateUiBackgroundTo.setDuration(uiBackgroundAnimationDuration);
        animateUiBackgroundFrom.setDuration(uiBackgroundAnimationDuration);

        long a = 0;
        if(direction){
            if(animateUiBackgroundTo.isStarted()) {
                a = animateUiBackgroundTo.getDuration() - animateUiBackgroundTo.getCurrentPlayTime();
            }else if(animateUiBackgroundFrom.isStarted()){
                a = animateUiBackgroundFrom.getCurrentPlayTime();
            }
            animateUiBackgroundFrom.start();
            animateUiBackgroundFrom.setCurrentPlayTime(a);
        }else {
            if(animateUiBackgroundFrom.isStarted()) {
                a = animateUiBackgroundFrom.getDuration() - animateUiBackgroundFrom.getCurrentPlayTime();
            }else if(animateUiBackgroundTo.isStarted()) {
                a = animateUiBackgroundTo.getCurrentPlayTime();
            }
            animateUiBackgroundTo.start();
            animateUiBackgroundTo.setCurrentPlayTime(a);
        }
    }

    void safeAnimateHamburger(boolean direction){
        toHamburger.setDuration(hamburgerAnimationDuration);
        toCross.setDuration(hamburgerAnimationDuration);

        long a = 0;
        if(direction){
            if(toHamburger.isRunning()) {
                a = toHamburger.getDuration() - toHamburger.getCurrentPlayTime();
            }else if(toCross.isRunning()){
                a = toCross.getCurrentPlayTime();
            }
            toCross.start();
            toCross.setCurrentPlayTime(a);
        }else {
            if(toCross.isRunning()) {
                a = toCross.getDuration() - toCross.getCurrentPlayTime();
            }else if(toHamburger.isRunning()) {
                a = toHamburger.getCurrentPlayTime();
            }
            toHamburger.start();
            toHamburger.setCurrentPlayTime(a);
        }
    }

    //nullable-duration animation uses after MainView-activity resumed
    void nullableUiBackgroundAnimation(boolean direction){
        if(direction){
            animateUiBackgroundFrom.setDuration(0);
            animateUiBackgroundFrom.start();
        }else {
            animateUiBackgroundTo.setDuration(0);
            animateUiBackgroundTo.start();
        }
    }


    void nullableHamburgerAnimation(boolean direction){
        if(direction){
            toCross.setDuration(0);
            toCross.start();
        }else {
            toHamburger.setDuration(0);
            toHamburger.start();
        }
    }

    void initUiBackgroundAnimation(View view, Resources resources){
        int from = resources.getColor(R.color.White);
        int to = resources.getColor(R.color.fullAlpha);

        animateUiBackgroundFrom.removeAllListeners();
        animateUiBackgroundTo.removeAllListeners();

        animateUiBackgroundFrom.setDuration(resources.getInteger(R.integer.backgroundColorChangeDuration));
        animateUiBackgroundTo.setDuration(resources.getInteger(R.integer.backgroundColorChangeDuration));
        uiBackgroundAnimationDuration = resources.getInteger(R.integer.backgroundColorChangeDuration);


        animateUiBackgroundTo.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (animateUiBackgroundFrom.isStarted()) {
                    animateUiBackgroundTo.setCurrentPlayTime(animateUiBackgroundTo.getDuration() - animateUiBackgroundFrom.getCurrentPlayTime());
                    animateUiBackgroundFrom.cancel();
                }
                }
        });

        animateUiBackgroundFrom.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (animateUiBackgroundTo.isStarted()) {
                    animateUiBackgroundFrom.setCurrentPlayTime(animateUiBackgroundFrom.getDuration() - animateUiBackgroundTo.getCurrentPlayTime());
                    animateUiBackgroundTo.cancel();
                }
            }
        });

        animateUiBackgroundTo.addUpdateListener(animation -> view.setBackgroundColor(ColorUtils.blendARGB(from
                ,to
                ,(Float) animateUiBackgroundTo.getAnimatedValue())));

        animateUiBackgroundFrom.addUpdateListener(animation -> view.setBackgroundColor(ColorUtils.blendARGB(to
                ,from
                ,(Float) animateUiBackgroundFrom.getAnimatedValue())));

    }

    void initHamburgerAnimator(ImageView upperLine,
                               ImageView centerLine,
                               ImageView lowerLine,
                               Resources resources) {

        long duration = resources.getInteger(R.integer.hamburgerAnimationDuration);
        float outsideLinesPosDifferenceInPxByY = resources.getDimension(R.dimen.hamburgerLowerLineMarginTop) - resources.getDimension(R.dimen.hamburgerUpperLineMarginTop);
        float viewWidthInPx = resources.getDimension(R.dimen.hamburgerLinesWidth);
        float viewHeightInPx = resources.getDimension(R.dimen.hamburgerLinesHeight);
        int hamburgerColor = resources.getColor(R.color.White);
        int crossColor = resources.getColor(R.color.Black);

        hamburgerAnimationDuration = duration;

        toCross.setDuration(duration);
        toCross.setInterpolator(null);

        toHamburger.setDuration(duration);
        toHamburger.setInterpolator(null);

        float outsideLinesHalf = outsideLinesPosDifferenceInPxByY / 2;
        float viewWidthHalf = viewWidthInPx / 2;
        float viewHeightHalf = viewHeightInPx/ 2;
        float viewSinFromPivot = outsideLinesHalf / (float) Math.sin(Math.toRadians(45)) - outsideLinesHalf;
        float lineScaleByX = (viewSinFromPivot * 2 + viewWidthInPx) / viewWidthInPx;

        float firstTactUpperLinePivotX = viewWidthInPx / 2 + outsideLinesPosDifferenceInPxByY / 2;
        float secondTactUpperLinePivotX = viewWidthHalf - (outsideLinesHalf / (float) Math.sin(Math.toRadians(45)) - outsideLinesHalf);
        float firstTactLowerLinePivotX = viewWidthInPx / 2 - outsideLinesPosDifferenceInPxByY / 2;
        float secondTactLowerLinePivotX = viewWidthHalf + (outsideLinesHalf / (float) Math.sin(Math.toRadians(45)) - outsideLinesHalf);

        upperLine.setPivotY(viewHeightHalf);
        upperLine.setPivotX(viewWidthHalf);

        centerLine.setPivotY(viewHeightHalf);
        centerLine.setPivotX(viewWidthHalf);

        lowerLine.setPivotY(viewHeightHalf);
        lowerLine.setPivotX(viewWidthHalf);

        toCross.addUpdateListener(animation -> {
            upperLine.setPivotX((Float) toCross.getAnimatedValue() >= animateTo / 3f ? firstTactUpperLinePivotX : secondTactUpperLinePivotX);
            upperLine.setTranslationX((Float) toCross.getAnimatedValue() >= animateTo / 3 ? 0 : viewSinFromPivot);
            upperLine.setTranslationY((Float) toCross.getAnimatedValue() >= animateTo / 3 ? 0 : outsideLinesHalf);
            upperLine.setRotation((Float) toCross.getAnimatedValue());

            centerLine.setScaleX((Float) toCross.getAnimatedValue() >= animateTo / 3f ? 1 : (1f + (((toCross.getAnimatedFraction() - 0.3f) / 0.7f) * (lineScaleByX - 1f))));
            centerLine.setRotation((Float) toCross.getAnimatedValue() >= animateTo / 3f ? (Float) toCross.getAnimatedValue() : ((Float) toCross.getAnimatedValue() * (toCross.getAnimatedFraction() + 0.6666667f)));

            lowerLine.setPivotX((Float) toCross.getAnimatedValue() >= animateTo / 3f ? firstTactLowerLinePivotX : secondTactLowerLinePivotX);
            lowerLine.setTranslationX((Float) toCross.getAnimatedValue() >= animateTo / 3 ? 0 : -viewSinFromPivot);
            lowerLine.setTranslationY((Float) toCross.getAnimatedValue() >= animateTo / 3 ? 0 : -outsideLinesHalf);
            lowerLine.setRotation((Float) toCross.getAnimatedValue() * 1);

            upperLine.setColorFilter(ColorUtils.blendARGB(hamburgerColor, crossColor, toCross.getAnimatedFraction()));
            centerLine.setColorFilter(ColorUtils.blendARGB(hamburgerColor, crossColor, toCross.getAnimatedFraction()));
            lowerLine.setColorFilter(ColorUtils.blendARGB(hamburgerColor, crossColor, toCross.getAnimatedFraction()));
        });

        toHamburger.addUpdateListener(animation -> {

            upperLine.setPivotX((Float) toHamburger.getAnimatedValue() >= animateTo / 3f ? firstTactUpperLinePivotX : secondTactUpperLinePivotX);
            upperLine.setTranslationX((Float) toHamburger.getAnimatedValue() >= animateTo / 3 ? 0 : viewSinFromPivot);
            upperLine.setTranslationY((Float) toHamburger.getAnimatedValue() >= animateTo / 3 ? 0 : outsideLinesHalf);
            upperLine.setRotation((Float) toHamburger.getAnimatedValue());

            centerLine.setScaleX((Float) toHamburger.getAnimatedValue() <= (animateTo / 3) ? (1f + ((0.7f - toHamburger.getAnimatedFraction()) / 0.7f) * (lineScaleByX - 1f)) : 1f);
            centerLine.setRotation((Float) toHamburger.getAnimatedValue() <= animateTo / 3f ? ((Float) toHamburger.getAnimatedValue() * ((1 - toHamburger.getAnimatedFraction()) + 0.6666667f)) : (Float) toHamburger.getAnimatedValue());

            lowerLine.setPivotX((Float) toHamburger.getAnimatedValue() >= animateTo / 3f ? firstTactLowerLinePivotX : secondTactLowerLinePivotX);
            lowerLine.setTranslationX((Float) toHamburger.getAnimatedValue() >= animateTo / 3 ? 0 : -viewSinFromPivot);
            lowerLine.setTranslationY((Float) toHamburger.getAnimatedValue() >= animateTo / 3 ? 0 : -outsideLinesHalf);
            lowerLine.setRotation((Float) toHamburger.getAnimatedValue() * 1);

            upperLine.setColorFilter(ColorUtils.blendARGB(crossColor, hamburgerColor, toHamburger.getAnimatedFraction()));
            centerLine.setColorFilter(ColorUtils.blendARGB(crossColor, hamburgerColor, toHamburger.getAnimatedFraction()));
            lowerLine.setColorFilter(ColorUtils.blendARGB(crossColor, hamburgerColor, toHamburger.getAnimatedFraction()));
        });

        toHamburger.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (toCross.isStarted()) {
                    toHamburger.setCurrentPlayTime(duration - toCross.getCurrentPlayTime());
                    toCross.cancel();
                }
            }
        });

        toCross.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (toHamburger.isStarted()) {
                    toCross.setCurrentPlayTime(duration - toHamburger.getCurrentPlayTime());
                    toHamburger.cancel();
                }
            }
        });
    }
}
