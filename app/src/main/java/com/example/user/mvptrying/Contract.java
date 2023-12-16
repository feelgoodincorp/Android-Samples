package com.example.user.mvptrying;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.view.View;

/**
 * Created by User on 27.06.2018.
 */

public interface Contract {

    interface ContractModel{
        void downloadFile(Context context);
    }

    interface ContractView{
        void showImageFromPath(String path);
    }

    interface ContractPresenter{
        void bindView(Contract.ContractView view);
        void unbindView();
        void initReceiver();
        //если есть init, то должны быть register и unregister
        void downloadingCycle(Context context);
    }


}
