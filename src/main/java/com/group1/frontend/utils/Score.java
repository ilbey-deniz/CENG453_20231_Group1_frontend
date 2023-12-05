package com.group1.frontend.utils;

public class Score {
    private String username;
    private int score;

    public Score(String username, int score) {
        this.username = username;
        this.score = score;
    }
    //getters and setters
    public String getUsername() {
        return username;
    }
    public int getScore() {
        return score;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setScore(int score) {
        this.score = score;
    }

}
