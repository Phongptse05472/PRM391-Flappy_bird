package com.example.flappybird_prm391.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.flappybird_prm391.R;

public class Pipe {

    private int x, y;
    private int width, height;
    private Bitmap frame;

    public Pipe(Resources resources, boolean topPipe, float scale) {
        Bitmap raw = BitmapFactory.decodeResource(resources, topPipe ? R.drawable.pipe_green_top : R.drawable.pipe_green_bottom);
        frame = Bitmap.createScaledBitmap(raw, Math.round(raw.getWidth()*scale), Math.round(raw.getHeight()*scale), true);
        width = frame.getWidth();
        height = frame.getHeight();
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

    public Bitmap getFrame() {
        return frame;
    }

    public void setFrame(Bitmap frame) {
        this.frame = frame;
    }
}
