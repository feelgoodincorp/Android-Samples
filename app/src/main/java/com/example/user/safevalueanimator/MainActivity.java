package com.example.user.safevalueanimator;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    ImageView upper;
    ImageView center;
    ImageView lower;

    FrameLayout layout;
    boolean direction;

    private final float animateFrom = 0f;
    private final float animateTo = -135f;

    ValueAnimator toCross = ValueAnimator.ofFloat(animateFrom, animateTo);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upper = findViewById(R.id.upperLine);
        center = findViewById(R.id.centerLine);
        lower = findViewById(R.id.lowerLine);

        layout = findViewById(R.id.hamburgerFrameLayout);

        toCross.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                      @Override
                                      public void onAnimationUpdate(ValueAnimator animation) {

                                          float lineScaleByX = 1.4142135f;

                                          final float outsideLinesPosDifferenceInPxByY = getResources().getDimension(R.dimen.hamburgerLowerLineMarginTop) - getResources().getDimension(R.dimen.hamburgerUpperLineMarginTop);
                                          final float viewWidthInPx = getResources().getDimension(R.dimen.hamburgerLinesWidth);
                                          final float viewHeightInPx = getResources().getDimension(R.dimen.hamburgerLinesHeight);
                                          final float outsideLinesHalf = outsideLinesPosDifferenceInPxByY / 2;
                                          final float viewWidthHalf = viewWidthInPx / 2;
                                          final float viewSinFromPivot = outsideLinesHalf / (float) Math.sin(Math.toRadians(45)) - outsideLinesHalf;
                                          final float firstTactUpperLinePivotX = viewWidthInPx / 2 + outsideLinesPosDifferenceInPxByY / 2;
                                          final float secondTactUpperLinePivotX = viewWidthHalf - (outsideLinesHalf / (float) Math.sin(Math.toRadians(45)) - outsideLinesHalf);

                                          upper.setPivotX((Float) toCross.getAnimatedValue() >= animateTo / 3f ? firstTactUpperLinePivotX : secondTactUpperLinePivotX);
                                          upper.setTranslationX((Float) toCross.getAnimatedValue() >= animateTo / 3 ? 0 : viewSinFromPivot);
                                          upper.setTranslationY((Float) toCross.getAnimatedValue() >= animateTo / 3 ? 0 : outsideLinesHalf);
                                          upper.setRotation((Float) toCross.getAnimatedValue());
                                          upper.setTranslationY(0);


                                          //center.setScaleX(1.4142135f);
                                          //center.setRotation(-135); //попробовать 45
                                          center.setScaleX((Float) toCross.getAnimatedValue() >= animateTo / 3f ? 1 : lineScaleByX);
                                          center.setRotation((Float) toCross.getAnimatedValue() >= animateTo / 3f ? (Float) toCross.getAnimatedValue() : ((Float) toCross.getAnimatedValue() * (toCross.getAnimatedFraction() + 0.6666667f)));


                                      }
                                  });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCross.setDuration(5000);
                toCross.start();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        ViewAnimator.getAnimator().initHamburgerAnimator(upper,center,lower,getResources());
    }
}
