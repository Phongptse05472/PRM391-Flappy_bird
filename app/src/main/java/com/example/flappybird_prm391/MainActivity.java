package com.example.flappybird_prm391;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.flappybird_prm391.resourceshelper.LocalDataHelper;
import com.example.flappybird_prm391.resourceshelper.NetworkChecker;
import com.google.android.gms.common.AccountPicker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

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

    // Local data
    LocalDataHelper localDataHelper;

    // Accountpicker request code
    private final int ACCOUNT_PICKER_REQUEST_CODE = 1000;
    private final String ACCOUNT = "account";
    private final String INITIALIZED = "initialized";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        localDataHelper = new LocalDataHelper(context);
        // If user never logon before, get user email
        boolean isHasInternet = false;
        try {
            isHasInternet = new NetworkChecker().execute(context).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if(isHasInternet){
            if(localDataHelper.getSetting(ACCOUNT).isEmpty() && localDataHelper.getSetting(INITIALIZED).isEmpty()){
                Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                        new String[] {"com.google", "com.google.android.legacyimap"},
                        false, null, null, null, null);
                startActivityForResult(intent, ACCOUNT_PICKER_REQUEST_CODE);
            }
        }
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
                    Intent intent = new Intent(screen, LeaderboardActivity.class);
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
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        if (requestCode == ACCOUNT_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            System.out.println(accountName.split("@")[0]);
            localDataHelper.putSetting(ACCOUNT, accountName.split("@")[0]);
            localDataHelper.putSetting(INITIALIZED, "true");
        } else if (requestCode == ACCOUNT_PICKER_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            // No account selected
            localDataHelper.putSetting(INITIALIZED, "true");
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        //call close() of the helper class
        localDataHelper.close();
    }
}
