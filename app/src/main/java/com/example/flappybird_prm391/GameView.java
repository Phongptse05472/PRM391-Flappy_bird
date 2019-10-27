package com.example.flappybird_prm391;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.example.flappybird_prm391.model.Bird;
import com.example.flappybird_prm391.model.Pipe;

import java.util.Random;

/**
 * Due to low performed switched to using SurfaceView instead
 */
@Deprecated
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
     * Bird object
     */
    Bird bird;

    /**
     * Pipe objects
     */
    Pipe[] topPipe, bottomPipe;

    /**
     * Offset thats define how much pipes can move up and down
     */
    private int minPipeOffset, maxPipeOffset;

    /**
     * Random for calculate pipe move up down value and some random resources to be loaded
     */
    Random random;

    /**
     * Game state
     */
    private boolean playing = false, gameover = false;

    /**
     * Device display
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
     * Screen refresh rate count in milisecond
     */
    private final int SCREEN_DELAY_MILIS = 20;

    /**
     * Game setting !!!
     */
    private final int GRAVITY = 3;
    // Careful when setting GAP value (must be < maxPipeOffset)
    private final int PIPE_GAP = 400;
    private final int PIPE_DISTANCE = 700;
    private final int PIPE_NUMBER = 2;
    private final int PIPE_VELOCITY = 6;

    /**
     * Initialize
     */
    public GameView(Context context) {
        super(context);
        // Initialize screen
        random = new Random();
        setBackgroundResource(random.nextBoolean() ? R.drawable.background_day : R.drawable.background_night);
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        // Initialize pipe objects
        // Resource images size might not compatible with users divice so we need to calculate scale for resizing
        float pipeScale = getPipeResizeScale();
        topPipe = new Pipe[PIPE_NUMBER];
        bottomPipe = new Pipe[PIPE_NUMBER];
        for(int i = 0; i < PIPE_NUMBER; i++){
            topPipe[i] = new Pipe(getResources(), true, pipeScale);
            bottomPipe[i] = new Pipe(getResources(), false, pipeScale);
            topPipe[i].setX(screenWidth + i*PIPE_DISTANCE);
            bottomPipe[i].setX(screenWidth + i*PIPE_DISTANCE);
        }
        minPipeOffset = screenHeight/4;
        maxPipeOffset = screenHeight/2;
        // Initialize bird object
        // Resource images size might not compatible with users divice so we need to calculate scale for resizing
        float birdScale = getBirdResizeScale();
        bird = new Bird(getResources(), birdScale);
        bird.setX(screenWidth/2 - bird.getWidth()/2);
        bird.setY(screenHeight/2 - bird.getHeight()/2);
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
        if(playing){
            // Calculate birds new position
            bird.setVelocity(bird.getVelocity() - GRAVITY);
            bird.setY(bird.getY() - bird.getVelocity());
            //
            for(int i = 0; i < PIPE_NUMBER; i++) {
                topPipe[i].setX(topPipe[i].getX() - PIPE_VELOCITY);
                // if pipe moved out the screen so put it back to started position
                // reset its Y coordinate too
                if(topPipe[i].getX() < -topPipe[i].getWidth()){
                    System.out.println(topPipe[i].getX());
                    topPipe[i].setX(topPipe[i].getX() + (PIPE_NUMBER * PIPE_DISTANCE));
                    System.out.println(topPipe[i].getX());
                    topPipe[i].setY(minPipeOffset + random.nextInt(maxPipeOffset - minPipeOffset + 1));
                }
            }
            // Die if bird touch the ground
            if(bird.getY() > screenHeight - bird.getHeight() - 100) {
                playing = false;
            }
        }
        // Draw bird
        canvas.drawBitmap(bird.getFrame()[bird.getCurrentFrame()],
                bird.getX(),
                bird.getY(),
                null);
        // Draw pipes
        for(int i = 0; i < PIPE_NUMBER; i++){
            canvas.drawBitmap(topPipe[i].getFrame(), topPipe[i].getX(), topPipe[i].getY() - topPipe[i].getHeight(), null);
            canvas.drawBitmap(bottomPipe[i].getFrame(), topPipe[i].getX(), topPipe[i].getY() + PIPE_GAP, null);
        }
        handler.postDelayed(runnable, SCREEN_DELAY_MILIS);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        // Catch on tap screen
        if(action == MotionEvent.ACTION_DOWN){
            // Keeps the bird not flying out the screen
            if(bird.getY() > bird.getHeight() + 150) {
                bird.setVelocity(35);
            }
            if(!playing){
                playing = true;
                handler.postDelayed(runnable, SCREEN_DELAY_MILIS);
            }
        }

        // No callback parent class
        return true;
    }

    /**
     * Calculate pipe resize scale number based on pipe height and screen height (pHeight = 4/5 sHeight)
     * @return scale
     */
    private float getPipeResizeScale(){
        int rawPipeHeight = BitmapFactory.decodeResource(getResources(), R.drawable.pipe_green_bottom).getHeight();
        return (screenHeight/1.25f) / rawPipeHeight;
    }

    /**
     * Calculate bird resize scale number based on pipe height and screen height (pHeight = 1/20 sHeight)
     * @return scale
     */
    private float getBirdResizeScale(){
        int rawBirdHeight = BitmapFactory.decodeResource(getResources(), R.drawable.yellowbird_midflap).getHeight();
        return (screenHeight/20.0f) / rawBirdHeight;
    }
}
