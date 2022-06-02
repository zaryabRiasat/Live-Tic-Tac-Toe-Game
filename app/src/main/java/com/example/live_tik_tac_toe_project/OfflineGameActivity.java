package com.example.live_tik_tac_toe_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OfflineGameActivity extends AppCompatActivity
{
    private final List<int[]> combinationsList = new ArrayList<>();
    private int [] boxPositions = {0,0,0,0,0,0,0,0,0};
    private int playerTurn = 1;
    private int totalSelectedBoxes = 1;
    TextView playerOneName,playerTwoName;
    LinearLayout playerOneLayout,playerTwoLayout;
    ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_game);

          playerOneName = findViewById(R.id.playerOneName);
          playerTwoName = findViewById(R.id.playerTwoName);

          playerOneLayout = findViewById(R.id.playerOneLayout);
          playerTwoLayout = findViewById(R.id.playerTwoLayout);

          img1 = (ImageView) findViewById(R.id.imagebox1);
          img2 = (ImageView) findViewById(R.id.imagebox2);
          img3 = (ImageView) findViewById(R.id.imagebox3);
          img4 = (ImageView) findViewById(R.id.imagebox4);
          img5 = (ImageView) findViewById(R.id.imagebox5);
          img6 = (ImageView) findViewById(R.id.imagebox6);
          img7 = (ImageView) findViewById(R.id.imagebox7);
          img8 = (ImageView) findViewById(R.id.imagebox8);
          img9 = (ImageView) findViewById(R.id.imagebox9);

          combinationsList.add(new int[]{0, 1, 2});
          combinationsList.add(new int[]{3, 4, 5});
          combinationsList.add(new int[]{6, 7, 8});
          combinationsList.add(new int[]{0, 3, 6});
          combinationsList.add(new int[]{1, 4, 7});
          combinationsList.add(new int[]{2, 5, 8});
          combinationsList.add(new int[]{2, 4, 6});
          combinationsList.add(new int[]{0, 4, 8});

          String getPlayerOneName = getIntent().getStringExtra("playerOneName");
          String getPlayerTwoName = getIntent().getStringExtra("playerTwoName");

          playerOneName.setText(getPlayerOneName);
          playerTwoName.setText(getPlayerTwoName);

          img1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(isBoxSelectable(0))
                  {
                      performAction((ImageView)v,0);
                  }
              }
          });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBoxSelectable(1))
                {
                    performAction((ImageView)v,1);
                }
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBoxSelectable(2))
                {
                    performAction((ImageView)v,2);
                }
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBoxSelectable(3))
                {
                    performAction((ImageView)v,3);
                }
            }
        });

        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBoxSelectable(4))
                {
                    performAction((ImageView)v,4);
                }
            }
        });

        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBoxSelectable(5))
                {
                    performAction((ImageView)v,5);
                }
            }
        });

        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBoxSelectable(6))
                {
                    performAction((ImageView)v,6);
                }
            }
        });

        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBoxSelectable(7))
                {
                    performAction((ImageView)v,7);
                }
            }
        });

        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBoxSelectable(8))
                {
                    performAction((ImageView)v,8);
                }
            }
        });

    }

    private void performAction(ImageView imageView, int selectedBoxPosition)
    {
        boxPositions[selectedBoxPosition]=playerTurn;

        if(playerTurn==1)
        {
            imageView.setImageResource(R.drawable.x);

            if(checkPlayerWin())
            {
                OfflineWinDialog offlineWinDialog = new OfflineWinDialog(OfflineGameActivity.this, playerOneName.getText().toString() + " Has Won!",OfflineGameActivity.this);
                offlineWinDialog.setCancelable(false);
                offlineWinDialog.show();
            }
            else if(totalSelectedBoxes==9)
            {
                OfflineWinDialog offlineWinDialog = new OfflineWinDialog(OfflineGameActivity.this, "It is a Draw!",OfflineGameActivity.this);
                offlineWinDialog.setCancelable(false);
                offlineWinDialog.show();
            }
            else
            {
                changePlayerTurn(2);
                totalSelectedBoxes++;
            }
        }
        else
        {
            imageView.setImageResource(R.drawable.o);

            if(checkPlayerWin())
            {
                OfflineWinDialog offlineWinDialog = new OfflineWinDialog(OfflineGameActivity.this, playerTwoName.getText().toString() + " Has Won!",OfflineGameActivity.this);
                offlineWinDialog.setCancelable(false);
                offlineWinDialog.show();
            }
            else if(totalSelectedBoxes==9)
            {
                OfflineWinDialog offlineWinDialog = new OfflineWinDialog(OfflineGameActivity.this, "It is a Draw!",OfflineGameActivity.this);
                offlineWinDialog.setCancelable(false);
                offlineWinDialog.show();
            }
            else
            {
                changePlayerTurn(1);
                totalSelectedBoxes++;
            }
        }
    }

    private void changePlayerTurn(int currentPlayerTurn)
    {
        playerTurn = currentPlayerTurn;

        if(playerTurn==1)
        {
            playerOneLayout.setBackgroundResource(R.drawable.round_back_blue_border);
            playerTwoLayout.setBackgroundResource(R.drawable.round_back_dark_blue);
        }
        else
        {
            playerTwoLayout.setBackgroundResource(R.drawable.round_back_blue_border);
            playerOneLayout.setBackgroundResource(R.drawable.round_back_dark_blue);
        }
    }

    private boolean checkPlayerWin()
    {
        for(int i=0;i<combinationsList.size();i++)
        {
            int [] combination = combinationsList.get(i);

            if(boxPositions[combination[0]]==playerTurn && boxPositions[combination[1]]==playerTurn && boxPositions[combination[2]]==playerTurn)
                return true;
        }
        return false;
    }

    private boolean isBoxSelectable(int boxPosition)
    {
        if(boxPositions[boxPosition]==0)
            return true;
        return false;
    }

    public void restartMatch()
    {
        boxPositions = new int[]{0,0,0,0,0,0,0,0,0};
        playerTurn=1;
        totalSelectedBoxes=1;
        img1.setImageResource(R.drawable.transparent_image);
        img2.setImageResource(R.drawable.transparent_image);
        img3.setImageResource(R.drawable.transparent_image);
        img4.setImageResource(R.drawable.transparent_image);
        img5.setImageResource(R.drawable.transparent_image);
        img6.setImageResource(R.drawable.transparent_image);
        img7.setImageResource(R.drawable.transparent_image);
        img8.setImageResource(R.drawable.transparent_image);
        img9.setImageResource(R.drawable.transparent_image);
    }

    public void goToMain()
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gamemenu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.itClose:
                Toast.makeText(OfflineGameActivity.this, "Search kerna hay", Toast.LENGTH_SHORT).show();
                break;
            case R.id.itSound:
                Toast.makeText(OfflineGameActivity.this, "Search kerna hay", Toast.LENGTH_SHORT).show();
                break;
            case R.id.itLeave:
                Intent intent = new Intent(OfflineGameActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}