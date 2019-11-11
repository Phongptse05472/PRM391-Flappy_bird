package com.example.flappybird_prm391;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.flappybird_prm391.common.Tuple3;
import com.example.flappybird_prm391.model.Score;

import java.util.List;

public class LeaderboardActivity extends Activity {

    // Screen activity
    Context context;
    // Local DB
    LocalDataHelper localDataHelper;
    // Score list layout
    LinearLayout colScore;
    LinearLayout colRank;
    LinearLayout colDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard_screen_layout);
        context = this;
        // btn event binding
        ImageButton btnOk = findViewById(R.id.btnOk2);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaderboardActivity screen = (LeaderboardActivity) context;
                Intent intent = new Intent(screen, MainActivity.class);
                screen.startActivity(intent);
                screen.finish();
            }
        });
        ImageButton btnOnlineScore = findViewById(R.id.btnOnlScoreSmall);
        btnOnlineScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaderboardActivity screen = (LeaderboardActivity) context;
                Intent intent = new Intent(screen, GlobalLeaderboardActivity.class);
                screen.startActivity(intent);
                screen.finish();
            }
        });
        // Text scale
        scale = this.getResources().getDisplayMetrics().density;
        // Score list layout initialize
        colScore = findViewById(R.id.colScore);
        colRank = findViewById(R.id.colRank);
        colDate = findViewById(R.id.colDate);
        // DB management initialize
        localDataHelper = new LocalDataHelper(this);
        // Get top 10
        List<Score> scores = localDataHelper.getScoreBoard();
        // To display
        for(int i = 1; i <= scores.size(); i++){
            Tuple3<TextView, TextView, TextView> dispVal = makeDisplayScore(this, i, scores.get(i - 1));
            colRank.addView(dispVal.getItem1());
            colScore.addView(dispVal.getItem2());
            colDate.addView(dispVal.getItem3());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Screen scale for convert value from px to dp
    float scale;

    /**
     * Create textview to display from score obj
     * @param context screen activity
     * @param score score to create textview
     * @return Pair of Score - Date
     */
    private Tuple3<TextView, TextView, TextView> makeDisplayScore(Context context, int rank, Score score) {
        // Rank
        TextView rankNum = new TextView(context);
        rankNum.setText(String.valueOf(rank));
        rankNum.setGravity(Gravity.CENTER);
        rankNum.setTextColor(Color.BLACK);
        rankNum.setTextSize(15f);
        rankNum.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (50 * scale + 0.5f)));
        // Score
        TextView scoreNum = new TextView(context);
        scoreNum.setText(String.valueOf(score.getScore()));
        scoreNum.setGravity(Gravity.CENTER);
        scoreNum.setTextColor(Color.BLACK);
        scoreNum.setTextSize(15f);
        scoreNum.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (50 * scale + 0.5f)));
        // Date
        TextView date = new TextView(context);
        date.setText(String.valueOf(score.getDate()));
        date.setGravity(Gravity.CENTER);
        date.setTextColor(Color.BLACK);
        date.setTextSize(15f);
        date.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (50 * scale + 0.5f)));
        return new Tuple3<>(rankNum, scoreNum, date);
    }
}
