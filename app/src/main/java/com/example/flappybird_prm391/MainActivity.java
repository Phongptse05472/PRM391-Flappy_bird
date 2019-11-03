package com.example.flappybird_prm391;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity {

    int[] birdImageIds = {
            R.drawable.yellowbird_downflap,
            R.drawable.yellowbird_midflap,
            R.drawable.yellowbird_upflap
    };

    // Prevent duplicate call because of clicking too fast
    private boolean btnPlayClicked = false;
    private boolean btnScoreClicked = false;

    // Screen activity
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        // Btn play onClick event binding
        ImageView btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btnPlayClicked){
                    btnPlayClicked = true;
                    startGame(v);
                }
            }
        });
        //
        ImageView btnViewScore = findViewById(R.id.btnScore);
        btnViewScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btnScoreClicked){
                    btnScoreClicked = true;
                    MainActivity screen = (MainActivity) context;
                    Intent intent = new Intent(screen, ScoreboardActivity.class);
                    startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });
        // Title go up down animation
        ImageView title = findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        Animation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.1f);
        mAnimation.setDuration(1500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        title.setAnimation(mAnimation);
        // Demo bird playing animation
        final ImageView demoBird = findViewById(R.id.demoBird);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;
            boolean flyUp = false;
            public void run() {
                if(flyUp){
                    i--;
                } else {
                    i++;
                }
                if(i > 1) {
                    flyUp = true;
                } else if (i < 1) {
                    flyUp = false;
                }
                demoBird.setImageResource(birdImageIds[i]);
                handler.postDelayed(this, 250);  //for interval...
            }
        };
        handler.postDelayed(runnable, 250); //for initial delay..
    }

    public void startGame(View view){
        Intent intent = new Intent(this, MainGameActivity.class);
        startActivity(intent);
        finish();
    }
}
