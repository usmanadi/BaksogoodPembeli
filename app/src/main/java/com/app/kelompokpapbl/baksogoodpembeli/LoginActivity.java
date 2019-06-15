package com.app.kelompokpapbl.baksogoodpembeli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by ASUS on 04/05/2018.
 */

public class LoginActivity extends AppCompatActivity {
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;
    private String email, password;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail_login);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword_login);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignup = (TextView) findViewById(R.id.textViewSignUp);

        progressDialog = new ProgressDialog(this);

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            // user is already logged in
            // open profile activity
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            //close this activity
            finish();
        }

        //attaching click listener
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if(TextUtils.isEmpty(email)){

                    Toast.makeText(LoginActivity.this,"Please enter email", Toast.LENGTH_LONG).show();

                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"Please enter password",Toast.LENGTH_LONG).show();

                }
                else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                }
                else {
                    userLogin(email,password);
                }

            }
        });

        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void userLogin(String email, String password){
        //creating a new user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this,"email dan password tidak ditemukan",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
