package com.example.user.demoApp;

abstract class MainViewState {

    void onEnter(Contract.MenuView view){}
    void setOwner(Contract.MenuPresenter owner){}

    //could require view as parameter
    void onExit(){}
    void restoreState(Contract.MenuView view){}

    int [] generateAnimatingQueue(int pivot, int arraySize){
        int sortedArray [] = new int [arraySize];
        for(int a = 0; a<arraySize; a++){
            sortedArray[a] = a>pivot ? (a-pivot) : (pivot-a);
        }
        return sortedArray;
    }
}
