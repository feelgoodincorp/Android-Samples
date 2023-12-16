package com.example.user.statemachinetrying;

import android.content.Context;

public class StateClosed implements State {

    StateContextOwner contextOwner;
    //TODO неплохо бы задействовать ресурсы для указания направления анимации
    // или определить их в самой view и затем использовать в state

    public StateClosed(StateContextOwner stateContextOwner){
        this.contextOwner = stateContextOwner;
    }

    @Override
    public void onEnter(Contract.View view) {
        view.animate(0f,1000L);
    }

    @Override
    public void onExit() {

    }

    @Override
    public void restoreState(Contract.View view) {
        //видны только те методы, которые опрееделены в контракте
        //или так или если будет NPE то в конструкторе наполнять поля через getResources
        view.getResource().getDimension(R.dimen.viewWidth);
        view.animate(0f,0L);
    }


    //нужно здесь переопределить все методы состояния и на каждый реагировать по своему?

}
