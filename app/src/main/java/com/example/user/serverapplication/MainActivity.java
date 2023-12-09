package com.example.user.serverapplication;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;

//почему нельзя использовать заготовки местоположений для анимации?в процессе работы программы параметр transition меняется
public class MainActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
    TextView textView;
    ScrollView aboutTextView;
    ImageView hamburgerImageView;
    Button menu_exit_button;
    Button menu_about_button;
    Button menu_settings_button;
    Button menu_home_button;
    Button secondary_menu_back;
    Button secondary_menu_theme;
    Button secondary_menu_log_in;
    Button secondary_menu_account;
    Button secondary_menu_cache;

    public String url= "https://reqres.in/api/users/2";

    byte hamburgerIsOpen = 0;
    int screenWidthInDP;
    int screenWidthInPX;

    int screenHeightInDP;
    int screenHeightInPX;

    int primaryMenuTranslateValue=0;
    int secondaryMenuTransValue=0;

    int firstLevelLeft=0;
    int secondLevelLeft=0;

    int duration;
    long startOffset;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putByte("hamburgerIsOpen",hamburgerIsOpen);
        savedInstanceState.putInt("primaryMenuTranslateValue",primaryMenuTranslateValue);
        savedInstanceState.putInt("secondaryMenuTransValue",secondaryMenuTransValue);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_activity);

        textView = findViewById(R.id.myTextView);
        hamburgerImageView = findViewById(R.id.hamburger);
        menu_exit_button = findViewById(R.id.menu_button_exit);
        menu_about_button = findViewById(R.id.menu_button_about);
        menu_settings_button = findViewById(R.id.menu_button_settings);
        menu_home_button = findViewById(R.id.menu_button_home);

        secondary_menu_back = findViewById(R.id.secondary_menu_button1);
        secondary_menu_account = findViewById(R.id.secondary_menu_button2);
        secondary_menu_cache = findViewById(R.id.secondary_menu_button3);
        secondary_menu_theme = findViewById(R.id.secondary_menu_button4);
        secondary_menu_log_in = findViewById(R.id.secondary_menu_button5);

        screenWidthInDP = getResources().getConfiguration().screenWidthDp;
        screenWidthInPX = getResources().getDisplayMetrics().widthPixels;
        screenHeightInDP = getResources().getConfiguration().screenHeightDp;
        screenHeightInPX = getResources().getDisplayMetrics().heightPixels;

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        //можно сделать в XML?

        final ArrayList<View> viewArrayList = new ArrayList<>();
        viewArrayList.add(menu_home_button);
        viewArrayList.add(menu_settings_button);
        viewArrayList.add(menu_about_button);
        viewArrayList.add(menu_exit_button);//опорная для верхних view

        final ArrayList<View> viewArrayList2 = new ArrayList<>();
        viewArrayList2.add(secondary_menu_log_in);
        viewArrayList2.add(secondary_menu_theme);
        viewArrayList2.add(secondary_menu_cache);
        viewArrayList2.add(secondary_menu_account);
        viewArrayList2.add(secondary_menu_back);//опорная для верхних view

        final ArrayList<View> tempBackBtnArray = new ArrayList<>();
        tempBackBtnArray.add(secondary_menu_back);

        final int fromMenuToAboutStartOffset = 40; //удалить
        final byte xPrimaryLeftWay=-1;
        final byte xAllHideRight=0;
        final float viewWidth = getResources().getDimension(R.dimen.firstMenuLevelLayoutWidth) * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        final int xallHideLeft=-screenWidthInPX;

        duration = getResources().getInteger(R.integer.XtranslationDduration);
        startOffset = (long) getResources().getInteger(R.integer.startOffset);




        int orientation = getResources().getConfiguration().orientation; //для создания значения перемещения в анимации в зависимости от ориентации
        if(orientation==1){
            firstLevelLeft=(int)Math.round(screenWidthInPX /2.5); //учитывая ширину view
            secondLevelLeft=(int)Math.round(screenWidthInPX /1.25);}
        else if(orientation==2){
            firstLevelLeft=(int)Math.round(screenHeightInPX /2.5); //учитывая ширину view
            secondLevelLeft=(int)Math.round(screenHeightInPX /1.25);}
        //продолжить else if непредвиденным результатом

        //вся остальная логика должна быть ниже clickListeners и instanceState или в них,выше - объявления переменных

        View.OnClickListener menuViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.hamburger:
                        //объеденить два свича!
                        switch (hamburgerIsOpen){case 0:hamburgerImageView.setImageResource(R.drawable.m_vector); break;
                                                 case 1: hamburgerImageView.setImageResource(R.drawable.m_vector_reverse);break;
                                                 case 2: hamburgerImageView.setImageResource(R.drawable.m_vector_reverse);break;}// лишний case(один из двух)
                        final Drawable drawable = hamburgerImageView.getDrawable();// сделать без пересоздания переменной
                        Animatable animatable = ((Animatable) drawable);
                        animatable.start();
                        //определение состояния кнопки меню
                        switch (hamburgerIsOpen){
                            case 0:hamburgerIsOpen=1;primaryMenuTranslateValue=xPrimaryLeftWay*firstLevelLeft;secondaryMenuTransValue=0;break;  //show primary menu
                            case 1:hamburgerIsOpen=0;primaryMenuTranslateValue=xPrimaryLeftWay*xAllHideRight;secondaryMenuTransValue=0;break;  //hide primary menu
                            //нужна ли строка?
                            case 2:hamburgerIsOpen=0;primaryMenuTranslateValue=xPrimaryLeftWay*xAllHideRight;secondaryMenuTransValue=0; break;  //hide all menu
                        }
                        animateMenuItems(0,primaryMenuTranslateValue,duration,startOffset,viewArrayList);
                        animateMenuItems(0,secondaryMenuTransValue,duration,startOffset,viewArrayList2);
                        break;
                    case R.id.menu_button_home:
                        hamburgerIsOpen=2;
                        primaryMenuTranslateValue=secondLevelLeft*xPrimaryLeftWay;
                        secondaryMenuTransValue=firstLevelLeft*xPrimaryLeftWay;
                        animateMenuItems(0,primaryMenuTranslateValue,duration,startOffset,viewArrayList);
                        animateMenuItems(0,secondaryMenuTransValue,duration,startOffset,viewArrayList2);
                        break;
                    case R.id.menu_button_settings:
                        hamburgerIsOpen=2;
                        primaryMenuTranslateValue=secondLevelLeft*xPrimaryLeftWay;
                        secondaryMenuTransValue=firstLevelLeft*xPrimaryLeftWay;
                        animateMenuItems(1,primaryMenuTranslateValue,duration,startOffset,viewArrayList);
                        animateMenuItems(1,secondaryMenuTransValue,duration,startOffset,viewArrayList2);
                        break;
                    case R.id.menu_button_about:
                        hamburgerIsOpen=2;
                        viewArrayList2.remove(secondary_menu_back);
                        primaryMenuTranslateValue=xallHideLeft-(int)viewWidth;// или height?
                        secondaryMenuTransValue=xallHideLeft-(int) viewWidth;
                        animateMenuItems(2, primaryMenuTranslateValue, duration, fromMenuToAboutStartOffset, viewArrayList);
                        animateMenuItems(2, secondaryMenuTransValue, duration, fromMenuToAboutStartOffset, viewArrayList2);
                        viewArrayList2.add(secondary_menu_back);
                        animateMenuItems(0, firstLevelLeft * xPrimaryLeftWay, duration, fromMenuToAboutStartOffset, tempBackBtnArray);
                        break;
                    case R.id.menu_button_exit:
                        hamburgerIsOpen=2;
                        primaryMenuTranslateValue=secondLevelLeft*xPrimaryLeftWay;
                        secondaryMenuTransValue=firstLevelLeft*xPrimaryLeftWay;
                        animateMenuItems(3,primaryMenuTranslateValue,duration,startOffset,viewArrayList);
                        animateMenuItems(3,secondaryMenuTransValue,duration,startOffset,viewArrayList2);
                        break;
                }
            }
        };

        View.OnClickListener menuViewOnClickListener2 = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.secondary_menu_button1 :
                        primaryMenuTranslateValue=firstLevelLeft*xPrimaryLeftWay;secondaryMenuTransValue=0;
                        animateMenuItems(3,primaryMenuTranslateValue,duration,startOffset,viewArrayList);
                        animateMenuItems(4,secondaryMenuTransValue,duration,startOffset,viewArrayList2);
                        hamburgerIsOpen=1;
                        //etc isOpen=0
                    case R.id.secondary_menu_button2 : break;
                    case R.id.secondary_menu_button3 : break;
                    case R.id.secondary_menu_button4 : break;
                    case R.id.secondary_menu_button5 : break;
                }
            }
        };

        hamburgerImageView.setOnClickListener(menuViewOnClickListener);
        menu_about_button.setOnClickListener(menuViewOnClickListener);
        menu_exit_button.setOnClickListener(menuViewOnClickListener);
        menu_home_button.setOnClickListener(menuViewOnClickListener);
        menu_settings_button.setOnClickListener(menuViewOnClickListener);

        secondary_menu_back.setOnClickListener(menuViewOnClickListener2);
        secondary_menu_log_in.setOnClickListener(menuViewOnClickListener2);
        secondary_menu_account.setOnClickListener(menuViewOnClickListener2);
        secondary_menu_theme.setOnClickListener(menuViewOnClickListener2);
        secondary_menu_cache.setOnClickListener(menuViewOnClickListener2);

        if(savedInstanceState != null){
            //сохранение данных а затем нажатие определенных кнопок(веток дерева) в соответствии с сохраняемой древовидной иерархией
            hamburgerIsOpen = savedInstanceState.getByte("hamburgerIsOpen");
            Drawable drawable;
            Animatable animatable;
            int tempDuration = duration; duration=0;
            long tempStartOffset = startOffset; startOffset=0;

            switch (hamburgerIsOpen){
                case 0:hamburgerImageView.setImageResource(R.drawable.m_vector);break;
                case 1:hamburgerIsOpen--;
                       hamburgerImageView.performClick();
                       hamburgerImageView.setImageResource(R.drawable.m_vector_reverse_nullable_duration);
                       drawable = hamburgerImageView.getDrawable();// сделать без пересоздания переменной
                       animatable = ((Animatable) drawable);
                       animatable.start();break;
                case 2:menu_exit_button.performClick();
                       hamburgerImageView.setImageResource(R.drawable.m_vector_reverse_nullable_duration);
                       drawable = hamburgerImageView.getDrawable();// сделать без пересоздания переменной
                       animatable = ((Animatable) drawable);
                       animatable.start();break;

            }//в этом моменте используется клик для показа secondaryMenu(сейчас - независимо от нажатой кнопки)
            duration=tempDuration;
            startOffset=tempStartOffset;

        }


        final Request request = new Request.Builder().url(url).build();

        /* client.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 e.printStackTrace();
             }

             @Override
             public void onResponse(Call call, final Response response) throws IOException {
                 final String myResponse = response.body().string();

                 MainActivity.this.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         textView.setText(myResponse);
                     }
                 });
             }
         });*/
    }

    private void animateMenuItems(int chosedView, int translateValue, int duration, long startOffset, ArrayList<View> viewArrayList){
        if(!viewArrayList.isEmpty()){
            int length=viewArrayList.size();
            int start=chosedView;
            for(int i=0; i<length;i++){
                if(chosedView>=start){
                    chosedView=chosedView-i;
                    if(chosedView<0||chosedView<length){length++;}//просто
                }else if(chosedView<start) {
                    chosedView=chosedView+i;
                    if(chosedView<0||chosedView>length){length++;}//length++,без if/else
                }
                if(chosedView>=0&&chosedView<viewArrayList.size()){
                    ObjectAnimator animator = ObjectAnimator.ofFloat(viewArrayList.get(chosedView), View.TRANSLATION_X, translateValue);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(duration);
                    animator.setStartDelay(startOffset);
                    animator.start(); //вместо стартОффсетов у всех одинаковая анимация,но разный currentTimePlaying
                    animator.setStartDelay(startOffset);
                    startOffset=(startOffset*3)/2;
                }
            }
        }
    }
}
