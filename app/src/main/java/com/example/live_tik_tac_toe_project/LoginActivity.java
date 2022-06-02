package com.example.live_tik_tac_toe_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity
{
    Button btnLogin,btnRegisterLink;
    EditText etName, etPass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();
        action();

    }

    private void action()
    {
        Intent intent = getIntent();
        String s1 =intent.getStringExtra("UserName");
        String s2 =intent.getStringExtra("Passworrd");
        //etName.setText(s1);
        //etPass.setText(s2);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, OnlineActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initialization()
    {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etName = (EditText) findViewById(R.id.etUserName);
        etPass = (EditText) findViewById(R.id.etPassword);
        btnRegisterLink = (Button) findViewById(R.id.btnRegisterLink);
    }
}