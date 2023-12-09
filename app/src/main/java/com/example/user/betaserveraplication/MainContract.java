package com.example.user.betaserveraplication;

import android.content.Context;

/**
 * Created by User on 10.06.2018.
 */

public interface MainContract {
    interface View{
       void showImage();

    }

    interface Presenter{
       void checkSslUpdate(Context context);
       void initReceiver();
       void startLoadingCycle();

       //зачем вообще onDestroy если есть биндеры?
       // останавливать процессы/действия
       // в MainActivity -
       //   if (!isFinishing() && mPresenter != null) {
       //        mPresenter.unbindView();
       //   }

       void onDestroy();
       void bindView(MainContract.View view);
       void unbindView();

    }

    interface Model{
        void loadData();
    }

}
