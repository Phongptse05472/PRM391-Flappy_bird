package com.example.flappybird_prm391;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.flappybird_prm391.model.Score;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class MainGameActivity extends Activity {

    Context context;
    GameView_SurfaceView gameView;
    ConstraintLayout gameOverLayout;
    ImageButton btnOk;
    ImageButton btnViewScore;
    ImageView gameOverTitle;
    ImageView scoreBoard;
    ImageView medal;
    LinearLayout scoreDisplay;
    LinearLayout bestScoreDisplay;
    Point displaySize;
    LocalScoreManagement localScoreManagement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = this;
        localScoreManagement = new LocalScoreManagement(this);
        displaySize = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(displaySize);
        setContentView(R.layout.game_screen_layout);
        btnOk = findViewById(R.id.btnOk);
        btnOk.setVisibility(View.INVISIBLE);
        btnViewScore = findViewById(R.id.btnScoreSmall);
        btnViewScore.setVisibility(View.INVISIBLE);
        gameOverTitle = findViewById(R.id.imgGameOver);
        gameOverTitle.setVisibility(View.INVISIBLE);
        scoreBoard = findViewById(R.id.scoreBoard);
        scoreBoard.setVisibility(View.INVISIBLE);
        medal = findViewById(R.id.medal);
        medal.setVisibility(View.INVISIBLE);
        gameOverLayout = findViewById(R.id.gameOverLayout);
        scoreDisplay = findViewById(R.id.scoreDisplay);
        scoreDisplay.setVisibility(View.INVISIBLE);
        bestScoreDisplay = findViewById(R.id.bestScoreDisplay);
        bestScoreDisplay.setVisibility(View.INVISIBLE);
        gameView = findViewById(R.id.gameView);
        gameView.init(displaySize);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(btnOk.getAlpha() == 1f){
                MainGameActivity screen = (MainGameActivity) context;
                Intent intent = new Intent(screen, MainActivity.class);
                startActivity(intent);
                screen.finish();
            }
            }
        });

        btnViewScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(btnViewScore.getAlpha() == 1f){
                MainGameActivity screen = (MainGameActivity) context;
                Intent intent = new Intent(screen, ScoreboardActivity.class);
                startActivity(intent);
                screen.finish();
            }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    public void gameover(final int score) {
        NumberDisplayController ndc = new NumberDisplayController();
        // Score Management
        Score top = updateLocalScoreBoard(score);
        int topscore = top.getScore();
        final List<ImageView> topScoreDisp = ndc.smallNum2Display(context, String.valueOf(topscore));
        final List<ImageView> currentScoreDisp = ndc.smallNum2Display(context, String.valueOf(score));
        // Animations
        final ObjectAnimator titleFadeInAnimator = ObjectAnimator.ofFloat(gameOverTitle, "alpha", 0f, 1f);
        titleFadeInAnimator.setDuration(1000);
        final ObjectAnimator scoreBoardFadeInAnimator = ObjectAnimator.ofFloat(scoreBoard, "alpha", 0f, 1f);
        scoreBoardFadeInAnimator.setDuration(1000);
        final ObjectAnimator btnOkFadeInAnimator = ObjectAnimator.ofFloat(btnOk, "alpha", 0f, 1f);
        btnOkFadeInAnimator.setDuration(1000);
        final ObjectAnimator btnScoreFadeInAnimator = ObjectAnimator.ofFloat(btnViewScore, "alpha", 0f, 1f);
        btnScoreFadeInAnimator.setDuration(1000);
        final ObjectAnimator scoreFadeInAnimator = ObjectAnimator.ofFloat(scoreDisplay, "alpha", 0f, 1f);
        scoreFadeInAnimator.setDuration(1000);
        final ObjectAnimator bestScoreFadeInAnimator = ObjectAnimator.ofFloat(bestScoreDisplay, "alpha", 0f, 1f);
        bestScoreFadeInAnimator.setDuration(1000);
        final AnimatorSet animatorSet = new AnimatorSet();
        // Medal
        final Integer medalId = getMedal(score);
        final ObjectAnimator medalFadeInAnimator = ObjectAnimator.ofFloat(medal, "alpha", 0f, 1f);
        medalFadeInAnimator.setDuration(1000);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                gameOverLayout.requestFocus();
                gameOverTitle.setVisibility(View.VISIBLE);
                gameOverTitle.setAlpha(0f);
                scoreBoard.setVisibility(View.VISIBLE);
                scoreBoard.setAlpha(0f);
                btnOk.setVisibility(View.VISIBLE);
                btnOk.setAlpha(0f);
                btnViewScore.setVisibility(View.VISIBLE);
                btnViewScore.setAlpha(0f);
                // Medal
                if(medalId != null) {
                    medal.setImageResource(medalId);
                    medal.setVisibility(View.VISIBLE);
                    medal.setAlpha(0f);
                }
                // Session score
                scoreDisplay.setVisibility(View.VISIBLE);
                scoreDisplay.setAlpha(0f);
                for(ImageView scoreNum : currentScoreDisp){
                    scoreNum.setScaleType(ImageView.ScaleType.FIT_XY);
                    LinearLayout.LayoutParams prm = new LinearLayout.LayoutParams(40, LinearLayout.LayoutParams.MATCH_PARENT);
                    prm.setMargins(5,0,0,0);
                    scoreNum.setLayoutParams(prm);
                    scoreDisplay.addView(scoreNum);
                }
                // Best score
                bestScoreDisplay.setVisibility(View.VISIBLE);
                bestScoreDisplay.setAlpha(0f);
                for(ImageView scoreNum : topScoreDisp){
                    scoreNum.setScaleType(ImageView.ScaleType.FIT_XY);
                    LinearLayout.LayoutParams prm = new LinearLayout.LayoutParams(40, LinearLayout.LayoutParams.MATCH_PARENT);
                    prm.setMargins(5,0,0,0);
                    scoreNum.setLayoutParams(prm);
                    bestScoreDisplay.addView(scoreNum);
                }
                // Start end game animations
                animatorSet.play(titleFadeInAnimator).before(scoreBoardFadeInAnimator);
                if(medalId != null) {
                    animatorSet.play(scoreBoardFadeInAnimator).with(scoreFadeInAnimator).with(bestScoreFadeInAnimator).with(medalFadeInAnimator);
                } else {
                    animatorSet.play(scoreBoardFadeInAnimator).with(scoreFadeInAnimator).with(bestScoreFadeInAnimator);
                }
                animatorSet.play(btnScoreFadeInAnimator).with(btnOkFadeInAnimator).after(scoreBoardFadeInAnimator);
                animatorSet.start();
            }
        });
    }

    public Integer getMedal(int score){
        if(score >= 10 && score < 20){
            return R.drawable.medal_bronze;
        } else if(score >= 20 && score < 30) {
            return R.drawable.medal_sliver;
        } else if(score >= 30 && score < 50) {
            return R.drawable.medal_gold;
        } else if(score >= 50) {
            return R.drawable.medal_platium;
        } else {
            return null;
        }
    }

    public String getCurrentDateString(){
        String result;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        result = dtf.format(now);
        return result;
    }

    /**
     * Update local scoreboard
     * @param currentScore current session score
     * @return top score
     */
    public Score updateLocalScoreBoard(int currentScore){
        List<Score> scoreBoard = localScoreManagement.getScoreBoard();
        scoreBoard.add(new Score(-1, getCurrentDateString(), currentScore));
        Collections.sort(scoreBoard);
        scoreBoard = scoreBoard.subList(0, scoreBoard.size() < 10 ? scoreBoard.size() : 10);
        localScoreManagement.clearData();
        localScoreManagement.insertBatchScore(scoreBoard);
        return scoreBoard.get(0);
    }
}
