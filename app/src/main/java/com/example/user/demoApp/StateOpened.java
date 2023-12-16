package com.example.user.demoApp;

import android.util.Log;

public class StateOpened extends MainViewState{

    private Contract.MenuPresenter contextOwner;
    private int columnAnimatingPivot;

    StateOpened(int animatingPivot){
        columnAnimatingPivot = animatingPivot;
    }

    @Override
    public void onEnter(Contract.MenuView view) {

        float primaryColumnPosition = view.getResource().getDimension(R.dimen.primaryMenuOpened);
        float secondaryColumnPosition = view.getResource().getDimension(R.dimen.primaryMenuClosed);
        long duration = view.getResource().getInteger(R.integer.translationDuration);
        long startOffset = view.getResource().getInteger(R.integer.startOffset);
        int primaryArraySize = view.getPrimaryMenuColumn().size();
        int secondaryArraySize = view.getSecondaryMenuColumn().size();

        //prevents out of bounds of array
        if(columnAnimatingPivot > primaryArraySize && columnAnimatingPivot > secondaryArraySize){
            columnAnimatingPivot =0;
            Log.e("out of bounds","animation pivot is more that array of views");
        }

        int [] primaryColumnQueue = generateAnimatingQueue(columnAnimatingPivot,primaryArraySize);
        for(int a = 0; a<primaryArraySize; a++){
            MainActivityAnimator.getAnimator().translateViewByX(view.getPrimaryMenuColumn().get(a),primaryColumnPosition,duration,startOffset*primaryColumnQueue[a]);
        }

        int [] secondaryColumnQueue = generateAnimatingQueue(columnAnimatingPivot,secondaryArraySize);
        for(int a = 0; a<secondaryArraySize; a++){
            MainActivityAnimator.getAnimator().translateViewByX(view.getSecondaryMenuColumn().get(a),secondaryColumnPosition,duration,startOffset*secondaryColumnQueue[a]);
        }

        //prevent animation if this animation already completed(have sense only for animation with "true" value)
        if(!(contextOwner.getPreviousState() instanceof StateSecondaryMenu)&&!(contextOwner.getPreviousState() instanceof StateOpened)){
            MainActivityAnimator.getAnimator().safeAnimateUiBackgroundColor(true);
            MainActivityAnimator.getAnimator().safeAnimateHamburger(true);
        }

        view.setBackgroundClickable(false);
    }

    @Override
    public void setOwner(Contract.MenuPresenter owner) {
        this.contextOwner = owner;
    }

    @Override
    public void onExit() {}

    @Override
    public void restoreState(Contract.MenuView view) {
        view.setBackgroundClickable(false);
        MainActivityAnimator.getAnimator().nullableUiBackgroundAnimation(true);
        MainActivityAnimator.getAnimator().nullableHamburgerAnimation(true);
        for(int a = 0; a<view.getPrimaryMenuColumn().size(); a++){
            MainActivityAnimator.getAnimator().translateViewByX(
                    view.getPrimaryMenuColumn().get(a),
                    view.getResource().getDimension(R.dimen.primaryMenuOpened),
                    0,
                    0);
        }
    }
}