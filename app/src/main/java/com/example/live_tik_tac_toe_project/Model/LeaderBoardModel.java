package com.example.live_tik_tac_toe_project.Model;

public class LeaderBoardModel {

    private String name;
    private String score;

    public LeaderBoardModel() {
    }

    public LeaderBoardModel(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
