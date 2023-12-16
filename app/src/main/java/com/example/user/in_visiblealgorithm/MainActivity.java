package com.example.user.in_visiblealgorithm;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    Button play;
    Button exit;
    RelativeLayout rel_layout;
    ImageView webView;
    //в проекте можно реализовать viewpager
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //default states
        webView = (ImageView) findViewById(R.id.web_view);
        rel_layout = (RelativeLayout)findViewById(R.id.rel_layout2);
        play = (Button)findViewById(R.id.play_button);
        exit = (Button)findViewById(R.id.exit_button);

        exit.setAlpha(0.0f);
        exit.setClickable(false);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setAlpha(0.0f);
                play.setClickable(false);
                exit.setClickable(true);
                exit.setAlpha(1.0f);
                rel_layout.setClickable(false);
                webView.setClickable(false);
                /*play.postOnAnimationDelayed(setFeatureDrawableAlpha(R.id.play_button,0);*/
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setAlpha(1.0f);
                play.setClickable(true);
                exit.setAlpha(0.0f);
                exit.setClickable(false);
                rel_layout.setClickable(true);
                webView.setClickable(true);
            }
        });
        rel_layout.setAlpha(0.2f);
        rel_layout.setVisibility(View.INVISIBLE);
        rel_layout.setBackgroundColor(Color.BLACK);
        webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rel_layout.getVisibility()==View.INVISIBLE){
                    rel_layout.setVisibility(View.VISIBLE);
                    exit.setClickable(false);
                    play.setClickable(false);
                }
            }
        });

        rel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rel_layout.getVisibility()==View.VISIBLE){
                    rel_layout.setVisibility(View.INVISIBLE);
                    exit.setClickable(false);
                    play.setClickable(true);
                }
            }
        });


        /*Button button = new Button(this);
        button.callOnClick();
        button.didTouchFocusSelect();
        button.getAlpha();
        button.getBreakStrategy();
        button.hasOnClickListeners();
        button.isClickable();
        button.isInLayout();*/

    }
}
