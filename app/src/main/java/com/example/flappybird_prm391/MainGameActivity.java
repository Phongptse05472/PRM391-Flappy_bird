package com.example.flappybird_prm391;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flappybird_prm391.constraint.Urls;
import com.example.flappybird_prm391.model.Score;

import org.json.JSONException;
import org.json.JSONObject;

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
    LocalDataHelper localDataHelper;

    // Account
    private final String ACCOUNT = "account";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = this;
        localDataHelper = new LocalDataHelper(this);
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
                Intent intent = new Intent(screen, LeaderboardActivity.class);
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
        // Score Management
        final Score top = updateLocalScoreBoard(score);
        // Online Score Handle
        if(!localDataHelper.getSetting(ACCOUNT).isEmpty()){
            RequestQueue request = Volley.newRequestQueue(context);
            try {
                JSONObject sendData = new JSONObject()
                        .put("account", localDataHelper.getSetting(ACCOUNT))
                        .put("score", top.getScore())
                        .put("mDate", getCurrentDateString());

                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Urls.SUBMIT.getType(),
                        Urls.SUBMIT.getUrl(),
                        sendData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("Response : ", response.toString());
                                displayResult(score, top);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Response : ", error.toString());
                                showErrorDialog("Connection error", "Something wrong with connection, score not saved to online scoreboard");
                                displayResult(score, top);
                            }
                        }
                );
                objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request.add(objectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
                showErrorDialog("Unknow error", "Something wrong occured, score not saved to online scoreboard");
                displayResult(score, top);
            }
        } else {
            displayResult(score, top);
        }
    }

    public void displayResult(int score, Score top){
        NumberDisplayController ndc = new NumberDisplayController();

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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:dd:mm");
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
        List<Score> scoreBoard = localDataHelper.getScoreBoard();
        scoreBoard.add(new Score(-1, getCurrentDateString(), currentScore));
        Collections.sort(scoreBoard);
        scoreBoard = scoreBoard.subList(0, scoreBoard.size() < 10 ? scoreBoard.size() : 10);
        localDataHelper.clearData();
        localDataHelper.insertBatchScore(scoreBoard);
        return scoreBoard.get(0);
    }

    private void showErrorDialog(String title, String message){
        AlertDialog errorDialog = new AlertDialog.Builder(context).create();
        errorDialog.setTitle(title);
        errorDialog.setMessage(message);
        errorDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        errorDialog.show();
    }
}
