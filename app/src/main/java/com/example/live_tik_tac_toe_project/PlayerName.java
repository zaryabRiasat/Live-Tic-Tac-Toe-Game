package com.example.live_tik_tac_toe_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PlayerName extends AppCompatActivity
{
    EditText playerNameEt;
    AppCompatButton startGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_name);

        initialization();
        action();
    }

    private void action()
    {
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String getPlayerName = playerNameEt.getText().toString();

                if(getPlayerName.isEmpty()){
                    Toast.makeText(PlayerName.this, "Please enter player name", Toast.LENGTH_SHORT).show();
                }
                else{

                    Intent intent = new Intent(PlayerName.this, MainActivity.class);

                    intent.putExtra("playerName", getPlayerName);

                    startActivity(intent);

                    finish();
                }
            }
        });
    }

    private void initialization()
    {
        playerNameEt = findViewById(R.id.playerNameEt);
        startGameBtn = findViewById(R.id.startGameBtn);

    }
}