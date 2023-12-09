package com.example.user.betaserveraplication;

import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


//цикл с  internetConnection
//  проверка URL
//  запрос
//  если не удалось - нужно выйти из цикла (internetConnection && shouldContinue) ИЛИ
//проверять inCon в main, а shouldContinue в зависимости от результата запроса или isOnline
//  временной интервал когда?
//TODO настроить работу счетчика (по моему там неверно стартовое значение или работа с ним) p.s. не скачивалась корова когда было count<6
//TODO исправить баг с продолжением работы сервиса,если при работе активности я открываю другую(shoudContinue или новая переменная pause  в oncreate/destroy активности)
//TODO можно вообще не делать проверку типа файла на клиенте
//TODO работа с другими разширениями,
//TODO конфлкты могут  быть в типе файла при : чтении из БД(не совпадают реальное расширение и указанное в бд),
//TODO и все????????????там скорее не будет найдено такого файла с таким типом
//TODO все должно ограничиватся лишь количеством файлов в папке ресуров сервера
//TODO  тогда нужно и в базу и в файлы добавлять изменения  - задокументировать особенность
//TODO (пользователь,добавляя данные,грузит их на сервер в сервлет,
//TODO а сервлет добавляет данные о изображении в базу,это все сохраняет логику "файлы вне базы")
//TODO настроить корректную работу с переменными shouldContinue и downloadState
//TODO всякие проверки на клиент,
//TODO (закрытие потоков в клиент),проверить работу счетчика при перевороте
//TODO нужно ли пытатся обработать ответ сервера встроеным потоком okhttp?
//TODO как то работать с переменной result (Activity.RESULT_)
//TODO что будет если запрос превысит время ожидания или время поставки нового запроса?
//TODO( по идее не должен начатся,новый,пока не закончен старый)
//TODO закоментировать все логи и стэк-трассировки (меньше нагрузки на устройство(с учетом,что оно мобильное - это ощутимый плюс))

    public class BetaIntentService extends IntentService {

        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();

        //должно ли быть такое в ресурсах?
        String imagesFolderName = "/imagesFolder/";

        static String contentType;
        static boolean isSupportedFromat = false;

        public static boolean downloadState = false;
        public int responseStatus;
        public boolean isLastDataItem = false;
        public static int result = Activity.RESULT_CANCELED;
        //public static int counter = 1;
        int counter = 1;
        public static final String RECEIEVERINTENT =  "receiverIntent"; //индетификатор для интент фильтра b в MainAсtivity

        public static boolean isIntentServiceRunning = false;
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = null;
                boolean responseOK = false;
                int tryCount = 0;

                while (!responseOK && tryCount < 3) {
                    try {
                        //проверяем доступ к инету,к сервису и код ответа,(?) потом делаем попытки
                        //TODO логика проверки доступов к интернету и серверу
                        //TODO добавление в intent,чтоб его читал MainActivity
                        response = chain.proceed(request);

                        //проверка на окончание списка данных
                        if(response.header("moreThatLastRowID").equals("true")){
                            isLastDataItem = true; tryCount = 3;
                        }
                        responseOK = response.isSuccessful();
                        Log.d("intercept", "Request is proceeded");
                    } catch (Exception e){
                        Log.e("intercept", "Request is not successful - " + tryCount);
                    } finally{
                        tryCount++;
                    }
                }
                return response;
            }
        })
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();


        public static String resp;


        public BetaIntentService() {
            super("BetaIntentService");
        }

        @Override
        public void onTaskRemoved(Intent rootIntent) {
            Log.i(getClass().getName(), "IntentService_removed from Recent");
            super.onTaskRemoved(rootIntent);
        }

        @Override
        public void onDestroy() {
            Log.i(getClass().getName(), "IntentService_onDestroy()");
            isIntentServiceRunning = false;
            super.onDestroy();
        }

        @Override
        public void onCreate() {
            Log.i(getClass().getName(), "IntentService_onCreate()");
            isIntentServiceRunning = true;
            super.onCreate();
        }

        @Override
        protected void onHandleIntent(Intent intent) {

            getExternalMounts();

            Bundle bundle = intent.getExtras();
            counter = bundle.getInt("counter");
            //здесь будет распарсен bundle

                try {
                    downloadState = false;

                    String URL;
                    URL = getResources().getString(R.string.URL).concat(String.valueOf(counter));
                    Log.i(getClass().getName(), "Service getting data with while() - " + String.valueOf(downloadState));

                    final Request request = new Request.Builder()
                            .url(URL)
                            .get()
                            .build();

                    Response response = client.newCall(request).execute();
                    responseStatus = response.code();

                    //TODO нужно ли переделывать в switch список кодов ответов
                    if ((response.code()) != 200) {
                        //TODO
                        }else{
                        Log.i(getClass().getName(), "counter =  " + String.valueOf(counter));
                        //проверка типа контентных данных
                        Log.i(getClass().getName(), "CHECKPOINT 1 , checking content type");
                        contentType = response.headers().get("Content-Type");
                        String[] supportedFormats = getResources().getStringArray(R.array.supportedFormats);
                        for (String format : supportedFormats) {
                            if(contentType.equals(format)){isSupportedFromat = true;}
                        }

                        Log.i(getClass().getName(), "CHECKPOINT 2 , content type = " + contentType);

                        if (!isSupportedFromat) {
                            Log.i(getClass().getName(), "is supported " + String.valueOf(isSupportedFromat));
                        } else {
                            //TODO правильная работа с директориями
                            File directory;
                            if(isSDPresent){
                                //SD-карта присутствует
                                System.out.println("SDCARD is EXISTS");
                                directory = new File(Environment.getExternalStorageDirectory().getPath()+imagesFolderName);
                                directory.mkdirs();
                            }else{
                                System.out.println("NO SDCARD");
                                directory = new File(Environment.getExternalStorageDirectory().getPath()+imagesFolderName);
                                directory.mkdirs();
                            }

                            File downloadFile = new File(directory+"/"+"IntentService_Example"  + counter + contentType);
                            if (downloadFile.exists()){
                                downloadFile.delete();}

                            Log.i(getClass().getName(), "CHECKPOINT 3 , (empty) file created");
                            InputStream inputStream = response.body().byteStream();
                            FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);

                            Log.i(getClass().getName(), "CHECKPOINT 4 , i/o streams created");
                            //запись в файл
                            IOUtils.copy(inputStream, fileOutputStream);
                            Log.i(getClass().getName(), "CHECKPOINT 5 , copying ended");
                            inputStream.close();
                            fileOutputStream.close();
                            Log.i(getClass().getName(), "DATA DOWNLOADED");
                            downloadState = true;
                        }
                    }
                }catch (Exception e){
                    //e.printStackTrace();
                    Log.i(getClass().getName(), "Exception inside while cycle");
                }
                finally {
                    Intent dataIntent = new Intent(RECEIEVERINTENT);
                    dataIntent.putExtra("downloadState", downloadState);
                    dataIntent.putExtra("responseStatus",responseStatus);
                    dataIntent.putExtra("isLastDataItem",isLastDataItem);
                    sendBroadcast(dataIntent);

                    //не ждать и делать новый запрос,так как тратится запрос на выявление этого
                    if(!isLastDataItem) {
                        //интервал между запросами
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }


        public boolean isOnline() {
            String command = "ping -c 1 google.com";
            try {
                //TODO whaitFor можно поменять?
                return (Runtime.getRuntime().exec (command).waitFor() == 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        String externalpath = new String();
        String internalpath = new String();

        public  void getExternalMounts() {
            /*Runtime runtime = Runtime.getRuntime();
            try
            {
                Process proc = runtime.exec("mount");
                InputStream is = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                String line;

                BufferedReader br = new BufferedReader(isr);
                while ((line = br.readLine()) != null) {
                    if (line.contains("secure")) continue;
                    if (line.contains("asec")) continue;

                    if (line.contains("fat")) {//external card
                        String columns[] = line.split(" ");
                        if (columns != null && columns.length > 1) {
                            externalpath = externalpath.concat("*" + columns[1] + "\n");
                        }
                    }
                    else if (line.contains("fuse")) {//internal storage
                        String columns[] = line.split(" ");
                        if (columns != null && columns.length > 1) {
                            internalpath = internalpath.concat(columns[1] + "\n");
                        }
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            System.out.println("Path  of sd card external............"+externalpath);
            System.out.println("Path  of internal memory............"+internalpath);*/

            String s = System.getenv("SECONDARY_STORAGE");
            if(s == null){
                MainActivity.tempState =  "SDcard  -are  NOT exists";
                Log.i("test", "sdcard not available");}
            else
                MainActivity.tempState =  "SDcard  -are exists";
                Log.i("test", System.getenv("SECONDARY_STORAGE"));
        }
    }
