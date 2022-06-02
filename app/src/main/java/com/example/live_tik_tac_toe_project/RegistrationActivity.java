package com.example.live_tik_tac_toe_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity
{
    EditText etUserName,etEmail,etPassword,etConfirmPassword;
    Button btnRegister,btnReset;
    RadioGroup rgGender;
    RadioButton rbMale,rbFemale;
    FirebaseAuth mAuth;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initialization();
        action();
    }

    private void action()
    {
        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerUser();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                resetUser();
            }
        });

    }

    private void resetUser() {
        etUserName.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
        rgGender.clearCheck();
    }

    private void registerUser() {
        String name = etUserName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        if(name.isEmpty()){
            etUserName.setError("Name is required");
            etUserName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please Provide valid Email");
            etEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            etPassword.setError("Password must be of length 6");
            etPassword.requestFocus();
            return;
        }
        if(confirmPassword.isEmpty()){
            etConfirmPassword.setError("Rewrite the Password");
            etConfirmPassword.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            etConfirmPassword.setError("Password do not matches.Rewrite!");
            etConfirmPassword.requestFocus();
            return;
        }
        if(!rbMale.isChecked() && !rbFemale.isChecked()){
            Toast.makeText(getApplicationContext(),"Select a gender",Toast.LENGTH_LONG).show();
            return;
        }
        if(rbMale.isChecked()){
            gender = "Male";
        }
        if(rbFemale.isChecked()){
            gender = "Female";
        }

        //Insert data into the database

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(name, email, gender);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "User has been successfully registered", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegistrationActivity.this,OnlineActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed to register", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            if(errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")){
                                Toast.makeText(getApplicationContext(), "The Email is already in use by another account.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Failed to register (Create problem)", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }

    private void initialization()
    {
        mAuth = FirebaseAuth.getInstance();
        etUserName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }
}