package com.app.kelompokpapbl.baksogoodpembeli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.kelompokpapbl.baksogoodpembeli.model.PembeliModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextNama;
    private EditText editTextAlamat;
    private Button buttonSignup;
    private ProgressDialog progressDialog;
    private TextView textViewSignin;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    //firebase database reference object
    private DatabaseReference databasePembeli;

    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextNama = (EditText) findViewById(R.id.editNama);
        editTextAlamat = (EditText) findViewById(R.id.editAlamat);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        progressDialog = new ProgressDialog(MainActivity.this);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        databasePembeli = FirebaseDatabase.getInstance().getReference("pembeli");

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            // user is already logged in
            // open profile activity
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            //close this activity
            finish();

        }

        //attaching listener to button
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if(TextUtils.isEmpty(email)){

                    Toast.makeText(MainActivity.this,"Please enter email", Toast.LENGTH_LONG).show();

                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this,"Please enter password",Toast.LENGTH_LONG).show();

                }
                else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(email,password);
                }
            }
        });

        textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

    }

    //Set UserDisplay Name

    private void userProfile(){
        String nama = editTextNama.getText().toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Toast.makeText(this, "Berhasil", Toast.LENGTH_LONG).show();
        if(user!= null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nama.trim())
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override

                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Log.d("TESTING", "User profile updated.");
                                insertPembeli();
                            }
                        }
                    });
        }
    }

    private void registerUser(String email,String password){
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //Toast.makeText(MainActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            userProfile();
                        }else{
                            Toast.makeText(MainActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void insertPembeli() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //getting the values to save
        String nama = editTextNama.getText().toString();
        String alamat = editTextAlamat.getText().toString();
        String email = editTextEmail.getText().toString();
        //checking if the value is provided
        if (!TextUtils.isEmpty(nama)&&!TextUtils.isEmpty(alamat)&&!TextUtils.isEmpty(email)) {
            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Data
            //String id = databaseDosen.push().getKey();
            //creating an Dosen Object
            PembeliModel pembeli = new PembeliModel(nama, alamat, email);
            //Saving the Data
            databasePembeli.child(user.getUid()).setValue(pembeli);
            //setting edittext to blank again
            editTextNama.setText("");
            editTextAlamat.setText("");
            editTextEmail.setText("");
            editTextPassword.setText("");
            //displaying a success toast
            Toast.makeText(this, "Pembeli added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter all field", Toast.LENGTH_LONG).show();
        }
    }
}
