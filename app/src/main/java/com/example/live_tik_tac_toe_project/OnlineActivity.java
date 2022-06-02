package com.example.live_tik_tac_toe_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlineActivity extends AppCompatActivity
{
    Button btnPlayOnline, btnLeaderBoard, btnPlayOffline, btnLogout;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        initialization();
        fetchData();
        action();
    }

    private void fetchData() {
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                String name = userProfile.userName;
                String email=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("user_data",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Name",name);
                editor.putString("email",email);
                editor.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error " + error,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void action()
    {
        btnPlayOnline.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Intent intent = new Intent(OnlineActivity.this,OnlineGameActivity.class);
//                Intent intent = new Intent(OnlineActivity.this,PlayerNameOnline.class);
//                startActivity(intent);

                Intent intent = new Intent(OnlineActivity.this,OnlineRunActivity.class);
                startActivity(intent);

            }
        });

        btnLeaderBoard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(OnlineActivity.this,LeaderBoardActivity.class);
                startActivity(intent);

            }
        });

        btnPlayOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddOfflinePlayers.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("user_data",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    private void initialization()
    {
        btnPlayOnline = (Button) findViewById(R.id.btnPlayOnline);
        btnLeaderBoard = (Button) findViewById(R.id.btnLeaderBoard);
        btnPlayOffline = (Button) findViewById(R.id.btnPlayerOfflineFromOnlineActivity);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();
    }
}