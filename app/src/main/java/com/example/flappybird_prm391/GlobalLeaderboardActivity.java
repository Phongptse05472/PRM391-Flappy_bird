package com.example.flappybird_prm391;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flappybird_prm391.common.Tuple4;
import com.example.flappybird_prm391.constraint.Urls;
import com.example.flappybird_prm391.model.OnlineScore;
import com.google.android.gms.common.AccountPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class GlobalLeaderboardActivity extends Activity {

    // Screen activity
    Context context;
    // Local DB
    LocalDataHelper localDataHelper;
    // Score list layout
    LinearLayout colScore;
    LinearLayout colRank;
    LinearLayout colAccount;
    LinearLayout colDate;

    // Account
    private final int ACCOUNT_PICKER_REQUEST_CODE = 1000;
    private final String ACCOUNT = "account";
    private String accountName = "";
    private int userScoreIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_scoreboard_screen_layout);
        context = this;
        // btn event binding
        ImageButton btnOk = findViewById(R.id.btnOk2);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalLeaderboardActivity screen = (GlobalLeaderboardActivity) context;
                Intent intent = new Intent(screen, MainActivity.class);
                screen.startActivity(intent);
                screen.finish();
            }
        });
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalLeaderboardActivity screen = (GlobalLeaderboardActivity) context;
                Intent intent = new Intent(screen, LeaderboardActivity.class);
                screen.startActivity(intent);
                screen.finish();
            }
        });
        // Text scale
        scale = this.getResources().getDisplayMetrics().density;
        // Score list layout initialize
        colScore = findViewById(R.id.colScore);
        colAccount = findViewById(R.id.colAccount);
        colRank = findViewById(R.id.colRank);
        colDate = findViewById(R.id.colDate);
        // DB management initialize
        localDataHelper = new LocalDataHelper(this);
        // Get top 100
        if(localDataHelper.getSetting(ACCOUNT).isEmpty()){
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[] {"com.google", "com.google.android.legacyimap"},
                    false, null, null, null, null);
            startActivityForResult(intent, ACCOUNT_PICKER_REQUEST_CODE);
        } else {
            callServer();
        }


    }

    private void callServer(){
        if(!isNetworkConnected()){
            showWarningDialog("Connection error", "Device is offline");
        }
        accountName = localDataHelper.getSetting(ACCOUNT);
        RequestQueue request = Volley.newRequestQueue(context);
        try {
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Urls.TOP100.getType(),
                    Urls.TOP100.getUrl(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response : ", response.toString());
                            try {
                                if(response.getBoolean("result")){
                                    initScoreList(response.getJSONArray("resultData"));
                                } else {
                                    showWarningDialog("Unknow error", "Something wrong occured, could not get leaderboard");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showWarningDialog("Unknow error", "Something wrong occured, could not get leaderboard");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Response : ", error.toString());
                            showWarningDialog("Connection error", "Something wrong with connection, could not get leaderboard");
                        }
                    }
            );
            objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.add(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            showWarningDialog("Unknow error", "Something wrong occured, could not get leaderboard");
        }
    }

    private void initScoreList(JSONArray list) throws JSONException{
        final List<OnlineScore> listScore = new ArrayList<>();
        for(int i = 0; i < list.length(); i++){
            JSONObject obj = list.getJSONObject(i);
            OnlineScore score = new OnlineScore(
                    obj.getInt("rank"),
                    obj.getString("account"),
                    obj.getInt("score"),
                    obj.getString("mDate")
            );
            listScore.add(score);
        }
        boolean hasUser = false;
        for(int i = 0; i < listScore.size(); i++){
            if(listScore.get(i).getAccount().equals(accountName)){
                hasUser = true;
                userScoreIndex = i;
                break;
            }
        }
        if(!hasUser){
            RequestQueue request = Volley.newRequestQueue(context);
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Urls.SELF_BEST.getType(),
                    MessageFormat.format(Urls.SELF_BEST.getUrl(), accountName),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response : ", response.toString());
                            try {
                                if(response.getBoolean("result")){
                                    JSONObject obj = response.getJSONObject("resultData");
                                    OnlineScore score = new OnlineScore(
                                            obj.getInt("rank"),
                                            obj.getString("account"),
                                            obj.getInt("score"),
                                            obj.getString("mDate")
                                    );
                                    if(score.getRank() != 0){
                                        listScore.add(score);
                                        userScoreIndex = listScore.size() - 1;
                                    }
                                    renderList(listScore);
                                } else {
                                    showWarningDialog("Unknow error", "Something wrong occured, could not get leaderboard");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showWarningDialog("Unknow error", "Something wrong occured, could not get leaderboard");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Response : ", error.toString());
                            showWarningDialog("Connection error", "Something wrong with connection, could not get leaderboard");
                        }
                    }
            );
            objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.add(objectRequest);
        } else {
            renderList(listScore);
        }
    }

    private void renderList(List<OnlineScore> listScore){
        for(int i = 0; i < listScore.size(); i++){
            int textColor = i == userScoreIndex ? Color.RED : Color.BLACK;
            Tuple4<TextView, TextView, TextView, TextView> dispVal = makeDisplayScore(context, listScore.get(i), textColor);
            colRank.addView(dispVal.getItem1());
            colAccount.addView(dispVal.getItem2());
            colScore.addView(dispVal.getItem3());
            colDate.addView(dispVal.getItem4());
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
    private Tuple4<TextView, TextView, TextView, TextView> makeDisplayScore(Context context, OnlineScore score, int color) {
        // Rank
        TextView rankNum = new TextView(context);
        rankNum.setText(String.valueOf(score.getRank()));
        rankNum.setGravity(Gravity.CENTER);
        rankNum.setTextColor(color);
        rankNum.setTextSize(12f);
        rankNum.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (50 * scale + 0.5f)));
        // Account
        TextView account = new TextView(context);
        account.setText(String.valueOf(score.getAccount()));
        account.setGravity(Gravity.CENTER);
        account.setTextColor(color);
        account.setTextSize(12f);
        account.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (50 * scale + 0.5f)));
        // Score
        TextView scoreNum = new TextView(context);
        scoreNum.setText(String.valueOf(score.getScore()));
        scoreNum.setGravity(Gravity.CENTER);
        scoreNum.setTextColor(color);
        scoreNum.setTextSize(12f);
        scoreNum.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (50 * scale + 0.5f)));
        // Date
        TextView date = new TextView(context);
        date.setText(String.valueOf(score.getDate()));
        date.setGravity(Gravity.CENTER);
        date.setTextColor(color);
        date.setTextSize(12f);
        date.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (50 * scale + 0.5f)));
        return new Tuple4<>(rankNum, account, scoreNum, date);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        if (requestCode == ACCOUNT_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            System.out.println(accountName.split("@")[0]);
            localDataHelper.putSetting(ACCOUNT, accountName.split("@")[0]);
            callServer();
        } else if (requestCode == ACCOUNT_PICKER_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            // No account selected
            showWarningDialog("No account", "Sorry this function is only for registered player");
        }
    }

    private void showWarningDialog(String title, String message){
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
