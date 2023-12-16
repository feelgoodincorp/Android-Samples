package com.example.user.in_visiblealgorithm;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by User on 23.12.2017.
 */

public class ImageSlider  extends Activity {
    static FrameLayout frameLayout;
    int maxLines = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.pager_item);
        setcontentview в конце онкриейт не работает,если использовать findbyview,только в начале
        ViewPager mViewPager = (ViewPager) findViewById(R.id.ViewPager);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(this);
        mViewPager.setAdapter(adapterView);

        //интересный метод
        //mViewPager.canScrollVertically()*/

        frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams( new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        frameLayout.setVisibility(View.INVISIBLE);

        final ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        scrollView.smoothScrollTo(0,80);//___________________

        final RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        relativeLayout.setScrollContainer(false);
        relativeLayout.setFocusable(false);//--------------------------------------------------------

        final TextView textView = new TextView(this);
        textView.setText("lalalalalalalalalalallalalala /n lalalallalalalalallalalalalal /n llalalallallalalalllalalallalalalallala");
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        textViewParams.
                setMargins(65,getResources().getDisplayMetrics().heightPixels,
                        65,0);
        textView.setTextSize(20f);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(maxLines);
        textView.setLayoutParams(textViewParams);

        textView.setScrollContainer(false);
        textView.setFocusable(false);//______________________________________________________________
        scrollView.smoothScrollTo(0,80);//___________________
        textView.isClickable();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxLines == 3) {
                    textView.setMaxLines(Integer.MAX_VALUE);
                    maxLines = 4;
                    scrollView.smoothScrollTo(0, 80);//___________________
                }
            }
        });

        relativeLayout.addView(textView);
        scrollView.addView(relativeLayout);
        frameLayout.addView(scrollView);




        RelativeLayout relativeLayout2 = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout2.setLayoutParams(layoutParams2);

        ViewPager pager = new ViewPager(this);
        AndroidImageAdapter adapter = new AndroidImageAdapter(this);
        pager.setAdapter(adapter);
        pager.setLayoutParams(layoutParams2);
        relativeLayout2.addView(pager);
        relativeLayout2.addView(frameLayout);


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.INVISIBLE);
            }
        });

        setContentView(relativeLayout2);


    }
}