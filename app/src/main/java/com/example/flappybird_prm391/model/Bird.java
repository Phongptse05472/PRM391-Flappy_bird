package com.example.flappybird_prm391.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.flappybird_prm391.R;

public class Bird {

    private int width;
    private int height;
    private int currentFrame = 1;
    private int wingSpeed;
    private int velocity;
    private int x, y;
    private Bitmap[] frame = new Bitmap[3];

    public Bird(Resources resources, float scale) {
        Bitmap raw = BitmapFactory.decodeResource(resources, R.drawable.yellowbird_downflap);
        frame[0] = Bitmap.createScaledBitmap(raw, Math.round(raw.getWidth()*scale), Math.round(raw.getHeight()*scale), true);
        raw = BitmapFactory.decodeResource(resources, R.drawable.yellowbird_midflap);
        frame[1] = Bitmap.createScaledBitmap(raw, Math.round(raw.getWidth()*scale), Math.round(raw.getHeight()*scale), true);
        raw = BitmapFactory.decodeResource(resources, R.drawable.yellowbird_upflap);
        frame[2] = Bitmap.createScaledBitmap(raw, Math.round(raw.getWidth()*scale), Math.round(raw.getHeight()*scale), true);
        width = frame[0].getWidth();
        height = frame[0].getHeight();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
        // Update bird frame by its velocity
        if (velocity < 15 && velocity > -15){
            currentFrame = 1;
        } else if(velocity >= 15) {
            currentFrame = 0;
        } else {
            currentFrame = 2;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap[] getFrame() {
        return frame;
    }

    public void setFrame(Bitmap[] frame) {
        this.frame = frame;
    }

    public int getWingSpeed() {
        return wingSpeed;
    }

    public void setWingSpeed(int wingSpeed) {
        this.wingSpeed = wingSpeed;
    }
}
