package com.example.user.betaserveraplication;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;


public class AnimationHandler {


    //TODO animateHamburger();
    //TODO animateBackground();

    public void animateMenuItems(int firstView, int translateValue, int duration, long startOffset, ArrayList<View> viewArrayList){
        if(!viewArrayList.isEmpty()){
            int length=viewArrayList.size();
            int start=firstView;
            for(int i=0; i<length;i++){
                if(firstView>=start){
                    firstView=firstView-i;
                    if(firstView<0||firstView<length){length++;}//просто
                }else if(firstView<start) {
                    firstView=firstView+i;
                    if(firstView<0||firstView>length){length++;}//length++,без if/else
                }
                if(firstView>=0&&firstView<viewArrayList.size()){
                    //TODO определить - надо ли?
                    /*if(viewArrayList.get(firstView).getAnimation()!=null){
                        viewArrayList.get(firstView).getAnimation().cancel();
                    }*/
                    ObjectAnimator animator = ObjectAnimator.ofFloat(viewArrayList.get(firstView), View.TRANSLATION_X, translateValue);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(duration);
                    animator.setStartDelay(startOffset);//строка лишняя,но без нее виден недостаток нажатия всех кнопок разом(внезапный телепорт компонентов)
                    animator.start();
                    animator.setStartDelay(startOffset);//можно удалить эту и оставить выше,но это все по прежднему сказывается на отзывчивости(80мс)
                    startOffset=(startOffset*3)/2;
                }
            }
        }
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

}
