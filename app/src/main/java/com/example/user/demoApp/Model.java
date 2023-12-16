package com.example.user.demoApp;

import android.support.annotation.NonNull;

import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class Model implements Contract.Model {

    private Model(){}
    private static Model model = new Model();
    static Model getModel() {
        return model;
    }

    //flags of index of position in database relative end
    private final String lastIndex = "0";
    private final String afterLastIndex = "1";
    private final String lastIndexHeaderKey = "indexRelativeEnd";
    private final String [] supportedImageFormats = {".jpg",".png", ".gif"}; //".bmp",
    private final String fileName = "image";

    //error messages text
    private final String contentTypeError = "unknown content type";
    private final String contentSupportError = "unsupported content type";
    private final String freeSpaceError = "not enough space";
    private final String writingError = "writing on disk is failed";
    private final String internetUnavailableError = "internet unavailable";
    private final String unknowError = "unknow error";
    private final String serverUnavailableError = "server unavailable";
    private final String counterReset = "image counter has been reset";

    //network interaction timeouts in seconds
    private final int connectTimeout = 10;
    private final int writeTimeout = 15;
    private final int readTimeout = 15;


    private OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {

        Response response = null;
        int tryCount = 0;

        while (tryCount < 3) {
            try {
                response = chain.proceed(chain.request());

                //Проверить на работоспособность(c break и без)
                //checking list for completion
                String header = response.header(lastIndexHeaderKey);
                if(header!=null && (header.equals(lastIndex) || header.equals(afterLastIndex))){
                    Presenter.getPresenter().resetCounter();
                }else{
                    Presenter.getPresenter().resetCounter();
                    throw new IOException(counterReset);
                }


                /*
                //checking list for completion
                if(response.header(lastIndexHeaderKey)==null){
                    Presenter.getPresenter().resetCounter();
                    throw new IOException(counterReset);
                }else if(response.header(lastIndexHeaderKey).equals(lastIndex)){
                    Presenter.getPresenter().resetCounter();
                    //нужен ли брейк?
                    break;
                }else if(response.header(lastIndexHeaderKey).equals(afterLastIndex)){
                    Presenter.getPresenter().resetCounter();
                    break;
                }*/

            } catch (Exception e){
                if(!checkInternetAccess()) {
                    throw new IOException(internetUnavailableError);
                }else if(!checkServerAccess()) {
                    throw new IOException(serverUnavailableError);
                }else {
                    e.printStackTrace();
                    throw new IOException(unknowError);
                }
            } finally{
                tryCount++;
            }
        }

        return response;
    })
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .build();


    @Override
    public void loadImage(String url, String path){
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Presenter.getPresenter().setRequestResult(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String contentType;

                if(response.headers().get("Content-Type")==null){
                    onFailure(call, new IOException(contentTypeError));
                    throw new IOException(contentTypeError);
                }else if(!isSupportedFormat(response.headers().get("Content-Type"))){
                    onFailure(call, new IOException(contentSupportError));
                    throw new IOException(contentSupportError);
                }else {
                    contentType = response.headers().get("Content-Type");
                }

                if(response.headers().get("Content-Length")!=null){
                    if(Integer.valueOf(response.headers().get("Content-Length"))<getFreeMemory(new File(path))) {
                        onFailure(call, new IOException(freeSpaceError));
                        throw new IOException(freeSpaceError);
                    }
                }

                try {
                    //previous file will be overwritten
                    File downloadFile = new File(path + "/" + fileName +  contentType);
                    InputStream inputStream = response.body().byteStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);
                    IOUtils.copy(inputStream, fileOutputStream);
                    inputStream.close();
                    fileOutputStream.close();
                    Presenter.getPresenter().setViewImage(downloadFile);
                }catch (Exception e){
                    onFailure(call, new IOException(writingError));
                    throw new IOException(writingError);
                }
            }
        });
    }

    private boolean checkInternetAccess(){
        final String command = "ping -c 1 google.com";
        try {
            return (Runtime.getRuntime().exec (command).waitFor() == 0);
        } catch (InterruptedException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean checkServerAccess(){
        final String command = "ping -c 1 " + Presenter.getPresenter().getDataBaseUrl() + "/" + Presenter.getPresenter().getServletName();
        try {
            return (Runtime.getRuntime().exec (command).waitFor() == 0);
        } catch (InterruptedException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isSupportedFormat(String contentType){
        for (String format : supportedImageFormats) {
            if(contentType.equals(format)) return true;
        }
        return false;
    }

    private long getFreeMemory(File path) {
        if ((null != path) && (path.exists()) && (path.isDirectory())) {
            return path.getFreeSpace();
        }
        return -1;
    }
}