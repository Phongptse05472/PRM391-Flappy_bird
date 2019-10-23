package com.example.flappybird_prm391;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.View;

import java.util.logging.LogRecord;

/**\
 * gameview
 */
public class GameView extends View {

    /**
     * Handler for the runnable
     */
    Handler handler;

    /**
     * Screen refresh thread
     */
    Runnable runnable;

    /**
     * Background image resource
     */
    Bitmap background;

    /**
     * Bird image resource
     */
    Bitmap[] bird;

    private int birdWidth;

    private int birdHeight;

    /**
     * Current bird frame index
     */
    private int birdFrame = 1;

    /**
     * Bird wing state
     */
    private boolean flyUp = true;

    /**
     *
     */
    Display display;

    /**
     * Coordinate of screen bottom
     */
    int screenHeight;
    /**
     * Coordinate of screen right
     */
    int screenWidth;

    /**
     * Playzone
     */
    Rect rect;

    /**
     * Screen refresh rate count in milisecond
     */
    private final int REFRESH_RATE = 25;


    /**
     * Initialize
     */
    public GameView(Context context) {
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Screen redraw
                invalidate();
            }
        };
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background_day);
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        rect = new Rect(0,0, screenWidth, screenHeight);
        bird = new Bitmap[3];
        bird[0] = BitmapFactory.decodeResource(getResources(), R.drawable.yellowbird_downflap);
        bird[1] = BitmapFactory.decodeResource(getResources(), R.drawable.yellowbird_midflap);
        bird[2] = BitmapFactory.decodeResource(getResources(), R.drawable.yellowbird_upflap);
        birdWidth = bird[0].getWidth();
        birdHeight = bird[0].getHeight();
   }

    /**
     * Screen drawing
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw background
        canvas.drawBitmap(background, null, rect, null);
        // Draw bird
        if(birdFrame == 2){
            flyUp = false;
        } else if (birdFrame == 0) {
            flyUp = true;
        }
        if (flyUp) {
            birdFrame++;
        } else {
            birdFrame--;
        }
        // Draw at center
        canvas.drawBitmap(bird[birdFrame],
                screenWidth/2 - birdWidth/2,
                screenHeight/2 - birdHeight/2,
                null);
        handler.postDelayed(runnable, REFRESH_RATE);
    }
}
