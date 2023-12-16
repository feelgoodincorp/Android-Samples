package com.example.user.testanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int startOffset = 200;
    int a;
    TextView tv1;
    TextView tv2;
    TextView metadata;
    boolean side = true;

    float xTranslateView1From = 0f;
    float xTranslateView1To = 200f;
    float xTranslateView2From = 0f;
    float xTranslateView2To = 20;

    ValueAnimator valueAnimator1ViewToRight;
    ValueAnimator valueAnimator1ViewToLeft;
    ValueAnimator valueAnimator2ViewToRight;
    ValueAnimator valueAnimator2ViewToLeft;

    AnimatorSet animatorSet1 = new AnimatorSet();
    AnimatorSet animatorSet2 = new AnimatorSet();

    Float f1 = 0f;
    Float f2 = 0f;

    long time;
    long duration = 3000l;

    boolean isLeftListenerSet=false;
    boolean isRightListenerSet=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        metadata = findViewById(R.id.metadata);
        tv1 = findViewById(R.id.textview1);
        tv2 = findViewById(R.id.textview2);

        final ArrayList<View> arrayList = new ArrayList<>();
        arrayList.add(tv1);
        arrayList.add(tv2);

        chechAnimatedValue();

        //нужен только startOffset  для возвращающей анимации
        //или работать с глобальной переменной, которая, когда обе анимации вызванны, ставит стартовым значением то,
        //или startOffset берется из getCurrentPlayTime() - prevView.getStartOffset (+startOffset?)
        //может ли работать предыдущая анимация, когда в новой проигран старт, но есть стартоффсет, вроде да!




        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(side){
                    side=false;
                    duration = 3000l;


                    //у первой toleft другой currentTime
                    if(valueAnimator1ViewToLeft!=null&&valueAnimator1ViewToLeft.isRunning()) {
                        duration = valueAnimator1ViewToLeft.getCurrentPlayTime();
                        valueAnimator1ViewToLeft.cancel();
                        valueAnimator1ViewToRight = ValueAnimator.ofFloat((Float) valueAnimator1ViewToLeft.getAnimatedValue(), 200f);
                    }else {
                        valueAnimator1ViewToRight = ValueAnimator.ofFloat(0f,200f);
                        duration = 3000l;
                    }

                    if(valueAnimator2ViewToLeft!=null&&valueAnimator2ViewToLeft.isStarted()){
                        //   не может быть cancel, пока не начнется сама анимация, а она не начнется пока startOffset
                        // valueAnimator2ViewToLeft.cancel();
                        valueAnimator2ViewToRight = ValueAnimator.ofFloat((Float) valueAnimator1ViewToLeft.getAnimatedValue(), 200f);
                    }else {
                        valueAnimator2ViewToRight = ValueAnimator.ofFloat(0f,200f);
                    }

                    valueAnimator1ViewToRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv1.setTranslationX((Float) valueAnimator1ViewToRight.getAnimatedValue());
                        }
                    });

                    valueAnimator2ViewToRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv2.setTranslationX((Float) valueAnimator2ViewToRight.getAnimatedValue());
                        }
                    });

                    //нужен только startOffset  для возвращающей анимации
                    //или работать с глобальной переменной, которая, когда обе анимации вызванны, ставит стартовым значением
                    //может ли работать предыдущая анимация, когда в новой проигран старт, но есть стартоффсет, вроде да!

                    valueAnimator1ViewToRight.setInterpolator(null);
                    valueAnimator2ViewToRight.setInterpolator(null);

                    //Toast.makeText(MainActivity.this, "dur=" + duration, Toast.LENGTH_SHORT).show();



                    //Боже, а ведь это еще не два onclicklistener-а у двух view

                    ////////////ПРОСТО ПОМЕНЯТЬ  DURATION ,У ПРЕРВАННОЙ 2R,ЧТОБ ОН НЕ УСПЕВАЛ ЗАКАНЧИВАТЬ СВОЮ СТАРУЮ АНИМАЦИЮ

                    // это все не работает, потому что нужно слушатели вообзе куда то убрать
                    if(!isRightListenerSet) {
                        isRightListenerSet = true;
                        valueAnimator2ViewToRight.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                if (valueAnimator2ViewToLeft != null && valueAnimator2ViewToLeft.isStarted()) {
                                    valueAnimator2ViewToLeft.cancel();
                                }
                            }
                        });
                    }

                    valueAnimator1ViewToRight.setDuration(duration);
                    valueAnimator2ViewToRight.setDuration(duration);
                    valueAnimator2ViewToRight.setStartDelay(400);
                    valueAnimator1ViewToRight.start();
                    valueAnimator2ViewToRight.start();


                }else {
                    side=true;
                    duration = 3000l;

                    if(valueAnimator1ViewToRight!=null&&valueAnimator1ViewToRight.isRunning()) {
                        duration = valueAnimator1ViewToRight.getCurrentPlayTime();
                        valueAnimator1ViewToRight.cancel();
                        valueAnimator1ViewToLeft = ValueAnimator.ofFloat((Float) valueAnimator1ViewToRight.getAnimatedValue(), 0f);
                    }else {
                        valueAnimator1ViewToLeft = ValueAnimator.ofFloat(200f,0f);
                    }



                    // Это все происходит потому что первая анимация вызвана,
                    // она играет, а вторая вызвана, затем проиграла, а первая не завершена
                    // и продолжает играть с позиции, которая расчитана в момент,
                    // когда первая анимация закончила играть и освободила view

                    // startListener когда задержанная анимация начинает двигатся, стартует следующая, имеющая такую же задержку




                    if(valueAnimator2ViewToRight!=null&&valueAnimator2ViewToRight.isStarted()&&!valueAnimator2ViewToRight.isRunning()){
                        //   не может быть cancel, пока не начнется сама анимация, а она не начнется пока startOffset
                        valueAnimator2ViewToRight.cancel();
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat((Float) valueAnimator1ViewToRight.getAnimatedValue(), 0f);
                        //Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                    }else if(valueAnimator2ViewToRight!=null&&valueAnimator2ViewToRight.isStarted()&&valueAnimator2ViewToRight.isRunning()) {
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat((Float) valueAnimator1ViewToRight.getAnimatedValue(), 0f);
                        //Toast.makeText(MainActivity.this, "no cancel", Toast.LENGTH_SHORT).show();
                    }else{
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat(200f,0f);
                        //Toast.makeText(MainActivity.this, "new anim", Toast.LENGTH_SHORT).show();
                    }


                    /*
                    // решает проблему 1
                    if(valueAnimator2ViewToRight!=null&&valueAnimator2ViewToRight.isStarted()){
                        //   не может быть cancel, пока не начнется сама анимация, а она не начнется пока startOffset
                        // valueAnimator2ViewToLeft.cancel();
                        Toast.makeText(MainActivity.this, "startted", Toast.LENGTH_SHORT).show();
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat((Float) valueAnimator1ViewToRight.getAnimatedValue(), 0f);
                    }else {
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat(200f,0f);
                    }*/

                    // эта определяет разницу между начатой и идущей анимацией
                    // в зависимости от того, isRunning или isStarted можно вызывать cancel или использовать конструкцию из решения проблемы 1
                    /*if(valueAnimator2ViewToRight!=null&&valueAnimator2ViewToRight.isRunning()){
                        metadata.setText("running");
                        tv2.clearAnimation();
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat((Float) valueAnimator1ViewToRight.getAnimatedValue(), 0f);
                    }else if(valueAnimator2ViewToRight!=null&&valueAnimator2ViewToRight.isStarted()&&!valueAnimator2ViewToRight.isRunning()){
                        metadata.setText("started, not running");
                        valueAnimator2ViewToRight.cancel();
                    }else {
                        metadata.setText("not started, not running");
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat(200f,0f);
                    }

                    /*if(valueAnimator2ViewToRight!=null&&valueAnimator2ViewToRight.isStarted()){
                        //  valueAnimator2ViewToRight.cancel();
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat((Float) valueAnimator1ViewToRight.getAnimatedValue(), 0f);
                    }else {
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat(200f,0f);
                    }*/


                    valueAnimator1ViewToLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv1.setTranslationX((Float) valueAnimator1ViewToLeft.getAnimatedValue());
                        }
                    });

                    valueAnimator2ViewToLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv2.setTranslationX((Float) valueAnimator2ViewToLeft.getAnimatedValue());
                        }
                    });

                    /*valueAnimator2ViewToLeft.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            //когда будет начинатся анимацтя налево,анимацтя направо отмемняется здесь
                            Log.i("ANIMATION " , "START");
                            if(valueAnimator2ViewToRight!=null&&valueAnimator2ViewToRight.isStarted()){
                                valueAnimator2ViewToRight.cancel();
                            }

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Log.i("ANIMATION " , "END");
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });*/
                    if(!isLeftListenerSet) {
                        isLeftListenerSet=true;
                        valueAnimator2ViewToLeft.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                if (valueAnimator2ViewToRight != null && valueAnimator2ViewToRight.isStarted()) {
                                    valueAnimator2ViewToRight.cancel();
                                }
                            }
                        });
                    }

                    // это почему то не работает
                    //valueAnimator2ViewToRight.setDuration(800).cancel();

                    valueAnimator1ViewToLeft.setInterpolator(null);
                    valueAnimator2ViewToLeft.setInterpolator(null);

                    valueAnimator1ViewToLeft.setDuration(duration);
                    valueAnimator2ViewToLeft.setDuration(duration);
                            //есть три проблемы-
                            //1. при старте нижняя не точно повторяет траектории верхней
                            //2. продолжается/начинается уже не требуемая анимация
                            //3.
                            //DURATION должен быть разным(?), если посередине прервалась анимация, то
                            // у v1 одно значение, у v2 другое в одно и то же время
                            // может получится сделать с интерполятором
                            // по идее это уберет ситуацию,
                            // когда срабатывает сначала l-r,а затем r-l анимация,
                            // хотя последней вызывалась l-r
                            // как минимум переменная delay зависит от duration
                            // если duration больше delay,происходит эта ошибка
                            // если анимация в диапазоне 0 - 400, - все ок
                            // если анимация в диапазоне 400 - 750, - сдвиг
                            // если анимация в диапазоне 750 - ___ ,  - все ок
                    valueAnimator2ViewToLeft.setStartDelay(400);
                    valueAnimator1ViewToLeft.start();
                    valueAnimator2ViewToLeft.start();

                }

            }
        };

        tv1.setOnClickListener(onClickListener);


    }

    public void xAxisViewsRowAnimation(float transitionValue, View view){

    }


    public  void chechAnimatedValue(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /*if(animator1_1 !=null){f1 =(Float) animator1_1.getAnimatedValue();}
                    if(animator2_1!=null){f2 = (Float) animator2_1.getAnimatedValue();}
                    if(metadata!=null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                metadata.setText("1_animatedValue = "+ String.valueOf(f1) + "\n"
                                        + "2_animatedValue = "+ String.valueOf(f2));
                            }
                        });
                    }*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(metadata!=null&&tv1!=null&&tv2!=null){
                                metadata.setText(String.valueOf(tv1.getX()) + "\n" + tv2.getX()+"         "+" side="+side);
                            }
                            if(duration!=0){
                                metadata.setText(metadata.getText() + String.valueOf(duration));
                            }

                        }
                    });



                }
            }
        }).start();
    }




}

 /*
                    ______________Рабочая версия анимации одной view_______________

  if(side){
                    side=false;

                                    //обе проверки нужны
                    if(valueAnimator1ViewToLeft!=null&&valueAnimator1ViewToLeft.isRunning()){
                        valueAnimator1ViewToLeft.cancel(); // || pause();

                        valueAnimator1ViewToRight = ValueAnimator.ofFloat((Float) valueAnimator1ViewToLeft.getAnimatedValue(),200f);
                        //valueAnimator1ViewToRight = ValueAnimator.ofFloat((Float) valueAnimator1ViewToRight.getAnimatedValue());
                    }else {
                        valueAnimator1ViewToRight = ValueAnimator.ofFloat(0f,200f);
                    }

                    valueAnimator1ViewToRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv1.setTranslationX((Float) valueAnimator1ViewToRight.getAnimatedValue());
                        }
                    });

                    valueAnimator1ViewToRight.start();



                }else {
                    side=true;

                    if(valueAnimator1ViewToRight!=null&&valueAnimator1ViewToRight.isRunning()){
                        valueAnimator1ViewToRight.cancel();

                        valueAnimator1ViewToLeft = ValueAnimator.ofFloat((Float) valueAnimator1ViewToRight.getAnimatedValue(),0f);

                    }else {
                        valueAnimator1ViewToLeft = ValueAnimator.ofFloat(200f, 0f);
                    }

                    valueAnimator1ViewToLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv1.setTranslationX((Float) valueAnimator1ViewToLeft.getAnimatedValue());
                        }
                    });

                    valueAnimator1ViewToLeft.start();

                }

            }

            ____________________________________________v2.0__________________

             if(side){
                    side=false;

                    if(valueAnimator1ViewToLeft!=null&&valueAnimator1ViewToLeft.isRunning()) {
                        valueAnimator1ViewToLeft.cancel();
                        valueAnimator1ViewToRight = ValueAnimator.ofFloat((Float) valueAnimator1ViewToLeft.getAnimatedValue(), 200f);
                    }else {
                        valueAnimator1ViewToRight = ValueAnimator.ofFloat(0f,200f);
                    }

                    if(valueAnimator2ViewToLeft!=null&&valueAnimator2ViewToLeft.isRunning()){
                        valueAnimator2ViewToLeft.cancel();
                        valueAnimator2ViewToRight = ValueAnimator.ofFloat((Float) valueAnimator2ViewToLeft.getAnimatedValue(), 200f);
                    }else {
                        valueAnimator2ViewToRight = ValueAnimator.ofFloat(0f,200f);
                    }

                    valueAnimator1ViewToRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv1.setTranslationX((Float) valueAnimator1ViewToRight.getAnimatedValue());
                        }
                    });

                    valueAnimator2ViewToRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv2.setTranslationX((Float) valueAnimator2ViewToRight.getAnimatedValue());
                        }
                    });

                    //нужен только startOffset  для возвращающей анимации
                    //или работать с глобальной переменной, которая, когда обе анимации вызванны, ставит стартовым значением
                    //может ли работать предыдущая анимация, когда в новой проигран старт, но есть стартоффсет, вроде да!

                    valueAnimator2ViewToRight.setStartDelay(60);
                    valueAnimator1ViewToRight.start();
                    valueAnimator2ViewToRight.start();



                }else {
                    side=true;

                    if(valueAnimator1ViewToRight!=null&&valueAnimator1ViewToRight.isRunning()) {
                        valueAnimator1ViewToRight.cancel();
                        valueAnimator1ViewToLeft = ValueAnimator.ofFloat((Float) valueAnimator1ViewToRight.getAnimatedValue(), 0f);
                    }else {
                        valueAnimator1ViewToLeft = ValueAnimator.ofFloat(200f,0f);
                    }

                    if(valueAnimator2ViewToRight!=null&&valueAnimator2ViewToRight.isRunning()){
                        valueAnimator2ViewToRight.cancel();
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat((Float) valueAnimator2ViewToRight.getAnimatedValue(), 0f);
                    }else {
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat(200f,0f);
                    }

                    valueAnimator1ViewToLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv1.setTranslationX((Float) valueAnimator1ViewToLeft.getAnimatedValue());
                        }
                    });

                    valueAnimator2ViewToLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv2.setTranslationX((Float) valueAnimator2ViewToLeft.getAnimatedValue());
                        }
                    });
                    valueAnimator2ViewToLeft.setStartDelay(60);
                    valueAnimator1ViewToLeft.start();
                    valueAnimator2ViewToLeft.start();

                }

                ______________________здесь я пробую и пробую______

                 if(side){
                    side=false;

                    if(valueAnimator1ViewToLeft!=null&&valueAnimator1ViewToLeft.isRunning()) {

                        f2 = (Float)valueAnimator1ViewToLeft.getAnimatedValue();
                        valueAnimator1ViewToLeft.cancel();
                        valueAnimator1ViewToRight = ValueAnimator.ofFloat((Float) valueAnimator1ViewToLeft.getAnimatedValue(), 200f);
                    }else {
                        valueAnimator1ViewToRight = ValueAnimator.ofFloat(0f,200f);
                    }

                    if(valueAnimator2ViewToLeft!=null&&valueAnimator2ViewToLeft.isRunning()){
                        //valueAnimator2ViewToLeft.cancel();
                        valueAnimator2ViewToRight = ValueAnimator.ofFloat(f2, 200f);
                    }else {
                        valueAnimator2ViewToRight = ValueAnimator.ofFloat(f2,200f);
                    }

                    valueAnimator1ViewToRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv1.setTranslationX((Float) valueAnimator1ViewToRight.getAnimatedValue());
                        }
                    });

                    valueAnimator2ViewToRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv2.setTranslationX((Float) valueAnimator2ViewToRight.getAnimatedValue());
                        }
                    });

                    //нужен только startOffset  для возвращающей анимации
                    //или работать с глобальной переменной, которая, когда обе анимации вызванны, ставит стартовым значением
                    //может ли работать предыдущая анимация, когда в новой проигран старт, но есть стартоффсет, вроде да!
                    valueAnimator1ViewToRight.setDuration(2000);
                    valueAnimator2ViewToRight.setDuration(2000);

                    valueAnimator1ViewToRight.setInterpolator(null);
                    valueAnimator2ViewToRight.setInterpolator(null);

                    valueAnimator2ViewToRight.setStartDelay(600);
                    valueAnimator1ViewToRight.start();
                    valueAnimator2ViewToRight.start();
                    chechAnimatedValue();



                }else {
                    side=true;
                    Toast.makeText(MainActivity.this, "handled part", Toast.LENGTH_SHORT).show();
                    if(valueAnimator1ViewToRight!=null&&valueAnimator1ViewToRight.isRunning()) {
                        f1 = (Float) valueAnimator1ViewToRight.getAnimatedValue();
                        time = valueAnimator1ViewToRight.getCurrentPlayTime();
                        valueAnimator1ViewToRight.cancel();
                        //  записываем значение, которое потом отдатся аниматору второй вью

                        valueAnimator1ViewToLeft = ValueAnimator.ofFloat((Float) valueAnimator1ViewToRight.getAnimatedValue(), 0f);
                    }else {
                        valueAnimator1ViewToLeft = ValueAnimator.ofFloat(200f,0f);
                    }

                    if(valueAnimator2ViewToRight!=null&&valueAnimator2ViewToRight.isRunning()){
                        //valueAnimator2ViewToRight.cancel();
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat(f1, 0f);
                    }else {
                        valueAnimator2ViewToLeft = ValueAnimator.ofFloat(f1,0f);
                    }

                    valueAnimator1ViewToLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv1.setTranslationX((Float) valueAnimator1ViewToLeft.getAnimatedValue());
                        }
                    });

                    valueAnimator2ViewToLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tv2.setTranslationX((Float) valueAnimator2ViewToLeft.getAnimatedValue());
                        }
                    });
                    valueAnimator1ViewToLeft.setDuration(2000);
                    valueAnimator2ViewToLeft.setDuration(2000);

                    valueAnimator1ViewToLeft.setInterpolator(null);
                    valueAnimator2ViewToLeft.setInterpolator(null);

                    //параметр from = расчеты с currentPlayTime + то время, которое будет играть анимация первой view,
                    // до старта анимации второй view(тоесть ее startOffset)
                    valueAnimator2ViewToLeft.setStartDelay(600);
                    valueAnimator1ViewToLeft.start();
                    valueAnimator2ViewToLeft.start();

                }

 */


