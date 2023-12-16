package com.example.user.vvpwithcorrectswipe;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 28.01.2018.
 */

public class ElementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ImageView> imageViews = new ArrayList<>();

        imageViews.add((ImageView) findViewById(R.id.image1));
        imageViews.add((ImageView) findViewById(R.id.image2));
        imageViews.add((ImageView) findViewById(R.id.image3));

        animateImageViews(imageViews);
    }

    private void animateImageViews(List<ImageView> imageViews) {
        for (ImageView imageView : imageViews) {
            animateImageView(imageView);
        }
    }

    private void animateImageView(ImageView imageView) {
        final Drawable drawable = imageView.getDrawable();
        if (drawable instanceof Animatable) {
            Animatable animatable = ((Animatable) drawable);
            animatable.start();
        }
    }
}