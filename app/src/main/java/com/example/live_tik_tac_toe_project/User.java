package com.example.live_tik_tac_toe_project;

public class User {
    public String userName,email,gender;
    public int score,level;
    User(){

    }
    User(String userName,String email,String gender){
        this.userName = userName;
        this.email = email;
        this.gender = gender;
        this.score = 0;
        this.level = 0;
    }

    //For Storing Fb data
    User(String userName,String email){
        this.userName = userName;
        this.email = email;
        this.score = 0;
        this.level = 0;
    }
}
