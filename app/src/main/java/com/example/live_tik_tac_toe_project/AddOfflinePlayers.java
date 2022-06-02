package com.example.live_tik_tac_toe_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddOfflinePlayers extends AppCompatActivity {

    EditText playerOneName,playerTwoName;
    Button startGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offline_players);

        playerOneName = (EditText) findViewById(R.id.etPlayerOneName);
        playerTwoName = (EditText) findViewById(R.id.etPlayerTwoName);
        startGame = (Button) findViewById(R.id.btnStartGame);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedPlayerOneName = playerOneName.getText().toString();
                String typedPlayerTwoName = playerTwoName.getText().toString();
                if(typedPlayerOneName.isEmpty()||typedPlayerTwoName.isEmpty())
                    Toast.makeText(getApplicationContext(),"Enter Player Names!",Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent(getApplicationContext(), OfflineGameActivity.class);
                    intent.putExtra("playerOneName", typedPlayerOneName);
                    intent.putExtra("playerTwoName", typedPlayerTwoName);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}