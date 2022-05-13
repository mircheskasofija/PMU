package com.example.splash;

/*public class MapActivity extends AppCompatActivity *//*implements OnMapReadyCallback*//* {

    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    private int REQUEST_CODE = 111;
//    private ConnectivityManager manager;
//    private NetworkInfo networkInfo;
    private GoogleMap mMap;
    private Marker marker;
    private Geocoder geocoder;
    private double selectedLat, selectedLng;
    private List<Address> addresses;
    private String selectedAddress;
    Button mapButton;
    TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapButton = findViewById(R.id.location_button);
        location = findViewById(R.id.location_sight);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        //mapFragment.getMapAsync(this);

        client = LocationServices.getFusedLocationProviderClient(MapActivity.this);

        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
        else{
            ActivityCompat.requestPermissions(MapActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){

                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mMap = googleMap;

                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                            googleMap.addMarker(markerOptions).showInfoWindow();

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(@NonNull LatLng latLng) {
                                        selectedLat = latLng.latitude;
                                        selectedLng = latLng.longitude;

                                        geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

                                        if(selectedLat != 0){
                                            try {
                                                addresses = geocoder.getFromLocation(selectedLat, selectedLng, 1);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            if(addresses != null){
                                                String mAddress = addresses.get(0).getAddressLine(0);

                                                selectedAddress = mAddress;

                                                if(mAddress != null){
                                                    MarkerOptions markerOptions = new MarkerOptions();
                                                    LatLng latLng1 = new LatLng(selectedLat, selectedLng);

                                                    markerOptions.position(latLng).title(selectedAddress);

                                                    mMap.addMarker(markerOptions).showInfoWindow();
                                                }
                                                else{
                                                    Toast.makeText(MapActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else{
                                                Toast.makeText(MapActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                        else{
                                            Toast.makeText(MapActivity.this, "Latlng null!", Toast.LENGTH_SHORT).show();

                                        }

                                        mapButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String address = addresses.get(0).getAddressLine(0);
                                                Intent intent = new Intent(getApplicationContext(), GuideAddFragment.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.putExtra("mytext", address);
                                                finish();
                                            }
                                        });

                                }
                            });
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
        else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }

    }

*//*    private void CheckConnection(){
        manager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = manager.getActiveNetworkInfo();
    }*//*

    private void GetAddress(double mLat, double mLng){
        geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

        if(mLat != 0){
            try {
                addresses = geocoder.getFromLocation(mLat, mLng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses != null){
                String mAddress = addresses.get(0).getAddressLine(0);

                selectedAddress = mAddress;

                if(mAddress != null){
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(mLat, mLng);

                    markerOptions.position(latLng).title(selectedAddress);

                    mMap.addMarker(markerOptions).showInfoWindow();
                }
                else{
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        }
        else{
            Toast.makeText(this, "Latlng null!", Toast.LENGTH_SHORT).show();

        }
    }*/

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    Button sendLocation;
    Marker marker;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);
        sendLocation = findViewById(R.id.location_button);
        address = findViewById(R.id.locationAddress);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                sendLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Geocoder geocoder;
                        List<Address> addresses = null;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try{
                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        } catch (IOException e){
                            e.printStackTrace();
                        }

                        String address_= addresses.get(0).getAddressLine(0);
                        Intent intent = new Intent(getApplicationContext(), MapHelper.class);
                        intent.putExtra("extra", address_);
                        startActivity(intent);

                        /*getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_map, new GuideAddFragment()).commit();

                        address = addresses.get(0).getAddressLine(0);*/
                        //Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
                        //intent.putExtra("mytext", address);
                        //startActivity(intent);
                    }
                });
                gMap.clear();
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                marker = gMap.addMarker(markerOptions);
            }
        });
    }

}



