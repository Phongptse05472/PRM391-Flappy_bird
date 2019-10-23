package com.example.flappybird_prm391;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class MainGame extends Activity {
    GameView gameView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }
}
