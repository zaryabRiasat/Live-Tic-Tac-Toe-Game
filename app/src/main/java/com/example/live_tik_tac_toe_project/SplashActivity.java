package com.example.live_tik_tac_toe_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity
{
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Initialization();
        Action();
    }

    private void Action()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                imageView.animate().scaleX(3.5f).scaleY(3.5f).setDuration(3000);
                finish();
            }
        },6000);
    }

    private void Initialization()
    {
        imageView = (ImageView) findViewById(R.id.imageView);
    }
}