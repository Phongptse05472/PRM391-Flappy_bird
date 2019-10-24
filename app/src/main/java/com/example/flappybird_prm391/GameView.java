package com.example.flappybird_prm391;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;
import java.util.stream.IntStream;

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

    private int velocity = 0;

    private int birdX, birdY;

    //
    Bitmap topPipe, bottomPipe;

    private int minPipeOffset, maxPipeOffset;

    private int[] pipeX;

    private int[] topPipeY;

    Random random;

    /**
     *
     */
    private boolean playing = false;

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
    private final int REFRESH_RATE = 20;

    /**
     * Game setting !!!
     */
    private final int GRAVITY = 3;
    private final int PIPE_GAP = 400;
    private final int PIPE_DISTANCE = 600;
    private final int PIPE_NUMBER = 2;
    private final int PIPE_VELOCITY = 6;

    /**
     * Initialize
     */
    public GameView(Context context) {
        super(context);
        // Initialize screen
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background_day);
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        rect = new Rect(0,0, screenWidth, screenHeight);
        // Initialize bird params
        bird = new Bitmap[3];
        bird[0] = BitmapFactory.decodeResource(getResources(), R.drawable.yellowbird_downflap);
        bird[1] = BitmapFactory.decodeResource(getResources(), R.drawable.yellowbird_midflap);
        bird[2] = BitmapFactory.decodeResource(getResources(), R.drawable.yellowbird_upflap);
        birdWidth = bird[0].getWidth();
        birdHeight = bird[0].getHeight();
        birdX = screenWidth/2 - birdWidth/2;
        birdY = screenHeight/2 - birdHeight/2;
        // Initialize pipe params
        bottomPipe = BitmapFactory.decodeResource(getResources(), R.drawable.pipe_green);
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        topPipe = Bitmap.createBitmap(bottomPipe, 0, 0, bottomPipe.getWidth(), bottomPipe.getHeight(), matrix, true);
        minPipeOffset = PIPE_GAP/2;
        maxPipeOffset = screenHeight - minPipeOffset - PIPE_GAP;
        random = new Random();
        pipeX = new int[PIPE_NUMBER];
        topPipeY = new int[PIPE_NUMBER];
        for(int i = 0; i < PIPE_NUMBER; i++){
            pipeX[i] = screenWidth + i*PIPE_DISTANCE;
        }

        // Draw the screen
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Screen redraw
                invalidate();
            }
        };
   }

    /**
     * Screen drawing
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw background
        canvas.drawBitmap(background, null, rect, null);
        if(playing){
            // Calculate birds new position
            velocity -= GRAVITY;
            birdY -= velocity;
            // Update bird frame by its velocity
            if (velocity < 20 && velocity > -20){
                birdFrame = 1;
            } else if(velocity >= 20) {
                birdFrame = 0;
            } else {
                birdFrame = 2;
            }
            //
            for(int i = 0; i < PIPE_NUMBER; i++) {
                pipeX[i] -= PIPE_VELOCITY;
                if(pipeX[i] < -topPipe.getWidth()){
                    pipeX[i] += PIPE_NUMBER * PIPE_DISTANCE;
                    topPipeY[i] = minPipeOffset + random.nextInt(maxPipeOffset - minPipeOffset + 1);
                }
            }
            // Die if bird touch the ground
            if(birdY > screenHeight - birdHeight - 100) {
                playing = false;
            }
    //        System.out.println(birdY);
        }
        // Draw bird
        canvas.drawBitmap(bird[birdFrame],
                birdX,
                birdY,
                null);
        // Draw pipes
        for(int i = 0; i < PIPE_NUMBER; i++){
            canvas.drawBitmap(topPipe, pipeX[i], topPipeY[i] - topPipe.getHeight(), null);
            canvas.drawBitmap(bottomPipe, pipeX[i], topPipeY[i] + PIPE_GAP, null);
        }
        handler.postDelayed(runnable, REFRESH_RATE);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        // Catch on tap screen
        if(action == MotionEvent.ACTION_DOWN){
            // Keeps the bird not flying out the screen
            System.out.println(birdY);
            System.out.println(birdHeight);
            if(birdY > birdHeight + 150) {
                velocity = 35;
            }
            if(!playing){
                playing = true;
                handler.postDelayed(runnable, REFRESH_RATE);
            }
        }

        // No callback parent class
        return true;
    }
}
