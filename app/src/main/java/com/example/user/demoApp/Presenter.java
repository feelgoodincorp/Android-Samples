package com.example.user.demoApp;

import android.os.storage.StorageManager;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Presenter implements Contract.MenuPresenter, Contract.MenuPresenter.SafeMethodsQueue {

    private Presenter(){}

    private static Presenter presenterInstance = new Presenter();
    static synchronized Contract.MenuPresenter getPresenter() {
        return presenterInstance;
    }

    private Contract.Model model = Model.getModel();
    private Contract.MenuView view;

    private MainViewState previousState;

    @Override
    public  String getDataBaseUrl() {
        return dataBaseUrl;
    }

    @Override
    public String getServletName() {
        return servlet;
    }

    private static final String dataBaseUrl = "http://192.168.1.8:8081";
    private static final String servlet = "servletDemoApp";
    private File imagesPath;
    private File currentImage;
    private final String folderName = "DemoApplication";
    private int counter = 1;
    private final String diskError = "disk access is failed";

    //default state value(initialization)
    private MainViewState state = new StateClosed(0);

    //called from onResume
    @Override
    public void bindView(Contract.MenuView view) {
        this.view = view;
        if(!SafeMethodsQueue.queue.isEmpty()&&view!=null){
            presenterInstance.run();
        }
    }

    //called from onPause
    @Override
    public void unbindView() {
        this.view = null;
        //adding uncovered by state-pattern methods to queue for saving interaction results after rotation
        if(currentImage!=null){
            queue.add(() -> setViewImage(currentImage));
        }
    }

    //setState() should be called only from MainView to avoid the NullPointerException
    @Override
    public void setState(MainViewState state) {
        previousState = this.state;
        this.state.onExit();
        this.state = state;
        this.state.setOwner(this);
        this.state.onEnter(view);
    }

    @Override
    public MainViewState getState() {
        return state;
    }

    @Override
    public MainViewState getPreviousState() {
        return previousState;
    }

    @Override
    public void nextImage(StorageManager storageManager){
        if (view != null) {
            if(imagesPath==null || !imagesPath.canWrite() && !imagesPath.canRead()) {
                String externalStoragePath = getExternalStoragePath(storageManager, true);
                String internalStoragePath = getExternalStoragePath(storageManager, false);
                if(externalStoragePath!=null){
                    File externalPath = new File(externalStoragePath);
                    if (externalPath.isDirectory() && externalPath.canRead() && externalPath.canWrite()) {
                        imagesPath = externalPath;
                    }
                }else if(internalStoragePath!=null){
                    File internalPath = new File(internalStoragePath);
                    if (internalPath.isDirectory() && internalPath.canRead() && internalPath.canWrite()) {
                        imagesPath = internalPath;
                    }
                }else {
                    setRequestResult(diskError);
                }
            }

            String URL = dataBaseUrl + "/" + servlet + "/" + counter;
            String path = imagesPath.getPath()+ "/"+folderName;

            //TODO проверить результат операции(удалить папку предварительно)(добавить setRequestResult если что)
            File dir = new File(path+"/");
            if(!dir.exists()){
                System.out.println(dir.mkdirs());
            }

            model.loadImage(URL, path);
            counter++;
        }else {
            queue.add(() -> nextImage(storageManager));
        }
    }

    private static String getExternalStoragePath(StorageManager mStorageManager, boolean isStorageRemovable) {
        Class<?> storageVolumeClass;
        try {
            storageVolumeClass = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClass.getMethod("getPath");
            Method isRemovable = storageVolumeClass.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (isStorageRemovable == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //indexing from 1 to not confuse with indexing in the database
    @Override
    public void resetCounter(){
        counter = 1;
    }

    @Override
    public void setRequestResult(String result){
        if(view!=null){
            view.setText(result);
        }else {
            queue.add(() -> setRequestResult(result));
        }
    }

    @Override
    public void setViewImage(File image){
        currentImage = image;
        if(view!=null){
            view.setContentImage(image);
        }else {
            queue.add(() -> view.setContentImage(image));
        }
    }
}


