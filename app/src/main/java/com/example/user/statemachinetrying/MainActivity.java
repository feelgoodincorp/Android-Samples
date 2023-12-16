package com.example.user.statemachinetrying;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity implements Contract.View{


    Presenter presenter = Presenter.getPresenter();
    Button button1;
    Button button2;
    ImageView imageView;


    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override public void onRestoreInstanceState(Bundle state){
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //сохранение состояния можно делегировать presenter-у, потому что он имеет право содержать логику
        // общения между собой и представлением
        // хотя сохранение состояния к этому может не относится
        //Хотя! presenter не обязан хранить информацию о view,
        // но возможно может хранить информацию о состояниях(ведь он contextStateOwner),
        // а состояния уже регулируют view

        //все сеттинги позиций в методе presenter.bind(){ invalidateMenu(currentState.getMenuPos) };
        //                                                presenter.getMenuPos()
        //                          в любом случае при ротации должен будет вызыватся setState()?????
        //                          нет,потому что вызывается bind,использующий currentState
        //                          но currenrsyaye не может хранить всех переменных?
        //                          изменение состояния должно работать только с изменяемыми собой переменными
        //                          и open и close state меняют одну переменную(animateTo-),
        //                          но currentState может не содержать такого метода
        //                          поля-метаданные о состоянии хранит stateContext или состояние?
        //                          в bind() метод сетит последние значения ИЗ contextOwner
        //                          в newState() метод сетит последние значения В contextOwner
        //                          брать переменные

        //у stateOwner должен быть метод restoreState, который на основе currentState делает...
        //вызыввает restoreAnimationResult у аниматора(у любого аниматора он должен быть)
        // состояния должны оперировать числами

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        imageView = findViewById(R.id.imageView);

        button1.setOnClickListener(v -> presenter.setState(new StateClosed(presenter)));
        button2.setOnClickListener(v -> presenter.setState(new StateOpened(presenter)));
    }

    @Override
    public void setText(String string) {

    }


/*
    @Override
    protected void onStart() {
        super.onStart();

    }
*/

    @Override
    protected void onStart(){
        super.onStart();
        presenter.bindView(this);
        Log.d("Log", "MainActivity: onStart()");
        //              а где изменение поведения в зависимости от состояния --- в зависимости от текущего состояния кнопка hamburger отвечает за разные значения анимации menu -
        //              нужно реализовать в каждом состоянии каждый метод view - тогда в зависимости от вызваного состояния будут выполнены методы view с разной логикой
        //              в методе свои значения для анимации
        //              но такой подход не учитывает предыдущего состояния - нельзя заставить анимацию двигатся по разному при разных комбинациях состояний "начальное - конечное"

        //              сохранение состояния - state может хранить в локальных переменных результатов своей работы,а затем задействовать их при вызове себя

        //state машина используется для view,и ее(view) методы вызываются внутри onEnter каждого состояния
        //все state принимают на вход view и работают с ним, так что стейт машина работает для view


        try {
            presenter.getState().restoreState(this);
            Log.d("Log", "State restoring");
        }catch (Exception e){
            Log.i("NOOO","exception");
        }
    }

    @Override
    public void onStop(){
       super.onStop();
       presenter.unbindView();
    }

    @Override
    public void animate(float translation, long duration){
        imageView.animate().translationX(translation).setDuration(duration).start();
    }

    @Override
    public Resources getResource(){
        Log.i("REOURCES hash",String.valueOf(getResources().toString()));
        return getResources();
        //проверить когда доступен контекст
    }

    @Override public void onPause(){
        super.onPause();
    }

}
