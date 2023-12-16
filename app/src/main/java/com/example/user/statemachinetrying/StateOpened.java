package com.example.user.statemachinetrying;


public class StateOpened implements State {

    StateContextOwner contextOwner;

    public StateOpened(StateContextOwner stateContextOwner){
        this.contextOwner = stateContextOwner;
    }


    @Override
    public void onEnter(Contract.View view) {
        view.animate(-300f,1000L);
    }

    @Override
    public void onExit() {

    }

    @Override
    public void restoreState(Contract.View view) {
        view.animate(-300f,0L);
    }
}
