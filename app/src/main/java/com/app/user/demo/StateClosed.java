package com.app.user.demo;

import android.util.Log;

public class StateClosed extends MainViewState {

    private Contract.MenuPresenter contextOwner;
    private int columnAnimatingPivot;

    StateClosed(int animatingPivot){
        columnAnimatingPivot = animatingPivot;
    }

    @Override
    public void onEnter(Contract.MenuView view) {

        float nullablePosition = view.getResource().getDimension(R.dimen.primaryMenuClosed);
        long duration = view.getResource().getInteger(R.integer.translationDuration);
        long startOffset = view.getResource().getInteger(R.integer.startOffset);
        int primaryArraySize = view.getPrimaryMenuColumn().size();
        int secondaryArraySize = view.getSecondaryMenuColumn().size();

        //prevents out of bounds of array
        if(columnAnimatingPivot > primaryArraySize && columnAnimatingPivot > secondaryArraySize){
            columnAnimatingPivot =0;
            Log.e("out of bounds","animation pivot is more that array of views");
        }

        int [] queue = generateAnimatingQueue(columnAnimatingPivot,primaryArraySize);
        for(int a = 0; a<primaryArraySize; a++){
            MainActivityAnimator.getAnimator().translateViewByX(view.getPrimaryMenuColumn().get(a),nullablePosition,duration,startOffset*queue[a]);
        }

        int [] queue2 = generateAnimatingQueue(columnAnimatingPivot,secondaryArraySize);
        for(int a = 0; a<secondaryArraySize; a++){
            MainActivityAnimator.getAnimator().translateViewByX(view.getSecondaryMenuColumn().get(a),nullablePosition,duration,startOffset*queue2[a]);
        }
        MainActivityAnimator.getAnimator().safeAnimateUiBackgroundColor(false);
        MainActivityAnimator.getAnimator().safeAnimateHamburger(false);

        view.setBackgroundClickable(true);
    }

    @Override
    public void setOwner(Contract.MenuPresenter owner) {
        this.contextOwner = owner;
    }

    @Override
    public void onExit() {}

    @Override
    public void restoreState(Contract.MenuView view) {
        view.setBackgroundClickable(true);
        MainActivityAnimator.getAnimator().nullableUiBackgroundAnimation(false);
        MainActivityAnimator.getAnimator().nullableHamburgerAnimation(false);
    }
}
