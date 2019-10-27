package com.example.flappybird_prm391;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Not using right now
 */
@Deprecated
public class GameThread extends Thread {

    SurfaceHolder surfaceHolder; //Surfaceholder object reference
    GameEngine gameEngine; // The game itself
    boolean isRunning; // Flag to detect whether the thread is running or not
    long startTime, loopTime; // Loop start time and loop duration
    private final int SCREEN_DELAY_MILIS = 20; // Screen refresh rate count in milisecond

    public GameThread(Context context, Resources resources, SurfaceHolder surfaceHolder){
        this.surfaceHolder = surfaceHolder;
        this.gameEngine = new GameEngine(context, resources);
        this.isRunning = true;
    }

    @Override
    public void run() {
        // Looping until the boolean is false
        while(isRunning){
            startTime = SystemClock.uptimeMillis();
            //locking the canvas
            Canvas canvas = surfaceHolder.lockCanvas(null);
            if(canvas != null){
                synchronized (surfaceHolder){
                    // Redraw the screen
                    gameEngine.update(canvas);
                    //unlocking the canvas
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            //loop time
            loopTime = SystemClock.uptimeMillis() - startTime;
            // Pausing here to make sure we update the right amount per second
            if(loopTime < SCREEN_DELAY_MILIS){
                try{
                    Thread.sleep(SCREEN_DELAY_MILIS - loopTime);
                }catch(InterruptedException e){
                    Log.e("Interrupted","Interrupted while sleeping");
                }
            }
        }
    }

    // Return whether the thread is running
    public boolean isRunning(){
        return isRunning;
    }

    // Sets the thread state, false = stopped, true = running
    public void setIsRunning(boolean state){
        isRunning = state;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }
}
