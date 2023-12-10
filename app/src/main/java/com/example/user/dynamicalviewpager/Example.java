package com.example.user.dynamicalviewpager;

import android.util.Log;


public class Example implements Runnable {
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    int counter = 0;

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
                        pauseLock.wait(); // will cause this Thread to block until
                        // another thread calls pauseLock.notifyAll()
                        // Note that calling wait() will
                        // relinquish the synchronized lock that this
                        // thread holds on pauseLock so another thread
                        // can acquire the lock to call notifyAll()
                        // (link with explanation below this code)
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) { // running might have changed since we paused
                        break;
                    }
                }
            }
            // Your code here
            counter++;
            MainActivity.setCounter(String.valueOf(counter));
        }
    }

    public void stop() {
        running = false;
        // you might also want to interrupt() the Thread that is
        // running this Runnable, too, or perhaps call:
        //resume();
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

    public boolean isStarted(){
        return !paused;
    }

};