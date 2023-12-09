package com.example.user.asyncokhttphandle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    //нужен ли класс-оббертка для view? или Dependency Injection. или оставить savedIntanceState на активности
    //sequency set или текущая реализация
    //отдельный класс для каждой анимации - гамбургер и список
    OneViewAnimation oneViewAnimation = OneViewAnimation.getViewAnimator();
    MenuListAnimator menuListAnimator = MenuListAnimator.getViewAnimator();

    RelativeLayout relativeLayout;
    boolean btnState;
    FrameLayout frameLayout;
    TextView metadata;

    ArrayList<View> firstRowMenuArray;
    ArrayList<View> secondRowMenuArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_menu_views_layout);

        /*frameLayout = findViewById(R.id.hamburgerFrameLayout);
        metadata = findViewById(R.id.metadataTv);

        ImageView upperLine = findViewById(R.id.upperLine);
        ImageView centerLine = findViewById(R.id.centerLine);
        ImageView lowerLine = findViewById(R.id.lowerLine);

        //TODO последний параметр в try/catch(а нужно ли? чтоб на этапе компиляции не было нехватки ресурсов)
        oneViewAnimation.initHamburgerAnimator(
                upperLine,
                centerLine,
                lowerLine,
                (long) getResources().getInteger(R.integer.hamburgerAnimationDuration),
                getResources().getDimension(R.dimen.hamburgerLowerLineMarginTop)-getResources().getDimension(R.dimen.hamburgerUpperLineMarginTop),
                getResources().getDimension(R.dimen.hamburgerLinesWidth),
                getResources().getDimension(R.dimen.hamburgerLinesHeight),
                getResources().getColor(R.color.White),
                getResources().getColor(R.color.Black));*/


        Button button1 = findViewById(R.id.v1r1);
        Button button2 = findViewById(R.id.v2r1);
        Button button3 = findViewById(R.id.v1r2);
        Button button4 = findViewById(R.id.v2r2);
        relativeLayout = findViewById(R.id.relativelayout);

        firstRowMenuArray = new ArrayList<>();
        firstRowMenuArray.add(button1);
        firstRowMenuArray.add(button2);

        secondRowMenuArray = new ArrayList<>();
        secondRowMenuArray.add(button3);
        secondRowMenuArray.add(button4);

        menuListAnimator.initAnimateBackground(relativeLayout,3000,"#FFFFFF","#000000");

        //старт,отмена,старт

        //у класса-меню-аниматора должен быть метод, который инвалидирует позиции view - сетит переданным в onSaved
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuListAnimator.animateBackgroundColor(btnState);
                if(!btnState){
                    menuListAnimator.animateItems(firstRowMenuArray,1000L,100L,200f,1);
                    menuListAnimator.animateItems(secondRowMenuArray,1000L,100L,200f,1);
                }else {
                    menuListAnimator.animateItems(firstRowMenuArray,1000L,100L,0f,1);
                    menuListAnimator.animateItems(secondRowMenuArray,1000L,100L,0f,1);
                }
                btnState = !btnState;
            }
        });



        //TODO  Bundle для сохранения состояния передавать состояние кнопки(их два - крестик и гамбургер)
        //при рекавере в зависимости от сохраненного состояния с нулевой длительностью будет сетится
        // анимация(или без анимации,просто состояние  view(setTtransitionX\setRotationX))
        //значения которых взяты из началльного и конечного значений для анимации

        //нужна ли повторная инициализация?
        //да,чтоб не обращатся к нессуществующим view
        /*oneViewAnimation.initAnimation(imageViewToMove);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*currentMenuState = currentMenuState==MENU_IS_OPENED ? MENU_IS_CLOSED : MENU_IS_OPENED;
                switch (currentMenuState){
                    case MENU_IS_CLOSED:
                        oneViewAnimation.startViewAnimation(btnState);
                        currentMenuState = MENU_IS_OPENED;
                    case MENU_IS_OPENED:
                        oneViewAnimation.startViewAnimation(btnState);
                }*//*
                if(btnState){
                    oneViewAnimation.startViewAnimation(btnState);
                    btnState=false;
                }else{
                    oneViewAnimation.startViewAnimation(btnState);
                    btnState=true;
                }
            }
        });*/

    }


    @Override
    protected void onSaveInstanceState(Bundle instanceState){
        super.onSaveInstanceState(instanceState);
        instanceState.putBoolean("side",btnState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle instanceState){
        super.onRestoreInstanceState(instanceState);
        btnState = instanceState.getBoolean("side");
        menuListAnimator.notifyViewsPosition(firstRowMenuArray);
        menuListAnimator.notifyViewsPosition(secondRowMenuArray);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

}



