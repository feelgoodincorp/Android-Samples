package com.example.user.testanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;



public class AnimationHandler {

    Float animatedValueFrom = 0f;
    Float animatedValueTo   = 200f;

    Float firstLevelRight = 200f;
    Float firstLevelLeft = 0f;

    long curTime;


    long duration = 1500l;

    ValueAnimator valueAnimatorView1Right = ValueAnimator.ofFloat(firstLevelLeft,firstLevelRight);
    ValueAnimator valueAnimatorView1Left = ValueAnimator.ofFloat(firstLevelRight,firstLevelLeft);

    ValueAnimator valueAnimatorView2Right = ValueAnimator.ofFloat(firstLevelLeft,firstLevelRight);
    ValueAnimator valueAnimatorView2Left = ValueAnimator.ofFloat(firstLevelRight,firstLevelLeft);


    public void initAnim(final View view, final View view2){
        valueAnimatorView1Right.setDuration(duration);
        valueAnimatorView1Left.setDuration(duration);
        valueAnimatorView2Right.setDuration(duration);
        valueAnimatorView2Left.setDuration(duration);
/*
        valueAnimatorView1Right.setInterpolator(null);
        valueAnimatorView1Left.setInterpolator(null);
        valueAnimatorView2Right.setInterpolator(null);
        valueAnimatorView2Left.setInterpolator(null);
*/



        valueAnimatorView1Right.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                // started или running?
                // все таки можно раотать с getCurrentTime() + интерполятор поддерживается + не пересоздавать новых обьектов?
                // аниматор другого уровня берет эту вью и анимирует в другое место,
                // стартовая позииция зависит от current time текущей, если такая есть
                curTime =  valueAnimatorView1Left.getCurrentPlayTime();
                if (valueAnimatorView1Left != null && valueAnimatorView1Left.isStarted()) {
                    valueAnimatorView1Right.setCurrentPlayTime(duration - curTime);
                    //Log.i("Left View1 animation " , "CANCELED");

                    valueAnimatorView1Left.cancel();
                    Log.i("Right View1 animation " , "STARTED");
                }

                // с каждым новым вызовом этого слушателя, он срабатывает все чаще(см. логи) на две единицы
                // (уже нет,было из-за слушателей и пересоздания их экземпляров)
            }


            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Right View1 animation " , "CANCELED");
            }
        });


        // отменить анимацию влево, если стартовала анимация вправо
        valueAnimatorView1Left.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                /*if (valueAnimatorView1Right != null && valueAnimatorView1Right.isRunning()) {
                    valueAnimatorView1Right.cancel();
                }*/

                curTime =  valueAnimatorView1Right.getCurrentPlayTime();
                if (valueAnimatorView1Right != null && valueAnimatorView1Right.isStarted()) {
                    valueAnimatorView1Left.setCurrentPlayTime(duration - curTime);
                    //Log.i("Right View1 animation " , "CANCELED");

                    valueAnimatorView1Right.cancel();
                    Log.i("Left View1 animation " , "STARTED");
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Left View1 animation " , "CANCELED");
            }
        });


        valueAnimatorView1Right.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //view.setTranslationX((Float) valueAnimatorView1Right.getAnimatedValue());
                if(!valueAnimatorView1Left.isRunning()) {
                    view.setTranslationX((Float) valueAnimatorView1Right.getAnimatedValue());
                }else {
                    Log.i("OH MY","GOSHHHHHHHHHHHHHHHH1");
                    //слушателей только один
                }
            }
        });


        valueAnimatorView1Left.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //view.setTranslationX((Float) valueAnimatorView1Left.getAnimatedValue());
                if(!valueAnimatorView1Right.isRunning()) {
                    view.setTranslationX((Float) valueAnimatorView1Left.getAnimatedValue());
                }else {
                    Log.i("OH MY","GOSHHHHHHHHHHHHHHHH2");
                }
            }
        });

        //____________________________________________Right View 2 Anim____________________________________________

        valueAnimatorView2Right.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {

                curTime =  valueAnimatorView2Left.getCurrentPlayTime();
                if (valueAnimatorView2Left != null && valueAnimatorView2Left.isStarted()&&!valueAnimatorView2Left.isStarted()) {
                    valueAnimatorView2Right.setCurrentPlayTime(duration - curTime);
                    //Log.i("Left View2 animation " , "CANCELED");

                    valueAnimatorView2Left.cancel();
                    Log.i("Right View2 animation " , "STARTED");

                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Right View2 animation " , "CANCELED");
            }
        });


        valueAnimatorView2Left.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {

                curTime =  valueAnimatorView2Right.getCurrentPlayTime();
                if (valueAnimatorView2Right != null && valueAnimatorView2Right.isStarted()&&!valueAnimatorView2Right.isStarted()) {
                    valueAnimatorView2Left.setCurrentPlayTime(duration - curTime);
                    //Log.i("Right View2 animation " , "CANCELED");

                    valueAnimatorView2Right.cancel();
                    Log.i("Left View2 animation " , "STARTED");
                }else {

                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Left View2 animation " , "CANCELED");
            }
        });

        valueAnimatorView2Right.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //view2.setTranslationX((Float) valueAnimatorView2Right.getAnimatedValue());
                if(valueAnimatorView2Right!=null&&valueAnimatorView2Right.isStarted()) {
                    view2.setTranslationX((Float) valueAnimatorView2Right.getAnimatedValue());
                }else {
                    Log.i("OH MY","GOSHHHHHHHHHHHHHHHH3");
                }
            }
        });


        valueAnimatorView2Left.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //view2.setTranslationX((Float) valueAnimatorView2Left.getAnimatedValue());
                if(valueAnimatorView2Left!=null&&valueAnimatorView2Left.isStarted()) {
                    view2.setTranslationX((Float) valueAnimatorView2Left.getAnimatedValue());
                }else {
                    Log.i("OH MY","GOSHHHHHHHHHHHHHHHH4");
                    //срабатывало во всей длительности противоположной анимации
                }
            }
        });

    }

    public void startViewAnimation(boolean side){

        if(side){
            Log.i("Right animations " , "start()");
            //valueAnimatorView1Right.setStartDelay(2000);
            valueAnimatorView1Right.start();
            valueAnimatorView2Right.setStartDelay(800);
            valueAnimatorView2Right.start();

            //тип не прошел еще один start+offset, а я уже новый делаю
        }else {
            Log.i("left animations " , "start()");
            //valueAnimatorView1Left.setStartDelay(2000);
            valueAnimatorView1Left.start();
            valueAnimatorView2Left.setStartDelay(800);
            valueAnimatorView2Left.start();
            //______________________________________________________________________________________________________
            //вот что вызывает ошибку!!!!два старта одной вьюхи!!!!!!!!!!!! (вызввают скорее всего конфликт updatelistener-ов)

            // если вовремя вызвать cancel,все работает
            /*valueAnimatorView2Left.cancel();

            valueAnimatorView2Right.start();*/

        }
       /* Log.i("ANIM CHECK ", "ISRUNNING");
        Log.i("ANIM CHECK 1R", String.valueOf(valueAnimatorView1Right.isRunning()));
        Log.i("ANIM CHECK 2R", String.valueOf(valueAnimatorView2Right.isRunning()));
        Log.i("ANIM CHECK 1L", String.valueOf(valueAnimatorView1Left.isRunning()));
        Log.i("ANIM CHECK 2L", String.valueOf(valueAnimatorView2Left.isRunning()));


        Log.i("ANIM CHECK ", "ISSTARTED");
        Log.i("ANIM CHECK 1R", String.valueOf(valueAnimatorView1Right.isStarted()));
        Log.i("ANIM CHECK 2R", String.valueOf(valueAnimatorView2Right.isStarted()));
        Log.i("ANIM CHECK 1L", String.valueOf(valueAnimatorView1Left.isStarted()));
        Log.i("ANIM CHECK 2L", String.valueOf(valueAnimatorView2Left.isStarted()));
        Log.i("animation method  ", "end of calling___________________________________________________");*/

    }

    //попробовать сетить currentPlayTime в cance для противоположной анимации
    // сейчас не работает, потому что анимация вправо началась,
    // тут же закончилась и началась влево, а во второй вью еще вправо не началась/закончилась
    // в логкете куча вызовов слушателя, как и в ситуации, когда у меня пересоздавался обьект и его слушатели и
    // они срабатывали по несколько раз за одно событие этих слушателей
	// еще не забыть разницу между isRunning и isStarted
    // еще учесть - не работает в радиусе startOffset
    // это может быть таким конфликтом, когда одна анимация идет влево,другая вправо, все стоит на месте и стек переполняется
    //  либо стартуют две анимации в одну сторону
    // слушатель на старты вторых анимаций
    // нельзя отменить не стартовавшую анимацию (стартует когда срабатывает start-слушатель)


    //___старые записи_____

    //два cancel - один заканчивает когда посередине начинается новая анимация, второй, когда ...
    // может cancel нужен только один, если учесть что delayed анимация корректно работает?


    // работая с глобальными переменными from и to, можно делать одну переменную анимации на одну view,
    // и просто с повторным нажатием на view меняются параметры и стартует тот же аниматор


    // через delay срабатывает слушатель старта, который отменяет предыдущую анимацию
    // (или вообще существующие анимации, иначе может быть какаянибудь ошибка)

    // или методы стартуют в последовательности выбора или
    //нажатие(вызов новых анимаций)
    //проверка первого(выбранного) элемента из уровня на isRunning, cancel если true
    //
    // эта view стартует вправо? отменить влево, а
    // принцип 0-delay у первого элемента в том, что в методе setDelay динамически меняющаяся переменаая в момент нажатия
    //
    // проверка на текущиие анимации, если есть,
    // если над вью стартует новая анимация, старая cancel(), а startOffset-ы уже заранее расставлены
    // у всех есть listener-ы, они говорят, используется ли сейчас view для анимирования, или нет

    // отменить анимацию влево, когда стартует анимация выше в иерархии вьюх одного уровня(анимация первее этой)

    //если будут вызваны две анимации направо, только в разный промежуток времени, то это вызовет конфликт?
    // решить его можно будет тем же слушателем, - закрывать все существующие над view анимации

    // можно поменять cancel на end, чтоб решить проблему с stackoverflow, но это не точно

    // отменить анимацию влево, если стартовала анимация вправо
}
