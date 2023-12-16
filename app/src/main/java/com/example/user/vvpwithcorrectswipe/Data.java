package com.example.user.vvpwithcorrectswipe;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 28.01.2018.
 */

public class Data {
    //можно не каждый раз создавать новую вьюхху,а использовать имеющиеся
    ArrayList data = new ArrayList();

    void generateData(String s){

        data.add(s);
    }

    public List<?> getData() {
        return data;
    }


}
