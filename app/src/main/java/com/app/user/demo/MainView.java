package com.app.user.demo;


import android.content.Context;
import android.os.storage.StorageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

public class MainView extends AppCompatActivity implements Contract.MenuView{

    Contract.MenuPresenter presenter = Presenter.getPresenter();

    RelativeLayout uiRelativeLayout;
    RelativeLayout contentLayout;
    ImageView contentImageView;
    FrameLayout hamburgerButton;

    ImageView upperHamburgerLine;
    ImageView middleHamburgerLine;
    ImageView lowerHamburgerLine;

    Button primaryMenuButtonHome;
    Button primaryMenuButtonSettings;
    Button primaryMenuButtonAbout;
    Button primaryMenuButtonExit;

    Button secondaryMenuButtonLogIn;
    Button secondaryMenuButtonMenuTheme;
    Button secondaryMenuButtonCache;
    Button secondaryMenuButtonAccount;
    Button secondaryMenuButtonBack;

    //использовать строковые ресурсы
    private final String a = "reading from disk is failed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hamburgerButton = (FrameLayout) findViewById(R.id.hamburgerFrameLayout);
        uiRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutUI);
        contentLayout = (RelativeLayout) findViewById(R.id.relativeLayoutContent);
        contentImageView = (ImageView) findViewById(R.id.contentImageView);

        upperHamburgerLine = (ImageView) findViewById(R.id.upperLine);
        middleHamburgerLine = (ImageView) findViewById(R.id.centerLine);
        lowerHamburgerLine = (ImageView) findViewById(R.id.lowerLine);

        primaryMenuButtonHome = (Button) findViewById(R.id.primary_menu_button_home);
        primaryMenuButtonSettings = (Button) findViewById(R.id.primary_menu_button_settings);
        primaryMenuButtonAbout = (Button) findViewById(R.id.primary_menu_button_about);
        primaryMenuButtonExit = (Button) findViewById(R.id.primary_menu_button_exit);

        secondaryMenuButtonLogIn = (Button) findViewById(R.id.secondary_menu_button_log_in);
        secondaryMenuButtonMenuTheme = (Button) findViewById(R.id.secondary_menu_button_menu_theme);
        secondaryMenuButtonCache = (Button) findViewById(R.id.secondary_menu_button_cache);
        secondaryMenuButtonAccount = (Button) findViewById(R.id.secondary_menu_button_account);
        secondaryMenuButtonBack = (Button) findViewById(R.id.secondary_menu_button_back);

        hamburgerButton.setOnClickListener(v -> {
            if(presenter.getState().getClass().getName().equals(StateClosed.class.getName())){
                presenter.setState(new StateOpened(0));
            }else {
                presenter.setState(new StateClosed(0));
            }
        });

        uiRelativeLayout.setOnClickListener(v -> presenter.nextImage((StorageManager) getSystemService(Context.STORAGE_SERVICE)));

        primaryMenuButtonExit.setOnClickListener(view -> {
            finish();
            System.exit(0);
        });

        primaryMenuButtonSettings.setOnClickListener(v -> presenter.setState(new StateSecondaryMenu(1)));

        secondaryMenuButtonBack.setOnClickListener(v -> presenter.setState(new StateOpened(4)));
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.bindView(this);
        MainActivityAnimator.getAnimator().initUiBackgroundAnimation(uiRelativeLayout, getResources());
        MainActivityAnimator.getAnimator().initHamburgerAnimator(upperHamburgerLine, middleHamburgerLine, lowerHamburgerLine, getResources());
        presenter.getState().restoreState(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        presenter.unbindView();
    }

    @Override
    public void setText(final String string){
        runOnUiThread(() -> Toast.makeText(MainView.this, String.valueOf(string), Toast.LENGTH_SHORT).show());
    }

    //TODO redo using init-method in MainActivityAnimator by setting animating values for each view.
    @Override
    public ArrayList<View> getPrimaryMenuColumn() {
        ArrayList<View> primaryMenuButtonsContainer = new ArrayList<>();
        primaryMenuButtonsContainer.add(primaryMenuButtonHome);
        primaryMenuButtonsContainer.add(primaryMenuButtonSettings);
        primaryMenuButtonsContainer.add(primaryMenuButtonAbout);
        primaryMenuButtonsContainer.add(primaryMenuButtonExit);
        return primaryMenuButtonsContainer;
    }

    @Override
    public ArrayList<View> getSecondaryMenuColumn() {
        ArrayList<View> secondaryMenuButtonsContainer = new ArrayList<>();
        secondaryMenuButtonsContainer.add(secondaryMenuButtonLogIn);
        secondaryMenuButtonsContainer.add(secondaryMenuButtonMenuTheme);
        secondaryMenuButtonsContainer.add(secondaryMenuButtonCache);
        secondaryMenuButtonsContainer.add(secondaryMenuButtonAccount);
        secondaryMenuButtonsContainer.add(secondaryMenuButtonBack);
        return secondaryMenuButtonsContainer;
    }

    @Override
    public Resources getResource() {
        return getResources();
    }

    @Override
    public void setContentImage(File file) {
        runOnUiThread(() -> {
            try{
                //To prevent one of the OutOfMemory scenarios (at the same time two images are processed by the Glide), is used clear() (source - https://github.com/bumptech/glide/issues/2229)
                Glide.clear(contentImageView);
                contentImageView.setImageBitmap(null);
                if(file.getAbsolutePath().endsWith(".gif")){
                    Glide.with(this)
                            .load(file)
                            .asGif()
                            .fitCenter()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(contentImageView);
                }else{
                    Glide.with(this)
                            .load(file)
                            .asBitmap()
                            .fitCenter()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(contentImageView);
                }
            }catch (Exception e){
                Presenter.getPresenter().setRequestResult("reading from disk is failed");
            }
        });
    }


    /*//uses for decode '.bmp' files with scaling to requested size
    private Bitmap decodeWithScale(File image, int reqWidth, int reqHeight) {
        try {
            Bitmap b2;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), options);

            final int height = options.outHeight;
            final int width = options.outWidth;
            float inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final float heightRatio = (float) height / (float) reqHeight;
                final float widthRatio = (float) width / (float) reqWidth;

                inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
            }

            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inJustDecodeBounds = false;
            //cast does not allow creating images with custom size
            options2.inSampleSize = (int) inSampleSize;
            b2 = BitmapFactory.decodeFile(image.getAbsolutePath(), options2);
            return b2;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }*/

    public void setBackgroundClickable(boolean clickable){
        uiRelativeLayout.setClickable(clickable);
    }
}