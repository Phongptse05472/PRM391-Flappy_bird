package com.example.flappybird_prm391.model;

public class Score implements Comparable<Score>{
    private int id;
    private String date;
    private int score;

    public Score() {
        this.id = 0;
    }

    public Score(int id, String date, int score) {
        this.id = id;
        this.date = date;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    public int compareTo(Score o) {
        return o.getScore() - this.score;
    }
}
