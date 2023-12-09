package com.example.user.betaserveraplication;

import android.animation.ObjectAnimator;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    //минимальная версия 16 - из-за fontFamily
    //animated vector compat 11+
    //onTaskRemoved 14+
    //все именно так,потому что иначе NullPointerException
    //не забыть удалять/заменять файлы из папки или переименовывать в name(1) перед выходом,чтоб не было конфликтов

    //TODO решить какие нужны переменные - именно экзепмляры классов или их интерфейсов?
    private MainModel model = new MainModel();
    private  MainPresenter presenter;

    //TODO Удалить
    static String tempState;

    TextView textView;
    ImageView contentImageView;
    ImageView hamburgerImageView;
    RelativeLayout contentImageViewContainerRelativeLayout;

    Button primary_menu_exit_button;
    Button primary_menu_about_button;
    Button primary_menu_settings_button;
    Button primary_menu_home_button;
    Button secondary_menu_back;
    Button secondary_menu_theme;
    Button secondary_menu_log_in;
    Button secondary_menu_account;
    Button secondary_menu_cache;

    byte hamburgerMenuPosition = 0;
    int primaryMenuTranslateValue=0;
    int secondaryMenuTranslateValue =0;

    int firstLevelLeft=0; //определятся в oncreate в зависимости от ориентации экрана
    int secondLevelLeft=0;//
    int firstItemMenu = 0;

    int duration;
    long startOffset;

    ArrayList<View> primaryMenuArrayList;
    ArrayList<View> secondaryMenuArrayList;
    ArrayList<View> tempBackBtnArray;

    View.OnClickListener primaryMenuOnClickListener;
    View.OnClickListener hamburgerOnClickListener;
    View.OnClickListener secondaryMenuOnClickListener;

    //нужна переменная для более быстрой длительности анимации меню от hamburgerPosition 2 к 0
    //разобратся какие переменные в этом блоке не нужны
    final byte xPrimaryLeftWay = -1;  //позиция первичного меню
    final byte xAllHideRight = 0;     //позиция за правым краем

    int alphaColor;
    int whiteColor;
    int blackColor;

    int startHamburgerAnimationColor;
    int endHamburgerAnimationColor;
    int startBackgroundAnimationColor;
    int endBackgroundAnimationColor;

    final String intentActionConnectivityChange = "android.net.conn.CONNECTIVITY_CHANGE";
    final String intentActionReceiverIntent = BetaIntentService.RECEIEVERINTENT;

    String metadata;
    static boolean internetConnectionState;
    static boolean internetAccess;
    static boolean databaseAccess;
    static String serviceIsRuns;
    static String sslProtocol;
    static int responseStatus;
    static boolean isLastDataItem;
    static String contentType;
    static boolean isFileExist;
    static int count;// эту и некоторые другие можно (нужно/ненужно?) не создавать и использвать основные переменные (хотя структура будет менее понятная)
    //TODO нужны ли даннве о таймаутах?

    static boolean isDownloadCycleRuns; //flags
    static  boolean isDownloadCycleShouldContinue;

    int counter;// = 1;

    //TODO ресивер работает с ответом базы данных и получает ответ,можно кинуть в
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent==null) {Log.i("Receiver"," intent = null");}
            else{

                Log.i("Receiver", "action: " + intent.getAction());
                Log.i("Receiver", "component: " + intent.getComponent());

                Bundle bundle = intent.getExtras();
                responseStatus = bundle.getInt("responseStatus");
                isLastDataItem = bundle.getBoolean("isLastDataItem");
                contentType = bundle.getString("contentType");

                //если появился/исчез - сменить continueDataReceiving
                //запуск сервиса в новой версии с сплэшскрином можно вынести в саму его активность

                //обнуляется ли интент после ротации - нет
                //после завершения работы сервиса,интент != null

                //TODO если NPE,вызвать проверку header и если надо, то обнулить счетчик
                switch (intent.getAction()){
                    case intentActionConnectivityChange :
                        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                        internetConnectionState = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                        break;
                    case intentActionReceiverIntent :
                        //то,что данные о том,какие данные  должны быть скачаны находятся в mainActivity - аналогия с тем,что обычно от ui приходят все запросы
                        //TODO проверка на наличие файла, сеттинг в ImageView, counter++


                        if(isLastDataItem){counter=1;}
                        else {counter++;
                            metadata = " counter = ".concat(String.valueOf(counter))
                                    .concat(" content type = ").concat(String.valueOf(contentType))
                                    .concat(" responseStatus = ").concat(String.valueOf(responseStatus))
                                    .concat(" isLastDataItem = ").concat(String.valueOf(isLastDataItem));
                            textView.setText(metadata);
                            //TODO здесь же по идее должен быть сеттинг данных во вью
                            //TODO не инкрементировать,если недоступен (инет/база/что либо?(не получены данные))
                        }

                        break;
                    default:break;
                }
            }
        }
    };


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putByte("hamburgerMenuPosition",hamburgerMenuPosition);
        savedInstanceState.putInt("primaryMenuTranslateValue",primaryMenuTranslateValue);
        savedInstanceState.putInt("secondaryMenuTranslateValue",secondaryMenuTranslateValue);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(presenter!=null){
            Log.i("presenter"," != null");
        }else {
            Log.i("presenter"," = null,will be created");
            presenter = new MainPresenter(this.model);
        }

        //presenter = getPresenter();
        presenter.bindView(this);
        Log.i("PRESENTER", " BIND");
        presenter.startLoadingCycle();
        Log.i("PRESENTER_METHOD", " STARTING FROM ACTIVITY");
        //затем в онкликлисенерах вызываем методы презентера



        //почему то цвета нельзя иницилизировать вне onCreate
        alphaColor = getResources().getColor(R.color.fullAlpha);
        whiteColor = getResources().getColor(R.color.colorWhite);
        blackColor = getResources().getColor(R.color.colorBlack);

        initViews();

        primaryMenuArrayList = new ArrayList<>();
        primaryMenuArrayList.add(primary_menu_home_button);
        primaryMenuArrayList.add(primary_menu_settings_button);
        primaryMenuArrayList.add(primary_menu_about_button);
        primaryMenuArrayList.add(primary_menu_exit_button);//в xml опорная для верхних view

        secondaryMenuArrayList = new ArrayList<>();
        secondaryMenuArrayList.add(secondary_menu_log_in);
        secondaryMenuArrayList.add(secondary_menu_theme);
        secondaryMenuArrayList.add(secondary_menu_cache);
        secondaryMenuArrayList.add(secondary_menu_account);
        //back button в xml опорная для верхних view, не используется в массиве потому что анимируется отдельно

        tempBackBtnArray = new ArrayList<>();
        tempBackBtnArray.add(secondary_menu_back);

        final int screenWidthInDP = getResources().getConfiguration().screenWidthDp;
        final int screenWidthInPX = getResources().getDisplayMetrics().widthPixels;
        final int screenHeightInDP = getResources().getConfiguration().screenHeightDp;
        final int screenHeightInPX = getResources().getDisplayMetrics().heightPixels;

        final DisplayMetrics metrics = getResources().getDisplayMetrics();



        //в чем измеряется этот viewWidth?
        final float viewWidth = getResources().getDimension(R.dimen.firstMenuLevelLayoutWidth) * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        final int xallHideLeft = -screenWidthInPX; //позиция за левым краем (возможно переменная не нужна(если нет about))

        duration = getResources().getInteger(R.integer.XtranslationDduration);
        startOffset = (long) getResources().getInteger(R.integer.startOffset);

        //TODO расположить в onresume?
        int orientation = getResources().getConfiguration().orientation; //для создания значения перемещения в анимации в зависимости от ориентации
        switch (orientation) {
            case 1:
                firstLevelLeft = (int) Math.round(screenWidthInPX / 2.5); //портретная, учитывая ширину view
                secondLevelLeft = (int) Math.round(screenWidthInPX / 1.25);break;
            case 2:
                firstLevelLeft = (int) Math.round(screenHeightInPX / 2.5); //ландшафтная, учитывая ширину view
                secondLevelLeft = (int) Math.round(screenHeightInPX / 1.25);break;
            default: //выяснить,какие есть другие значения | first и second имеют нулевые значения (позиция меню за правым краем)
                break;
        }

        initMenuOnClickListeners();

        //это вообще должно быть вверху настолько,насколько это возможно(оно уже?учитывая setting onClickListener)
        //короче нужно  определить область,в которой можно писать остальной код
        //и вообще учитывать,что onCreate срабатывает только при загрузке активности и все должно быть структурированно более в сторону процед.ориент.программирования

        //TODO расчет изображения при пейзажной оринетации + может хранить файлы в папке относительно места приложения?
        //TODO удалить
        textView.setText(tempState);

        //переместить на начало onCreate
        if(savedInstanceState != null){
            //сохранение данных а затем нажатие определенных кнопок(веток дерева) в соответствии с сохраняемой древовидной иерархией
            hamburgerMenuPosition = savedInstanceState.getByte("hamburgerMenuPosition");
            Drawable drawable;
            Animatable animatable;
            int tempDuration = duration; duration=0;
            long tempStartOffset = startOffset; startOffset=0;

            switch (hamburgerMenuPosition){
                case 0:hamburgerImageView.setImageResource(R.drawable.m_vector);break;
                case 1:
                    hamburgerMenuPosition=0;
                    hamburgerImageView.performClick();
                    hamburgerImageView.setImageResource(R.drawable.m_vector_reverse_nullable_duration);
                    drawable = hamburgerImageView.getDrawable();// сделать без пересоздания переменной
                    animatable = ((Animatable) drawable);
                    animatable.start();break;
                case 2:
                    //потому что первичное (и вторичное) меню не вызывают анимации цвета
                    hamburgerMenuPosition=0;
                    hamburgerImageView.performClick();
                    primary_menu_exit_button.performClick();
                    hamburgerImageView.setImageResource(R.drawable.m_vector_reverse_nullable_duration);
                    drawable = hamburgerImageView.getDrawable();// сделать без пересоздания переменной
                    animatable = ((Animatable) drawable);
                    animatable.start();break;
            }//в этом моменте используется клик для показа secondaryMenu(сейчас - независимо от нажатой кнопки)

            duration=tempDuration;
            startOffset=tempStartOffset;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BetaIntentService.RECEIEVERINTENT);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);

        //проверка состояния интернет(вкл/выкл) именно перед стартом,чтоб если выкл,не вызывать интент
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        internetConnectionState = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        Log.i("OnResume", " starting cycle from onResume");
        //обнуляется ли интент после ротации - нет
        if(isDownloadCycleRuns){
            Log.i("OnResume", " shouldContinue = true, without native calling downloadCycle();");
            isDownloadCycleShouldContinue=true;
        }else{
            Log.i("OnResume", " shouldContinue = true, with native calling downloadCycle();");
            isDownloadCycleShouldContinue=true;
            downloadCycle();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("PRESENTER", " UNBIND");
        presenter.unbindView();

        isDownloadCycleShouldContinue=false;
        unregisterReceiver(receiver);
        Log.i("OnPause", "onPause");
    }

    private void animateMenuItems(int firstView, int translateValue, int duration, long startOffset, ArrayList<View> viewArrayList){
        if(!viewArrayList.isEmpty()){
            int length=viewArrayList.size();
            int start=firstView;
            for(int i=0; i<length;i++){
                if(firstView>=start){
                    firstView=firstView-i;
                    if(firstView<0||firstView<length){length++;}//просто
                }else if(firstView<start) {
                    firstView=firstView+i;
                    if(firstView<0||firstView>length){length++;}//length++,без if/else
                }
                if(firstView>=0&&firstView<viewArrayList.size()){
                    ObjectAnimator animator = ObjectAnimator.ofFloat(viewArrayList.get(firstView), View.TRANSLATION_X, translateValue);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(duration);
                    animator.setStartDelay(startOffset);//строка лишняя,но без нее виден недостаток нажатия всех кнопок разом(внезапный телепорт компонентов)
                    animator.start();
                    animator.setStartDelay(startOffset);//можно удалить эту и оставить выше,но это все по прежднему сказывается на отзывчивости(80мс)
                    startOffset=(startOffset*3)/2;
                }
            }
        }
    }

    private void animateMenuItemsColor(View animatableView,String propertyName,int startColor,int endColor,int duration){
        final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(animatableView,
                propertyName,
                new android.support.graphics.drawable.ArgbEvaluator(), // try/catch
                startColor,
                endColor);
        backgroundColorAnimator.setDuration(duration);
        backgroundColorAnimator.start();
    }

    private void downloadCycle(){
        //условие начала цикла - крутится всегда,показывая бесконечную работу с интерфейсом,
        //условие остановки цикла - (не обновлять данные,когда вызвано чужое приложение поверх этого и когда выбрано меню) срабатывание onPause
        //TODO запускать сервис только после sleep(чтоб при паузе и возобновлении не было нового,поменяного в фоне,изображения)

        isDownloadCycleRuns = true;
        Log.i("downloadCycle", " isDownloadCycleRuns = true, cycle started");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isDownloadCycleShouldContinue){
                    while (BetaIntentService.isIntentServiceRunning){
                        try {
                            Thread.sleep(1000); //первый sleep ждет,пока закончится работа сервиса
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //TODO объект будет конечно же перемещен, а вообще нужно проверить потокобезопасность такого подхода
                    Intent startServiceIntent = new Intent(MainActivity.this, BetaIntentService.class);
                    //TODO здесь будет передача параметров загрузки
                    startServiceIntent.putExtra("counter",counter);
                    startService(startServiceIntent);
                    Log.i("Starting", " IntentService from HomeButton,waiting 10 seconds");
                    try {
                        Thread.sleep(10000); //второй sleep задает интервал между загрузками
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //TODO тут может не сработать(условие выхода из цикла?),потому что нужно прописать false после start
                isDownloadCycleRuns=false;
                Log.i("downloadCycle", " isDownloadCycleRuns = false, cycle ended");
            }
        }).start();

    }

    void initViews(){
        textView = (TextView) findViewById(R.id.metadataTextView);
        contentImageView = (ImageView) findViewById(R.id.contentImageView);
        hamburgerImageView = (ImageView)findViewById(R.id.hamburgerImageView);
        contentImageViewContainerRelativeLayout = (RelativeLayout) findViewById(R.id.contentContainerRelativeLayout);

        primary_menu_exit_button = findViewById(R.id.primary_menu_button_exit);
        primary_menu_about_button = findViewById(R.id.primary_menu_button_about);
        primary_menu_settings_button = findViewById(R.id.primary_menu_button_settings);
        primary_menu_home_button = findViewById(R.id.primary_menu_button_home);

        secondary_menu_back = findViewById(R.id.secondary_menu_button_back);
        secondary_menu_account = findViewById(R.id.secondary_menu_button_account);
        secondary_menu_cache = findViewById(R.id.secondary_menu_button_cache);
        secondary_menu_theme = findViewById(R.id.secondary_menu_button_menu_theme);
        secondary_menu_log_in = findViewById(R.id.secondary_menu_button_log_in);
    }

    void initMenuOnClickListeners(){

        hamburgerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (hamburgerMenuPosition) {
                    case 0:
                        startHamburgerAnimationColor=whiteColor;   endHamburgerAnimationColor=blackColor;
                        startBackgroundAnimationColor=alphaColor;  endBackgroundAnimationColor=whiteColor;
                        primaryMenuTranslateValue = xPrimaryLeftWay * firstLevelLeft;
                        secondaryMenuTranslateValue = 0;
                        hamburgerMenuPosition = 1;
                        hamburgerImageView.setImageResource(R.drawable.m_vector);

                        break;  //show primary menu
                    case 1: //break не нужен(!)
                    case 2:
                        startHamburgerAnimationColor=blackColor;   endHamburgerAnimationColor=whiteColor;
                        startBackgroundAnimationColor=whiteColor;  endBackgroundAnimationColor=alphaColor;
                        primaryMenuTranslateValue = xPrimaryLeftWay * xAllHideRight;
                        secondaryMenuTranslateValue = 0;
                        hamburgerMenuPosition = 0;
                        hamburgerImageView.setImageResource(R.drawable.m_vector_reverse);
                        break;  //hide all opened menu items
                    default:break;
                }
                final Drawable drawable = hamburgerImageView.getDrawable();// сделать без пересоздания переменной
                Animatable animatable = ((Animatable) drawable);
                animatable.start();

                animateMenuItemsColor(hamburgerImageView, "colorFilter", startHamburgerAnimationColor, endHamburgerAnimationColor, duration * 2);
                animateMenuItemsColor(contentImageViewContainerRelativeLayout, "backgroundColor", startBackgroundAnimationColor, endBackgroundAnimationColor, duration);

                animateMenuItems(0, primaryMenuTranslateValue, duration, startOffset, primaryMenuArrayList);
                animateMenuItems(0, secondaryMenuTranslateValue, duration, startOffset, secondaryMenuArrayList);
                animateMenuItems(0, 0, duration,duration, tempBackBtnArray);
                //нажатие на гамбургер в любом случае скрывает back button
            }
        };


        primaryMenuOnClickListener = new View.OnClickListener() {
            //проблем использования firstItemMenu переменной в невозможности использовать отдельно back/about
            //убрать бэк из  массива вообще и анимировать отдельно

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.primary_menu_button_home:
                        firstItemMenu=0;
                        break;
                    case R.id.primary_menu_button_settings:
                        firstItemMenu=1;
                        break;
                    case R.id.primary_menu_button_about:
                        firstItemMenu=2;
                        break;
                    case R.id.primary_menu_button_exit:
                        firstItemMenu=3;
                        textView.setText("NO CONNECTION");
                        //TODO обычный выход из приложения
                        //TODO не забыть обозначить shouldContinue как false,чтоб и интент тоже завершился
                        break;
                }
                //если есть различия в поведении переменных кнопок,это нужно описать в их case
                hamburgerMenuPosition=2;
                primaryMenuTranslateValue=secondLevelLeft*xPrimaryLeftWay;
                secondaryMenuTranslateValue =firstLevelLeft*xPrimaryLeftWay;
                animateMenuItems(firstItemMenu,primaryMenuTranslateValue,duration,startOffset,primaryMenuArrayList);
                animateMenuItems(firstItemMenu, secondaryMenuTranslateValue,duration,startOffset,secondaryMenuArrayList);
                animateMenuItems(0, firstLevelLeft * xPrimaryLeftWay, duration,startOffset, tempBackBtnArray);
                //back button тоже показывается, логика ее скрытия описана в своем onclicklistener и hamburger.close
            }
        };

        secondaryMenuOnClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.secondary_menu_button_back :
                        firstItemMenu=3;
                        primaryMenuTranslateValue = firstLevelLeft*xPrimaryLeftWay;
                        secondaryMenuTranslateValue = 0;
                        hamburgerMenuPosition=1;
                        //etc isOpen=0
                    case R.id.secondary_menu_button_account : break;
                    case R.id.secondary_menu_button_cache : break;
                    case R.id.secondary_menu_button_menu_theme : break;
                    case R.id.secondary_menu_button_log_in : break;
                }
                animateMenuItems(firstItemMenu, primaryMenuTranslateValue,duration,startOffset,primaryMenuArrayList);
                animateMenuItems(firstItemMenu,secondaryMenuTranslateValue,duration,startOffset,secondaryMenuArrayList);
                animateMenuItems(0, secondaryMenuTranslateValue, duration,0, tempBackBtnArray);
            }
        };

        hamburgerImageView.setOnClickListener(hamburgerOnClickListener);
        primary_menu_home_button.setOnClickListener(primaryMenuOnClickListener);
        primary_menu_settings_button.setOnClickListener(primaryMenuOnClickListener);
        primary_menu_about_button.setOnClickListener(primaryMenuOnClickListener);
        primary_menu_exit_button.setOnClickListener(primaryMenuOnClickListener);

        secondary_menu_back.setOnClickListener(secondaryMenuOnClickListener);
        secondary_menu_account.setOnClickListener(secondaryMenuOnClickListener);
        secondary_menu_cache.setOnClickListener(secondaryMenuOnClickListener);
        secondary_menu_theme.setOnClickListener(secondaryMenuOnClickListener);
        secondary_menu_log_in.setOnClickListener(secondaryMenuOnClickListener);
    }

    @Override
    public void showImage() {
    }

    protected MainPresenter getPresenter(){



        if(presenter == null){
            Log.i("getPresenter:"," presenter == null");
            presenter = new MainPresenter(this.model);
        }else {
            Log.i("getPresenter:"," presenter != null");
        }
        return (MainPresenter) presenter;
    }

}
