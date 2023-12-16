package com.example.user.statemachinetrying;

import android.content.res.Resources;

class Presenter implements Contract.Presenter, Contract.Presenter.SafeMethodsQueue ,StateContextOwner{

    private Presenter(){}

    private static Presenter presenterInstance = new Presenter();
    static synchronized Presenter getPresenter() {
        return presenterInstance;
    }

    Model model = Model.getModel();
    Contract.View view;

    //может можно будет удалить
    StateClosed stateClosed;
    StateOpened stateOpened;


    //дефолтное значение  = инициализация
    private State state = new StateClosed(this);


    /*//TODO проверка на NotNull должна заменится стейтом?
        //(отсутствие активности как отдельное состояние)
        if(view!=null){
            view.setText("successful");
        }else {
            //TODO presenter.run() должен вызыватся в методах presenter-a,которые(й) вызывается в onCreate
            presenterInstance.addMethod(() -> view.setText("view was = null"));
        }*/

    @Override
    public void bindView(Contract.View view) {
        this.view = view;
        if(!SafeMethodsQueue.queue.isEmpty()&&view!=null){
            presenterInstance.run();
        }

    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    //возможна ситуация, когда view = null и вызывается setState
    //если setState вызывается кем-то кроме view,а такое может и не быть в принципе

    @Override
    public void setState(State state) {
        this.state.onExit();
        this.state = state;
        this.state.onEnter(view);
    }

    @Override
    public State getState() {
        return state;
    }


}
