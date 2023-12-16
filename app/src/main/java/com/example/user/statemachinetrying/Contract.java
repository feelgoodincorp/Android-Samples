package com.example.user.statemachinetrying;

import android.content.res.Resources;

import java.util.LinkedList;
import java.util.Queue;

public interface Contract {
    //com.example.user.fullapplication.Contract - описывает взаимодействия между компонентами mvp


    //Model - содержит бизнес-логику(логику,ради которой создавалось приложение)
    interface Model{

    }

    //View - отобажает UI/данные из Presenter
    interface View{
        void setText(String string);
        void animate(float translation,long duration);
        Resources getResource();
    }

    //Presenter - сообщает Model и View; определяет логику отображения данных Model во View
    interface Presenter{

        //TODO как работать с статическими методами в интерфейсах?
        void bindView(View view);
        void unbindView();

        //saveDataOnCard(Path path);
        //saveDataOnPhone(Path path);

        //type - реализовать в классе как флаг?не в интерфейсе,чтоб сделать его более абстрактным
        //getData(String dataType, int id);

        //saveImage(Path);
        //getImage(Path);

        //логику добавления должен реализовывать bind()/unbind?

        interface SafeMethodsQueue{

            Queue<MethodWrapper> queue = new LinkedList<>();

            @FunctionalInterface
            interface MethodWrapper{
                void execute();
            }

            default void addMethod(MethodWrapper methodWrapper){
                queue.add(methodWrapper);
            }

            default void run(){
                queue.poll().execute();
            }

        }
    }
}
