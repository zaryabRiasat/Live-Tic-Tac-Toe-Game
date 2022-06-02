package com.example.live_tik_tac_toe_project.Model;

public class WonClass {

    String player_id,player_name;
    int score;

    public WonClass() {
    }

    public WonClass(String player_id, String player_name, int score) {
        this.player_id = player_id;
        this.player_name = player_name;
        this.score = score;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
