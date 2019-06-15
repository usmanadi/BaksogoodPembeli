package com.app.kelompokpapbl.baksogoodpembeli;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.app.kelompokpapbl.baksogoodpembeli.model.PembeliModel;
import com.app.kelompokpapbl.baksogoodpembeli.model.PesananModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.Button;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String lat, lon;
    private Button btnselesai;
    private static final int LOCATION_REQUEST=500;
    ArrayList<LatLng> listPoints;

    LocationManager locationManager;

    private FirebaseAuth firebaseAuth;
    //firebase database reference object
    private DatabaseReference databasePesanan;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        btnselesai = (Button) findViewById(R.id.selesai);

        databasePesanan = FirebaseDatabase.getInstance().getReference("pesanan");

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            //closing activity
            finish();
        }

        btnselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertPesanan(lat, lon);

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Reset marker when already 1
                if(listPoints.size()==1){
                    listPoints.clear();
                    mMap.clear();
                }
                //save first point selected
                listPoints.add(latLng);
                //create marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(markerOptions);
                double lati = latLng.latitude;
                double loni = latLng.longitude;
                lat = Double.toString(lati);
                lon = Double.toString(loni);
                Toast.makeText(getApplicationContext(),lat+", "+ loni, Toast.LENGTH_LONG).show();
            }
        });
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    private void insertPesanan(String lat, String lon) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //checking if the value is provided
        if (!TextUtils.isEmpty(user.getDisplayName())) {
            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Data
            //String id = databaseDosen.push().getKey();
            //creating an Dosen Object
            PesananModel pesanan = new PesananModel(user.getDisplayName(), lat, lon);
            //Saving the Data
            databasePesanan.child(user.getDisplayName()).setValue(pesanan);
            //displaying a success toast
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Gagal", Toast.LENGTH_LONG).show();
        }
    }
}
