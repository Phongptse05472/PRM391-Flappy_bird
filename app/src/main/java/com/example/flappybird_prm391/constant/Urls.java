package com.example.flappybird_prm391.constant;

import com.android.volley.Request;

/**
 * RESTful API Backend Server Urls
 */
public enum  Urls {

    TOP100(Request.Method.GET, "http://fbscoreapi.azurewebsites.net/rest/score/top100"),
    ACCOUNT_EXIST(Request.Method.GET, "http://fbscoreapi.azurewebsites.net/rest/score/checkExist?account={0}"),
    SUBMIT(Request.Method.POST, "http://fbscoreapi.azurewebsites.net/rest/score/submit"),
    SELF_BEST(Request.Method.GET, "http://fbscoreapi.azurewebsites.net/rest/score/selfBest?account={0}")
    ;

    private final int type;
    private final String url;

    Urls(int type, String url) {
        this.type = type;
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
