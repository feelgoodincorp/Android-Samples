package com.example.user.in_visiblealgorithm;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by User on 25.12.2017.
 */

public class AdaptiveImageView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                ,RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);
        TextView textView = new TextView(this);
        textView.setLayoutParams(layoutParams);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.horizontal);

        int Ydisplay = getResources().getDisplayMetrics().heightPixels;
        int Xdisplay = getResources().getDisplayMetrics().widthPixels;


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.horizontal);
        int Ybitmap = bitmap.getHeight();
        int Xbitmap = bitmap.getWidth();

        float displayPercent = (Ydisplay%Xdisplay);
        float imagePercent = (Ybitmap%Xbitmap);

        if(displayPercent-20>imagePercent&&displayPercent+20<imagePercent){
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else {
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        textView.setText("DisplayPer "+displayPercent+"ImgPer "+imagePercent);
        relativeLayout.addView(imageView);
        relativeLayout.addView(textView);

        setContentView(relativeLayout);


    }

}
