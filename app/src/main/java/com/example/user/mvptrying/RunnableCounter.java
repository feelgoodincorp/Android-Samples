package com.example.user.mvptrying;

import android.util.Log;

/**
 * Created by User on 31.08.2018.
 */

public class RunnableCounter implements Runnable {
        private volatile boolean running = true;
        private volatile boolean paused = false;
        private final Object pauseLock = new Object();

        int counter = 0;

        //  TODO отдельно иттератор, отдельно обработчик запросов -  статический counter может менятся на 0, не влияя на иттераторный метод
        //  иттератор вызывает метод обработчика запросов(кидает (не!)число(,а url) в сервис, пока он работает - ждет(?но не больше некого времени?),
        //  а когда сервис закончил работать, снова запускается иттератор)
        //  вообще нужен сервис??????????(чтоб при ротации экрана загрузка продолжалась)
        //  оверрлоад wait-метода, для входящего значения(или его отсутствия)
        //  счетчик написать в сервисе? сервис виснет в wait, если долго не используется,но его наличие -
        //  - это ресурсы | сервис занимает больше чем поток в wait? сервис наследуется от thread и существует в объекте всего приложения
        //  поток счетчик вызывает сервис
        //  учесть, что сервис может работать(отложеный запуск из-за нехватки ресурсов) дольше времени, которое иттератор ждет(wait) | (cначала старт сервиса, затем старт
        //  TODO  (проверить,синхронно ли обрабатывается старт сервиса и иттератора))
        //  потоком нельзя управлять пока не вызовется метод run


        @Override
        public void run () {
            while (running){
                synchronized (pauseLock) {
                    Log.i("CYCLE","IS RUNS");
                    try {
                        pauseLock.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!running) { // may have changed while waiting to
                        // synchronize on pauseLock
                        break;
                    }
                    if (paused) {
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException ex) {
                            break;
                        }
                        if (!running) { // running might have changed since we paused
                            break;
                        }
                    }
                }
                // Your code here
                MyModel.getModelInstance().downloadFile(MyApplication.getContext());
            }
        }

        public void stop() {
            running = false;
            // you might also want to interrupt() the Thread that is
            // running this Runnable, too, or perhaps call:
            resume();
            // to unblock
        }

        public void pause() {
            // you may want to throw an IllegalStateException if !running
            paused = true;
            Log.i("CYCLE","IS PAUSED");
        }

        public void resume() {
            synchronized (pauseLock) {
                paused = false;
                pauseLock.notifyAll(); // Unblocks thread
                Log.i("CYCLE","IS RESUMED");
            }
        }

    };