package com.example.user.statemachinetrying;

public interface StateContextOwner {

    void setState(final State state);

    State getState();

}
