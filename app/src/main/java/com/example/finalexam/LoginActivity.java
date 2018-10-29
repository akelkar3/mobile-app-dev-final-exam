package com.example.finalexam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalexam.utils.FirebaseApi;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    final FirebaseApi apicalls = new FirebaseApi(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //create api object pass activity




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(email == null || email.equals("")){
                    Toast.makeText(LoginActivity.this, "Enter Email !!", Toast.LENGTH_SHORT).show();
                } else if(password == null || password.equals("")){
                    Toast.makeText(LoginActivity.this, "Enter Password !!", Toast.LENGTH_SHORT).show();
                } else{
                    //login user
                    apicalls.login(email, password);
                    //goto the main activity
                    //finish this activity.
                }
            }
        });
    }

    @Override
    protected void onStart() {

        // include code to handle the case if the user is already logged in,
        apicalls.autoLogin();
        // which should take the user to main activity and finish this activity.
        super.onStart();
    }
}

