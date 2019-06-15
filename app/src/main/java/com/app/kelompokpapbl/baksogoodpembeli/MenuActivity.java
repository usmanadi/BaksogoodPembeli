package com.app.kelompokpapbl.baksogoodpembeli;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.kelompokpapbl.baksogoodpembeli.model.PembeliModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;

/**
 * Created by ASUS on 04/05/2018.
 */

public class MenuActivity extends AppCompatActivity{

    TextView nama;
    TextView penjual;
    Button logout;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        nama = (TextView)findViewById(R.id.nama);
        penjual = (TextView)findViewById(R.id.namaPenjual);

        logout = (Button)findViewById(R.id.buttonLogout);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            //closing activity
            finish();
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //displaying logged in user name
        nama.setText(user.getDisplayName()+" "+user.getEmail());

        //adding listener to button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            }
        });

        penjual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                finish();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

//                PembeliModel infoPembeli = new PembeliModel();
//                for (DataSnapshot ds : dataSnapshot.getChildren()){
//                    infoPembeli.setNama(ds.child(user.getUid()).getValue(PembeliModel.class).getNama());
//                    infoPembeli.setStatus_pesanan(ds.child(user.getUid()).getValue(PembeliModel.class).getStatus_pesanan());
//                }
//
//                penjual.setText(infoPembeli.getNama());
//                if (infoPembeli.equals("true")){
//                    statuspenjual.setText("Berjualan");
//                }
//                if (infoPembeli.equals("false")){
//                    statuspenjual.setText("Tidak Berjualan");
//                }

                String childView = String.valueOf(dataSnapshot.child("pembeli").child(user.getUid()).child("nama").getValue());
                penjual.setText(childView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
