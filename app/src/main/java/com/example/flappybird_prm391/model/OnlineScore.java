package com.example.flappybird_prm391.model;

public class OnlineScore implements Comparable<OnlineScore>{

    private int rank;
    private String account;
    private int score;
    private String date;


    public OnlineScore() {
    }

    public OnlineScore(int rank, String account, int score, String date) {
        this.rank = rank;
        this.account = account;
        this.score = score;
        this.date = date;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(OnlineScore o) {
        return o.getScore() - this.score;
    }
}