package com.example.user.betaserveraplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * Created by User on 10.06.2018.
 */

public class MainPresenter implements MainContract.Presenter {


    private MainContract.View  mView;
    private MainContract.Model mModel;

    String checkpoint = "checkpoint/";
    String logMainPresenterConstructor = "checkpoint/ MainPresenterConstructor";

    public MainPresenter(MainContract.Model model){

        this.mModel = model;
        Log.i(checkpoint, logMainPresenterConstructor);
    }


    @Override
    public void bindView(MainContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }



    @Override
    public void checkSslUpdate(Context context) {
        // устранение бага с отсутствием поддержки ssl1._ на android < v4.4
        try {

            ProviderInstaller.installIfNeeded(context);
            SSLContext sslContext = SSLContext.getInstance("SSLv3");
            sslContext.init(null, null, null);
            SSLEngine engine = sslContext.createSSLEngine();
            engine.setEnabledProtocols(new String[] {/*"SSLv1._",*/ "SSLv3"});
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initReceiver() {

    }

    @Override
    public void startLoadingCycle() {
        Log.i("LOADING CYCLE","CALLING");
    }

    @Override
    public void onDestroy() {

    }


}
