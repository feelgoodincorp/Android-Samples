package com.example.user.imageresizing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

//TODO могут быть проблемы с передачей изображения в imageView,если они много весят

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    int counter = 0;
    float screenRatio;
    float imageRatio;
    float allowableRatioDifference;
    float realRatioDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (counter >= 3) {
                        counter = 1;
                    } else {
                        counter++;
                    }

                    final int resource = getResources().getIdentifier("a" + counter, "drawable", getPackageName());

                    final Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), resource);

                  allowableRatioDifference = 0.7f; //меньше значение - первозданнее картинка


                  DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();

                  screenRatio = ((float) metrics.heightPixels / (float) metrics.widthPixels);

                  imageRatio  = (float)bitmap.getHeight() /  (float)bitmap.getWidth();

                    if (screenRatio > imageRatio) {
                        realRatioDifference = screenRatio - imageRatio;
                    } else {
                        realRatioDifference = imageRatio - screenRatio;
                    }


                    runOnUiThread(new Runnable() //run on ui thread
                    {
                        public void run()
                        {
                            if (realRatioDifference < allowableRatioDifference) {
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            } else {
                                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            }

                            imageView.setImageBitmap(bitmap);
                            textView.setText("screenRatio = " + screenRatio + "\n" +
                                    "imageRatio = " + imageRatio + "\n" +
                                    "difference = " + realRatioDifference + "\n" +
                                    "counter = " + counter);
                        }
                    });

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
