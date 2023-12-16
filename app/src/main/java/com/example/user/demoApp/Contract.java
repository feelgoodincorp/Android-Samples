package com.example.user.demoApp;

import android.content.res.Resources;
import android.os.storage.StorageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public interface Contract {

    interface Model{
        void loadImage(String url, String path);
    }

    interface MenuView{
        ArrayList<android.view.View> getPrimaryMenuColumn();
        ArrayList<android.view.View> getSecondaryMenuColumn();
        Resources getResource();
        void setContentImage(File file);
        void setText(String string);
        void setBackgroundClickable(boolean clickable);
    }

    interface MenuPresenter{
        void bindView(MenuView view);
        void unbindView();
        MainViewState getPreviousState();
        MainViewState getState();
        void setState(final MainViewState state);
        void setRequestResult(String result);
        void setViewImage(File image);
        void nextImage(StorageManager storageManager);
        void resetCounter();
        String getDataBaseUrl();
        String getServletName();

        interface SafeMethodsQueue{
            Queue<MethodWrapper> queue = new LinkedList<>();

            @FunctionalInterface
            interface MethodWrapper{
                void execute();
            }

            default void run(){
                queue.poll().execute();
            }

        }
    }
}
