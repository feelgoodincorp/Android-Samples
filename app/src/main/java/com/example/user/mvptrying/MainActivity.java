package com.example.user.mvptrying;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements Contract.ContractView{

    MyModel model = MyModel.getModelInstance();
    MyPresenter presenter = MyPresenter.getPresenterInstance();


    static int counterValue;
    static int responseStatus;
    static boolean isLastDataItem;
    static boolean connectionState;
    static String contentType;


    static String metadata;
    static TextView metaDataTextView;

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

    View.OnClickListener primaryMenuOnClickListener;
    View.OnClickListener secondaryMenuOnClickListener;
    View.OnClickListener hamburgerOnClickListener;


    // если все view будут в массиве, то можно отправить в initViews массив и обработать в for(массив будет использоватся для анимации
    // (хотя можно просто обозначить порядок в том сортировочном методе и по порядку вызывать start()))


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        presenter.initReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "OnResume");
        presenter.isDownloadCycleShouldContinue = true;
        presenter.bindView(this);
        presenter.registerMyReceiver(presenter.receiver, getApplicationContext());
        Log.i("MainActivity", "MyPresenter :: bindView()");

        if(presenter.checkConnection(getApplicationContext())){
            presenter.isDownloadCycleShouldContinue=true;
            if(!presenter.isDownloadCycleRuns){
                Log.i("MainActivity", "download cycle starting");
                presenter.downloadingCycle(getApplicationContext());
            }else{

            }
        }else{
            presenter.isDownloadCycleShouldContinue=false;
            Log.i("MainActivity", "download cycle will not starting");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("OnPause", "onPause");
        presenter.unregisterMyReceiver(presenter.receiver, getApplicationContext());
        presenter.isDownloadCycleShouldContinue = false;
        presenter.unbindView();
        Log.i("MainActivity", "MyPresenter :: unbindView()");
    }

    public void initViews(){

        metaDataTextView = findViewById(R.id.metadataTextView);
        contentImageView = findViewById(R.id.contentImageView);
        hamburgerImageView = findViewById(R.id.hamburgerImageView);
        contentImageViewContainerRelativeLayout = findViewById(R.id.contentContainerRelativeLayout);

        primary_menu_exit_button = findViewById(R.id.primary_menu_button_exit);
        primary_menu_about_button = findViewById(R.id.primary_menu_button_about);
        primary_menu_settings_button = findViewById(R.id.primary_menu_button_settings);
        primary_menu_home_button = findViewById(R.id.primary_menu_button_home);

        secondary_menu_back = findViewById(R.id.secondary_menu_button_back);
        secondary_menu_account = findViewById(R.id.secondary_menu_button_account);
        secondary_menu_cache = findViewById(R.id.secondary_menu_button_cache);
        secondary_menu_theme = findViewById(R.id.secondary_menu_button_menu_theme);
        secondary_menu_log_in = findViewById(R.id.secondary_menu_button_log_in);

        hamburgerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        hamburgerImageView.setOnClickListener(hamburgerOnClickListener);

    }

    @Override
    public void showImageFromPath(String path) {
        //Bitmap bitmap = BitmapFactory.decodeFile(pathToPicture);
        //contentImageView.setImageBitmap(bitmap);
    }

    // этого метода нет в интерфейсе, потому что он именно для этой активности
    public static void updateMetadataValue(String parameter, String data){
        switch (parameter){
            //все возможные принимаемые параметры
            //как вариант - перехватить не подходящий передаваемый тип(по моему classcastexception)
            case "contentType": contentType = data; break;
            case "connectionState": connectionState = Boolean.valueOf(data); break;
            case "isLastDataItem": isLastDataItem = Boolean.valueOf(data); break;
            case "counter":  counterValue = Integer.valueOf(data); break;
            case "responseStatus": responseStatus = Integer.valueOf(data); break;
            default: break;
        }

        metadata = " counter = ".concat(String.valueOf(counterValue))
                .concat(" content type = ").concat(String.valueOf(contentType))
                .concat(" responseStatus = ").concat(String.valueOf(responseStatus))
                .concat(" isLastDataItem = ").concat(String.valueOf(isLastDataItem))
                .concat(" connectionState = ").concat(String.valueOf(connectionState));

        metaDataTextView.setText(metadata);
    }
}
